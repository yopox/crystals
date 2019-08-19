package com.yopox.crystals.logic

import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Fighters.ID.*
import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells
import com.yopox.crystals.screens.Fight

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

open class Entity(val type: Fighters.ID, val name: String, enemy: Boolean = true) {
    var baseStats = Stats()
    var stats = Stats()
    var gold = 1
    var team = if (enemy) Team.ENEMIES else Team.ALLIES

    open fun getIcon() = when (type) {
        HERO -> Icons.Warrior
        SNAKE -> Icons.Snake
        BAT -> Icons.Bat
    }

    internal open fun getMove(fighters: ArrayList<Entity>): Fight.Move {
        return Fight.Move(this, Spells.baseSpell(Jobs.ID.ANY), arrayListOf<Entity>())
    }
}