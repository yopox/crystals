package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Util
import ktx.graphics.use

class Button(val x: Float, val y: Float, val text: String, val onClick: () -> Unit) {
    private var clicked = false
    private val textX = Util.textOffsetX(Util.font, text, Util.BUTTON_WIDTH)

    fun draw(sR: ShapeRenderer, batch: SpriteBatch) {
        when (clicked) {
            true -> Util.drawFilledRect(sR, x, y, Util.BUTTON_WIDTH, Util.BUTTON_HEIGHT)
            else -> {
                Util.drawRect(sR, x, y, Util.BUTTON_WIDTH, Util.BUTTON_HEIGHT)
                batch.use {
                    Util.font.draw(it, text, x + textX, y + Util.BUTTON_HEIGHT / 3 * 2)
                }
            }
        }
    }

    fun touch(x: Int, y: Int) {
        if (x >= this.x && x <= this.x + Util.BUTTON_WIDTH && y >= this.y && y <= this.y + Util.BUTTON_HEIGHT)
            clicked = true
    }

    fun lift(x: Int, y: Int) {
        clicked = false
        if (x >= this.x && x <= this.x + Util.BUTTON_WIDTH && y >= this.y && y <= this.y + Util.BUTTON_HEIGHT)
            onClick()
    }

}