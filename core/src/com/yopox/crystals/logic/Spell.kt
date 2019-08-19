package com.yopox.crystals.logic

import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Jobs

enum class Target {
    SINGLE,
    ALL,
    SELF
}

class Spell(
        val id: Actions.ID,
        val name: String,
        val job: Jobs.ID,
        val cost: Int,
        val target: Target,
        val effect: (Hero) -> Unit
) {

    fun use(targets: ArrayList<Hero>) {
        targets.forEach { effect(it) }
    }

}