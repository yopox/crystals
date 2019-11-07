package com.yopox.crystals.logic

import com.yopox.crystals.def.*
import com.yopox.crystals.logic.fight.Fighter
import com.yopox.crystals.logic.fight.Stats
import com.yopox.crystals.screens.Fight
import com.yopox.crystals.ui.ActionIcon

class Hero(val job: Jobs.ID, name: String, initStats: Stats, val desc: String, icon: Icons.ID) : Fighter(Fighters.ID.HERO, name, icon, false) {

    var crystals = ArrayList<Crystal>()
    var xp = 0

    init {
        reset()
        baseStats = initStats.copy()
        stats = baseStats.copy()
    }

    override fun prepare() {
        stats to baseStats
    }

    override fun getMove(fighters: List<Fighter>): Fight.Move {
        return Fight.Move(this, Spells.map.getValue(Fight.Intent.action), Fight.Intent.targets)
    }

    fun setActionsIcons(buttons: List<ActionIcon>) {
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

    fun useCrystal(crystal: Crystal) = when (crystal.unlocked) {
        1 -> if (RNG(RNG.UNLOCK_2ND_SPELL)) {
            crystal.unlocked++
            Fight.Intent.action = crystal.spells[1].id
            freeSpell = true
        } else Unit
        2 -> if (RNG(RNG.UNLOCK_3RD_SPELL)) {
            crystal.unlocked++
            Fight.Intent.action = crystal.spells[2].id
            freeSpell = true
        } else Unit
        else -> Unit
    }

    fun addXP(amount: Int) {
        if (amount <= 0) return
        this.xp += amount
        while (this.xp >= Progress.XP_LEVELS[level]) {
            level++
        }
    }

    fun reset() {
        xp = 0
        crystals.clear()
        crystals.add(Crystal.baseCrystal(job))
    }

}