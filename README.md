# ScalaTest Plus ScalaCheck
ScalaTest + ScalaCheck provides integration support between ScalaTest and ScalaCheck.

**Usage**

To use it for ScalaTest 3.2.18 and ScalaCheck 1.17.x: 

SBT: 

```
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-17" % "3.2.18.0" % "test"
```

Maven: 

```
<dependency>
  <groupId>org.scalatestplus</groupId>
  <artifactId>scalacheck-1-17_2.13</artifactId>
  <version>3.2.18.0</version>
  <scope>test</scope>
</dependency>
```

For more information, please checkout our property-based testing user guide at https://www.scalatest.org/user_guide/property_based_testing .

**Publishing**

Please use the following commands to publish to Sonatype: 

```
$ sbt clean +publishSigned
```

**Creating Scaladoc**

1. Run sbt doc: 

```
> sbt doc
```

2. Copy generated files in .jvm to main source: 

```
> cp -r scalatestPlusScalaCheck/.jvm/target/scala-2.13/src_managed/main/org scalatestPlusScalaCheck/src/main/scala/
```

3. Comment out source generators in build.sbt under sharedSettings and scalatestPlusScalaCheck's .jvmSettings .

4. Syntax highlight the source: 

```
> cd ../highlight-scaladoc
> ant highlight-in-place -Dsrcdir=../scalatestplus-scalacheck/scalatestPlusScalaCheck/src/main
```
5. Rebuild the scaladoc:: 

```
> cd ../scalatestplus-scalacheck
> sbt scalatestPlusScalaCheckJVM/clean scalatestPlusScalaCheckJVM/doc
```

6. Copy out the scaladoc and add ads section: 

```
> cp -r scalatestPlusScalaCheck/.jvm/target/scala-2.13/api ../scalatest-doc
> cd ../scalatest-doc
> ../highlight-scaladoc/scripts/add_adbutler_scalatest.sh
> ../highlight-scaladoc/scripts/add_ga4_scalatest.sh
```