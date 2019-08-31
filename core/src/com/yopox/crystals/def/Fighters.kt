package com.yopox.crystals.def

import com.yopox.crystals.def.Actions.ID.*
import com.yopox.crystals.def.Fighters.ID.BAT
import com.yopox.crystals.def.Fighters.ID.SNAKE
import com.yopox.crystals.def.Spells.getSpell
import com.yopox.crystals.logic.Hero
import com.yopox.crystals.logic.fight.Fighter
import com.yopox.crystals.logic.fight.Monster
import com.yopox.crystals.logic.fight.Stats

object Fighters {

    enum class ID {
        HERO,
        SNAKE,
        BAT
    }

    // Heroes
    private val warrior = Hero(Jobs.ID.WARRIOR, "Xavier", Stats(35, 8, 6, 0, 5, 4), "Xavier has powerful\nfighting skills. He's called\n'Xavier le guerrier'.")
    private val mage = Hero(Jobs.ID.MAGE, "Faroo", Stats(20, 15, 2, 10, 4, 4), "Faroo is a mage.")

    val heroes = listOf(warrior, mage)

    // Monsters
    private val snake = Monster(SNAKE, "Snake",
            Stats(10, 4, 3, 0, 0, 2),
            arrayListOf(getSpell(ATTACK), getSpell(WAIT)))
    private val bat = Monster(BAT, "Bat",
            Stats(6, 3, 2, 0, 5, 8),
            arrayListOf(getSpell(ATTACK), getSpell(ULTRASOUND)))

    val map = mapOf(
            SNAKE to snake,
            BAT to bat
    ).withDefault { bat }

}