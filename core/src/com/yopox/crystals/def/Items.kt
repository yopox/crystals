package com.yopox.crystals.def

import com.yopox.crystals.def.Items.ID.*
import com.yopox.crystals.logic.equipment.Item

object Items {

    enum class ID {
        POTION,
        CARROT,
        SCROLL,
        CRYSTAL,
        SWORD
    }

    private val carrot = Item("Carrot", "Heals 15HP.", 5, Icons.ID.CARROT)
    private val badCarrot = Item("bad_carrot", "Is this a glitch?", 0, Icons.ID.CARROT)
    private val potion = Item("Potion", "Heals 50HP.\nSecond line test.", 20, Icons.ID.POTION)
    private val scroll = Item("Fire Scroll", "Casts a fireball.", 80, Icons.ID.SCROLL)
    private val crystal = Item("Warrior Crystal", "Looks powerful.", 300, Icons.ID.CRYSTAL)
    private val sword = Item("Sword", "ATK : 2", 15, Icons.ID.SWORD)

    private val map = mapOf(
            POTION to potion,
            CARROT to carrot,
            SCROLL to scroll,
            CRYSTAL to crystal,
            SWORD to sword
    ).withDefault { badCarrot }

    operator fun invoke(id: ID) = map.getValue(id)
    fun random() = map.values.random()

}