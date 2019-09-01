package com.yopox.crystals.def

import com.yopox.crystals.def.Actions.ID.*
import com.yopox.crystals.def.Fighters.ID.*
import com.yopox.crystals.def.Spells.getSpell
import com.yopox.crystals.logic.Hero
import com.yopox.crystals.logic.fight.Monster
import com.yopox.crystals.logic.fight.Stats

object Fighters {

    enum class ID {
        HERO,
        SNAKE,
        BAT,
        DOG
    }

    // Heroes
    private val warrior = Hero(Jobs.ID.WARRIOR, "Xavier",
            Stats(25, 8, 6, 0, 3, 4),
            "Xavier has powerful\nfighting skills. He's called\n'Xavier le guerrier'.")

    private val mage = Hero(Jobs.ID.MAGE, "Faroo",
            Stats(20, 15, 3, 10, 2, 4),
            "Faroo is a mage.")

    val heroes = listOf(warrior, mage)

    // Monsters
    private val snake = Monster(SNAKE, "Snake",
            Stats(8, 4, 5, 0, 0, 2),
            arrayListOf(getSpell(ATTACK), getSpell(WAIT)))

    private val bat = Monster(BAT, "Bat",
            Stats(6, 3, 4, 0, 1, 8),
            arrayListOf(getSpell(ATTACK), getSpell(ULTRASOUND)))

    private val dog = Monster(DOG, "Dog",
            Stats(18, 2, 8, 0, 0, 10),
            arrayListOf(getSpell(ATTACK), getSpell(BARK)))

    val map = mapOf(
            SNAKE to snake,
            BAT to bat,
            DOG to dog
    ).withDefault { bat }

}