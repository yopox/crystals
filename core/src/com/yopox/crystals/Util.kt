package com.yopox.crystals

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import ktx.graphics.use
import kotlin.math.min

enum class ScreenState {
    MAIN,
    TRANSITION_OP,
    TRANSITION_EN
}

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
    const val TEXT_NEXT = "Next"
    const val TEXT_PREVIOUS = "Previous"
    const val TEXT_CONTINUE = "Continue"
    const val TEXT_TRIP = "Trip"
    const val TEXT_BAG = "Bag"
    const val TEXT_OPTIONS = "Options"

    /**
     * Generates [BitmapFont] objects from `.ttf` files.
     */
    fun genFonts() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("babyblocks.ttf"))
        val generator2 = FreeTypeFontGenerator(Gdx.files.internal("bubbleTime.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = 8
        val parameter2 = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter2.size = 21
        font = generator.generateFont(parameter)
        bigFont = generator2.generateFont(parameter2)

        generator.dispose()
        generator2.dispose()
    }

    /**
     * Returns the x position for the text to be centered.
     *
     * @param bitmapFont The desired font
     * @param value The text
     * @param targetW The text container width
     */
    fun textOffsetX(bitmapFont: BitmapFont, value: String, targetW: Float): Float {
        val glyphLayout = GlyphLayout()
        glyphLayout.setText(bitmapFont, value)
        return (targetW - glyphLayout.width) / 2
    }

    /**
     * Border width for boxes.
     */
    const val BOX_WIDTH = 1f
    const val BUTTON_WIDTH = 36f
    const val BUTTON_HEIGHT = 16f

    /**
     * Draw a black rectangle with a white [Util.BOX_WIDTH] px inner border.
     *
     * @param x X position (bottom-left)
     * @param y Y position (bottom-left)
     */
    fun drawRect(shapeRenderer: ShapeRenderer, x: Float, y: Float, w: Float, h: Float) {
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(x, y, w, h)
            it.color = Color.BLACK
            it.rect(x + BOX_WIDTH, y + BOX_WIDTH, w - 2 * BOX_WIDTH, h - 2 * BOX_WIDTH)
        }
    }

    /**
     * Draw a filled white rectangle.
     *
     * @param x X position (bottom-left)
     * @param y Y position (bottom-left)
     */
    fun drawFilledRect(shapeRenderer: ShapeRenderer, x: Float, y: Float, w: Float, h: Float) {
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(x, y, w, h)
        }
    }

    fun unproject(camera: Camera, screenX: Int, screenY: Int): Pair<Int, Int> {
        val pos = camera.unproject(Vector3(screenX.toFloat(), screenY.toFloat(), 0f))
        return Pair(pos.x.toInt(), pos.y.toInt())
    }

    /**
     * Transition time (in frames).
     */
    const val TRANSITION_TIME = 30
    private const val TRANSITION_STUN = 2
    private var TRANSITION_FRAME = 0

    /**
     * Maps [0; 1] to [0; 1] with out cubic easing.
     */
    fun easeOutQuad(t: Float): Float {
        return t * (2 - t)
    }

    /**
     * Draws a white wipe transition.
     */
    fun drawWipe(sR: ShapeRenderer, leftToRight: Boolean = true, reverse: Boolean = false): Boolean {
        var progress = min(TRANSITION_FRAME, TRANSITION_TIME)
        if (reverse) progress = TRANSITION_TIME - progress

        val width = easeOutQuad(progress.toFloat() / TRANSITION_TIME) * WIDTH
        val offset = when (leftToRight) {
            true -> 0f
            else -> WIDTH - width
        }

        sR.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(offset, 0f, width, HEIGHT)
        }

        if (TRANSITION_FRAME == TRANSITION_TIME + TRANSITION_STUN) {
            TRANSITION_FRAME = 0
            return true
        }

        TRANSITION_FRAME++
        return false

    }

    /**
     * Maps an [Int] to a [String] including the sign (`+`/`-`).
     */
    fun signedInt(i: Int): String {
        return when(i <= 0) {
            true -> "$i"
            else -> "+$i"
        }
    }

}