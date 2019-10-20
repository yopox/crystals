package com.yopox.crystals.screens

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Events
import com.yopox.crystals.logic.Event
import com.yopox.crystals.ui.Transition
import ktx.app.KtxScreen
import ktx.graphics.use

class Display(private val game: Crystals) : KtxScreen {

    companion object {
        fun changeEvent(event: Event) {
            this.event = event
            textX = Util.textOffsetX(Util.font, event.name, Util.WIDTH)
        }

        var event: Event = Event()
        var textX: Float = 0f
    }

    private val batch = SpriteBatch()
    private val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    private val shapeRenderer = ShapeRenderer()
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    private val icons: Texture = Crystals.assetManager["icons.png"]

    private var state = ScreenState.TRANSITION_OP
    private var frame = 0


    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        batch.use {
            it.draw(icons, Util.WIDTH / 2 - 7, Util.HEIGHT / 2 + 3, event.iconX, 0, 14, 14)
            Util.font.draw(it, event.name, textX, Util.HEIGHT / 2)
        }

        when (state) {
            ScreenState.TRANSITION_OP -> {
                if (Transition.drawWipe(shapeRenderer, leftToRight = false, reverse = true)) {
                    state = ScreenState.MAIN
                }
            }
            ScreenState.TRANSITION_EN -> {
                if (Transition.drawWipe(shapeRenderer)) {
                    resetState()
                    when (event.id) {
                        Events.ID.BATTLE -> {
                            game.getScreen<Fight>().setup()
                            game.setScreen<Fight>()
                        }
                        Events.ID.INN -> {
                            game.getScreen<Inn>().setup(event)
                            game.setScreen<Inn>()
                        }
                        Events.ID.TEMPLE -> {
                            game.getScreen<Temple>().setup(event)
                            game.setScreen<Temple>()
                        }
                        else -> game.setScreen<Trip>()
                    }
                }
            }
            else -> {
                frame++
                if (frame == Transition.DISPLAY_LEN) {
                    state = ScreenState.TRANSITION_EN
                }
            }
        }

    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        camera.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f)
    }

    private fun resetState() {
        state = ScreenState.TRANSITION_OP
        frame = 0
    }

}