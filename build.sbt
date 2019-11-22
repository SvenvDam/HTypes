import Dependencies._

version in ThisBuild := "0.3"

scalaVersion in ThisBuild := "2.13.0"
crossScalaVersions := Seq("2.13.0", "2.12.9", "2.11.12")

val compilerOptions = Seq(
  "-language:postfixOps",
  "-language:higherKinds"
)

def createModule(
    moduleName: String,
    libDependencies: Seq[ModuleID] = Seq.empty,
    projectDependencies: Seq[ProjectReference] = Seq.empty
  ) = Project(id = moduleName, base = file(moduleName))
  .dependsOn(projectDependencies.map(_ % "compile->compile;test->test"): _*)
  .configs(IntegrationTest)
  .settings(
    name := moduleName,
    libraryDependencies ++= libDependencies,
    test in Test := (test in Test dependsOn
      (projectDependencies.map(p => test in Test in p): _*)).value,
    scalacOptions ++= compilerOptions,
    parallelExecution in Test := false
  )

lazy val root = (project in file("."))
  .aggregate(
    hTypesCore,
    hTypesAkkaStream
  )
  .settings(skip in publish := true)

lazy val hTypesCore = createModule("htypes", commonDependencies)

lazy val hTypesAkkaStream = createModule("htypes-akka-stream", commonDependencies ++ akkaStream, Seq(hTypesCore))
