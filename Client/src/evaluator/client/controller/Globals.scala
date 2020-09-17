package evaluator.client.controller

import utopia.bunnymunch.jawn.JsonBunny
import utopia.flow.async.ThreadPool
import utopia.flow.parse.JsonParser

import scala.concurrent.ExecutionContext

/**
  * Globally used values for this project
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
object Globals
{
	/**
	  * Globally used execution context
	  */
	implicit val exc: ExecutionContext = new ThreadPool("E-Valuator").executionContext
	/**
	  * Globally used json parser
	  */
	implicit val jsonParser: JsonParser = JsonBunny
}
