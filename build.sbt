import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import scalanative.sbtplugin.ScalaNativePluginInternal.NativeTest

val sharedSettings = Seq(
  name := "scalatestplus-scalacheck",
  organization := "org.scalatestplus",
  version := "1.0.0-SNAP1",
  homepage := Some(url("https://github.com/scalatest/scalatestplus-scalacheck")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "bvenners",
      "Bill Venners",
      "bill@artima.com",
      url("https://github.com/bvenners")
    ),
    Developer(
      "cheeseng",
      "Chua Chee Seng",
      "cheeseng@amaseng.com",
      url("https://github.com/cheeseng")
    )
  ),
  crossScalaVersions := List("2.11.12", "2.12.8", "2.13.0-M5"),
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  libraryDependencies ++= Seq(
    "org.scalacheck" %%% "scalacheck" % "1.14.1-86bd34e-SNAPSHOT",
    "org.scalatest" %%% "scalatest" % "3.1.0-SNAP7"
  ),
  sourceGenerators in Compile += {
    Def.task {
      GenScalaCheckGen.genMain((sourceManaged in Compile).value / "org" / "scalatest" / "check", version.value, scalaVersion.value)
    }
  },
  sourceGenerators in Test += {
    Def.task {
      GenScalaCheckGen.genTest((sourceManaged in Test).value / "org" / "scalatest" / "check", version.value, scalaVersion.value)
    }
  },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    Some("publish-releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
  pgpSecretRing := file((Path.userHome / ".gnupg" / "secring.gpg").getAbsolutePath),
  pgpPassphrase := None
)

lazy val scalatestPlusScalaCheck =
  // select supported platforms
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .crossType(CrossType.Pure) // [Pure, Full, Dummy], default: CrossType.Full
    .settings(sharedSettings)
    .jsSettings(scalaVersion := "2.12.8")
    .jvmSettings(scalaVersion := "2.12.8")
    .nativeSettings(
      scalaVersion := "2.11.12", 
      nativeLinkStubs in NativeTest := true
    )

lazy val scalatestPlusScalaCheckJS     = scalatestPlusScalaCheck.js
lazy val scalatestPlusScalaCheckJVM    = scalatestPlusScalaCheck.jvm
lazy val scalatestPlusScalaCheckNative = scalatestPlusScalaCheck.native
