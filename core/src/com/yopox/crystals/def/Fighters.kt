package com.yopox.crystals.def

import com.yopox.crystals.def.Actions.ID.*
import com.yopox.crystals.def.Fighters.ID.BAT
import com.yopox.crystals.def.Fighters.ID.SNAKE
import com.yopox.crystals.def.Spells.getSpell
import com.yopox.crystals.logic.fight.Fighter
import com.yopox.crystals.logic.fight.Monster
import com.yopox.crystals.logic.fight.Stats

object Fighters {

    enum class ID {
        HERO,
        SNAKE,
        BAT
    }

    private val snake = Monster(SNAKE, "Snake",
            Stats(8, 4, 3, 0, 0, 2),
            arrayListOf(getSpell(ATTACK), getSpell(WAIT)))
    private val bat = Monster(BAT, "Bat",
            Stats(6, 3, 2, 0, 0, 8),
            arrayListOf(getSpell(ATTACK), getSpell(ULTRASOUND)))

    val map = mapOf(
            SNAKE to snake,
            BAT to bat
    ).withDefault { bat }

}