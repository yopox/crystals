package com.yopox.crystals.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.screens.Trip
import ktx.app.KtxScreen
import ktx.graphics.use

abstract class Screen(val name: String, internal val game: Crystals) : KtxScreen, InputProcessor {

    internal val batch = SpriteBatch()
    internal val shapeRenderer = ShapeRenderer()
    internal val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    internal var blockInput = true
    internal val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    internal var state = ScreenState.OPENING
    internal var buttons = mutableListOf<Button>()

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

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw the title
        batch.use {
            Util.bigFont.draw(it, name, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
        }

        buttons.forEach { it.draw(shapeRenderer, batch) }
    }

    fun drawTransitions() = when (state) {
        ScreenState.OPENING -> {
            if (Transition.drawWipe(shapeRenderer, false, reverse = true))
                stateChange(ScreenState.OPENING)
            else Unit
        }
        ScreenState.ENDING -> {
            if (Transition.drawWipe(shapeRenderer))
                stateChange(ScreenState.ENDING)
            else Unit
        }
        else -> Unit
    }

    abstract fun resetState(): Unit

    open fun stateChange(st: ScreenState) = when (st) {
        ScreenState.ENDING -> {
            resetState(); game.setScreen<Trip>()
        }
        ScreenState.OPENING -> {
            state = ScreenState.MAIN; blockInput = false
        }
        else -> Unit
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (pointer > 0 || button > 0 || blockInput) return false
        val (x, y) = Util.unproject(camera, screenX, screenY)
        inputUp(x, y)
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (pointer > 0 || button > 0 || blockInput) return false
        val (x, y) = Util.unproject(camera, screenX, screenY)
        inputDown(x, y)
        return true
    }

    abstract fun inputUp(x: Int, y: Int)

    abstract fun inputDown(x: Int, y: Int)

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

    override fun keyTyped(character: Char): Boolean = false

    override fun scrolled(amount: Int): Boolean = false

    override fun keyUp(keycode: Int): Boolean = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

    override fun keyDown(keycode: Int): Boolean = false

}