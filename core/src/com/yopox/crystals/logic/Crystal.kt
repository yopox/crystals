package com.yopox.crystals.logic

import com.yopox.crystals.data.Job
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells

class Crystal(val job: Jobs.ID) {

    val spells = ArrayList<Spell>()

    companion object {

        fun random(job: Jobs.ID): Crystal {
            val crystal = Crystal(job)
            crystal.spells.add(Spells.map.filterValues { it.job == job }.values.random())
            crystal.spells.add(Spells.map.filterValues { it.job == job && !crystal.contains(it.id) }.values.random())
            crystal.spells.add(Spells.map.filterValues { it.job == job && !crystal.contains(it.id) }.values.random())
            return crystal
        }
    }

    fun contains(spell: Spells.ID): Boolean {
        for (sp in spells) {
            if (sp.id == spell) return true
        }
        return false
    }


}