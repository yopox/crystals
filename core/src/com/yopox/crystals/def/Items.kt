package com.yopox.crystals.def

import com.yopox.crystals.def.Items.ID.POTION
import com.yopox.crystals.logic.Item

object Items {

    enum class ID {
        POTION
    }

    private val potion = Item("Potion", 10)

    val map = mapOf(
            potion to POTION
    ).withDefault { POTION }

}