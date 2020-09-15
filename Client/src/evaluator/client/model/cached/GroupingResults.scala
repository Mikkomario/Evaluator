package evaluator.client.model.cached

import evaluator.client.model.enumeration.Feeling

object GroupingResults
{
	/**
	  * An empty set of results
	  */
	val empty = GroupingResults(Map(), Set())
}

/**
  * Contains user selection results
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
case class GroupingResults(associations: Map[String, Feeling], skippedWords: Set[String])
{
	/**
	  * @param association A word -> feeling association
	  * @return A copy of these results with specified association added
	  */
	def +(association: (String, Feeling)) = copy(associations = associations + association)
	
	/**
	  * @param word A skipped word
	  * @return A copy of these results with a skipped word registered
	  */
	def skipping(word: String) = copy(skippedWords = skippedWords + word)
}
