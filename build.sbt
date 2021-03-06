import Dependencies._

version in ThisBuild := "0.5"

scalaVersion in ThisBuild := "2.13.0"

val compilerOptions = Seq(
  "-language:postfixOps",
  "-language:higherKinds"
)

val scala211 = "2.11.12"
val scala212 = "2.12.9"
val scala213 = "2.13.1"

def createModule(
    moduleName: String,
    libDependencies: Seq[ModuleID] = Seq.empty,
    projectDependencies: Seq[ProjectReference] = Seq.empty,
    scalaVersions: Seq[String] = Seq(scala211, scala212, scala213)
  ) = Project(id = moduleName, base = file(moduleName))
  .dependsOn(projectDependencies.map(_ % "compile->compile;test->test"): _*)
  .settings(
    name := moduleName,
    libraryDependencies ++= libDependencies,
    test in Test := (test in Test dependsOn
      (projectDependencies.map(p => test in Test in p): _*)).value,
    scalacOptions ++= compilerOptions,
    parallelExecution in Test := false,
    crossScalaVersions := scalaVersions
  )

lazy val root = (project in file("."))
  .settings(
    name := "HTypes"
  )
  .aggregate(
    hTypesCore,
    hTypesAkkaStream,
    hTypesCatsEffect,
    hTypesZIO
  )
  .settings(skip in publish := true)

lazy val hTypesCore = createModule("htypes-core", commonDependencies)

lazy val hTypesAkkaStream = createModule("htypes-akka-stream", commonDependencies ++ akkaStream, Seq(hTypesCore))

lazy val hTypesCatsEffect = createModule("htypes-cats-effect", commonDependencies ++ catsEffect, Seq(hTypesCore))

lazy val hTypesZIO = createModule("htypes-zio", commonDependencies ++ zio, Seq(hTypesCore))
