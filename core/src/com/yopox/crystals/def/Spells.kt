package com.yopox.crystals.def

import com.yopox.crystals.logic.Spell

object Spells {

    enum class ID {
        HEAL,
        HEAL2,
        REGEN,
        SHIELD,
        BEAM,
        BALL,
        FIRE,
        DOUBLE_HIT,
        DANCE,
        EARTHQUAKE,
        TALK,
        ESCAPE
    }

    // priest spells
    private val heal = Spell(Jobs.ID.PRIEST, "Heal", 2, ID.HEAL) {}
    private val heal2 = Spell(Jobs.ID.PRIEST, "Heal +", 6, ID.HEAL2) {}
    private val regen = Spell(Jobs.ID.PRIEST, "Regen", 3, ID.REGEN) {}
    private val shield = Spell(Jobs.ID.PRIEST, "Shield", 4, ID.SHIELD) {}
    private val beam = Spell(Jobs.ID.PRIEST, "Beam", 3, ID.BEAM) {}
    private val ball = Spell(Jobs.ID.PRIEST, "Ball", 7, ID.BALL) {}

    // Mage spells
    private val fire = Spell(Jobs.ID.MAGE, "Fire", 4, ID.FIRE) {}

    // warrior spells
    private val doubleHit = Spell(Jobs.ID.WARRIOR, "Double Hit", 4, ID.DOUBLE_HIT) {}

    // Bard spells
    private val dance = Spell(Jobs.ID.BARD, "Dance", 2, ID.DANCE) {}

    // Geomancer spells
    private val earthquake = Spell(Jobs.ID.GEOMANCER, "Earthquake", 6, ID.EARTHQUAKE) {}

    // Invoker spells
    private val talk = Spell(Jobs.ID.INVOKER, "Talk", 0, ID.TALK) {}

    // rogue spells
    private val escape = Spell(Jobs.ID.ROGUE, "Escape", 0, ID.ESCAPE) {}

    val map = mapOf(
            ID.HEAL to heal,
            ID.HEAL2 to heal2,
            ID.REGEN to regen,
            ID.SHIELD to shield,
            ID.BEAM to beam,
            ID.BALL to ball,
            ID.FIRE to fire,
            ID.DOUBLE_HIT to doubleHit,
            ID.DANCE to dance,
            ID.EARTHQUAKE to earthquake,
            ID.TALK to talk,
            ID.ESCAPE to escape
    )
}