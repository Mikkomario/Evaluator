package evaluator.client.controller

/**
  * Used for accessing tested list of words
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
object Words
{
	// ATTRIBUTES   -------------------------------
	
	private val values = Vector("Test 1", "Test 2", "Test 3")
	
	
	// COMPUTED -----------------------------------
	
	/**
	  * @return An infinite iterator that returns words
	  */
	def iterator =
	{
		// TODO: This is just a placeholder implementation
		var currentIterator = values.iterator
		Iterator.continually {
			if (!currentIterator.hasNext)
				currentIterator = values.iterator
			currentIterator.next()
		}
	}
}
