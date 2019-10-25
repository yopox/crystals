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
        SHOPKEEPER,
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
        CLOSET4_OPENED,
        CLOSET4_CLOSED,
        PILLAR_TOP,
        PILLAR_MIDDLE,
        PILLAR_BOTTOM,
        BENCH_LEFT,
        BENCH_MIDDLE,

        // Items
        COINS,
        BAG_OF_COINS,
        POTION,
        CARROT,
        SCROLL,
        BEER,

        // Equipment
        SWORD,
        CRYSTAL,
        CROWN,
        LONG_SWORD,

        // Others
        EXCLAMATION,
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
    private val Shopkeeper = Pair(30, 4)
    private val Snake = Pair(28, 8)
    private val Bat = Pair(26, 8)
    private val Dog = Pair(31, 7)
    private val Spider = Pair(28, 5)

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
    private val Closet4Closed = Pair(5, 7)
    private val Closet4Opened = Pair(6, 7)
    private val PillarTop = Pair(3, 11)
    private val PillarMiddle = Pair(3, 12)
    private val PillarBottom = Pair(3, 13)
    private val BenchLeft = Pair(3, 8)
    private val BenchMiddle = Pair(4, 8)

    // Items
    private val Coins = Pair(9, 26)
    private val BagOfCoins = Pair(10, 26)
    private val Potion = Pair(18, 25)
    private val Carrot = Pair(18, 30)
    private val Scroll = Pair(17, 27)
    private val Beer = Pair(15, 31)

    // Equipment
    private val Sword = Pair(0, 28)
    private val Crystal = Pair(18, 22)
    private val Crown = Pair(12, 24)
    private val LongSword = Pair(0, 29)

    // Others
    private val Exclamation = Pair(20, 25)
    private val Unknown = Pair(21, 25)

    operator fun invoke(id: ID) = when (id) {
        PRIEST -> Priest
        MAGE -> Mage
        MONK -> Monk
        WARRIOR -> Warrior
        INVOKER -> Invoker
        BARD -> Bard
        ROGUE -> Rogue
        GEOMANCER -> Geomancer
        SHOPKEEPER -> Shopkeeper

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
        CLOSET4_OPENED -> Closet4Opened
        CLOSET4_CLOSED -> Closet4Closed
        PILLAR_TOP -> PillarTop
        PILLAR_MIDDLE -> PillarMiddle
        PILLAR_BOTTOM -> PillarBottom
        BENCH_LEFT -> BenchLeft
        BENCH_MIDDLE -> BenchMiddle

        POTION -> Potion
        CARROT -> Carrot
        SCROLL -> Scroll
        BEER -> Beer
        COINS -> Coins
        BAG_OF_COINS -> BagOfCoins

        SWORD -> Sword
        CRYSTAL -> Crystal
        CROWN -> Crown
        LONG_SWORD -> LongSword

        EXCLAMATION -> Exclamation
        else -> Unknown
    }

    val changingIcons = mapOf(
            CHEST_CLOSED to CHEST_OPENED,
            CLOSET4_CLOSED to CLOSET4_OPENED
    )

}