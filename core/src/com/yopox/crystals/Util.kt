package com.yopox.crystals

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import ktx.graphics.use


enum class ScreenState {
    MAIN,
    TRANSITION_OP,
    TRANSITION_EN
}

/**
 * Utilitarian variables and functions.
 *
 * TODO: Move all texts in another file & translation support
 */
object Util {

    lateinit var font: BitmapFont
    lateinit var bigFont: BitmapFont

    const val BUILD_NB = "build 0.1"

    const val WIDTH = 160f
    const val HEIGHT = 90f
    const val TITLE_OFFSET = 8

    const val FONT_SIZE = 8
    const val BIG_FONT_SIZE = 21
    const val TEXT_TITLE = "Crystals"
    const val TEXT_NEWGAME = "Click to start"
    const val TEXT_NEXT = "Next"
    const val TEXT_PREVIOUS = "Previous"
    const val TEXT_CONTINUE = "Continue"
    const val TEXT_TRIP = "Trip"
    const val TEXT_STATUS = "Status"
    const val TEXT_BAG = "Bag"
    const val TEXT_OPTIONS = "Options"
    const val TEXT_FIGHT = "Fight"
    const val TEXT_ACTIONS = "Actions"
    const val TEXT_LOG = "Battle log"
    const val TEXT_TARGET = "Choose a target"
    const val TEXT_GAME_OVER = "Game Over"
    const val TEXT_RETURN_TITLE = "Return to title"
    /**
     * Generates [BitmapFont] objects from `.ttf` files.
     */
    fun genFonts() {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("babyblocks.ttf"))
        val generator2 = FreeTypeFontGenerator(Gdx.files.internal("bubbleTime.ttf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = FONT_SIZE
        val parameter2 = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter2.size = BIG_FONT_SIZE
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

    fun sizeX(bitmapFont: BitmapFont, value: String): Float {
        val glyphLayout = GlyphLayout()
        glyphLayout.setText(bitmapFont, value)
        return glyphLayout.width
    }

    /**
     * Border width for boxes.
     */
    const val BOX_WIDTH = 1f
    const val BUTTON_WIDTH = 36f
    const val BUTTON_HEIGHT = 16f
    const val CHUNK_SEP = 20f

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
     * Maps an [Int] to a [String] including the sign (`+`/`-`).
     */
    fun signedInt(i: Int): String = if (i <= 0) "$i" else "+$i"

    fun <T> next(set: List<T>, elem: T): T {
        val nextPos = (set.indexOf(elem) - 1) % set.size
        return set.elementAt(nextPos)
    }

    fun <T> previous(set: List<T>, elem: T): T {
        val previousPos = (set.indexOf(elem) + 1 + set.size) % set.size
        return set.elementAt(previousPos)
    }

    val invertShader = ShaderProgram(
            Gdx.files.internal("shader/invertVert.vs").readString(),
            Gdx.files.internal("shader/invertFrag.vs").readString())
    val defaultShader: ShaderProgram? = SpriteBatch.createDefaultShader()


}