package evaluator.client.model.enumeration

import utopia.flow.util.RichComparable

/**
 * Represents a level of intensity
 * @author Mikko Hilpinen
 * @since 15.9.2020, v0.1
 */
sealed trait Intensity extends RichComparable[Intensity]

object Intensity
{
	// ATTRIBUTES   ------------------------
	
	/**
	  * Possible values of this enumeration
	  */
	val values = Vector[Intensity](Weak, Strong, VeryStrong)
	
	
	// COMPUTED ----------------------------
	
	/**
	  * @return The smallest intensity available
	  */
	def minimum = Weak
	/**
	  * @return The largest intensity available
	  */
	def maximum = VeryStrong
	
	
	// NESTED   ----------------------------
	
	/**
	  * A very strong intensity
	  */
	case object VeryStrong extends Intensity
	{
		override def compareTo(o: Intensity) = o match
		{
			case VeryStrong => 0
			case _ => 1
		}
	}
	
	/**
	 * A strong intensity
	 */
	case object Strong extends Intensity
	{
		override def compareTo(o: Intensity) = o match
		{
			case VeryStrong => -1
			case Strong => 0
			case Weak => 1
		}
	}
	
	/**
	 * A weak intensity
	 */
	case object Weak extends Intensity
	{
		override def compareTo(o: Intensity) = o match
		{
			case Weak => 0
			case _ => -1
		}
	}
}