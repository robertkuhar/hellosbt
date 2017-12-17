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

build.sbt from scala/scala-seed.g8:
```
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "HelloSBT",
    libraryDependencies += scalaTest % Test
  )
```

build.sbt from http://www.scala-sbt.org/1.x/docs/Basic-Def.html#What+is+a+build+definition%3F:
```
lazy val root = (project in file("."))
  .settings(
    name := "Hello",
    scalaVersion := "2.12.3"
  )
```

Which way is "correct"?  Googling around this comes up in something called "Scopes".  I'm not going to worry about it
now.  It does seem like for a simple HelloSBT project the inThisBuild stuff is overkill.  I think I could have done it
all with just up-the-middle settings.  I'm beginning to remember my distaste for SBT (and I think I projected this onto
Scala as well).  Incidental Complexity.

The contents of `.settings` is a sequence of key-value pairs called "Setting Expressions" using the *build.sbt DSL*.

"Setting Expressions" aren't the only thing that can appear here.  Apparently there are also "Task Expressions" and
some "Input thingie" which they present in the docs as `SettingKey[T]`, `TaskKey[T]`, or `InputKey[T]`.

Setting Keys are Strings, so numeric values are right out.

"The build.sbt may also be interspersed with `vals`, `lazy vals`, and `defs`.  Top-level objects and classes are not
allowed in build.sbt.  Those should go in the project/ directory as Scala source files." - yeah, this is also where I
started to get worried before.  You've got to wander the build.sbt and project/ directory to figure out what is going
on.  Its not a "everything is in one place" thing.  I bet IntelliJ helps here.

"There are three flavors of key":

1. SettingKey[T]: a key for a value computed once (the value is computed when loading the subproject, and kept around).
1. TaskKey[T]: a key for a value, called a task, that has to be recomputed each time, potentially with side effects.
1. InputKey[T]: a key for a task that has command line arguments as input. Check out Input Tasks for more details.

Its a bizarre 2-step to add a Task.  First you need to get the task a name and a Type, then you can use it in the
.settings thing.

```
lazy val root = (project in file("."))
  .settings(
    name := "HelloSBT",
    ...
    hello := { println("Hello!") },
    ...
  )

// This declaration makes me able to use hello in .settings as a Task.
lazy val hello = taskKey[Unit]("An example task")
```

"If you type the name of a setting key rather than a task key, the value of the setting key will be displayed. Typing a
task key name executes the task but doesn’t display the resulting value; to see a task’s result, use show <task name>
rather than plain <task name>. The convention for keys names is to use camelCase so that the command line name and the
Scala identifiers are the same." - Great.  The task executes, but you don't get to see the output?!?

```
sbt:HelloSBT> hello
Hello!
[success] Total time: 0 s, completed Dec 16, 2017 5:55:57 PM
sbt:HelloSBT> show hello
Hello!
[info] ()
[success] Total time: 0 s, completed Dec 16, 2017 5:56:01 PM
sbt:HelloSBT> show hello
Hello!
[info] ()
[success] Total time: 0 s, completed Dec 16, 2017 5:58:24 PM
```
The `[info] ()` is the return value of hello which is a `Unit` (what Scala calls the concept of *void*)

I think this stupid, but I can work with it.  Modifications to the `hello` task configuration to allow it to return the
String "OK" on completion was easy enough.  Change the return type down in that `lazy val hello` and modify the task
code in `.settings`.

```
lazy val root = (project in file("."))
  .settings(
    name := "HelloSBT",
    ...
    hello := {
      println("Hello!")
      "OK"
    },
    ...
  )

lazy val hello = taskKey[String]("An example task")
```

From the REPL `hello` Task execution now looks like...

```
sbt:HelloSBT> reload
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/robert.kuhar/.sbt/1.0/plugins
[info] Loading project definition from /Users/robert.kuhar/dev/hellosbt/project
[info] Loading settings from build.sbt ...
[info] Set current project to HelloSBT (in build file:/Users/robert.kuhar/dev/hellosbt/)
sbt:HelloSBT> show hello
Hello!
[info] OK
[success] Total time: 0 s, completed Dec 16, 2017 5:58:32 PM
sbt:HelloSBT> hello
Hello!
[success] Total time: 0 s, completed Dec 16, 2017 5:58:42 PM
sbt:HelloSBT>
```

From the command line you also have to be explicit if you want the return...
```
tcc-rkuhar:hellosbt robert.kuhar$ sbt "show hello"
[info] Loading settings from idea.sbt ...
[info] Loading global plugins from /Users/robert.kuhar/.sbt/1.0/plugins
[info] Loading project definition from /Users/robert.kuhar/dev/hellosbt/project
[info] Loading settings from build.sbt ...
[info] Set current project to HelloSBT (in build file:/Users/robert.kuhar/dev/hellosbt/)
Hello!
[info] OK
[success] Total time: 0 s, completed Dec 16, 2017 6:05:33 PM
```

## 2017-12-17 Getting Started, Adding library dependencies

http://www.scala-sbt.org/1.x/docs/Basic-Def.html#Adding+library+dependencies

I successfully added a logging dependency but I can't figure out how to instantiate a Logger correctly in my Hello
App!?!  This is now https://stackoverflow.com/questions/47859580/why-does-classoft-not-work-in-my-object
