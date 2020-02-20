addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "0.6.0")

addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "0.6.0")

val scalaJSVersion = Option(System.getenv("SCALAJS_VERSION")).getOrElse("1.0.0")

addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % scalaJSVersion)

addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.4.0-M2")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")

addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.2.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.9.4")
