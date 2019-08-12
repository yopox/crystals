package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.InputScreen
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.data.Def
import com.yopox.crystals.data.EVENT_TYPE
import com.yopox.crystals.data.Event
import com.yopox.crystals.data.Progress
import com.yopox.crystals.ui.*
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Trip Screen.
 *
 * TODO: "Continue" button reveal transition
 */
class Fight(private val game: Crystals) : KtxScreen, InputScreen {

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    private val buttons = ArrayList<Button>()
    private var state = ScreenState.TRANSITION_OP
    private val icons = ArrayList<Icon>()

    init {
        icons.add(Icon(Def.Icons.Priest, Pair(88f, 36f)))
        icons.add(Icon(Def.Icons.Snake, Pair(56f, 36f)))

        buttons.add(ActionButton(ACTIONS.ATTACK, Pair(10f, 5f)) { Gdx.app.log("Fight", "ATTACK") })
        buttons.add(ActionButton(ACTIONS.DEFENSE, Pair(26f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.ITEMS, Pair(42f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.W_MAGIC, Pair(58f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.ROB, Pair(74f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.GEOMANCY, Pair(90f, 5f)) {})
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw the dialog box
        Util.drawRect(shapeRenderer, -1f, -1f, 182f, 24f)

        // Draw the title
        batch.use {
            Util.bigFont.draw(it, Util.TEXT_FIGHT, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
            icons.map { icon -> icon.draw(it) }
            Util.font.draw(it, Util.TEXT_ACTIONS, 10f, 31f)
            buttons.map { it.draw(shapeRenderer, batch) }
        }

        when (state) {
            ScreenState.TRANSITION_OP -> {
                if (Util.drawWipe(shapeRenderer, false, reverse = true)) {
                    state = ScreenState.MAIN
                    blockInput = false
                }
            }
            ScreenState.TRANSITION_EN -> {
                if (Util.drawWipe(shapeRenderer)) {
                    resetState()
                    game.setScreen<Trip>()
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