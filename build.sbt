name := "ScalaHbase"

version := "0.1"

scalaVersion := "2.13.0"

val hBaseVersion = "2.2.0"
val hadoopVersion = "3.2.0"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion,
  "org.apache.hbase" % "hbase-common" % hBaseVersion,
  "org.apache.hbase" % "hbase-client" % hBaseVersion
)
