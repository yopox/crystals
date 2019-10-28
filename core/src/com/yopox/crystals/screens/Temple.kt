package com.yopox.crystals.screens

import com.badlogic.gdx.graphics.Texture
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.logic.Event
import com.yopox.crystals.ui.Icon
import com.yopox.crystals.ui.Screen
import com.yopox.crystals.ui.Tile
import ktx.graphics.use
import kotlin.math.sin

class Temple(game: Crystals) : Screen(Util.TEXT_TEMPLE, game) {
    private val icons = ArrayList<Icon>()
    private var frame = 0
    val ray: Texture = Crystals.assetManager["temple.png"]
    val circle: Texture = Crystals.assetManager["circle.png"]

    fun setup(event: Event) {
        icons.add(Tile.genTempleTile(Pair(72f, 30f)) { state = ScreenState.ENDING; blockInput = true })
    }

    override fun render(delta: Float) {
        super.render(delta)

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

        drawTransitions()
    }

    override fun resetState() {
        state = ScreenState.OPENING
        icons.clear()
    }

    override fun inputUp(x: Int, y: Int) {
        icons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        icons.forEach { it.touch(x, y) }
    }
}