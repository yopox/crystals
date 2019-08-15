package com.yopox.crystals.logic

import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells

class Spell(val job: Jobs.ID, val name: String, val cost: Int, val id: Spells.ID, val effect: (Fighter) -> Unit) {

    fun use(targets: ArrayList<Fighter>) {
        targets.forEach { effect(it) }
    }

}