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
 *
 * TODO: "Continue" button reveal transition
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
    private var statusX = arrayOf(0f, 0f)
    private var goldX = 0f

    init {
        val x = 16f
        val y = 7f
        buttons.add(Button(x, y, Util.TEXT_STATUS) {
            Gdx.app.log("trip", "Status")
        })
        buttons.add(Button(x + 40, y, Util.TEXT_BAG) {
            Gdx.app.log("trip", "Bag")
        })
        buttons.add(Button(x + 2 * 40, y, Util.TEXT_CONTINUE, clickable = false) {
            Gdx.app.log("trip", "Continue " + chunks[0].selected)
        })
        for (i in 0..4)
            chunks.add(Chunk(Util.WIDTH / 4 + 16f + i * 20, Util.HEIGHT / 3 - 3))
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw the title
        batch.use {
            Util.bigFont.draw(it, Util.TEXT_TRIP, 10f, Util.HEIGHT - 13)
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
        statusX[0] = Util.textOffsetX(Util.font, "${Crystals.player.stats.hp} HP", 44f)
        statusX[1] = Util.textOffsetX(Util.font, "${Crystals.player.stats.mp} MP", 44f)
        goldX = Util.textOffsetX(Util.font, "${Crystals.player.gold} GOLD", Util.WIDTH)
        goldX = 2 * goldX - 8f
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
        Util.drawRect(shapeRenderer, 8f, Util.HEIGHT / 3 - 3, 44f, 24f)
        batch.use {
            val stats = Crystals.player.stats
            Util.font.draw(it, "${stats.hp} HP", 8f + statusX[0], 46f)
            Util.font.draw(it, "${stats.mp} MP", 8f + statusX[1], 38f)
            Util.font.draw(it, "${Crystals.player.gold} GOLD", goldX, Util.HEIGHT - 7f)
        }
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.map { it.touch(x, y) }
        chunks[0].touch(x, y)
        if (chunks[0].selected >= 0)
            buttons[2].clickable = true
    }

}