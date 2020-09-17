package evaluator.client.model.cached

import evaluator.client.model.enumeration.Feeling
import utopia.genesis.shape.shape1D.Direction1D
import utopia.genesis.shape.shape1D.Direction1D.{Negative, Positive}

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
	// COMPUTED -------------------------
	
	/**
	  * @return The number of words within these results
	  */
	def size = associations.size + skippedWords.size
	
	/**
	  * @return Positive results [word -> feeling]
	  */
	def positive = apply(Positive)
	/**
	  * @return Negative results [word -> feeling]
	  */
	def negative = apply(Negative)
	
	/**
	  * @return Positively associated words
	  */
	def positiveWords = positive.keySet
	/**
	  * @return Negatively associated words
	  */
	def negativeWords = negative.keySet
	
	
	// OTHER    -------------------------
	
	def apply(word: String) = associations.get(word)
	
	def apply(feeling: Feeling) = associations.filter { _._2 == feeling }.keySet
	
	def apply(feelingSign: Direction1D) = associations.filter { _._2.sign == feelingSign }
	
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
