import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import scala.xml.{Node => XmlNode, NodeSeq => XmlNodeSeq, _}
import scala.xml.transform.{RewriteRule, RuleTransformer}
import java.io.PrintWriter
import scala.io.Source

val defaultScalaVersion = "2.13.13"

scalaVersion := defaultScalaVersion

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  Some("publish-releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact := false

publish := {}

publishLocal := {}

def docTask(docDir: File, resDir: File, projectName: String): File = {
  val docLibDir = docDir / "lib"
  val htmlSrcDir = resDir / "html"
  val cssFile = docLibDir / "template.css"
  val addlCssFile = htmlSrcDir / "addl.css"

  val css = Source.fromFile(cssFile).mkString
  val addlCss = Source.fromFile(addlCssFile).mkString

  if (!css.contains("pre.stHighlighted")) {
    val writer = new PrintWriter(cssFile)

    try {
      writer.println(css)
      writer.println(addlCss)
    }
    finally { writer.close }
  }

  if (projectName.contains("scalatest")) {
    (htmlSrcDir * "*.gif").get.foreach { gif =>
      IO.copyFile(gif, docLibDir / gif.name)
    }
  }
  docDir
}

val sharedSettings = Seq(
  name := "scalacheck-1.18",
  organization := "org.scalatestplus",
  version := "3.2.19.0",
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
    "org.scalatest" %%% "scalatest-core" % "3.2.19",
    "org.scalacheck" %%% "scalacheck" % "1.18.0",
    "org.scalatest" %%% "scalatest-shouldmatchers" % "3.2.18" % "test",
    "org.scalatest" %%% "scalatest-funspec" % "3.2.18" % "test",
    "org.scalatest" %%% "scalatest-funsuite" % "3.2.18" % "test"
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
  Compile / sourceGenerators  += {
    Def.task {
      GenScalaCheckGen.genMain((Compile / sourceManaged).value / "org" / "scalatest" / "check", version.value, scalaVersion.value)
    }
  },
  Test / sourceGenerators += {
    Def.task {
      GenScalaCheckGen.genTest((Test / sourceManaged).value / "org" / "scalatest" / "check", version.value, scalaVersion.value)
    }
  },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    Some("publish-releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  Test / publishArtifact := false,
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
  Compile / doc := docTask((Compile / doc).value,
                          (Compile / sourceDirectory).value,
                          name.value), 
  Compile / doc / scalacOptions := {
    if (scalaBinaryVersion.value startsWith "3") 
      Seq.empty
    else
      Seq("-doc-title", s"ScalaTest + ScalaCheck ${version.value}", 
          "-sourcepath", baseDirectory.value.getParentFile().getAbsolutePath(), 
          "-doc-source-url", s"https://github.com/scalatest/releases-source/blob/main/scalatestplus-scalacheck/${version.value}€{FILE_PATH}.scala")
  }, 
  Compile / packageDoc / publishArtifact := {
    if (scalaBinaryVersion.value startsWith "3")
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
      scalaVersion := defaultScalaVersion, 
      crossScalaVersions := List("2.12.19", defaultScalaVersion, "3.3.3"),
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
      Compile / sourceGenerators += {
        Def.task {
          GenResourcesJSVM.genResources((Compile / sourceManaged).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value) ++
          GenResourcesJSVM.genFailureMessages((Compile / sourceManaged).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value)
        }
      }
    )
    .jvmSettings(
      Compile / sourceGenerators += {
        Def.task {
          GenResourcesJVM.genResources((Compile / sourceManaged).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value) ++
          GenResourcesJVM.genFailureMessages((Compile / sourceManaged).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value)
        }
      }
    )
    .nativeSettings(
      Compile / sourceGenerators += {
        Def.task {
          GenResourcesJSVM.genResources((Compile / sourceManaged).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value) ++
          GenResourcesJSVM.genFailureMessages((Compile / sourceManaged).value / "org" / "scalatestplus" / "scalacheck", version.value, scalaVersion.value)
        }
      }
    )

lazy val scalatestPlusScalaCheckJS     = scalatestPlusScalaCheck.js
lazy val scalatestPlusScalaCheckJVM    = scalatestPlusScalaCheck.jvm
lazy val scalatestPlusScalaCheckNative = scalatestPlusScalaCheck.native
