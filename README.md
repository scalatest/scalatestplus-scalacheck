# ScalaTest Plus ScalaCheck
ScalaTest + ScalaCheck provides integration support between ScalaTest and ScalaCheck.

💖 Support ScalaTest
--------------------

[![Sponsor ScalaTest](https://img.shields.io/badge/sponsor-scalatest-ff69b4?logo=github-sponsors)](https://github.com/sponsors/scalatest)

ScalaTest has been a cornerstone of testing in the Scala ecosystem for over 17 years. It’s trusted by countless developers and teams to write expressive, flexible, and robust tests. We’ve always believed in keeping ScalaTest free and open source, but maintaining a tool used so widely takes time, care, and ongoing effort.

If ScalaTest has saved you time, helped you ship better software, or become a key part of your development workflow, please consider supporting our work. Your sponsorship helps us dedicate time to fixing bugs, improving documentation, adding new features, and keeping ScalaTest reliable for the entire community.

👉 [Become a sponsor for ScalaTest](https://github.com/sponsors/scalatest) to help keep Scala’s most widely used testing library thriving!

**Usage**

To use it for ScalaTest 3.2.20 and ScalaCheck 1.19.x: 

SBT: 

```
libraryDependencies += "org.scalatestplus" %% "scalacheck-1-19" % "3.2.20.0" % "test"
```

Maven: 

```
<dependency>
  <groupId>org.scalatestplus</groupId>
  <artifactId>scalacheck-1-19_3</artifactId>
  <version>3.2.20.0</version>
  <scope>test</scope>
</dependency>
```

For more information, please checkout our property-based testing user guide at https://www.scalatest.org/user_guide/property_based_testing .

**Publishing**

Please use the following commands to publish to Sonatype: 

```
$ sbt clean +publishSigned
$ sbt sonaUpload
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