package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.yopox.crystals.Crystals

class Icon(public val id: Int) {

    companion object {
        val icons: Texture = Crystals.assetManager["aseprite/icons.png"]
    }

    fun draw(batch: SpriteBatch) {

    }

}