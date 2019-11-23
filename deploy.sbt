organization in ThisBuild := "com.svenvandam"
homepage in ThisBuild := Some(url("https://github.com/SvenvDam/HTypes"))
scmInfo in ThisBuild := Some(ScmInfo(url("https://github.com/SvenvDam/HTypes.git"), "git@github.com:SvenvDam/HTypes.git"))
developers in ThisBuild := List(Developer("SvenvDam", "Sven van Dam", "", url("https://github.com/SvenvDam")))
licenses in ThisBuild += ("MIT", url("https://opensource.org/licenses/MIT"))
publishMavenStyle in ThisBuild := true
publishArtifact in Test := false

publishTo in ThisBuild := sonatypePublishToBundle.value
