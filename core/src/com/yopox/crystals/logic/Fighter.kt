package com.yopox.crystals.logic

import com.badlogic.gdx.Gdx
import com.yopox.crystals.data.Entity
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Icons
import com.yopox.crystals.ui.ActionIcon

class Fighter(val job: Jobs.ID, val name: String): Entity() {

    var crystals = arrayListOf(Crystal.random(job))

    fun setActionsIcons(buttons: ArrayList<ActionIcon>) {
        // Show all buttons
        buttons.forEach { it.show() }

        // Set basic icons
        buttons[0].changeType(Actions.ID.ATTACK)
        buttons[1].changeType(Actions.ID.DEFENSE)
        buttons[2].changeType(Actions.ID.ITEMS)

        // Set crystal icons or hide useless buttons
        for (i in 0..2) {
            val cr = crystals.getOrNull(i)
            if (cr != null) {
                buttons[3 + i].changeType(Jobs.map.getOrElse(cr.job) { Jobs.warrior }.iconId)
            } else {
                buttons[3 + i].hide()
            }
        }
    }

    fun setSubactionIcons(action: Int, buttons: ArrayList<ActionIcon>): Unit {

        // Show all buttons
        buttons.forEach { it.show() }

        // Set return icon
        buttons[0].changeType(Actions.ID.RETURN)

        for (i in 0..2) {
            if (crystals[action].unlocked > i) {
                val sp = crystals[action].spells[i]
                buttons[1 + i].changeType(sp.id)
            } else {
                buttons[1 + i].changeType(Actions.ID.LOCKED)
            }
        }

        for (i in 4..5) {
            buttons[i].hide()
        }

    }

    fun getIcon(): Pair<Int, Int> = when (job) {
        Jobs.ID.BARD -> Icons.Bard
        Jobs.ID.GEOMANCER -> Icons.Geomancer
        Jobs.ID.INVOKER -> Icons.Invoker
        Jobs.ID.MAGE -> Icons.Mage
        Jobs.ID.MONK -> Icons.Monk
        Jobs.ID.PRIEST -> Icons.Priest
        Jobs.ID.ROGUE -> Icons.Rogue
        Jobs.ID.WARRIOR -> Icons.Warrior
    }

}