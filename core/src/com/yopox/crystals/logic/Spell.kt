package com.yopox.crystals.logic

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
        val effect: (f1: Fighter, f2: Fighter) -> ArrayList<Fight.Block> = { _, _ -> ArrayList<Fight.Block>() }
) {

    fun use(move: Fight.Move): ArrayList<Fight.Block> {
        val blocks = ArrayList<Fight.Block>()
        move.targets.forEach { blocks.addAll(effect(move.fighter, it)) }
        return blocks
    }

}











