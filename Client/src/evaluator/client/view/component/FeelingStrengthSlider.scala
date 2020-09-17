package evaluator.client.view.component

import evaluator.client.model.enumeration.Intensity.Strong
import evaluator.client.model.enumeration.{Feeling, Intensity}
import evaluator.client.view.util.Icons.FeelingIcons
import evaluator.client.view.util.Setup._
import utopia.genesis.shape.shape1D.Direction1D
import utopia.genesis.shape.shape1D.Direction1D.{Negative, Positive}
import utopia.reflection.component.context.ColorContext
import utopia.reflection.component.swing.input.Slider
import utopia.reflection.component.swing.label.{ImageLabel, ItemLabel}
import utopia.reflection.component.swing.template.StackableAwtComponentWrapperWrapper
import utopia.reflection.component.template.display.Refreshable
import utopia.reflection.container.stack.StackLayout.Center
import utopia.reflection.container.swing.layout.multi.Stack
import utopia.reflection.shape.stack.LengthPriority.Low
import utopia.reflection.shape.stack.StackLength

object FeelingStrengthSlider
{
	/**
	  * Creates a new feeling strength slider
	  * @param sign Feeling sign (default = positive)
	  * @param defaultIntensity Initially selected feeling intensity (default = strong)
	  * @param initialWord Initially displayed word (default = empty)
	  * @param context Component creation context (implicit)
	  * @return A new feeling strength slider
	  */
	def apply(sign: Direction1D = Positive, defaultIntensity: Intensity = Strong, initialWord: String = "")
	         (implicit context: ColorContext) = new FeelingStrengthSlider(context, sign, defaultIntensity, initialWord)
}

/**
  * Used for inputting association / feeling strengths for individual words
  * @author Mikko Hilpinen
  * @since 17.9.2020, v0.1
  */
class FeelingStrengthSlider(parentContext: ColorContext, feelingSign: Direction1D = Positive,
                            defaultIntensity: Intensity = Strong, initialWord: String = "")
	extends StackableAwtComponentWrapperWrapper with Refreshable[(String, Intensity)]
{
	// ATTRIBUTES   -----------------------
	
	private val wordLabel = parentContext.forTextComponents().withFont(valueFont).use { implicit c =>
		ItemLabel.contextual(initialWord)
	}
	private val (view, slider) = parentContext.use { implicit c =>
		val color = (feelingSign match
		{
			case Positive => colorScheme.success
			case Negative => colorScheme.error
		}).inContext
		val backgroundIcon = FeelingIcons.background
		val smallValueIcon = backgroundIcon.asImageWithColor(color.timesSaturation(0.5)) +
			FeelingIcons(Feeling(feelingSign, Intensity.minimum)).singleColorImage
		val largeValueIcon = backgroundIcon.asImageWithColor(color) +
			FeelingIcons(Feeling(feelingSign, Intensity.maximum)).singleColorImage
		
		val slider = Slider.contextualSingleColorSelection(Intensity.values,
			StackLength(standardFieldWidth * 0.25, standardFieldWidth, priority = Low), color)
		val stack = Stack.buildRowWithContext(layout = Center, isRelated = true) { s =>
			s += wordLabel
			s += ImageLabel.contextual(smallValueIcon)
			s += slider
			s += ImageLabel.contextual(largeValueIcon)
		}
		stack -> slider
	}
	
	
	// INITIAL CODE ----------------------
	
	moveSliderToDefault()
	
	
	// COMPUTED --------------------------
	
	/**
	  * @return The currently displayed word
	  */
	def word = wordLabel.content
	def word_=(newWord: String) =
	{
		if (word != newWord)
		{
			wordLabel.content = newWord
			moveSliderToDefault()
		}
	}
	
	/**
	  * @return Currently selected feeling intensity
	  */
	def intensity = slider.value
	def intensity_=(newValue: Intensity) = moveSliderTo(newValue)
	
	/**
	  * @return Currenlty selected feeling
	  */
	def feeling = Feeling(feelingSign, intensity)
	
	
	// IMPLEMENTED  ----------------------
	
	override protected def wrapped = view
	
	override def content_=(newContent: (String, Intensity)) =
	{
		wordLabel.content = newContent._1
		moveSliderTo(newContent._2)
	}
	
	override def content = wordLabel.content -> slider.value
	
	
	// OTHER    --------------------------
	
	private def moveSliderToDefault() = moveSliderTo(defaultIntensity)
	
	private def moveSliderTo(intensity: Intensity) =
		slider.shiftTo(Intensity.values.indexOf(intensity).toDouble / (Intensity.values.size - 1))
}
