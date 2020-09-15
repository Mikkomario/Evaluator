package evaluator.client.view.vc

import evaluator.client.model.cached.GroupingResults
import evaluator.client.model.enumeration.Feeling
import evaluator.client.model.enumeration.Feeling.{Bad, Good, VeryBad, VeryGood}
import evaluator.client.view.component.FeelingButton
import evaluator.client.view.util.Setup._
import utopia.flow.datastructure.mutable.PointerWithEvents
import utopia.flow.event.Changing
import utopia.reflection.component.context.ColorContext
import utopia.reflection.component.drawing.immutable.BorderDrawer
import utopia.reflection.component.swing.label.ItemLabel
import utopia.reflection.component.swing.template.StackableAwtComponentWrapperWrapper
import utopia.reflection.component.template.input.InputWithPointer
import utopia.reflection.container.stack.StackLayout.Center
import utopia.reflection.container.swing.AwtContainerRelated
import utopia.reflection.container.swing.layout.multi.Stack
import utopia.reflection.shape.{Alignment, Border}
import utopia.reflection.shape.LengthExtensions._

object AssociationVC
{
	/**
	  * @param nextWord Function for retrieving the next word in the sequence
	  * @param context An implicit component creation context
	  * @return A new association view controller
	  */
	def apply(nextWord: GroupingResults => String)(implicit context: ColorContext) = new AssociationVC(context)(nextWord)
}

/**
  * A view used for associating words with feelings
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
class AssociationVC(parentContext: ColorContext)(nextWord: GroupingResults => String)
	extends StackableAwtComponentWrapperWrapper with InputWithPointer[GroupingResults, Changing[GroupingResults]]
		with AwtContainerRelated
{
	// ATTRIBUTES   ------------------------------
	
	private val currentResultsPointer = new PointerWithEvents(GroupingResults.empty)
	private val currentWordPointer = new PointerWithEvents(nextWord(currentResults))
	
	private val context = parentContext.withLightGrayBackground.forTextComponents(Alignment.Center)
	
	private val wordLabel = context.mapFont { _ * 1.2 }.mapInsets { _ + margins.verySmall }.use { implicit c =>
		val label = ItemLabel.contextualWithPointer[String](currentWordPointer)
		label.addCustomDrawer(BorderDrawer(Border.symmetric(margins.verySmall, label.textColor)))
		label
	}
	private val buttons = context.use { implicit c =>
		Vector(Some(VeryGood) -> '1', Some(Good) -> '2', None -> '3', Some(Bad) -> '4', Some(VeryBad) -> '5')
			.map { case (feeling, key) => new FeelingButton(feeling, key)({ associateWith(feeling) }) }
	}
	private val view = context.use { implicit c =>
		Stack.buildColumnWithContext(layout = Center) { s =>
			s += wordLabel
			s += Stack.buildRowWithContext(isRelated = true) { s => buttons.foreach { s += _ } }
		}.framed(margins.medium.any, c.containerBackground)
	}
	
	
	// COMPUTED -----------------------------------
	
	private def currentResults = currentResultsPointer.value
	private def currentResults_=(newResults: GroupingResults) = currentResultsPointer.value = newResults
	
	private def currentWord = currentWordPointer.value
	private def currentWord_=(newWord: String) = currentWordPointer.value = newWord
	
	
	// IMPLEMENTED  -------------------------------
	
	override def valuePointer = currentResultsPointer.view
	
	override protected def wrapped = view
	
	override def component = view.component
	
	
	// OTHER    -----------------------------------
	
	private def associateWith(feeling: Option[Feeling]) =
	{
		feeling match
		{
			case Some(association) => currentResults += currentWord -> association
			case None => currentResults = currentResults.skipping(currentWord)
		}
		// TODO: Handle case where all words have been used
		currentWord = nextWord(currentResults)
	}
}
