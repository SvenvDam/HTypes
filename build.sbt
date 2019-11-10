name := "HTypes"

version := "0.3"

scalaVersion := "2.13.0"
crossScalaVersions := Seq("2.13.0", "2.12.9", "2.11.12")

val hBaseVersion = "2.2.0"
val hadoopVersion = "2.8.5"

scalacOptions ++= Seq(
  "-language:postfixOps",
  "-language:higherKinds"
)

parallelExecution in Test := false

libraryDependencies ++= Seq(
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
