package evaluator.client.view.vc

import evaluator.client.model.cached.GroupingResults
import evaluator.client.model.enumeration.Feeling
import evaluator.client.model.enumeration.Feeling.{Bad, Good}
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
import utopia.reflection.localization.DisplayFunction
import utopia.reflection.localization.LocalString.StringLocal
import utopia.reflection.shape.{Alignment, Border}
import utopia.reflection.shape.LengthExtensions._

import scala.concurrent.Promise

object AssociationVC
{
	/**
	  * @param nextWord Function for retrieving the next word in the sequence
	  * @param context An implicit component creation context
	  * @return A new association view controller
	  */
	def apply(nextWord: GroupingResults => Option[String])(implicit context: ColorContext) =
		new AssociationVC(context)(nextWord)
}

/**
  * A view used for associating words with feelings
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
class AssociationVC(parentContext: ColorContext, options: Seq[Option[Feeling]] = Vector(Some(Good), None, Some(Bad)))
                   (nextWord: GroupingResults => Option[String])
	extends StackableAwtComponentWrapperWrapper with InputWithPointer[GroupingResults, Changing[GroupingResults]]
		with AwtContainerRelated
{
	// ATTRIBUTES   ------------------------------
	
	private val currentResultsPointer = new PointerWithEvents(GroupingResults.empty)
	private val currentWordPointer = new PointerWithEvents(nextWord(currentResults))
	private val completionPromise = Promise[GroupingResults]()
	
	private val context = parentContext.withLightGrayBackground.forTextComponents(Alignment.Center)
	
	private val wordLabel = context.withFont(valueFont).mapInsets { _ + margins.verySmall }.use { implicit c =>
		val label = ItemLabel.contextualWithPointer[Option[String]](currentWordPointer,
			DisplayFunction.noLocalization[Option[String]] { _.getOrElse("").noLanguage })
		label.addCustomDrawer(BorderDrawer(Border.symmetric(margins.verySmall, label.textColor)))
		label
	}
	private val buttons = context.use { implicit c =>
		options.zip((1 to options.size).map { _.toString.head }).map { case (feeling, key) =>
			new FeelingButton(feeling, key)({ associateWith(feeling) })
		}
	}
	private val view = context.use { implicit c =>
		Stack.buildColumnWithContext(layout = Center) { s =>
			s += wordLabel
			s += Stack.buildRowWithContext() { s => buttons.foreach { s += _ } }
		}.framed(margins.medium.any, c.containerBackground)
	}
	
	
	// COMPUTED -----------------------------------
	
	/**
	  * @return Eventual final results of this test
	  */
	def completion = completionPromise.future
	
	private def currentResults = currentResultsPointer.value
	private def currentResults_=(newResults: GroupingResults) = currentResultsPointer.value = newResults
	
	private def currentWord = currentWordPointer.value
	private def currentWord_=(newWord: String) = currentWordPointer.value = Some(newWord)
	private def currentWord_=(newWord: Option[String]) = currentWordPointer.value = newWord
	
	
	// IMPLEMENTED  -------------------------------
	
	override def valuePointer = currentResultsPointer.view
	
	override protected def wrapped = view
	
	override def component = view.component
	
	
	// OTHER    -----------------------------------
	
	private def associateWith(feeling: Option[Feeling]): Unit =
	{
		currentWord.foreach { word =>
			feeling match
			{
				case Some(association) => currentResults += word -> association
				case None => currentResults = currentResults.skipping(word)
			}
			val newWord = nextWord(currentResults)
			currentWord = newWord
			if (newWord.isEmpty)
			{
				buttons.foreach { _.enabled = false }
				completionPromise.trySuccess(currentResults)
			}
		}
	}
}
