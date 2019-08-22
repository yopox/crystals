package com.yopox.crystals.logic.fight

import com.badlogic.gdx.Gdx
import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Fighters.ID.*
import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells
import com.yopox.crystals.screens.Fight
import kotlin.math.min
import kotlin.math.round

data class Stats(var hp: Int = 20,
                 var mp: Int = 10,
                 var atk: Int = 5,
                 var wis: Int = 5,
                 var def: Int = 5,
                 var spd: Int = 5)

enum class Team {
    ALLIES,
    ENEMIES
}

enum class Stat {
    HP,
    MP,
    ATK,
    WIS,
    DEF,
    SPD
}

data class Buff(val stat: Stat, val amount: Int, var duration: Int = 0, val target: Fighter)

open class Fighter(val type: Fighters.ID, val name: String, enemy: Boolean = true) {
    var baseStats = Stats()
    var stats = Stats()
    var buffs = ArrayList<Buff>()
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

    fun beginTurn() {
        Gdx.app.log(name, "Turn")
        stats.atk = baseStats.atk
        stats.def = baseStats.def
        stats.wis = baseStats.wis
        stats.spd = baseStats.spd
        for (i in buffs.lastIndex downTo 0)
            if (buffs[i].duration == 0) buffs.removeAt(i)
        buffs.forEach { it.target.buff(it.stat, it.amount); it.duration-- }
    }

    internal open fun getMove(fighters: ArrayList<Fighter>): Fight.Move {
        return Fight.Move(this, Spells.baseSpell(Jobs.ID.ANY), arrayListOf())
    }

    fun attack(f: Fighter): ArrayList<Fight.Block> {
        if (!f.alive) return ArrayList()

        val blocks = ArrayList<Fight.Block>()

        // Damage calculation
        var damage = round(stats.atk * 60.0 / (60.0 + f.stats.def)).toInt()
        damage = min(damage, f.stats.hp)

        // Attack
        f.stats.hp -= damage
        if (damage > 0) blocks.add(Fight.Block(Fight.BlockType.DAMAGE, int1 = f.battleId))
        if (f.type == HERO) blocks.add(Fight.Block(Fight.BlockType.UPDATE_HP, int1 = f.stats.hp))
        blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} lost $damage HP."))

        // K.O. condition
        if (!f.alive) {
            blocks.add(Fight.Block(Fight.BlockType.KO, int1 = f.battleId))
            blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} is defeated!"))
        }

        return blocks
    }

    fun addBuff(stat: Stat, amount: Int, duration: Int, target: Fighter = this): ArrayList<Fight.Block> {
        target.buff(stat, amount)
        buffs.add(Buff(stat, amount, duration, target))
        return ArrayList()
    }

    fun buff(stat: Stat, amount: Int) = when (stat) {
        Stat.HP -> healHP(amount)
        Stat.MP -> healMP(amount)
        Stat.ATK -> stats.atk += amount
        Stat.WIS -> stats.wis += amount
        Stat.DEF -> stats.def += amount
        Stat.SPD -> stats.spd += amount
    }

    private fun healHP(amount: Int) {
        stats.hp = min(stats.hp + amount, baseStats.hp)
    }

    private fun healMP(amount: Int) {
        stats.mp = min(stats.mp + amount, baseStats.mp)
    }
}