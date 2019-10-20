package com.yopox.crystals.def

import com.yopox.crystals.def.Events.ID.*

object Events {

    enum class ID {
        BATTLE,
        INN,
        HOUSE,
        SHOP,
        GARDEN,
        TEMPLE,
        RANDOM
    }

    fun name(id: ID) = when (id) {
        BATTLE -> "Battle"
        INN -> "Inn"
        HOUSE -> "House"
        SHOP -> "Shop"
        GARDEN -> "Garden"
        TEMPLE -> "Temple"
        else -> "?"
    }

}