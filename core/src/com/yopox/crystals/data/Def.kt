package com.yopox.crystals.data

object Def {

    object Jobs {
        val Warrior = Job("Warrior",
                "Warrior crystals give\nthe ability to boost\nattack.",
                1, -2, 4, -2, 1, 1)
        val Priest = Job("Priest",
                "Priest crystals give\nthe ability to heal, but\nreduce attack.",
                1, 4, -2, 2, -2, 0)
        val Rogue = Job("Robber",
                "Robber crystals give\nthe ability to steal\nitems.",
                -1, 1, 1, 0, -1, 4)
    }

    object Icons {
        val Priest = Pair(31, 1)
        val Snake = Pair(28, 8)
        val Bat = Pair(26, 8)
    }

}