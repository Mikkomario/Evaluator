package evaluator.client.controller

import Globals._
import utopia.flow.container.ValueFileContainer
import utopia.flow.util.FileExtensions._

/**
  * Used for accessing tested list of words
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
object Words
{
	// ATTRIBUTES   -------------------------------
	
	// TODO: Update path (and content)
	private val container = new ValueFileContainer("Client/data/words.json")
	private val valuesView = container.pointer.lazyMap { _.getVector.map { _.getString } }
	
	
	// COMPUTED -----------------------------------
	
	/**
	  * @return Currently available list of words
	  */
	def values = valuesView.get
}
