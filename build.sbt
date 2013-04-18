name := "uploader"

version := "1.0"

scalaVersion := "2.10.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Classpaths.typesafeResolver

libraryDependencies += "org.apache.ant" % "ant" % "1.8.4"

libraryDependencies += "org.apache.httpcomponents" % "httpmime" % "4.2.3"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.2.3" 

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.1.2"