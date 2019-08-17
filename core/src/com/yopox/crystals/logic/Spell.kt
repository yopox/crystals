package com.yopox.crystals.logic

import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Actions

class Spell(val job: Jobs.ID, val name: String, val cost: Int, val id: Actions.ID, val effect: (Hero) -> Unit) {

    fun use(targets: ArrayList<Hero>) {
        targets.forEach { effect(it) }
    }

}