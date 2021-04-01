addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.0.0")

addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.0.0")

val scalaJSVersion = Option(System.getenv("SCALAJS_VERSION")).getOrElse("1.5.0")

addSbtPlugin("org.scala-js"       % "sbt-scalajs"                   % scalaJSVersion)

addSbtPlugin("org.scala-native"   % "sbt-scala-native"              % "0.4.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "2.0.1")

addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.9.6")

addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.5.4")
