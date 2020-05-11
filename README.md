# ScalaTest Plus ScalaCheck
ScalaTest + ScalaCheck provides integration support between ScalaTest and ScalaCheck.

**Publishing**

Please use the following commands to publish to Sonatype: 

```
$ export SCALAJS_VERSION=0.6.32
$ sbt clean +scalatestPlusScalaCheckJS/publishSigned +scalatestPlusScalaCheckJVM/publishSigned scalatestPlusScalaCheckNative/publishSigned
$ export SCALAJS_VERSION=1.0.1
$ sbt ++2.11.12 "project scalatestPlusScalaCheckJS" clean publishSigned
$ sbt ++2.12.11 "project scalatestPlusScalaCheckJS" clean publishSigned
$ sbt ++2.13.2 "project scalatestPlusScalaCheckJS" clean publishSigned
```
