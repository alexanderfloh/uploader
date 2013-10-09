uploader
========
_uploader_ is an Ant-task that allows automatically uploading `.dmp`-files to a [dmpster](https://github.com/alexanderfloh/dmpster) instance.

##Usage##
* Put the `uploader.jar` and its dependencies in a directory of your convenience
* Adapt your Ant-file and add the following:

```xml
<taskdef name="uploadDmps" className="DmpsterUpload">
  <classpath>
	  <fileset dir="lib/uploader">
		  <include name="*.jar" />
		</fileset>
	</classpath>
</taskdef>

<uploadDmps baseDir="${url.logfiles.source}" 
            serverUrl="http://lnz-dmpster/upload" 
            tags="${env.#sctm_execdef_name}, ${env.#sctm_build}, ${env.#sctm_version}" />
```
* `baseDir` is the directory where _uploader_ is looking for `.dmp` files.
* `serverUrl` is the URL to your _dmpster_ instance.
* `tags` is a comma-separated-list of tags that will be automatically added to the dmp.

##Building##
* Install sbt amd add it to your `PATH`.
* Check out the _uploader_ sources and navigate to the directory.
* type `sbt package` to create `uploader.jar`.
* You'll also need some dependent libs: akka-actor, commons-codec, commons-logging, httpclient, httpcore, httpmime and scala-library. You can find those libs in your local Ivy cache (usually in `<your home directory>/.ivy2/cache`).
