package com.yopox.crystals

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Character selection screen
 */
class CharacterSelection : KtxScreen, InputProcessor {

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    private val shapeRenderer = ShapeRenderer()

    private val posX1 = Util.buttonTextOffset(Util.TEXT_NEXT)
    private val posX2 = Util.buttonTextOffset(Util.TEXT_PREVIOUS)
    private val posX3 = Util.buttonTextOffset(Util.TEXT_CONTINUE)

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = this
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        batch.use {
            Util.bigFont.draw(it, Util.TEXT_CLASS, 8f, Util.HEIGHT - 8f)
        }

        drawButtons()
        drawClassDesc()

    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        camera.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f)
    }

    override fun dispose() {
        batch.dispose()
    }

    private fun drawButtons() {
        val x = Util.WIDTH / 4 + 32f + 41
        val y = 16f
        for (j in 0..2)
            Util.drawRect(shapeRenderer, x, y + j * 21, 36f, 16f)

        val textY = y + 11
        batch.use {
            Util.font.draw(it, Util.TEXT_NEXT, x + posX1, textY + 2 * 21)
            Util.font.draw(it, Util.TEXT_PREVIOUS, x + posX2, textY + 21)
            Util.font.draw(it, Util.TEXT_CONTINUE, x + posX3, textY)
        }
    }

    private fun drawClassDesc() {
        Util.drawRect(shapeRenderer, 8f, 29f, 96f, 32f)

        val x0 = 12f
        val offset = 32f

        batch.use {
            Util.font.draw(it, "Priest crystals give\nthe ability to heal, but\nreduce attack.", 12f, 56f)
            Util.font.draw(it, "HP +\nMP ++", x0, 24f)
            Util.font.draw(it, "ATK --\nWIS +", x0 + offset, 24f)
            Util.font.draw(it, "DEF -\nSPD -", x0 + 2 * offset, 24f)
        }
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val (x, y) = Util.unproject(camera, screenX, screenY)
        Gdx.app.log("up", "$x, $y")
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val (x, y) = Util.unproject(camera, screenX, screenY)
        Gdx.app.log("down", "$x, $y")
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun scrolled(amount: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }
}
