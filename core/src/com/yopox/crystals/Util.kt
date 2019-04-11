package com.yopox.crystals

import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout


object Util {
    const val WIDTH = 320f
    const val HEIGHT = 180f
    const val TITLE = "Crystals"
    const val NEWGAME = "Click to start"

    fun getPositionOffset(bitmapFont: BitmapFont, value: String, targetW: Int): Float {
        val glyphLayout = GlyphLayout()
        glyphLayout.setText(bitmapFont, value)
        return (targetW - glyphLayout.width) / 2
    }

}