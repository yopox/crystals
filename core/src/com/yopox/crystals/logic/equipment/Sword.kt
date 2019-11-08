package com.yopox.crystals.logic.equipment

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.yopox.crystals.Crystals
import com.yopox.crystals.def.Icons
import kotlin.random.Random

class Sword(name: String, description: String, value: Int, icon: Icons.ID) : Item(name, description, value, icon) {

    data class Combination(
            val blade: Int = Random.nextInt(10),
            val handle: Int = Random.nextInt(10),
            val guard: Int = Random.nextInt(10))

    val id = Combination()
    val texture: Texture

    init {
        // Texture generation
        texture = if (textures[id] != null) textures[id]!! else genTexture(id)
    }

    companion object {
        private val pixmap: Pixmap
        val textures = mutableMapOf<Combination, Texture>()

        init {
            val t: Texture = Crystals.assetManager["swords_sheet.png"]
            if (!t.textureData.isPrepared) t.textureData.prepare()
            pixmap = t.textureData.consumePixmap()
        }

        /**
         * Generates the [Texture] for a given [Sword.Combination].
         */
        fun genTexture(id: Combination): Texture {
            // Get the [TextureRegion]s

            // Get the resulting texture
            val subPixmap = Pixmap(16, 16, Pixmap.Format.RGBA8888)
            subPixmap.drawPixmap(pixmap, -16 * id.blade, 0)
            subPixmap.drawPixmap(pixmap, -16 * id.handle, -16)
            subPixmap.drawPixmap(pixmap, -16 * id.guard, -32)
            val t = Texture(subPixmap)

            // Set the map value
            textures[id] = t
            return t
        }
    }

}