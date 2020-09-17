package evaluator.client.view.main

import evaluator.client.controller.Words
import evaluator.client.view.vc.AssociationVC
import utopia.genesis.color.Color
import utopia.genesis.generic.GenesisDataType
import utopia.reflection.container.swing.window.Frame
import utopia.reflection.util.SingleFrameSetup

/**
  * The main application in this project
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
object EvaluatorApp extends App
{
	GenesisDataType.setup()
	
	import evaluator.client.controller.Globals._
	import evaluator.client.view.util.Setup._
	
	println(Words.values.take(3))
	val wordsIter = Words.values.iterator
	new SingleFrameSetup(actorHandler, Frame.windowed(
		AssociationVC { _ => wordsIter.nextOption() }(baseContext.inContextWithBackground(Color.black)))).start()
}
