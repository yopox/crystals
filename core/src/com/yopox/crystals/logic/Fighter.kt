package com.yopox.crystals.logic

import com.yopox.crystals.def.Jobs
import com.yopox.crystals.ui.Actions
import com.yopox.crystals.ui.ActionIcon

class Fighter(val job: Jobs.ID, val name: String) {

    var crystals = arrayListOf(Crystal.random(job))

    fun setActionsIcons(buttons: ArrayList<ActionIcon>) {
        // Show all buttons
        buttons.forEach { it.show() }

        // Set basic icons
        buttons[0].changeType(Actions.ATTACK)
        buttons[1].changeType(Actions.DEFENSE)
        buttons[2].changeType(Actions.ITEMS)

        // Set crystal icons or hide useless buttons
        for (i in 0..2) {
            val cr = crystals.getOrNull(i)
            if (cr != null) {
                buttons[3+i].changeType(Jobs.map.getOrElse(cr.job) { Jobs.warrior }.iconId)
            } else {
                buttons[3+i].hide()
            }
        }
    }

    fun setSubactionIcons(action: Int, buttons: ArrayList<ActionIcon>): Unit {

    }
    
}