import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val sharedSettings = Seq(
                       name := "scalatestplus-scalacheck", 
                       organization := "org.scalatest", 
                       version := "3.1.0-SNAP7", 
                       scalaVersion := "2.11.12", 
                       resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots", 
                       libraryDependencies ++= Seq(
                         "org.scalacheck" %%% "scalacheck" % "1.14.1-ccf4236-SNAPSHOT", 
                         "org.scalatest" %%% "scalatest" % "3.1.0-SNAP6"
                       ), 
                       sourceGenerators in Compile += {
                         Def.task {
                           GenScalaCheckGen.genMain((sourceManaged in Compile).value / "org" / "scalatest" / "check", version.value, scalaVersion.value)
                         }
                       } 
                     )

lazy val scalatestPlusScalaCheck =
  // select supported platforms
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .crossType(CrossType.Pure) // [Pure, Full, Dummy], default: CrossType.Full
    .settings(sharedSettings)

lazy val scalatestPlusScalaCheckJS     = scalatestPlusScalaCheck.js
lazy val scalatestPlusScalaCheckJVM    = scalatestPlusScalaCheck.jvm
lazy val scalatestPlusScalaCheckNative = scalatestPlusScalaCheck.native
