name := "ScalaHbase"

version := "0.1"

scalaVersion := "2.13.0"

val hBaseVersion = "2.2.0"
val hadoopVersion = "3.2.0"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion,
  "org.apache.hbase" % "hbase-common" % hBaseVersion,
  "org.apache.hbase" % "hbase-client" % hBaseVersion,

  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
  "org.mockito" % "mockito-all" % "1.10.19" % Test,

  "org.apache.hbase" % "hbase-server" % hBaseVersion % Test,
  "org.apache.hbase" % "hbase-server" % hBaseVersion % Test classifier "tests",
  "org.apache.hbase" % "hbase-common" % hBaseVersion % Test classifier "tests",
  "org.apache.hadoop" % "hadoop-common" % hadoopVersion % Test classifier "tests",
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion % Test,
  "org.apache.hadoop" % "hadoop-hdfs" % hadoopVersion % Test classifier "tests",
  "org.apache.hbase" % "hbase-hadoop-compat" % hBaseVersion % Test,
  "org.apache.hbase" % "hbase-hadoop2-compat" % hBaseVersion % Test,
  "org.apache.hbase" % "hbase-hadoop-compat" % hBaseVersion % Test classifier "tests",
  "org.apache.hbase" % "hbase-hadoop2-compat" % hBaseVersion % Test classifier "tests"
)
