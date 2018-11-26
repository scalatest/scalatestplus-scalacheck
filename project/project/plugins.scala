import sbt._
import Keys._

object ScalaTestPlusScalaCheckBuildPlugins extends Build {
  val root = Project("scalatestplus-scalacheck-plugins", file(".")) settings(
    libraryDependencies += "org.antlr" % "stringtemplate" % "3.2"
  )
}

