package com.yopox.crystals.def

import com.yopox.crystals.def.Fighters.ID.BAT
import com.yopox.crystals.def.Fighters.ID.SNAKE
import com.yopox.crystals.logic.Fighter

object Fighters {

    enum class ID {
        HERO,
        SNAKE,
        BAT
    }

    private val snake = Fighter(SNAKE, "Snake")
    private val bat = Fighter(BAT, "Bat")

    val map = mapOf(
            SNAKE to snake,
            BAT to bat
    ).withDefault { bat }

}