package com.yopox.crystals

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import ktx.graphics.use

/**
 * Utilitary variables and functions.
 *
 * TODO: Move all texts in another object / file ?
 */
object Util {

    lateinit var font: BitmapFont
    lateinit var bigFont: BitmapFont

    const val WIDTH = 160f
    const val HEIGHT = 90f
    const val TEXT_TITLE = "Crystals"
    const val TEXT_NEWGAME = "Click to start"
    const val TEXT_CLASS = "Priest"
    const val TEXT_NEXT = "Next"
    const val TEXT_PREVIOUS = "Previous"
    const val TEXT_CONTINUE = "Continue"
    const val TEXT_TRIP = "Trip"
    const val TEXT_BAG = "Bag"
    const val TEXT_OPTIONS = "Options"

    /**
     * Returns the x position for the text to be centered.
     *
     * @param bitmapFont The desired font
     * @param value The text
     * @param targetW The text container width
     */
    fun textOffsetX(bitmapFont: BitmapFont, value: String, targetW: Int): Float {
        val glyphLayout = GlyphLayout()
        glyphLayout.setText(bitmapFont, value)
        return (targetW - glyphLayout.width) / 2
    }

    fun buttonTextOffset(value: String) = textOffsetX(font, value, 36)

    const val BOXWIDTH = 1f

    /**
     * Draw a black rectangle with a white [Util.BOXWIDTH] px inner border.
     *
     * @param x X position (bottom-left)
     * @param y Y position (bottom-left)
     */
    fun drawRect(shapeRenderer: ShapeRenderer, x: Float, y: Float, w: Float, h: Float) {
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(x, y, w, h)
            it.color = Color.BLACK
            it.rect(x + BOXWIDTH, y + BOXWIDTH, w - 2 * BOXWIDTH, h - 2 * BOXWIDTH)
        }
    }

    fun unproject(camera: OrthographicCamera, screenX: Int, screenY: Int): Pair<Int, Int> {
        val pos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        return Pair(pos.x.toInt(), pos.y.toInt())
    }

    operator fun Vector3.component1(): Float = this.x
    operator fun Vector3.component2(): Float = this.y
    operator fun Vector3.component3(): Float = this.z

}