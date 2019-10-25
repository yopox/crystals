package com.yopox.crystals.def

import com.yopox.crystals.def.Items.ID.*
import com.yopox.crystals.logic.Item

object Items {

    enum class ID {
        POTION,
        CARROT,
        SCROLL,
        CRYSTAL,
        SWORD,
        LONG_SWORD
    }

    private val carrot = Item("Carrot", "Heals 15HP.", 5, Icons.ID.CARROT)
    private val potion = Item("Potion", "Heals 50HP.", 20, Icons.ID.POTION)
    private val scroll = Item("Fire Scroll", "Casts a fireball.", 80, Icons.ID.SCROLL)
    private val crystal = Item("Warrior Crystal", "!!!", 300, Icons.ID.CRYSTAL)
    private val sword = Item("Sword", "ATK : 2", 15, Icons.ID.SWORD)
    private val longSword = Item("Long Sword", "ATK : 6", 50, Icons.ID.LONG_SWORD)

    private val map = mapOf(
            POTION to potion,
            CARROT to carrot,
            SCROLL to scroll,
            CRYSTAL to crystal,
            SWORD to sword,
            LONG_SWORD to longSword
    ).withDefault { carrot }

    operator fun invoke(id: ID) = map.getValue(id)
    fun random() = map.values.random()

}