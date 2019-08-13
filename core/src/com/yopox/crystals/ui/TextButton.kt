package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Util
import ktx.graphics.use

/**
 * TextButton class.
 */
class TextButton(x: Float, y: Float, private val text: String, xl: Boolean = false, clickable: Boolean = true, onClick: () -> Unit) : Button(Pair(x, y), clickable, onClick) {

    private val width: Float = when (xl) {
        true -> 2 * Util.BUTTON_WIDTH
        else -> Util.BUTTON_WIDTH
    }
    override val size = Pair(width, Util.BUTTON_HEIGHT)
    private val textX = Util.textOffsetX(Util.font, text, width)

    override fun draw(sR: ShapeRenderer, batch: SpriteBatch) {
        when (clicked) {
            true -> Util.drawFilledRect(sR, pos.first, pos.second, width, Util.BUTTON_HEIGHT)
            else -> {
                Util.drawRect(sR, pos.first, pos.second, width, Util.BUTTON_HEIGHT)
                batch.use {
                    Util.font.draw(it, text, pos.first + textX, pos.second + Util.BUTTON_HEIGHT / 3 * 2)
                }
            }
        }
    }

}