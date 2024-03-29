package com.yopox.crystals.def

import com.yopox.crystals.def.Actions.ID.*
import com.yopox.crystals.def.Fighters.ID.*
import com.yopox.crystals.def.Items.ID.*
import com.yopox.crystals.logic.Hero
import com.yopox.crystals.logic.fight.Monster
import com.yopox.crystals.logic.fight.Stats

object Fighters {

    enum class ID {
        HERO,
        SNAKE,
        BAT,
        DOG,
        SPIDER
    }

    // Heroes
    private val warrior = Hero(Jobs.ID.WARRIOR, "Xavier",
            Stats(30, 8, 100, 0, 3, 4),
            "Xavier has powerful\nfighting skills. He's called\nXavier le guerrier'.",
            Icons.ID.WARRIOR)

    private val mage = Hero(Jobs.ID.MAGE, "Faroo",
            Stats(28, 15, 3, 10, 2, 4),
            "Faroo is a mage.",
            Icons.ID.MAGE)

    val heroes = listOf(warrior, mage)

    // Monsters
    private val snake = Monster(SNAKE, "Snake",
            Stats(8, 4, 5, 0, 0, 2),
            arrayListOf(Spells(ATTACK), Spells(WAIT)),
            Icons.ID.SNAKE,
            3,
            5,
            mapOf(Items(POTION) to 0.2))

    private val bat = Monster(BAT, "Bat",
            Stats(6, 3, 4, 0, 1, 8),
            arrayListOf(Spells(ATTACK), Spells(ULTRASOUND)),
            Icons.ID.BAT,
            2,
            2,
            mapOf())

    private val dog = Monster(DOG, "Dog",
            Stats(18, 2, 8, 0, 0, 10),
            arrayListOf(Spells(ATTACK), Spells(BARK)),
            Icons.ID.DOG,
            5,
            8,
            mapOf(Items(CARROT) to 0.4))

    private val spider = Monster(SPIDER, "Spider",
            Stats(11, 5, 4, 0, 2, 3),
            arrayListOf(Spells(ATTACK), Spells(BITE), Spells(WEB)),
            Icons.ID.SPIDER,
            5,
            2,
            mapOf())

    private val map = mapOf(
            SNAKE to snake,
            BAT to bat,
            DOG to dog,
            SPIDER to spider
    ).withDefault { bat }

    operator fun invoke(id: ID) = map.getValue(id)
    fun random() = map.values.random()

    fun getMonster(m: Monster, lvl: Int): Monster {
        val monster = Monster(m.type, m.name, m.monsterStats, m.spells, m.icon, m.xp, m.coins, m.loots)
        monster.scale(lvl)
        return monster
    }

}