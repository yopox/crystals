package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.yopox.crystals.Crystals

class Icon(id: Pair<Int, Int>, private val pos: Pair<Float, Float>) {

    companion object {
        val icons: Texture = Crystals.assetManager["1BitPack.png"]
        private const val SIZE = 16
        private const val BORDER = 1
    }

    private val x: Int = id.first * (SIZE + BORDER)
    private val y: Int = id.second * (SIZE + BORDER)

    fun draw(batch: SpriteBatch) {
        batch.draw(icons, pos.first, pos.second, x, y, SIZE, SIZE)
    }

}