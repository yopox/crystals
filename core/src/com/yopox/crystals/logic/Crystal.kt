package com.yopox.crystals.logic

import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Spells
import com.yopox.crystals.logic.fight.Spell

class Crystal(val job: Jobs.ID) {

    val spells = ArrayList<Spell>()
    var unlocked = 1

    companion object {

        fun random() = random(Jobs.randomID())

        fun random(job: Jobs.ID): Crystal {
            val crystal = Crystal(job)
            crystal.spells.add(Spells.map.filterValues { it.job == job }.values.random())
            crystal.fill()
            return crystal
        }

        fun baseCrystal(job: Jobs.ID): Crystal {
            val crystal = Crystal(job)
            crystal.spells.add(Spells.baseSpell(job))
            crystal.fill()
            return crystal
        }
    }

    fun contains(spell: Actions.ID): Boolean = spells.map { it.id }.contains(spell)

    /**
     * Fill crystal with empty spell slots.
     */
    fun fill() {
        while (spells.size < 3) {
            val newSpells = Spells.map.filterValues { it.job == job && !contains(it.id) }
            spells.add(newSpells.values.random())
        }
    }

}