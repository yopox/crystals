package com.yopox.crystals.def

import com.yopox.crystals.logic.Item

object Items {

    enum class ID {
        POTION,
        CARROT,
        SCROLL
    }

    private val carrot = Item("Carrot", "Heals 15HP.", 5, Icons.ID.CARROT)
    private val potion = Item("Potion", "Heals 50HP.", 20, Icons.ID.POTION)
    private val scroll = Item("Fire Scroll", "Casts a fireball.", 80, Icons.ID.SCROLL)

    private val map = mapOf(
            ID.POTION to potion,
            ID.CARROT to carrot,
            ID.SCROLL to scroll
    ).withDefault { carrot }

    operator fun invoke(id: ID) = map.getValue(id)
    fun random() = map.values.random()

}