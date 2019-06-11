# ScalaTest Plus ScalaCheck
ScalaTest + ScalaCheck provides integration support between ScalaTest and ScalaCheck.

**Publishing**

Please use the following commands to publish to Sonatype: 

```
$ export SCALAJS_VERSION=0.6.28
$ sbt clean scalatestPlusScalaCheckJS/publishSigned scalatestPlusScalaCheckJVM/publishSigned scalatestPlusScalaCheckNative/publishSigned
$ sbt ++2.10.7 clean scalatestPlusScalaCheckJS/publishSigned scalatestPlusScalaCheckJVM/publishSigned
$ sbt ++2.11.12 clean scalatestPlusScalaCheckJS/publishSigned scalatestPlusScalaCheckJVM/publishSigned
$ sbt ++2.13.0 clean scalatestPlusScalaCheckJS/publishSigned scalatestPlusScalaCheckJVM/publishSigned
$ export SCALAJS_VERSION=1.0.0-M3
$ sbt ++2.11.12 "project scalatestPlusScalaCheckJS" clean publishSigned
$ sbt ++2.12.6 "project scalatestPlusScalaCheckJS" clean publishSigned
$ export SCALAJS_VERSION=1.0.0-M8
$ sbt ++2.13.0 "project scalatestPlusScalaCheckJS" clean publishSigned
```
