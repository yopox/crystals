package com.yopox.crystals.def

import com.yopox.crystals.def.Icons.ID.*

object Icons {

    /**
     * Characters ID are stored in [Jobs.ID]
     */
    enum class ID {
        CHEST_CLOSED,
        CHEST_OPENED,
        BED,
        BOOKSHELF,
        BOOKSHELF_SKULL,
        JAR,
        WEB,
        CLOSET1,
        CLOSET2,
        CLOSET3
    }

    // Characters
    val Priest = Pair(0, 32)
    val Mage = Pair(1, 32)
    val Monk = Pair(2, 32)
    val Warrior = Pair(3, 32)
    val Invoker = Pair(4, 32)
    val Bard = Pair(5, 32)
    val Rogue = Pair(6, 32)
    val Geomancer = Pair(7, 32)
    val Snake = Pair(28, 8)
    val Bat = Pair(26, 8)
    val Dog = Pair(31, 7)
    val Spider = Pair(28, 5)
    val Unknown = Pair(21, 25)

    // Furniture
    val ChestClosed = Pair(8, 6)
    val ChestOpened = Pair(9, 6)
    val ChestLeatherClosed = Pair(10, 6)
    val ChestLeatherOpened = Pair(11, 6)
    val Bed = Pair(5, 8)
    val Bookshelf = Pair(3, 7)
    val BookshelfSkull = Pair(4, 7)
    val Jar = Pair(5, 14)
    val Web = Pair(2, 15)
    val Closet1 = Pair(8, 14)
    val Closet2 = Pair(9, 14)
    val Closet3 = Pair(10, 14)

    // Items
    val Potion = Pair(18, 25)

    val map = mapOf(
            CHEST_CLOSED to ChestClosed,
            CHEST_OPENED to ChestOpened,
            BED to Bed,
            BOOKSHELF to Bookshelf,
            BOOKSHELF_SKULL to BookshelfSkull,
            JAR to Jar,
            WEB to Web,
            CLOSET1 to Closet1,
            CLOSET2 to Closet2,
            CLOSET3 to Closet3
    ).withDefault { Unknown }

}