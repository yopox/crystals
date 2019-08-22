package com.yopox.crystals.logic.fight

import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.screens.Fight

enum class Target {
    SINGLE,
    ENEMIES,
    ALL,
    SELF
}

class Spell(
        val id: Actions.ID,
        val name: String,
        val job: Jobs.ID,
        val cost: Int,
        val target: Target,
        val effect: (f1: Fighter, f2: Fighter) -> List<Fight.Block> = { _, _ -> ArrayList() }
) {

    fun use(move: Fight.Move): ArrayList<Fight.Block> {
        move.fighter.stats.mp -= move.spell.cost
        val blocks = ArrayList<Fight.Block>()
        if (move.targets.isEmpty()) blocks.addAll(effect(move.fighter, move.fighter))
        else move.targets.forEach { blocks.addAll(effect(move.fighter, it)) }
        return blocks
    }

}










