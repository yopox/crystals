package com.yopox.crystals.ui

import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.RNG
import com.yopox.crystals.def.weighedRandom
import com.yopox.crystals.logic.Item
import com.yopox.crystals.ui.Content.NONE

enum class Content {
    NONE,
    GOLD,
    TREASURE
}

class Tile(id: Pair<Int, Int>, pos: Pair<Float, Float>, onClick: Function0<Unit>) : Icon(id, pos, onClick) {
    val type: Content = NONE
    var gold = 0
    var treasure: Item? = null
    var firstTouched = false

    companion object {

        fun genInnTile(pos: Pair<Float, Float>): Tile {
            val icon = RNG.inn.weighedRandom()
            return Tile(Icons.map.getValue(icon), pos) {}
        }
    }

}
