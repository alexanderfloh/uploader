import java.io.File
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.content.FileBody
import org.apache.http.entity.mime.content.StringBody
import org.apache.http.entity.mime.MultipartEntity
import org.apache.http.util.EntityUtils
import scala.io.Source
import org.apache.tools.ant.BuildException
import scala.language.implicitConversions
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

class DmpsterUpload extends org.apache.tools.ant.Task {
  private var baseDir: Option[String] = None
  private var serverUrl: Option[String] = None
  private var tags: Option[String] = None
  private var failOnError: Boolean = true

  def setBaseDir(baseDir: String): Unit = this.baseDir = Some(baseDir)
  def setServerUrl(serverUrl: String): Unit = this.serverUrl = Some(serverUrl)
  def setTags(tags: String): Unit = this.tags = Some(tags)
  def setFailOnError(fail: Boolean): Unit = this.failOnError = fail

  override def execute(): Unit = {
    val dir = baseDir getOrElse (throw new BuildException("baseDir can not be empty"))
    val url = serverUrl getOrElse (throw new BuildException("serverUrl can not be empty"))

    val directory = new File(dir)
    if (directory.isDirectory()) {
      log("uploading files from directory " + dir + " to " + url)

      import DmpsterUpload.fileFilter
      val dumpFiles = directory.listFiles((file: File) => file.getName.endsWith("dmp"))

      dumpFiles.map(file => Future { uploadFile(file, url, tags) })
        .foreach(Await.ready(_, Duration.Inf))
        
    } else {
      if (failOnError) throw new BuildException("'" + dir + "' is not a directory")
    }
  }

  def uploadFile(file: File, url: String, tags: Option[String]) = {
    log("uploading file " + file.getName())
    val client: HttpClient = new DefaultHttpClient()
    try {
      val requestEntity = new MultipartEntity()
      requestEntity.addPart("file", new FileBody(file))

      tags foreach { t => requestEntity.addPart("tags", new StringBody(t)) }

      val httpPost = new HttpPost(url)
      httpPost.setEntity(requestEntity)

      val response = client.execute(httpPost)
      val responseEntity = response.getEntity()
      println(Source.fromInputStream(responseEntity.getContent()).getLines.toList)
      EntityUtils.consume(responseEntity)
    } finally {
      client.getConnectionManager().shutdown()
    }
    log("finished uploading file " + file.getName())
  }
}

object DmpsterUpload {
  implicit def fileFilter(pred: (File => Boolean)): java.io.FileFilter = {
    new java.io.FileFilter() {
      override def accept(file: File) = pred(file)
    }
  }
}

