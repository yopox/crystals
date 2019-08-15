package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Util

/**
 * Abstract Button implementation, handles touch input.
 *
 * @param pos (x, y) pair
 * @param clickable is clickable
 * @param onClick click callback
 */
abstract class Button(protected val pos: Pair<Float, Float>, var clickable: Boolean = true, val onClick: () -> Unit) {
    protected var clicked = false
    var visible = true
    open val size = Pair(Util.BUTTON_WIDTH, Util.BUTTON_HEIGHT)

    abstract fun draw(sR: ShapeRenderer, batch: SpriteBatch)

    /**
     * Called when the player begins touching the screen.
     */
    fun touch(x: Int, y: Int) {
        if (clickable
                && x > pos.first
                && x < pos.first + size.first - 1
                && y > pos.second
                && y < pos.second + size.second - 1)
            clicked = true
    }

    /**
     * Called when the player stops touching the screen.
     */
    fun lift(x: Int, y: Int) {
        if (clicked && visible) {
            clicked = false
            if (x > pos.first
                    && x < pos.first + size.first - 1
                    && y > pos.second
                    && y < pos.second + size.second - 1)
                onClick()
        }
    }

    fun hide() {
        visible = false
    }

    fun show() {
        visible = true
    }

}