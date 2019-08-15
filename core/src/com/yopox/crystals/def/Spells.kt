package com.yopox.crystals.def

import com.yopox.crystals.def.Actions.ID.*
import com.yopox.crystals.logic.Spell

object Spells {

    // Priest spells
    private val heal = Spell(Jobs.ID.PRIEST, "Heal", 2, HEAL) {}
    private val heal2 = Spell(Jobs.ID.PRIEST, "Heal +", 6, HEAL2) {}
    private val cure = Spell(Jobs.ID.PRIEST, "Cure", 3, CURE) {}
    private val barrier = Spell(Jobs.ID.PRIEST, "Barrier", 4, BARRIER) {}
    private val beam = Spell(Jobs.ID.PRIEST, "Beam", 3, BEAMS) {}
    private val ball = Spell(Jobs.ID.PRIEST, "Ball", 7, BALL) {}

    // Mage spells
    private val wind = Spell(Jobs.ID.MAGE, "Wind", 2, WIND) {}
    private val fire = Spell(Jobs.ID.MAGE, "Fire", 4, FIRE) {}
    private val water = Spell(Jobs.ID.MAGE, "Water", 7, WATER) {}
    private val poison = Spell(Jobs.ID.MAGE, "Poison", 3, POISON) {}
    private val lightning = Spell(Jobs.ID.MAGE, "Lightning", 5, LIGHTNING) {}
    private val energy = Spell(Jobs.ID.MAGE, "Energy", 8, ENERGY) {}

    // Monk spells
    private val meditation = Spell(Jobs.ID.MONK, "Meditation", 2, MEDITATION) {}
    private val focus = Spell(Jobs.ID.MONK, "Focus", 2, FOCUS) {}
    private val kick = Spell(Jobs.ID.MONK, "Kick", 2, KICK) {}
    private val punch = Spell(Jobs.ID.MONK, "Punch", 2, PUNCH) {}
    private val chains = Spell(Jobs.ID.MONK, "Chains", 2, CHAINS) {}
    private val shuriken = Spell(Jobs.ID.MONK, "Shuriken", 2, SHURIKEN) {}

    // Warrior spells
    private val double = Spell(Jobs.ID.WARRIOR, "Double Hit", 4, DOUBLE) {}
    private val massive = Spell(Jobs.ID.WARRIOR, "Massive Hit", 8, MASSIVE) {}
    private val shield = Spell(Jobs.ID.WARRIOR, "Shield", 4, SHIELD) {}
    private val insult = Spell(Jobs.ID.WARRIOR, "Insult", 1, INSULT) {}
    private val storm = Spell(Jobs.ID.WARRIOR, "Storm", 10, STORM) {}
    private val jump = Spell(Jobs.ID.WARRIOR, "Aerial Hit", 7, JUMP) {}

    // Invoker spells
    private val fairy = Spell(Jobs.ID.INVOKER, "Fairy", 3, FAIRY) {}
    private val tame = Spell(Jobs.ID.INVOKER, "Tame", 1, TAME) {}
    private val golem = Spell(Jobs.ID.INVOKER, "Golem", 6, GOLEM) {}
    private val raiku = Spell(Jobs.ID.INVOKER, "Raiku", 8, RAIKU) {}
    private val wendigo = Spell(Jobs.ID.INVOKER, "Wendigo", 5, WENDIGO) {}
    private val deadKing = Spell(Jobs.ID.INVOKER, "Dead King", 12, DEAD_KING) {}

    // Rogue spells
    private val dagger = Spell(Jobs.ID.ROGUE, "Dagger", 5, DAGGER) {}
    private val escape = Spell(Jobs.ID.ROGUE, "Escape", 0, ESCAPE) {}
    private val rob = Spell(Jobs.ID.ROGUE, "Rob", 3, ROB) {}
    private val burgle = Spell(Jobs.ID.ROGUE, "Burgle", 3, BURGLE) {}
    private val bomb = Spell(Jobs.ID.ROGUE, "Bomb", 6, BOMB) {}
    private val bribe = Spell(Jobs.ID.ROGUE, "Bribe", 1, BRIBE) {}

    // Bard spells
    private val sing = Spell(Jobs.ID.BARD, "Sing", 2, SING) {}
    private val bewitch = Spell(Jobs.ID.BARD, "Bewitch", 2, BEWITCH) {}
    private val hide = Spell(Jobs.ID.BARD, "Hide", 2, HIDE) {}
    private val fireworks = Spell(Jobs.ID.BARD, "Fireworks", 8, FIREWORKS) {}
    private val wine = Spell(Jobs.ID.BARD, "Wine", 2, WINE) {}
    private val sausage = Spell(Jobs.ID.BARD, "Sausage", 2, SAUSAGE) {}

    // Geomancer spells
    private val earthquake = Spell(Jobs.ID.GEOMANCER, "Earthquake", 6, EARTHQUAKE) {}
    private val predict = Spell(Jobs.ID.GEOMANCER, "Predict", 1, PREDICT) {}
    private val tornado = Spell(Jobs.ID.GEOMANCER, "Tornado", 5, TORNADO) {}

    val map = mapOf(
            HEAL to heal,
            HEAL2 to heal2,
            CURE to cure,
            BARRIER to barrier,
            BEAMS to beam,
            BALL to ball,

            WIND to wind,
            FIRE to fire,
            WATER to water,
            POISON to poison,
            LIGHTNING to lightning,
            ENERGY to energy,

            MEDITATION to meditation,
            FOCUS to focus,
            KICK to kick,
            PUNCH to punch,
            CHAINS to chains,
            SHURIKEN to shuriken,

            DOUBLE to double,
            MASSIVE to massive,
            SHIELD to shield,
            INSULT to insult,
            STORM to storm,
            JUMP to jump,

            FAIRY to fairy,
            TAME to tame,
            GOLEM to golem,
            RAIKU to raiku,
            WENDIGO to wendigo,
            DEAD_KING to deadKing,

            DAGGER to dagger,
            ESCAPE to escape,
            ROB to rob,
            BURGLE to burgle,
            BOMB to bomb,
            BRIBE to bribe,

            SING to sing,
            BEWITCH to bewitch,
            HIDE to hide,
            FIREWORKS to fireworks,
            WINE to wine,
            SAUSAGE to sausage,

            EARTHQUAKE to earthquake,
            PREDICT to predict,
            TORNADO to tornado
    )

    fun baseSpell(job: Jobs.ID): Spell = when (job) {
        Jobs.ID.BARD -> map[SING]!!
        Jobs.ID.GEOMANCER -> map[TORNADO]!!
        Jobs.ID.INVOKER -> map[FAIRY]!!
        Jobs.ID.MAGE -> map[FIRE]!!
        Jobs.ID.MONK -> map[FOCUS]!!
        Jobs.ID.PRIEST -> map[HEAL]!!
        Jobs.ID.ROGUE -> map[ROB]!!
        Jobs.ID.WARRIOR -> map[DOUBLE]!!
    }
}