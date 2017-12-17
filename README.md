# hellosbt
SBT Getting Started repo

Project and source from http://www.scala-sbt.org/1.x/docs/Getting-Started.html.

# Notes

## 2017-12-16 Getting Started

### Directory Structure

http://www.scala-sbt.org/1.x/docs/Directories.html

The target/ pattern happens at numerous places in SBT's directory structure and there are a lot of files laying around
even after `$ sbt clean`.

The results of an `$ sbt compile` appear to be at `target/scala-2.12/classes`.  When you `$ sbt clean`, that stuff gets
whacked.

### Running

http://www.scala-sbt.org/1.x/docs/Running.html

Running tests and specific tests is pretty straight forward.  `$ sbt clean compile "testOnly example.HelloSpec"`
cleaned, compiled, and ran a specific Spec.  The docs call this "Batch mode".

They recommend using the sbt shell to do all of this.  Within the sbt shell the `"testOnly example.HelloSpec"` doesn't
need the quotes:  `sbt:HelloSBT> testOnly exampleHelloSpec`.  The JVM startup only happens once.  It appears to be
hot-swapping the changes well.

So the REPL reachable from sbt is called the Console:

    sbt:HelloSBT> console
    scala> println("hello")
    hello

    scala>

^D gets you out.

There is an SBT Shell within IntelliJ.  Stuff seems to run faster from the command line, but this gets your REPL right
into the IDE.  You can't get out of `console` in the IntelliJ one with ^D, but it turns out that `:help` is available
and `:quit` gets you out.

The Shell has a history command, but its not "history".  It is `!:` to show all history, or like `!:10` to show the last
10 commands.  There is a bunch of stuff you can do:  http://www.scala-sbt.org/1.x/docs/Running.html#History+Commands

### Build Definition

http://www.scala-sbt.org/1.x/docs/Basic-Def.html

You bind your project to a specific version of SBT through the `project/build.properties` file.  This allows other
SBT versions to use the correct version for the project.  If the required version is not available locally, the sbt
launcher will download it for you

"A build definition is defined in build.sbt, and it consists of a set of projects (of type Project). Because the term
project can be ambiguous, we often call it a subproject in this guide." - Uh, oh.

The "HelloSBT" project was boot strapped using some `$ sbt new scala/scala-seed.g8` workflow and I end up with a
build.sbt file that has more than a few differences relative to the once from the docs.

build.sbt from scala/scala-seed.g8 | build.sbt from http://www.scala-sbt.org/1.x/docs/Basic-Def.html#What+is+a+build+definition%3F
- | -
```lazy val root = (project in file(".")).
     settings(
       inThisBuild(List(
         organization := "com.example",
         scalaVersion := "2.12.3",
         version      := "0.1.0-SNAPSHOT"
       )),
       name := "HelloSBT",
       libraryDependencies += scalaTest % Test
     )``` |
```lazy val root = (project in file("."))
     .settings(
       name := "Hello",
       scalaVersion := "2.12.3"
     )```

Which way is "correct"?  Googling around this comes up in something called "Scopes".  I'm not going to worry about it
now.  It does seem like for a simple HelloSBT project the inThisBuild stuff is overkill.  I think I could have done it
all with just up-the-middle settings.