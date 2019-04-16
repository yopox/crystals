package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Util
import com.yopox.crystals.data.EVENT_TYPE
import com.yopox.crystals.data.Event
import ktx.graphics.use

/**
 * Handles the logic and visual aspects of Chunks.
 *
 * TODO: Chunk types
 */
class Chunk(private val x: Float, private val y: Float) {
    var events = ArrayList<Event>()
    var selected = -1
    private companion object {
        const val width = 16f
        const val height = 3 * 14 + 4f
    }

    /**
     * TODO: Real event generation
     */
    init {
        val eT = EVENT_TYPE.values()
        repeat(3) {
            events.add(Event(eT.random()))
        }
    }

    fun draw(sR: ShapeRenderer, batch: SpriteBatch, icons: Texture) {
        // Box
        Util.drawRect(sR, x, y, width, height)

        // Icons
        for (i in 0 until events.size) {
            val iconX = events[i].iconX
            val iconY = if (selected == i) 14 else 0
            batch.use {
                it.draw(icons, x + 1, y + 1 + 15*i, iconX, iconY, 14, 14)
            }
        }

        // Separation lines
        sR.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            for (j in 1..2) {
                val y2 = y + 15 * j
                it.rect(x, y2, width, 1f)
            }
        }
    }

    fun touch(tX: Int, tY: Int) {
        if (tX - x > 0 && tX - x < width && tY - y > 0 && tY - y < height)
            selected = ((tY - y) / 15).toInt()
    }

}