package com.yopox.crystals.screens

import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.ui.TextButton
import ktx.graphics.use

/**
 * Title Screen.
 */
class TitleScreen(game: Crystals) : Screen("", game) {

    private var posX1 = Util.textOffsetX(Util.bigFont, Util.TEXT_TITLE, Util.WIDTH)

    init {
        state = ScreenState.MAIN
        buttons.add(TextButton(Util.WIDTH / 2 - Util.BUTTON_WIDTH, Util.HEIGHT / 4, Util.TEXT_NEWGAME, true) {
            Progress.reset()
            blockInput = true
            state = ScreenState.ENDING
        })
    }

    override fun show() {
        super.show()
        blockInput = false
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        batch.use {
            Util.bigFont.draw(it, Util.TEXT_TITLE, posX1, Util.HEIGHT / 4 * 3)
            Util.font.draw(it, Util.BUILD_NB, 2f, 8f)
        }
        buttons.map { it.draw(shapeRenderer, batch) }

        drawTransitions()
    }

    override fun stateChange(st: ScreenState) = when (st) {
        ScreenState.ENDING -> {
            resetState(); game.setScreen<CharacterSelection>()
        }
        ScreenState.OPENING -> {
            state = ScreenState.MAIN; blockInput = false
        }
        else -> Unit
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.map { it.touch(x, y) }
    }

    override fun resetState() {
        blockInput = true
        state = ScreenState.OPENING
    }
}