package com.yopox.crystals

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.graphics.use


object Util {

    lateinit var font: BitmapFont
    lateinit var bigFont: BitmapFont

    const val WIDTH = 160f
    const val HEIGHT = 90f
    const val TEXT_TITLE = "Crystals"
    const val TEXT_NEWGAME = "Click to start"
    const val TEXT_TRIP = "Trip"
    const val TEXT_BAG = "Bag"
    const val TEXT_OPTIONS = "Options"

    fun getPositionOffset(bitmapFont: BitmapFont, value: String, targetW: Int): Float {
        val glyphLayout = GlyphLayout()
        glyphLayout.setText(bitmapFont, value)
        return (targetW - glyphLayout.width) / 2
    }

    const val BOXWIDTH = 1f

    fun drawRect(shapeRenderer: ShapeRenderer, x: Float, y: Float, w: Float, h: Float) {
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(x, y, w, h)
            it.color = Color.BLACK
            it.rect(x + BOXWIDTH, y + BOXWIDTH, w - 2 * BOXWIDTH, h - 2 * BOXWIDTH)
        }
    }

}