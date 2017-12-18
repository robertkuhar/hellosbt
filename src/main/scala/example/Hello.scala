package example

import org.slf4j.LoggerFactory

class Hello

object Hello extends Greeting with App {
  val logger = LoggerFactory.getLogger(classOf[Hello])
  logger.info(s"greeting is $greeting")

  logger.trace("message was logged at TRACE")
  logger.debug("message was logged at DEBUG")
  logger.info("message was logged at INFO")
  logger.warn("message was logged at WARN")
  logger.error("message was logged at ERROR")

  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
