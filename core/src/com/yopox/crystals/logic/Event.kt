package com.yopox.crystals.logic

import com.yopox.crystals.def.Events
import com.yopox.crystals.def.Events.ID.*


class Event(val id: Events.ID = BATTLE) {

    val name: String = Events.name(id)
    val iconX: Int = when (id) {
        BATTLE -> 0
        INN -> 14
        HOUSE -> (2 + (Math.random() * 3).toInt()) * 14
        SHOP -> 5 * 14
        GARDEN -> 8 * 14
        TEMPLE -> 9 * 14
        else -> 7 * 14
    }

}