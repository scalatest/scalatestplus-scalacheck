import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import scalanative.sbtplugin.ScalaNativePluginInternal.NativeTest

val sharedSettings = Seq(
                       name := "scalatestplus-scalacheck", 
                       organization := "org.scalatest", 
                       version := "3.1.0-SNAP7", 
                       resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots", 
                       libraryDependencies ++= Seq(
                         "org.scalacheck" %%% "scalacheck" % "1.14.1-ccf4236-SNAPSHOT", 
                         "org.scalatest" %%% "scalatest" % "3.1.0-SNAP7-local-feature-separate-scalacheck-module"
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
                       }
                     )

lazy val scalatestPlusScalaCheck =
  // select supported platforms
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .crossType(CrossType.Pure) // [Pure, Full, Dummy], default: CrossType.Full
    .settings(sharedSettings)
    .jsSettings(scalaVersion := "2.12.7")
    .jvmSettings(scalaVersion := "2.12.7")
    .nativeSettings(
      scalaVersion := "2.11.12", 
      nativeLinkStubs in NativeTest := true
    )

lazy val scalatestPlusScalaCheckJS     = scalatestPlusScalaCheck.js
lazy val scalatestPlusScalaCheckJVM    = scalatestPlusScalaCheck.jvm
lazy val scalatestPlusScalaCheckNative = scalatestPlusScalaCheck.native
