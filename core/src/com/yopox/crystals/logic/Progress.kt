package com.yopox.crystals.logic

import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Jobs
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.sqrt

object Progress {
    fun reset() {
        Fighters.heroes.forEach { it.reset() }
        gold = 100
    }

    var player = Fighters.heroes[0]
    var gold = 100
    val items = mutableMapOf<Item, Int>().withDefault { 0 }

    val XP_LEVELS = List(100) { i -> floor(exp(sqrt(4.0 * i))).toInt() }
    val XP_GAPS = List(100) { i -> (XP_LEVELS.getOrNull(i + 1) ?: Int.MAX_VALUE) - (XP_LEVELS.getOrNull(i) ?: 0)}

}