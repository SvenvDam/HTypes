organization := "com.svenvandam"
homepage := Some(url("https://github.com/SvenvDam/HTypes"))
scmInfo := Some(ScmInfo(url("https://github.com/SvenvDam/HTypes.git"), "git@github.com:SvenvDam/HTypes.git"))
developers := List(
  Developer(
    "SvenvDam",
    "Sven van Dam",
    "",
    url("https://github.com/SvenvDam")))
licenses += ("MIT", url("https://opensource.org/licenses/MIT"))
publishMavenStyle := true
publishArtifact in Test := false

publishTo in ThisBuild := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)
