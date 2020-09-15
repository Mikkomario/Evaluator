package evaluator.client.model.enumeration

import evaluator.client.model.enumeration.Intensity.{Strong, Weak}
import utopia.flow.util.RichComparable
import utopia.genesis.shape.shape1D.Direction1D
import utopia.genesis.shape.shape1D.Direction1D.{Negative, Positive}

/**
 * Represents a feeling user has about something
 * @author Mikko Hilpinen
 * @since 15.9.2020, v0.1
 */
sealed trait Feeling extends RichComparable[Feeling]
{
	// ABSTRACT -----------------------
	
	/**
	 * Whether this feeling is negative or positive
	 */
	val sign: Direction1D
	/**
	 * How strong this feeling is towards its direction (sign)
	 */
	val intensity: Intensity
	
	
	// IMPLEMENTED  -------------------
	
	override def compareTo(o: Feeling) = sign.compareOr(o.sign) { intensity.compareTo(o.intensity) }
}

object Feeling
{
	// OTHER    -----------------------
	
	/**
	 * @param sign Feeling sign (positive | negative)
	 * @param intensity Feeling intensity (weak | strong)
	 * @return A feeling with those attributes
	 */
	def apply(sign: Direction1D, intensity: Intensity) = sign match
	{
		case Positive =>
			intensity match
			{
				case Strong => VeryGood
				case Weak => Good
			}
		case Negative =>
			intensity match
			{
				case Strong => VeryBad
				case Weak => Bad
			}
	}
	
	
	// NESTED   -----------------------
	
	/**
	 * A very positive feeling
	 */
	case object VeryGood extends Feeling
	{
		override val sign = Positive
		override val intensity = Strong
	}
	
	/**
	 * A positive feeling
	 */
	case object Good extends Feeling
	{
		override val sign = Positive
		override val intensity = Weak
	}
	
	/**
	 * A negative feeling
	 */
	case object Bad extends Feeling
	{
		override val sign = Negative
		override val intensity = Weak
	}
	
	/**
	 * A very negative feeling
	 */
	case object VeryBad extends Feeling
	{
		override val sign = Negative
		override val intensity = Strong
	}
}
