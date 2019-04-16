package com.yopox.crystals.data

enum class EVENT_TYPE {
    BATTLE,
    INN,
    HOUSE,
    SHOP,
    RANDOM
}

class Event(val type: EVENT_TYPE = EVENT_TYPE.BATTLE) {

    val iconX
        get() = when (type) {
            EVENT_TYPE.BATTLE -> 0
            EVENT_TYPE.INN -> 14
            EVENT_TYPE.HOUSE -> 2 * 14
            EVENT_TYPE.SHOP -> 3 * 14
            else -> 4 * 14
        }

}