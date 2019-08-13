package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.InputScreen
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.ui.Button
import com.yopox.crystals.ui.TextButton
import ktx.app.KtxScreen
import ktx.graphics.use
import kotlin.math.min

/**
 * Title Screen.
 */
class TitleScreen(private val game: Crystals) : KtxScreen, InputScreen {

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = false
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    private var posX1 = Util.textOffsetX(Util.bigFont, Util.TEXT_TITLE, Util.WIDTH)
    private val buttons = ArrayList<Button>()

    private var state = ScreenState.MAIN

    init {
        buttons.add(TextButton(Util.WIDTH / 2 - Util.BUTTON_WIDTH, Util.HEIGHT / 4, Util.TEXT_NEWGAME, true) {
            blockInput = true
            state = ScreenState.TRANSITION_EN
        })
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        batch.use {
            Util.bigFont.draw(it, Util.TEXT_TITLE, posX1, Util.HEIGHT / 4 * 3)
            Util.font.draw(it, Util.BUILD_NB, 2f, 8f)
        }
        buttons.map { it.draw(shapeRenderer, batch) }

        when (state) {
            ScreenState.TRANSITION_EN -> if (Util.drawWipe(shapeRenderer)) {
                resetState()
                game.setScreen<CharacterSelection>()
            }
            ScreenState.TRANSITION_OP -> {
                if (Util.drawWipe(shapeRenderer, false, reverse = true)) {
                    state = ScreenState.MAIN
                    blockInput = false
                }
            }
            else -> Unit
        }
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = this
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        camera.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f)
    }

    override fun dispose() {
        batch.dispose()
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.map { it.touch(x, y) }
    }

    private fun resetState() {
        blockInput = true
        state = ScreenState.TRANSITION_OP
    }
}