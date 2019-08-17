package com.yopox.crystals.logic

import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Fighters.ID.*
import com.yopox.crystals.def.Icons

data class Stats(var hp: Int = 40,
                 var mp: Int = 10,
                 var atk: Int = 10,
                 var wis: Int = 10,
                 var def: Int = 10,
                 var spd: Int = 10)

enum class Team {
    ALLIES,
    ENEMIES
}

open class Entity(val type: Fighters.ID, enemy: Boolean = false) {
    var baseStats = Stats()
    var stats = Stats()
    var gold = 321
    var team = if (enemy) Team.ENEMIES else Team.ALLIES

    open fun getIcon() = when (type) {
        HERO -> Icons.Warrior
        SNAKE -> Icons.Snake
        BAT -> Icons.Bat
    }
}