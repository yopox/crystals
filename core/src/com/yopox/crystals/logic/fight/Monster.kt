package com.yopox.crystals.logic.fight

import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.equipment.Item
import com.yopox.crystals.screens.Fight

enum class Behavior {
    RANDOM
}

class Monster(type: Fighters.ID, name: String, val monsterStats: Stats, val spells: ArrayList<Spell>, icon: Icons.ID, val xp: Int, val coins: Int, val loots: Map<Item, Double>) : Fighter(type, name, icon, true) {

    private val behavior: Behavior = Behavior.RANDOM

    init {
        baseStats = monsterStats.copy()
    }

    override fun getMove(fighters: List<Fighter>): Fight.Move {

        val spells = spells.filter { it.cost <= stats.mp }
        val spell: Spell
        val targets = ArrayList<Fighter>()

        when (behavior) {
            else -> spell = spells.random()
        }

        when (spell.target) {
            Target.SINGLE -> targets.add(fighters.filter { it.team != team }.random())
            Target.ENEMIES -> targets.addAll(fighters.filter { it.team != team })
            Target.ALL -> targets.addAll(fighters)
            Target.SELF -> targets.add(this)
        }

        return Fight.Move(this, spell, targets)
    }

    fun scale(lvl: Int) {
        // TODO: Stats scaling
    }

}