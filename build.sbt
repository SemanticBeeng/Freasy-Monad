crossScalaVersions in ThisBuild := Seq("2.11.12", "2.12.2")

val version_scala = "2.11.12"
val version_macrosParadise = "2.1.1"

scalaVersion in ThisBuild := version_scala

lazy val root = project.in(file("."))
  .aggregate(js, jvm)
  .settings(publishArtifact := false)

val nexus = "https://oss.sonatype.org/"
val groupId = "com.github.thangiee"

lazy val shared = crossProject.in(file("."))
  .settings(
    name := "freasy-monad",
    version := "0.6.0",
    organization := groupId,
    scalacOptions ++= Seq(
      "-feature",
      "-encoding", "UTF-8",
      "-unchecked",
      "-deprecation",
      "-Xfuture",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-unchecked",
      "-Xplugin-require:macroparadise"
    ),
    resolvers += Resolver.bintrayIvyRepo("scalameta", "maven"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % "1.1.0" % "provided",
      "org.scalaz" %% "scalaz-core" % "7.2.21" % "provided",
      "org.scalatest" %% "scalatest" % "3.0.0" % "test",
      "org.scalameta" %% "scalameta" % "3.7.4",
      "org.scalameta" %% "contrib" % "3.7.4"
    ) /*++
    Seq(
        "org.typelevel" %% "macro-compat" % "1.1.1",
        "org.scala-lang" % "scala-reflect" % version_scala % "provided",
        "org.scala-lang" % "scala-compiler" % version_scala % "provided",
        compilerPlugin("org.scalamacros" % "paradise" % version_macrosParadise cross CrossVersion.patch)
      )*/,
    addCompilerPlugin( "org.scalameta" %% "paradise" % "3.0.0-M11" cross CrossVersion.full),
    publishSettings
  )
  .jvmSettings()
  .jsSettings()

lazy val jvm = shared.jvm
lazy val js = shared.js

lazy val publishSettings = Seq(
  // temporary workaround for https://github.com/scalameta/paradise/issues/55
  sources in (Compile, doc) := Nil, // macroparadise doesn't work with scaladoc yet.
  sonatypeProfileName := groupId,
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
    else                  Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  pomExtra :=
    <url>https://github.com/Thangiee/Freasy-Monad</url>
      <licenses>
        <license>
          <name>MIT license</name>
          <url>http://www.opensource.org/licenses/mit-license.php</url>
        </license>
      </licenses>
      <scm>
        <url>git://github.com/Thangiee/Freasy-Monad.git</url>
        <connection>scm:git://github.com/Thangiee/Freasy-Monad.git</connection>
      </scm>
      <developers>
        <developer>
          <id>Thangiee</id>
          <name>Thang Le</name>
          <url>https://github.com/Thangiee</url>
        </developer>
      </developers>
)

addCommandAlias("publishFreasyMonad", "publishSigned")
addCommandAlias("releaseFreasyMonad", s"sonatypeReleaseAll $groupId")
