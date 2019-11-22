import sbt._

object Dependencies {
  val hBaseVersion = "2.2.0"
  val hadoopVersion = "2.8.5"
  val akkaStreamVersion = "2.6.0"

  lazy val commonDependencies = Seq(
    "org.apache.hbase"           % "hbase-client"         % hBaseVersion  % Provided,
    "com.typesafe.scala-logging" %% "scala-logging"       % "3.9.2"       % Test,
    "org.scalatest"              %% "scalatest"           % "3.0.8"       % Test,
    "org.apache.hadoop"          % "hadoop-common"        % hadoopVersion % Test,
    "org.apache.hbase"           % "hbase-server"         % hBaseVersion  % Test,
    "org.apache.hadoop"          % "hadoop-hdfs"          % hadoopVersion % Test,
    "org.apache.hbase"           % "hbase-hadoop-compat"  % hBaseVersion  % Test,
    "org.apache.hbase"           % "hbase-hadoop2-compat" % hBaseVersion  % Test,
    "org.apache.hbase"           % "hbase-testing-util"   % hBaseVersion  % Test
  )

  lazy val akkaStream = Seq(
    "com.typesafe.akka" %% "akka-stream"         % akkaStreamVersion % Provided,
    "com.typesafe.akka" %% "akka-stream-testkit" % akkaStreamVersion % Test
  )
}
