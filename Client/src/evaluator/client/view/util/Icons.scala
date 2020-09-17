package evaluator.client.view.util

import evaluator.client.model.enumeration.Feeling
import evaluator.client.model.enumeration.Feeling._
import utopia.flow.util.FileExtensions._
import utopia.reflection.image.{SingleColorIcon, SingleColorIconCache}

/**
 * Used for accessing icons used in this project
 * @author Mikko Hilpinen
 * @since 15.9.2020, v0.1
 */
object Icons
{
	// ATTRIBUTES   -------------------------
	
	// TODO: Change path
	private val cache = new SingleColorIconCache("Client/resources/icons")
	
	
	// COMPUTED -----------------------------
	
	/**
	 * @return An access point to feeling-related icons
	 */
	def feeling = FeelingIcons
	
	
	// NESTED   -----------------------------
	
	/**
	 * Used for accessing feeling-related icons
	 */
	object FeelingIcons
	{
		// COMPUTED -------------------------
		
		/**
		 * @return A feeling background icon
		 */
		def background = cache("face-background.png")
		
		/**
		 * @return Icon for good feeling
		 */
		def good = cache("good.png")
		
		/**
		 * @return Icon for very good feeling
		 */
		def veryGood = cache("very-good.png")
		
		/**
		 * @return Icon for bad feeling
		 */
		def bad = cache("bad.png")
		
		/**
		 * @return Icon for very bad feeling
		 */
		def veryBad = cache("very-bad.png")
		
		/**
		 * @return Icon for meh / undefined / neutral feeling
		 */
		def meh = cache("meh.png")
		
		
		// OTHER    ---------------------------
		
		/**
		 * @param feeling A feeling
		 * @return An icon for that feeling
		 */
		def apply(feeling: Feeling) = feeling match
		{
			case SlightlyGood => good
			case Good | VeryGood => veryGood
			case SlightlyBad => bad
			case Bad | VeryBad => veryBad
		}
		
		/**
		 * @param feeling A feeling. None if neutral / no feeling.
		 * @return An icon for that feeling
		 */
		def apply(feeling: Option[Feeling]): SingleColorIcon = feeling match
		{
			case Some(defined) => apply(defined)
			case None => meh
		}
	}
}
