package com.yopox.crystals.logic.fight

import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Spells
import com.yopox.crystals.screens.Fight

class Monster(type: Fighters.ID, name: String, monsterStats: Stats, val spells: ArrayList<Spell>): Fighter(type, name, true) {

    init {
        baseStats = monsterStats.copy()
    }

    override fun getMove(fighters: ArrayList<Fighter>): Fight.Move {
        val spell = spells.filter { it.cost <= stats.mp }.random()
        val targets = ArrayList<Fighter>()

        when (spell.target) {
            Target.SINGLE -> targets.add(fighters.filter { it.team != team }.random())
            Target.ENEMIES -> targets.addAll(fighters.filter { it.team != team })
            Target.ALL -> targets.addAll(fighters)
            Target.SELF -> targets.add(this)
        }

        return Fight.Move(this, spell, targets)
    }
}