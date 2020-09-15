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
	/**
	 * A strong intensity
	 */
	case object Strong extends Intensity
	{
		override def compareTo(o: Intensity) = o match
		{
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
			case Strong => -1
			case Weak => 0
		}
	}
}