package com.yopox.crystals.def

import com.yopox.crystals.logic.Job

object Jobs {

    enum class ID {
        BARD,
        GEOMANCER,
        INVOKER,
        MAGE,
        MONK,
        PRIEST,
        ROGUE,
        WARRIOR,
        ANY,
        NONE
    }

    private val bard = Job("Bard", ID.BARD, Actions.ID.SONGS,
            "Bard crystals give\nthe ability to sing.",
            -1, 1, 1, 0, -1, 4)
    private val geomancer = Job("Geomancer", ID.GEOMANCER, Actions.ID.GEOMANCY,
            "Geomancer crystals give\nthe ability to turn\nnature into an ally.",
            -1, 1, 1, 0, -1, 4)
    private val invoker = Job("Invoker", ID.INVOKER, Actions.ID.INVOKE,
            "Invoker crystals give\nthe ability to summon\ncreatures.",
            -1, 1, 1, 0, -1, 4)
    private val mage = Job("Mage", ID.MAGE, Actions.ID.D_MAGIC,
            "Mage crystals give\nthe ability to use\ndark magic.",
            -1, 1, 1, 0, -1, 4)
    private val monk = Job("Monk", ID.MONK, Actions.ID.MONK,
            "Monk crystals let\nyou develop fighting\nskills.",
            -1, 1, 1, 0, -1, 4)
    private val priest = Job("Priest", ID.PRIEST, Actions.ID.W_MAGIC,
            "Priest crystals give\nthe ability to use\nwhite magic.",
            1, 4, -2, 2, -2, 0)
    private val rogue = Job("Robber", ID.ROGUE, Actions.ID.ROBBING,
            "Robber crystals give\nthe ability to steal\nitems.",
            -1, 1, 1, 0, -1, 4)
    private val warrior = Job("Warrior", ID.WARRIOR, Actions.ID.WARRIOR,
            "Warrior crystals let\nyou develop war\nskills.",
            1, -2, 4, -2, 1, 1)

    val default = warrior

    val map = mapOf(
            ID.BARD to bard,
            ID.GEOMANCER to geomancer,
            ID.INVOKER to invoker,
            ID.MAGE to mage,
            ID.MONK to monk,
            ID.PRIEST to priest,
            ID.ROGUE to rogue,
            ID.WARRIOR to warrior
    ).withDefault { mage }

    fun getJob(id: ID) = map.getValue(id)

}