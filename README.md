# ScalaTest Plus ScalaCheck
ScalaTest + ScalaCheck provides integration support between ScalaTest and ScalaCheck.

**Usage**

To use it for ScalaTest 3.2.2 and ScalaCheck 1.14.x: 

SBT: 

```
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-14" % "3.2.2.0" % "test"
```

Maven: 

```
<dependency>
  <groupId>org.scalatestplus</groupId>
  <artifactId>scalacheck-1-14</artifactId>
  <version>3.2.2.0</version>
  <scope>test</scope>
</dependency>
```

**Publishing**

Please use the following commands to publish to Sonatype: 

```
$ export SCALAJS_VERSION=0.6.33
$ sbt clean +scalatestPlusScalaCheckJS/publishSigned +scalatestPlusScalaCheckJVM/publishSigned scalatestPlusScalaCheckNative/publishSigned
$ export SCALAJS_VERSION=1.1.1
$ sbt ++2.11.12 "project scalatestPlusScalaCheckJS" clean publishSigned
$ sbt ++2.12.12 "project scalatestPlusScalaCheckJS" clean publishSigned
$ sbt ++2.13.3 "project scalatestPlusScalaCheckJS" clean publishSigned
```
