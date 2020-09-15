package evaluator.client.view.util

import utopia.flow.util.FileExtensions._
import utopia.genesis.generic.GenesisDataType
import utopia.genesis.util.DistanceExtensions._
import utopia.genesis.util.{Ppi, Screen}
import utopia.genesis.handling.mutable
import utopia.reflection.color.{ColorScheme, ColorSet}
import utopia.reflection.component.context.{AnimationContext, BaseContext, ScrollingContext}
import utopia.reflection.localization.{Localizer, NoLocalization}
import utopia.reflection.shape.Margins
import utopia.reflection.text.Font

/**
  * Contains global settings used in UI elements
  * @author Mikko Hilpinen
  * @since 15.9.2020, v0.1
  */
object Setup
{
	// INITIAL CODE ----------------------------
	
	GenesisDataType.setup()
	
	
	// ATTRIBUTES   ----------------------------
	
	implicit val ppi: Ppi = Screen.ppi
	
	val primaryColors = ColorSet.fromHexes("#212121", "#484848", "#000000").get
	val secondaryColors = ColorSet.fromHexes("#dd003b", "#ff5465", "#a30016").get
	val colorScheme = ColorScheme.twoTone(primaryColors, secondaryColors)
	
	val margins = Margins(2.5.mm.toPixels.round)
	val standardFontSize = 0.5.cm.toPixels.round.toInt
	
	val actorHandler = mutable.ActorHandler()
	
	val baseContext = BaseContext(actorHandler,
		Font.load("data/fonts/RobotoCondensed-Regular.ttf", standardFontSize)
			.getOrElse(Font("Arial", standardFontSize)), colorScheme, margins)
	
	implicit val animationContext: AnimationContext = AnimationContext(actorHandler)
	implicit val scrollingContext: ScrollingContext = ScrollingContext.withDarkRoundedBar(actorHandler,
		margins.medium.toInt, margins.medium * 6)
	
	implicit val localizer: Localizer = NoLocalization
}
