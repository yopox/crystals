package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Util
import ktx.graphics.use

class Button(private val x: Float, private val y: Float, private val text: String, xl: Boolean = false, val onClick: () -> Unit) {
    private var clicked = false
    private val width: Float = when (xl) {
        true -> 2 * Util.BUTTON_WIDTH
        else -> Util.BUTTON_WIDTH
    }
    private val textX = Util.textOffsetX(Util.font, text, width)

    fun draw(sR: ShapeRenderer, batch: SpriteBatch) {
        when (clicked) {
            true -> Util.drawFilledRect(sR, x, y, width, Util.BUTTON_HEIGHT)
            else -> {
                Util.drawRect(sR, x, y, width, Util.BUTTON_HEIGHT)
                batch.use {
                    Util.font.draw(it, text, x + textX, y + Util.BUTTON_HEIGHT / 3 * 2)
                }
            }
        }
    }

    /**
     * Called when the player begins to touch the screen.
     */
    fun touch(x: Int, y: Int) {
        if (x >= this.x
                && x <= this.x + width
                && y >= this.y
                && y <= this.y + Util.BUTTON_HEIGHT)
            clicked = true
    }

    /**
     * Called when the player stops touching the screen.
     */
    fun lift(x: Int, y: Int) {
        clicked = false
        if (x >= this.x
                && x <= this.x + width
                && y >= this.y
                && y <= this.y + Util.BUTTON_HEIGHT)
            onClick()
    }

}