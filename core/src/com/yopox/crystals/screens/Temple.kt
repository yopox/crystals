package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.InputScreen
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Events
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.Event
import com.yopox.crystals.ui.Button
import com.yopox.crystals.ui.Icon
import com.yopox.crystals.ui.Tile
import com.yopox.crystals.ui.Transition
import ktx.app.KtxScreen
import ktx.graphics.use
import kotlin.math.ceil
import kotlin.math.round
import kotlin.math.sin

class Temple(private val game: Crystals) : KtxScreen, InputScreen {
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    override var blockInput = true
    private var state = ScreenState.TRANSITION_OP
    private val icons = ArrayList<Icon>()
    private var frame = 0
    val ray: Texture = Crystals.assetManager["temple.png"]
    val circle: Texture = Crystals.assetManager["circle.png"]

    fun setup(event: Event) {
        icons.add(Tile.genTempleTile(Pair(72f, 30f)) { state = ScreenState.TRANSITION_EN ; blockInput = true })
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        batch.use {
            it.draw(ray, 0f, 0f)
        }

        frame = (frame + 1) % 240
        val icon = icons.last()
        icon.apply {
            pos = Pair(pos.first, Util.HEIGHT / 4 + 2 * sin(frame * Math.PI / 120).toFloat())
        }

        batch.use {
            if (!icon.clicked) it.draw(circle, icons.last().pos.first - 8, icons.last().pos.second - 8)
        }

        icons.map { i -> i.draw(shapeRenderer, batch) }

        batch.use {
            // Draw the title
            Util.bigFont.draw(it, Util.TEXT_TEMPLE, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
        }

        when (state) {
            ScreenState.TRANSITION_OP -> {
                if (Transition.drawWipe(shapeRenderer, leftToRight = false, reverse = true)) {
                    state = ScreenState.MAIN
                    blockInput = false
                }
            }
            ScreenState.TRANSITION_EN -> {
                if (Transition.drawWipe(shapeRenderer)) {
                    resetState()
                    game.setScreen<Trip>()
                }
            }
            else -> Unit
        }
    }

    private fun resetState() {
        state = ScreenState.TRANSITION_OP
        icons.clear()
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

    override fun inputUp(x: Int, y: Int) {
        icons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        icons.forEach { it.touch(x, y) }
    }
}