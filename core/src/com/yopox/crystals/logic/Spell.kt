package com.yopox.crystals.logic

import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells
import com.yopox.crystals.def.Actions

class Spell(val job: Jobs.ID, val name: String, val cost: Int, val id: Actions.ID, val effect: (Fighter) -> Unit) {

    fun use(targets: ArrayList<Fighter>) {
        targets.forEach { effect(it) }
    }

}