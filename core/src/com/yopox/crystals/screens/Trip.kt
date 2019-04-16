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
import com.yopox.crystals.data.EVENT_TYPE
import com.yopox.crystals.data.Event
import com.yopox.crystals.ui.Button
import com.yopox.crystals.ui.Chunk
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Trip Screen.
 */
class Trip(private val game: Crystals) : KtxScreen, InputScreen {

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    private val icons: Texture = Crystals.assetManager["aseprite/icons.png"]
    private val buttons = ArrayList<Button>()
    private val chunks = ArrayList<Chunk>()
    private var state = ScreenState.TRANSITION_OP

    init {
        buttons.add(Button(Util.WIDTH / 4 + 24f, 8f, Util.TEXT_BAG) {
            Gdx.app.log("trip", "bag")
        })
        buttons.add(Button(Util.WIDTH / 4 + 24f + 40f, 8f, Util.TEXT_OPTIONS) {
            Gdx.app.log("trip", "options")
        })
        for (i in 0..4)
            chunks.add(Chunk(Util.WIDTH / 4 + 16f + i * 20, Util.HEIGHT / 3))
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw the title
        batch.use {
            Util.bigFont.draw(it, Util.TEXT_TRIP, 10f, Util.HEIGHT - 10)
        }

        drawStatus()
        chunks.map { it.draw(shapeRenderer, batch, icons) }
        buttons.map { it.draw(shapeRenderer, batch) }

        when (state) {
            ScreenState.TRANSITION_OP -> {
                if (Util.drawWipe(shapeRenderer, false, reverse = true)) {
                    state = ScreenState.MAIN
                    blockInput = false
                }
            }
            ScreenState.TRANSITION_EN -> {
                if (Util.drawWipe(shapeRenderer)) {
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

    private fun drawStatus() {
        Util.drawRect(shapeRenderer, 8f, 8f, Util.WIDTH / 4, Util.HEIGHT / 2)
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.map { it.touch(x, y) }
        chunks[0].touch(x, y)
    }

}