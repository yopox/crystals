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

    val name: String
    val id: Int = (Math.random() * 10).toInt()

    init {
        name = getTypeName(type) + " #$id"
    }

    companion object {
        fun getTypeName(type: EVENT_TYPE): String = when(type) {
            EVENT_TYPE.BATTLE -> "Battle"
            EVENT_TYPE.INN -> "Inn"
            EVENT_TYPE.HOUSE -> "House"
            EVENT_TYPE.SHOP -> "Shop"
            else -> "?"
        }
    }

}