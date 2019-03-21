name := "sns-order-auditing"
organization := "sns.lando"
version := "0.1"

scalaVersion := "2.12.8"

publishTo := Some(Resolver.file("file", new File(Path.userHome.absolutePath + "/.m2/repository")))

libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"
