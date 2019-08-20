package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Crystals
import com.yopox.crystals.Util
import ktx.graphics.use

/**
 * Icon class : displays a 16*16 icon.
 *
 * @param id The icon ID, see [Def.Icons]
 * @param pos The icon position
 * @param onClick optional click callback
 */
class Icon(id: Pair<Int, Int>, pos: Pair<Float, Float>, onClick: () -> Unit = {}) : Button(pos, true, onClick) {

    companion object {
        val icons: Texture = Crystals.assetManager["1BitPack.png"]
        private const val SIZE = 16
        private const val BORDER = 1
    }

    val x: Int = id.first * (SIZE + BORDER)
    val y: Int = id.second * (SIZE + BORDER)
    override val size = Pair(SIZE.toFloat(), SIZE.toFloat())

    override fun draw(sR: ShapeRenderer, batch: SpriteBatch) {
        if (visible) when (clicked) {
                true -> Util.drawFilledRect(sR, pos.first, pos.second, size.first, size.second)
                else -> batch.use { it.draw(icons, pos.first, pos.second, x, y, SIZE, SIZE) }
            }
    }

}