package com.yopox.crystals.logic

import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Fighters.ID.*
import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells
import com.yopox.crystals.screens.Fight
import kotlin.math.min

data class Stats(var hp: Int = 20,
                 var mp: Int = 10,
                 var atk: Int = 10,
                 var wis: Int = 10,
                 var def: Int = 10,
                 var spd: Int = 10)

enum class Team {
    ALLIES,
    ENEMIES
}

open class Fighter(val type: Fighters.ID, val name: String, enemy: Boolean = true) {
    var baseStats = Stats()
    var stats = Stats()
    var gold = 1
    var team = if (enemy) Team.ENEMIES else Team.ALLIES
    var battleId = 0
    val alive: Boolean
        get() = stats.hp > 0

    open fun getIcon() = when (type) {
        HERO -> Icons.Warrior
        SNAKE -> Icons.Snake
        BAT -> Icons.Bat
    }

    internal open fun getMove(fighters: ArrayList<Fighter>): Fight.Move {
        return Fight.Move(this, Spells.baseSpell(Jobs.ID.ANY), arrayListOf())
    }

    fun attack(f: Fighter): ArrayList<Fight.Block> {
        val blocks = ArrayList<Fight.Block>()

        // Damage calculation
        val damage = min(stats.atk, f.stats.hp)

        // Attack
        f.stats.hp -= damage
        if (damage > 0) blocks.add(Fight.Block(Fight.BlockType.DAMAGE, int1 = f.battleId))
        blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} lost $damage HP."))

        // K.O. condition
        if (!f.alive) {
            blocks.add(Fight.Block(Fight.BlockType.KO, int1 = f.battleId))
            blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} is defeated!"))
        }

        return blocks
    }
}