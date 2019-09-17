package com.yopox.crystals.def

import com.yopox.crystals.def.Icons.ID.*

object Icons {

    /**
     * Characters ID are stored in [Jobs.ID]
     */
    enum class ID {
        // Fighters
        PRIEST,
        MAGE,
        MONK,
        WARRIOR,
        INVOKER,
        BARD,
        ROGUE,
        GEOMANCER,
        SNAKE,
        DOG,
        SPIDER,
        BAT,

        // Furniture
        CHEST_CLOSED,
        CHEST_OPENED,
        BED,
        BOOKSHELF,
        BOOKSHELF_SKULL,
        JAR,
        WEB,
        CLOSET1,
        CLOSET2,
        CLOSET3,

        // Items
        POTION,
        CARROT,
        UNKNOWN
    }

    // Characters
    private val Priest = Pair(0, 32)
    private val Mage = Pair(1, 32)
    private val Monk = Pair(2, 32)
    private val Warrior = Pair(3, 32)
    private val Invoker = Pair(4, 32)
    private val Bard = Pair(5, 32)
    private val Rogue = Pair(6, 32)
    private val Geomancer = Pair(7, 32)
    private val Snake = Pair(28, 8)
    private val Bat = Pair(26, 8)
    private val Dog = Pair(31, 7)
    private val Spider = Pair(28, 5)
    private val Unknown = Pair(21, 25)

    // Furniture
    private val ChestClosed = Pair(8, 6)
    private val ChestOpened = Pair(9, 6)
    private val ChestLeatherClosed = Pair(10, 6)
    private val ChestLeatherOpened = Pair(11, 6)
    private val Bed = Pair(5, 8)
    private val Bookshelf = Pair(3, 7)
    private val BookshelfSkull = Pair(4, 7)
    private val Jar = Pair(5, 14)
    private val Web = Pair(2, 15)
    private val Closet1 = Pair(8, 14)
    private val Closet2 = Pair(9, 14)
    private val Closet3 = Pair(10, 14)

    // Items
    private val Potion = Pair(18, 25)
    private val Carrot = Pair(18, 30)

    operator fun invoke(id: ID) = when(id) {
        PRIEST -> Priest
        MAGE -> Mage
        MONK -> Monk
        WARRIOR -> Warrior
        INVOKER -> Invoker
        BARD -> Bard
        ROGUE -> Rogue
        GEOMANCER -> Geomancer
        SNAKE -> Snake
        DOG -> Dog
        SPIDER -> Spider
        BAT -> Bat
        CHEST_CLOSED -> ChestClosed
        CHEST_OPENED -> ChestOpened
        BED -> Bed
        BOOKSHELF -> Bookshelf
        BOOKSHELF_SKULL -> BookshelfSkull
        JAR -> Jar
        WEB -> Web
        CLOSET1 -> Closet1
        CLOSET2 -> Closet2
        CLOSET3 -> Closet3
        POTION -> Potion
        CARROT -> Carrot
        else -> Unknown
    }

}