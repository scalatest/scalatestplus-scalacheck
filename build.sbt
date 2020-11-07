import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import scalanative.sbtplugin.ScalaNativePluginInternal.NativeTest
import scala.xml.{Node => XmlNode, NodeSeq => XmlNodeSeq, _}
import scala.xml.transform.{RewriteRule, RuleTransformer}

val sharedSettings = Seq(
  name := "scalacheck-1.15",
  organization := "org.scalatestplus",
  version := "3.2.3.0",
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
    "org.scalatest" %%% "scalatest-core" % "3.2.3",
    "org.scalacheck" %%% "scalacheck" % "1.15.1",
    "org.scalatest" %%% "scalatest-shouldmatchers" % "3.2.3" % "test",
    "org.scalatest" %%% "scalatest-funspec" % "3.2.3" % "test",
    "org.scalatest" %%% "scalatest-funsuite" % "3.2.3" % "test"
  ),
  // skip dependency elements with a scope
  pomPostProcess := { (node: XmlNode) =>
    new RuleTransformer(new RewriteRule {
      override def transform(node: XmlNode): XmlNodeSeq = node match {
        case e: Elem if e.label == "dependency"
            && e.child.exists(child => child.label == "scope") =>
          def txt(label: String): String = "\"" + e.child.filter(_.label == label).flatMap(_.text).mkString + "\""
          Comment(s""" scoped dependency ${txt("groupId")} % ${txt("artifactId")} % ${txt("version")} % ${txt("scope")} has been omitted """)
        case _ => node
      }
    }).transform(node).head
  },
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
  pomExtra := (
    <scm>
      <url>https://github.com/scalatest/scalatestplus-scalacheck</url>
      <connection>scm:git:git@github.com:scalatest/scalatestplus-scalacheck.git</connection>
      <developerConnection>
        scm:git:git@github.com:scalatest/scalatestplus-scalacheck.git
      </developerConnection>
    </scm>
  ),
  credentials += Credentials(Path.userHome / ".ivy2" / ".credentials"),
  scalacOptions in (Compile, doc) := {
    if (scalaBinaryVersion.value startsWith "0.2") 
      Seq.empty
    else
      Seq("-doc-title", s"ScalaTest + ScalaCheck ${version.value}")
  }, 
  publishArtifact in (Compile, packageDoc) := {
    if (scalaBinaryVersion.value startsWith "3.")
      false // Temporary disable publishing of doc in dotty, can't get it to build.
    else
      true  
  }
)

lazy val scalatestPlusScalaCheck =
  // select supported platforms
  crossProject(JSPlatform, JVMPlatform, NativePlatform)
    .crossType(CrossType.Pure) // [Pure, Full, Dummy], default: CrossType.Full
    .settings(sharedSettings)
    .enablePlugins(SbtOsgi)
    .settings(osgiSettings: _*).settings(
      scalaVersion := "2.13.3", 
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
      crossScalaVersions := List("2.11.12", "2.12.12", "2.13.3"),
      sourceGenerators in Compile += {
        Def.task {
          GenResourcesJSVM.genResources((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value) ++
          GenResourcesJSVM.genFailureMessages((sourceManaged in Compile).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value)
        }
      }
    )
    .jvmSettings(
      crossScalaVersions := List("2.11.12", "2.12.12", "2.13.3", "3.0.0-M1"),
      Test / scalacOptions ++= (if (isDotty.value) Seq("-language:implicitConversions") else Nil),
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
