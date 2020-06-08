# ScalaTest Plus ScalaCheck
ScalaTest + ScalaCheck provides integration support between ScalaTest and ScalaCheck.

**Usage**

To use it for ScalaTest 3.1.0 and ScalaCheck 1.14.x: 

SBT: 

```
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-14" % "3.1.2.0" % "test"
```

Maven: 

```
<dependency>
  <groupId>org.scalatestplus</groupId>
  <artifactId>scalacheck-1-14</artifactId>
  <version>3.1.0.0</version>
  <scope>test</scope>
</dependency>
```


**Publishing**

Please use the following commands to publish to Sonatype: 

```
$ export SCALAJS_VERSION=0.6.32
$ sbt clean +scalatestPlusScalaCheckJS/publishSigned +scalatestPlusScalaCheckJVM/publishSigned scalatestPlusScalaCheckNative/publishSigned
$ export SCALAJS_VERSION=1.0.0
$ sbt ++2.11.12 "project scalatestPlusScalaCheckJS" clean publishSigned
$ sbt ++2.12.10 "project scalatestPlusScalaCheckJS" clean publishSigned
$ sbt ++2.13.1 "project scalatestPlusScalaCheckJS" clean publishSigned
```
