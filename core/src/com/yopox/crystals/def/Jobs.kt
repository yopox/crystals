package com.yopox.crystals.def

import com.yopox.crystals.data.Job
import com.yopox.crystals.ui.Actions

object Jobs {

    enum class ID {
        BARD,
        GEOMANCER,
        INVOKER,
        MAGE,
        PRIEST,
        ROGUE,
        WARRIOR
    }

    val warrior = Job("warrior", ID.WARRIOR, Actions.WARRIOR,
            "warrior crystals give\nthe ability to boost\nattack.",
            1, -2, 4, -2, 1, 1)
    val priest = Job("priest", ID.PRIEST, Actions.W_MAGIC,
            "priest crystals give\nthe ability to heal, but\nreduce attack.",
            1, 4, -2, 2, -2, 0)
    val rogue = Job("Robber", ID.ROGUE, Actions.ROB,
            "Robber crystals give\nthe ability to steal\nitems.",
            -1, 1, 1, 0, -1, 4)

    val map = mapOf(
            ID.WARRIOR to warrior,
            ID.PRIEST to priest,
            ID.ROGUE to rogue
    )

}