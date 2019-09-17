package com.yopox.crystals.logic

import com.yopox.crystals.def.*
import com.yopox.crystals.logic.fight.Fighter
import com.yopox.crystals.logic.fight.Stats
import com.yopox.crystals.screens.Fight
import com.yopox.crystals.ui.ActionIcon

class Hero(val job: Jobs.ID, name: String, initStats: Stats, val desc: String, icon: Icons.ID) : Fighter(Fighters.ID.HERO, name, icon, false) {

    var crystals = arrayListOf(Crystal.baseCrystal(job))

    init {
        baseStats = initStats.copy()
        stats = baseStats.copy()
    }

    override fun prepare() {
        stats to baseStats
    }

    override fun getMove(fighters: ArrayList<Fighter>): Fight.Move {
        return Fight.Move(this, Spells.map.getValue(Fight.Intent.action), Fight.Intent.targets)
    }

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
                buttons[3 + i].changeType(Jobs(cr.job).iconId)
            } else {
                buttons[3 + i].hide()
            }
        }
    }

    fun setSubactionIcons(action: Int, buttons: ArrayList<ActionIcon>) {

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

}