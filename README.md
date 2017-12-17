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
