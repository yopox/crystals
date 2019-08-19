package com.yopox.crystals.def

object Actions {

    enum class ID {
        ATTACK,
        DEFENSE,
        ITEMS,
        // Crystals
        W_MAGIC,
        D_MAGIC,
        MONK,
        WARRIOR,
        INVOKE,
        ROBBING,
        SONGS,
        GEOMANCY,
        RETURN,
        // Spells
        // White Mage
        HEAL,
        HEAL2,
        CURE,
        BARRIER,
        BEAMS,
        BALL,
        // Dark Mage
        WIND,
        FIRE,
        WATER,
        POISON,
        LIGHTNING,
        ENERGY,
        // Monk
        MEDITATION,
        FOCUS,
        KICK,
        PUNCH,
        CHAINS,
        SHURIKEN,
        // Warrior
        DOUBLE,
        MASSIVE,
        SHIELD,
        INSULT,
        STORM,
        JUMP,
        // Invoker
        FAIRY,
        TAME,
        GOLEM,
        RAIKU,
        WENDIGO,
        DEAD_KING,
        // Rogue
        DAGGER,
        ESCAPE,
        ROB,
        BURGLE,
        BOMB,
        BRIBE,
        // Bard
        SING,
        BEWITCH,
        HIDE,
        FIREWORKS,
        WINE,
        SAUSAGE,
        // Geomancer
        EARTHQUAKE,
        PREDICT,
        TORNADO,
        // Misc
        LOCKED,
        WAIT
    }

    val categoryNames = mapOf(
            ID.W_MAGIC to "White Magic",
            ID.D_MAGIC to "Dark Magic",
            ID.WARRIOR to "War Abilities",
            ID.MONK to "Fighting Abilities",
            ID.INVOKE to "Invoker Abilities",
            ID.GEOMANCY to "Geomancy Abilities",
            ID.ROBBING to "Rogue Abilities",
            ID.SONGS to "Bard Abilities"
    ).withDefault { "Missing name" }

}