package evaluator.client.view.vc

import java.awt.event.KeyEvent

import evaluator.client.model.cached.GroupingResults
import evaluator.client.view.component.FeelingStrengthSlider
import evaluator.client.view.util.Setup._
import utopia.flow.util.CollectionExtensions._
import utopia.genesis.shape.shape1D.Direction1D
import utopia.genesis.shape.shape1D.Direction1D.Positive
import utopia.reflection.component.context.ColorContext
import utopia.reflection.component.swing.button.TextButton
import utopia.reflection.component.swing.template.StackableAwtComponentWrapperWrapper
import utopia.reflection.container.swing.layout.multi.Stack
import utopia.reflection.shape.Alignment

import scala.concurrent.Promise

/**
  * A view + controller for evaluating value association strengths
  * @author Mikko Hilpinen
  * @since 17.9.2020, v0.1
  */
class FeelingStrengthVC(parentContext: ColorContext, wordsToCategorize: Seq[String],
                        feelingSign: Direction1D = Positive, numberOfWordsPerPage: Int = 3)
	extends StackableAwtComponentWrapperWrapper
{
	// ATTRIBUTES   -----------------------------
	
	private implicit val languageCode: String = "en"
	
	private var numberOfCategorizedWords = 0
	private var results = GroupingResults.empty
	
	private val finalResultsPromise = Promise[GroupingResults]()
	
	private val sliders = parentContext.use { implicit c =>
		Vector.fill(numberOfWordsPerPage) { FeelingStrengthSlider(feelingSign) }
	}
	private val button = parentContext.forTextComponents(Alignment.Center).expandingHorizontally.forPrimaryColorButtons
		.use { implicit c =>
			TextButton.contextual("Next!", Set(KeyEvent.VK_ENTER)) { next() }
		}
	private val view = parentContext.use { implicit c =>
		Stack.buildColumnWithContext() { s =>
			sliders.foreach { s += _ }
			s += button
		}
	}
	
	
	// INITIAL CODE -----------------------------
	
	updateWords()
	
	
	// COMPUTED ---------------------------------
	
	/**
	  * @return Future containing results from this grouping once all words have been categorized
	  */
	def finalResultsFuture = finalResultsPromise.future
	
	/**
	  * @return Currently gathered results
	  */
	def currentResults = results
	
	
	// IMPLEMENTED  -----------------------------
	
	override protected def wrapped = view
	
	
	// OTHER    ---------------------------------
	
	private def next(): Unit =
	{
		updateResults()
		if (numberOfCategorizedWords < wordsToCategorize.size)
			updateWords()
		else
		{
			finalResultsPromise.trySuccess(results)
			button.enabled = false
		}
	}
	
	private def updateWords() =
	{
		val nextWords = wordsToCategorize.slice(numberOfCategorizedWords, numberOfCategorizedWords + numberOfWordsPerPage)
		sliders.foreachWithIndex { (slider, index) =>
			nextWords.getOption(index) match
			{
				case Some(word) =>
					slider.word = word
					if (slider.invisible)
						slider.visible = true
				case None => slider.visible = false
			}
		}
	}
	
	private def updateResults() =
	{
		sliders.filter { _.visible }.foreach { s => results += s.word -> s.feeling }
		numberOfCategorizedWords = results.size
	}
}
