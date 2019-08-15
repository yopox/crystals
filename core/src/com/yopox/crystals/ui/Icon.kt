package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Crystals

/**
 * Icon class : displays a 16*16 icon.
 *
 * @param id The icon ID, see [Def.Icons]
 * @param pos The icon position
 * @param onClick optional click callback
 */
class Icon(id: Pair<Int, Int>, pos: Pair<Float, Float>, onClick: () -> Unit = {}): Button(pos, true, onClick) {

    companion object {
        val icons: Texture = Crystals.assetManager["1BitPack.png"]
        private const val SIZE = 16
        private const val BORDER = 1
    }

    private val x: Int = id.first * (SIZE + BORDER)
    private val y: Int = id.second * (SIZE + BORDER)

    override fun draw(sR: ShapeRenderer, batch: SpriteBatch) {
        batch.draw(icons, pos.first, pos.second, x, y, SIZE, SIZE)
    }

}