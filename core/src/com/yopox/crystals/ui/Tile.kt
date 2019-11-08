package com.yopox.crystals.ui

import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.Items
import com.yopox.crystals.def.RNG
import com.yopox.crystals.def.weighedRandom
import com.yopox.crystals.logic.equipment.Item
import com.yopox.crystals.ui.Content.NONE

enum class Content {
    NONE,
    GOLD,
    TREASURE
}

/**
 * Icon with more properties.
 */
class Tile(id: Icons.ID, pos: Pair<Float, Float>, onClick: Function0<Unit>) : Icon(id, pos, onClick) {
    val type: Content = NONE
    var gold = 0
    var treasure: Item? = null
    var firstTouched = false

    companion object {

        fun genInnTile(pos: Pair<Float, Float>, onClick: Function0<Unit> = {}): Tile {
            val icon = RNG.inn.weighedRandom()
            val id = RNG.genTreasure(icon)
            val item = if (id != null) Items(id) else null
            return Tile(icon, pos, onClick).apply { treasure = item }
        }

        fun genTempleTile(pos: Pair<Float, Float>, onClick: Function0<Unit> = {}): Tile {
            val icon = RNG.temple.weighedRandom()
            return Tile(icon, pos, onClick)
        }

        fun genShopTile(pos: Pair<Float, Float>, onClick: Function0<Unit> = {}): Tile {
            val id = RNG.shopItems.weighedRandom()
            val item = Items(id)
            return Tile(item.id, pos, onClick).apply { treasure = item }
        }

        fun genGardenTile(pos: Pair<Float, Float>, onClick: Function0<Unit> = {}): Tile {
            val icon = RNG.garden.weighedRandom()
            val id = RNG.genTreasure(icon)
            val item = if (id != null) Items(id) else null
            return Tile(icon, pos, onClick).apply { treasure = item }
        }

    }

}
