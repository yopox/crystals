package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.yopox.crystals.Crystals
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.equipment.Sword
import com.yopox.crystals.ui.TextureType.SHEET
import com.yopox.crystals.ui.TextureType.SWORD

enum class TextureType {
    SHEET,
    SWORD
}

class CompositeTexture {

    private val type: TextureType
    private val texture: Texture?
    val size = Pair(ICON_SIZE.toFloat(), ICON_SIZE.toFloat())
    var sheetId: Pair<Int, Int> = Pair(0, 0)
    var swordId: Sword.Combination = Sword.Combination()

    constructor(id: Pair<Int, Int>) {
        type = SHEET
        sheetId = id
        texture = null
    }

    constructor(id: Sword.Combination) {
        type = SWORD
        swordId = id
        texture = genSword()
    }

    companion object {
        const val ICON_SIZE = 16
        const val SHEET_BORDER = 1
        val iconsSheet: Texture = Crystals.assetManager["1BitPack.png"]
        val swordsSheetPm: Pixmap

        init {
            val swordsSheet: Texture = Crystals.assetManager["swords_sheet.png"]
            if (!swordsSheet.textureData.isPrepared) swordsSheet.textureData.prepare()
            swordsSheetPm = swordsSheet.textureData.consumePixmap()
        }
    }

    /**
     * Generates a sword icon by combining a blade, handle, and a guard.
     * TODO: Probabilities for each element
     */
    private fun genSword(): Texture {
        val subPixmap = Pixmap(16, 16, Pixmap.Format.RGBA8888)
        subPixmap.drawPixmap(swordsSheetPm, -16 * swordId.blade, 0)
        subPixmap.drawPixmap(swordsSheetPm, -16 * swordId.handle, -16)
        subPixmap.drawPixmap(swordsSheetPm, -16 * swordId.guard, -32)

        return Texture(subPixmap)
    }

    fun draw(batch: SpriteBatch, pos: Pair<Float, Float>, flip: Boolean) {
        when (type) {
            SHEET -> {
                batch.draw(iconsSheet,
                        pos.first, pos.second,
                        ICON_SIZE.toFloat(), ICON_SIZE.toFloat(),
                        sheetId.first * (ICON_SIZE + SHEET_BORDER), sheetId.second *  (ICON_SIZE + SHEET_BORDER),
                        ICON_SIZE, ICON_SIZE,
                        flip, false)
            }
            else -> batch.draw(texture!!, pos.first, pos.second)
        }
    }

    fun changeIcon(id: Icons.ID?) {
        sheetId = Icons(id ?: Icons.ID.UNKNOWN)
    }

}