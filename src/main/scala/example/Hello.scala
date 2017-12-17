package example

import org.slf4j.LoggerFactory

object Hello extends Greeting with App {
  // val logger = LoggerFactory.getLogger(classOf[Hello])
  // logger.info(s"greeting is $greeting")
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
