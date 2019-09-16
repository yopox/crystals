package com.yopox.crystals.logic.fight

import com.badlogic.gdx.Gdx
import com.yopox.crystals.Util
import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Fighters.ID.*
import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells
import com.yopox.crystals.screens.Fight
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

data class Stats(var hp: Int = 20,
                 var mp: Int = 10,
                 var atk: Int = 5,
                 var wis: Int = 5,
                 var def: Int = 5,
                 var spd: Int = 5,
                 var atkCoef: Double = 1.0,
                 var defCoef: Double = 1.0) {
    infix fun to(stats: Stats) {
        atk = stats.atk
        wis = stats.wis
        def = stats.def
        spd = stats.spd
        atkCoef = stats.atkCoef
        defCoef = stats.defCoef
    }
}

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
    SPD,
    ATK_COEF,
    DEF_COEF
}

data class Buff(val stat: Stat, val amount: Double, var duration: Int = 0, val target: Fighter)

open class Fighter(val type: Fighters.ID, val name: String, enemy: Boolean = true) {
    var baseStats = Stats()
    var stats = Stats()
    var buffs = ArrayList<Buff>()
    var poison = ArrayList<Buff>()
    var gold = 1
    var team = if (enemy) Team.ENEMIES else Team.ALLIES
    var battleId = 0
    val alive: Boolean
        get() = stats.hp > 0

    open fun getIcon() = when (type) {
        HERO -> Icons.Warrior
        SNAKE -> Icons.Snake
        BAT -> Icons.Bat
        DOG -> Icons.Dog
        SPIDER -> Icons.Spider
        else -> Icons.Unknown
    }

    open fun prepare() {
        stats = baseStats.copy()
        buffs.clear()
    }

    fun beginTurn(fighters: ArrayList<Fighter>): List<Fight.Block> {
        val blocks = ArrayList<Fight.Block>()

        // Update stats
        stats to baseStats

        // Own buffs
        for (i in buffs.lastIndex downTo 0)
            if (buffs[i].duration == 0) buffs.removeAt(i)
        buffs.forEach { if (it.target == this) buff(it.stat, it.amount); it.duration-- }

        // Buffs given by other fighters
        for (f in fighters) {
            if (f != this) for (b in f.buffs) {
                if (b.target == this) buff(b.stat, b.amount)
            }
        }

        // Poison management
        if (poison.any()) {
            var damage = 0

            // Calculate poison damage
            poison.forEach { damage += it.amount.toInt(); it.duration-- }

            // Remove old poisons
            for (i in poison.lastIndex downTo 0)
                if (poison[i].duration == 0) poison.removeAt(i)

            // Display
            blocks.add(Fight.Block(Fight.BlockType.TEXT, "$name is hit by poison!"))

            // Remove HP
            stats.hp -= damage
            if (damage > 0) blocks.add(Fight.Block(Fight.BlockType.DAMAGE, content = Util.signedInt(-damage), int1 = battleId))
            if (type == HERO) blocks.add(Fight.Block(Fight.BlockType.UPDATE_HP, int1 = stats.hp))

            blocks.addAll(checkKO(this))
        }
        return blocks
    }

    internal open fun getMove(fighters: ArrayList<Fighter>): Fight.Move {
        return Fight.Move(this, Spells.baseSpell(Jobs.ID.ANY), arrayListOf())
    }

    fun attack(f: Fighter, leech: Boolean = false): ArrayList<Fight.Block> {
        if (!f.alive) return ArrayList()

        val blocks = ArrayList<Fight.Block>()

        // Attack
        val damage = calcPhysicalDamage(this, f)
        f.stats.hp -= damage
        blocks.add(Fight.Block(Fight.BlockType.DAMAGE, content = Util.signedInt(-damage), int1 = f.battleId))
        if (f.type == HERO) blocks.add(Fight.Block(Fight.BlockType.UPDATE_HP, int1 = f.stats.hp))

        if (leech) {
            val leeched = damage / 2
            healHP(leeched)
            blocks.add(Fight.Block(Fight.BlockType.DAMAGE, content = Util.signedInt(leeched), int1 = battleId))
            if (type == HERO) blocks.add(Fight.Block(Fight.BlockType.UPDATE_HP, int1 = stats.hp))
        }

        blocks.addAll(checkKO(f))

        return blocks
    }

    fun magicalAttack(f: Fighter, power: Int): ArrayList<Fight.Block> {
        if (!f.alive) return ArrayList()

        val blocks = ArrayList<Fight.Block>()

        // Attack
        val damage = calcMagicalDamage(this, f, power)
        f.stats.hp -= damage
        if (damage > 0) blocks.add(Fight.Block(Fight.BlockType.DAMAGE, content = Util.signedInt(-damage), int1 = f.battleId))
        if (f.type == HERO) blocks.add(Fight.Block(Fight.BlockType.UPDATE_HP, int1 = f.stats.hp))
        blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} lost $damage HP."))

        blocks.addAll(checkKO(f))

        return blocks
    }

    fun addBuff(stat: Stat, amount: Double, duration: Int, target: Fighter = this): ArrayList<Fight.Block> {
        target.buff(stat, amount)
        buffs.add(Buff(stat, amount, duration, target))
        return ArrayList()
    }

    fun buff(stat: Stat, amount: Double) = when (stat) {
        Stat.HP -> healHP(amount.toInt())
        Stat.MP -> healMP(amount.toInt())
        Stat.ATK -> stats.atk += amount.toInt()
        Stat.WIS -> stats.wis += amount.toInt()
        Stat.DEF -> stats.def += amount.toInt()
        Stat.SPD -> stats.spd += amount.toInt()
        Stat.ATK_COEF -> stats.atkCoef *= amount
        Stat.DEF_COEF -> stats.defCoef *= amount
    }

    fun healHP(amount: Int) {
        stats.hp = min(stats.hp + amount, baseStats.hp)
    }

    fun healMP(amount: Int) {
        stats.mp = min(stats.mp + amount, baseStats.mp)
    }

    private fun calcPhysicalDamage(from: Fighter, to: Fighter): Int {
        // Damage calculation
        val atk = from.stats.atk * from.stats.atkCoef
        val def = to.stats.def * to.stats.defCoef
        var damage = round(atk - def).toInt()

        // Min damage
        val minDamage = round(atk * Util.MIN_PHYSICAL_DAMAGE).toInt()
        damage = max(damage, minDamage)
        return min(damage, to.stats.hp)
    }

    private fun calcMagicalDamage(from: Fighter, to: Fighter, power: Int): Int {
        // Damage calculation
        val atk = from.stats.wis * power / 10 * from.stats.atkCoef
        val def = (to.stats.def / 2 + to.stats.wis / 2) * to.stats.defCoef
        var damage = round(atk - def).toInt()

        // Min damage
        val minDamage = round(atk * Util.MIN_MAGICAL_DAMAGE).toInt()
        damage = max(damage, minDamage)
        return min(damage, to.stats.hp)
    }

    private fun checkKO(f: Fighter): List<Fight.Block> {
        val blocks = ArrayList<Fight.Block>()

        // K.O. condition
        if (!f.alive) {
            blocks.add(Fight.Block(Fight.BlockType.KO, int1 = f.battleId))
            blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} is defeated!"))
        }

        return blocks
    }

    fun heal(f: Fighter, i: Int): List<Fight.Block> {
        val blocks = ArrayList<Fight.Block>()

        // Heal amount calculation
        var h = round(i * stats.wis / 20.0).toInt()
        h = min(f.baseStats.hp - f.stats.hp, h)

        if (h > 0) {
            f.healHP(h)
            blocks.add(Fight.Block(Fight.BlockType.DAMAGE, content = Util.signedInt(h), int1 = f.battleId))
            blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} recovered $h HP."))
        }
        return blocks
    }

    fun poison(f: Fighter, power: Int, duration: Int): List<Fight.Block> {
        val blocks = ArrayList<Fight.Block>()
        blocks.add(Fight.Block(Fight.BlockType.DAMAGE, content = "PSN", int1 = f.battleId))
        blocks.add(Fight.Block(Fight.BlockType.TEXT, "${f.name} is poisoned."))
        f.poison.add(Buff(Stat.HP, calcMagicalDamage(this, f, power).toDouble(), duration, f))
        return blocks
    }

}