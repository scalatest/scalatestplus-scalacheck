# ScalaTest Plus ScalaCheck
ScalaTest + ScalaCheck provides integration support between ScalaTest and ScalaCheck.

**Usage**

To use it for ScalaTest 3.2.3 and ScalaCheck 1.15.x: 

SBT: 

```
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-15" % "3.2.3.0" % "test"
```

Maven: 

```
<dependency>
  <groupId>org.scalatestplus</groupId>
  <artifactId>scalacheck-1-15</artifactId>
  <version>3.2.3.0</version>
  <scope>test</scope>
</dependency>
```

**Publishing**

Please use the following commands to publish to Sonatype: 

```
$ sbt clean +publishSigned
```
