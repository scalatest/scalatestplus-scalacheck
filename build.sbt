import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import scalanative.sbtplugin.ScalaNativePluginInternal.NativeTest

val sharedSettings = Seq(
  name := "scalacheck-1.14",
  organization := "org.scalatestplus",
  version := "3.2.0.0-M1",
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
  resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  libraryDependencies ++= Seq(
    "org.scalatest" %%% "scalatest-core" % "3.2.0-M1", 
    "org.scalatest" %%% "scalatest-shouldmatchers" % "3.2.0-M1" % "test", 
    "org.scalatest" %%% "scalatest-funspec" % "3.2.0-M1" % "test",
    "org.scalatest" %%% "scalatest-funsuite" % "3.2.0-M1" % "test"
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
    .enablePlugins(SbtOsgi)
    .settings(osgiSettings: _*).settings(
      OsgiKeys.exportPackage := Seq(
        "org.scalatestplus.scalacheck.*"
      ),
      OsgiKeys.importPackage := Seq(
        "org.scalatest.*",
        "org.scalactic.*", 
        "scala.*;version=\"$<range;[==,=+);$<replace;"+scalaBinaryVersion.value+";-;.>>\"",
        "*;resolution:=optional"
      ),
      OsgiKeys.additionalHeaders:= Map(
        "Bundle-Name" -> "ScalaTestPlusScalaCheck",
        "Bundle-Description" -> "ScalaTest+ScalaCheck is an open-source integration library between ScalaTest and ScalaCheck for Scala projects.",
        "Bundle-DocURL" -> "http://www.scalacheck.org/",
        "Bundle-Vendor" -> "Artima, Inc."
      )
    )
    .jsSettings(
      crossScalaVersions := List("2.10.7", "2.11.12", "2.12.10", "2.13.1"),
      libraryDependencies ++= Seq(
        "org.scalacheck" %%% "scalacheck" % "1.14.1"
      ), 
      sourceGenerators in Compile += {
        Def.task {
          GenResourcesJSVM.genResources((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value) ++
          GenResourcesJSVM.genFailureMessages((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value)
        }
      }
    )
    .jvmSettings(
      crossScalaVersions := List("2.10.7", "2.11.12", "2.12.10", "2.13.1"),
      libraryDependencies ++= Seq(
        "org.scalacheck" %%% "scalacheck" % "1.14.1"
      ), 
      sourceGenerators in Compile += {
        Def.task {
          GenResourcesJVM.genResources((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value) ++
          GenResourcesJVM.genFailureMessages((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value)
        }
      }
    )
    .nativeSettings(
      scalaVersion := "2.11.12", 
      nativeLinkStubs in NativeTest := true, 
      libraryDependencies ++= Seq(
        "org.scalacheck" %%% "scalacheck" % "1.14.1"
      ), 
      sourceGenerators in Compile += {
        Def.task {
          GenResourcesJSVM.genResources((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value) ++
          GenResourcesJSVM.genFailureMessages((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value)
        }
      }
    )

lazy val scalatestPlusScalaCheckJS     = scalatestPlusScalaCheck.js
lazy val scalatestPlusScalaCheckJVM    = scalatestPlusScalaCheck.jvm
lazy val scalatestPlusScalaCheckNative = scalatestPlusScalaCheck.native
