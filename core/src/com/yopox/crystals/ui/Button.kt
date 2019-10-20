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
abstract class Button(var pos: Pair<Float, Float>, var clickable: Boolean = true, var onClick: () -> Unit) {
    var clicked = false
        protected set
    var visible = true
    open val size = Pair(Util.BUTTON_WIDTH, Util.BUTTON_HEIGHT)

    abstract fun draw(sR: ShapeRenderer, batch: SpriteBatch)

    /**
     * Called when the player begins touching the screen.
     */
    fun touch(x: Int, y: Int) {
        if (clickable
                && x >= pos.first - 1
                && x <= pos.first + size.first
                && y >= pos.second - 1
                && y <= pos.second + size.second)
            clicked = true
    }

    /**
     * Called when the player stops touching the screen.
     */
    fun lift(x: Int, y: Int) {
        if (clicked && visible) {
            clicked = false
            if (x >= pos.first - 1
                    && x <= pos.first + size.first
                    && y >= pos.second - 1
                    && y <= pos.second + size.second)
                onClick()
        }
    }

    fun hide() {
        visible = false
    }

    fun show() {
        visible = true
    }

    fun reset() {
        clicked = false
    }

}