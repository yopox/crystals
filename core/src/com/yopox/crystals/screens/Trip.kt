package com.yopox.crystals.screens

import com.badlogic.gdx.graphics.Texture
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.logic.Event
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.screens.Trip.Destination.BAG
import com.yopox.crystals.screens.Trip.Destination.DISPLAY
import com.yopox.crystals.ui.Chunk
import com.yopox.crystals.ui.TextButton
import ktx.graphics.use

/**
 * Trip Screen.
 *
 * TODO: "Continue" button reveal transition
 */
class Trip(game: Crystals) : Screen(Util.TEXT_TRIP, game) {

    private enum class Destination {
        STATUS,
        BAG,
        DISPLAY
    }

    private val icons: Texture = Crystals.assetManager["icons.png"]
    private val chunks = ArrayList<Chunk>()
    private var statusX = arrayOf(0f, 0f)
    private var goldX = 0f
    private var destination = DISPLAY
    private var event: Event? = null

    init {
        val x = 16f
        val y = 7f
        buttons.add(TextButton(x, y, Util.TEXT_STATUS) {
            chunks[0].reroll()
        })
        buttons.add(TextButton(x + 40, y, Util.TEXT_BAG) {
            destination = BAG
            state = ScreenState.ENDING
            blockInput = true
        })
        buttons.add(TextButton(x + 2 * 40, y, Util.TEXT_CONTINUE, clickable = false) {
            destination = DISPLAY
            event = chunks[0].getEvent()
            state = ScreenState.ENDING
            blockInput = true
        })
        for (i in 0..4)
            chunks.add(Chunk(Util.WIDTH / 4 + 16f + i * Util.CHUNK_SEP, Util.HEIGHT / 3 - 3))
    }

    override fun show() {
        super.show()
        statusX[0] = Util.textOffsetX(Util.font, "${Progress.player.stats.hp} HP", 44f)
        statusX[1] = Util.textOffsetX(Util.font, "${Progress.player.stats.mp} MP", 44f)
        goldX = Util.WIDTH - 8f - Util.textSize("${Progress.gold} GOLD", Util.font).first
    }

    override fun render(delta: Float) {
        super.render(delta)

        drawStatus()
        chunks.map { it.draw(shapeRenderer, batch, icons) }
        buttons.map { it.draw(shapeRenderer, batch) }

        drawTransitions()
    }

    override fun stateChange(st: ScreenState) = when (st) {
        ScreenState.ENDING -> {
            resetState()
            when (destination) {
                BAG -> {
                    game.getScreen<Bag>().setup(); game.setScreen<Bag>()
                }
                else -> {
                    game.getScreen<Display>().setup(event!!); game.setScreen<Display>()
                }
            }

        }
        ScreenState.OPENING -> {
            state = ScreenState.MAIN; blockInput = false
        }
        else -> Unit
    }

    private fun drawStatus() {
        Util.drawRect(shapeRenderer, 8f, Util.HEIGHT / 3 - 3, 44f, 24f)
        batch.use {
            val stats = Progress.player.stats
            Util.font.draw(it, "${stats.hp} HP", 8f + statusX[0], 46f)
            Util.font.draw(it, "${stats.mp} MP", 8f + statusX[1], 38f)
            Util.font.draw(it, "${Progress.gold} GOLD", goldX, Util.HEIGHT - 7f)
        }
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.map { it.touch(x, y) }
        chunks[0].touch(x, y)
        chunks[0].getEvent()?.let {
            buttons[2].clickable = true
        }
    }

    override fun resetState() {
        blockInput = true
        chunks.removeAt(0)
        chunks.map { it.step() }
        chunks.add(Chunk(Util.WIDTH / 4 + 16f + 4 * Util.CHUNK_SEP, Util.HEIGHT / 3 - 3))
        buttons[2].clickable = false
        state = ScreenState.OPENING
    }

}