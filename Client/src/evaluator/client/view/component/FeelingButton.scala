package evaluator.client.view.component

import evaluator.client.model.enumeration.Feeling
import evaluator.client.model.enumeration.Intensity.Weak
import evaluator.client.view.util.Icons.FeelingIcons
import utopia.genesis.event.KeyStateEvent
import utopia.genesis.handling.KeyStateListener
import utopia.genesis.shape.shape1D.Direction1D.{Negative, Positive}
import utopia.reflection.color.TextColorStandard.{Dark, Light}
import utopia.reflection.component.context.TextContextLike
import utopia.reflection.component.swing.button.{ButtonImageSet, ImageButton}
import utopia.reflection.component.swing.label.TextLabel
import utopia.reflection.component.swing.template.StackableAwtComponentWrapperWrapper
import utopia.reflection.container.stack.StackLayout.Center
import utopia.reflection.container.swing.layout.multi.Stack
import utopia.reflection.shape.stack.StackLength
import utopia.reflection.localization.LocalString._

/**
 * A button used for choosing a feeling
 * @author Mikko Hilpinen
 * @since 15.9.2020, v0.1
 */
class FeelingButton(val feeling: Option[Feeling], hotKey: Char)(action: => Unit)(implicit context: TextContextLike)
	extends StackableAwtComponentWrapperWrapper
{
	// ATTRIBUTES   ----------------------------
	
	private val button =
	{
		// Selects the background color based on feeling and draws the expression on top of that
		val baseImage = FeelingIcons.background.asImageWithColor(color) + FeelingIcons(feeling).singleColorImage
		val buttonImage = context.containerBackground.textColorStandard match
		{
			case Light => ButtonImageSet.darkening(baseImage)
			case Dark => ButtonImageSet.brightening(baseImage)
		}
		ImageButton.contextualWithoutAction(buttonImage)
	}
	private val view = Stack.columnWithItems(Vector(
		button,
		TextLabel.contextual(hotKey.toString.noLanguageLocalizationSkipped)
	), StackLength.fixedZero, layout = Center)
	
	
	// INITIAL CODE ----------------------------
	
	// Triggers the button when the hotkey is pressed
	button.addKeyStateListener(KeyStateListener(KeyStateEvent.charFilter(hotKey) && KeyStateEvent.wasPressedFilter) {
		_ => button.trigger() })
	
	button.registerAction { () => action }
	
	
	// COMPUTED --------------------------------
	
	/**
	  * @return Whether this button is currently enabled / actionable
	  */
	def enabled = button.enabled
	def enabled_=(newEnabled: Boolean) = button.enabled = newEnabled
	
	private def color =
	{
		val set = feeling match
		{
			case Some(defined) =>
				val base = defined.sign match
				{
					case Positive => context.colorScheme.success
					case Negative => context.colorScheme.error
				}
				defined.intensity match
				{
					case Weak => base.map { _.timesSaturation(0.5) }
					case _ => base
				}
			case None => context.colorScheme.gray
		}
		set.forBackground(context.containerBackground)
	}
	
	
	// IMPLEMENTED  -------------------------
	
	override protected def wrapped = view
	
	
	// OTHER    -----------------------------
	
	/**
	 * Registers a new action to be triggered when this button is pressed
	 * @param action Triggered action (call by name)
	 */
	def registerAction(action: => Unit) = button.registerAction { () => action }
}
