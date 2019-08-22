package com.yopox.crystals.def

import com.yopox.crystals.def.Actions.ID.*
import com.yopox.crystals.logic.fight.Spell
import com.yopox.crystals.logic.fight.Stat
import com.yopox.crystals.logic.fight.Target.*
import com.yopox.crystals.screens.Fight
import kotlin.random.Random

object Spells {

    private fun noText(): ArrayList<Fight.Block> = ArrayList()
    private fun text(t: String): ArrayList<Fight.Block> = arrayListOf(Fight.Block(Fight.BlockType.TEXT, t))

    // Misc
    private val wait = Spell(WAIT, "Wait", Jobs.ID.ANY, 0, SELF)
    private val defense = Spell(DEFENSE, "Defend", Jobs.ID.ANY, 0, SELF) { f1, _ ->
        f1.buff(Stat.DEF, 25); noText()
    }
    private val attack = Spell(ATTACK, "Attack", Jobs.ID.ANY, 0, SINGLE) { f1, f2 ->
        f1.attack(f2)
    }

    // Priest spells
    private val heal = Spell(HEAL, "Heal", Jobs.ID.PRIEST, 2, SINGLE) { f1, f2 ->
        f1.heal(f2, 5)
    }
    private val heal2 = Spell(HEAL2, "Heal +", Jobs.ID.PRIEST, 6, SINGLE) { f1, f2 ->
        f1.heal(f2, 20)
    }
    private val cure = Spell(CURE, "Cure", Jobs.ID.PRIEST, 4, SINGLE)
    private val barrier = Spell(BARRIER, "Barrier", Jobs.ID.PRIEST, 4, SINGLE) { f1, f2 ->
        f1.addBuff(Stat.DEF, 40, 2, f2) +
                text("${f2.name} is protected!")
    }
    private val beam = Spell(BEAMS, "Beam", Jobs.ID.PRIEST, 3, ENEMIES) { f1, f2 ->
        f1.magicalAttack(f2, 3)
    }
    private val ball = Spell(BALL, "Ball", Jobs.ID.PRIEST, 6, SINGLE) { f1, f2 ->
        f1.magicalAttack(f2, 7)
    }

    // Mage spells
    private val wind = Spell(WIND, "Wind", Jobs.ID.MAGE, 2, SINGLE) { f1, f2 ->
        f1.magicalAttack(f2, 3)
    }
    private val fire = Spell(FIRE, "Fire", Jobs.ID.MAGE, 4, SINGLE) { f1, f2 ->
        f1.magicalAttack(f2, 5)
    }
    private val lightning = Spell(LIGHTNING, "Lightning", Jobs.ID.MAGE, 5, SINGLE) { f1, f2 ->
        f1.magicalAttack(f2, 7)
    }
    private val water = Spell(WATER, "Water", Jobs.ID.MAGE, 7, ENEMIES) { f1, f2 ->
        f1.magicalAttack(f2, 7)
    }
    private val energy = Spell(ENERGY, "Energy", Jobs.ID.MAGE, 8, SINGLE) { f1, f2 ->
        f1.magicalAttack(f2, 10)
    }
    private val poison = Spell(POISON, "Poison", Jobs.ID.MAGE, 3, SINGLE) { f1, f2 ->
        f1.poison(f2, 4, 3)
    }

    // Monk spells
    private val meditation = Spell(MEDITATION, "Meditation", Jobs.ID.MONK, 2, SELF)
    private val focus = Spell(FOCUS, "Focus", Jobs.ID.MONK, 2, SELF)
    private val kick = Spell(KICK, "Kick", Jobs.ID.MONK, 2, SINGLE)
    private val punch = Spell(PUNCH, "Punch", Jobs.ID.MONK, 2, SINGLE)
    private val chains = Spell(CHAINS, "Chains", Jobs.ID.MONK, 2, SINGLE)
    private val shuriken = Spell(SHURIKEN, "Shuriken", Jobs.ID.MONK, 2, SINGLE)

    // Warrior spells
    private val double = Spell(DOUBLE, "Double Hit", Jobs.ID.WARRIOR, 4, SINGLE) { f1, f2 ->
        f1.attack(f2) + f1.attack(f2)
    }
    private val massive = Spell(MASSIVE, "Massive Hit", Jobs.ID.WARRIOR, 8, SINGLE) { f1, f2 ->
        f1.buff(Stat.ATK, 25); f1.attack(f2)
    }
    private val shield = Spell(SHIELD, "Shield", Jobs.ID.WARRIOR, 4, SINGLE) { f1, f2 ->
        f1.addBuff(Stat.DEF, 120, 0, f2)
    }
    private val insult = Spell(INSULT, "Insult", Jobs.ID.WARRIOR, 1, SINGLE) { f1, f2 ->
        when (Random.nextInt(2)) {
            0 -> {
                f1.addBuff(Stat.DEF, -15, 2, f2) +
                        text("${f2.name} is frightened!")
            }
            else -> {
                f1.addBuff(Stat.ATK, 15, 2, f2) +
                        text("${f2.name} is upset!")
            }
        }
    }
    private val storm = Spell(STORM, "Storm", Jobs.ID.WARRIOR, 10, ENEMIES) { f1, f2 ->
        f1.buff(Stat.ATK, 10); f1.attack(f2)
    }
    private val jump = Spell(JUMP, "Aerial Hit", Jobs.ID.WARRIOR, 5, SINGLE)  { f1, f2 ->
        f1.buff(Stat.ATK, 5); f1.attack(f2)
    }

    // Invoker spells
    private val fairy = Spell(FAIRY, "Fairy", Jobs.ID.INVOKER, 3, SELF)
    private val tame = Spell(TAME, "Tame", Jobs.ID.INVOKER, 1, SINGLE)
    private val golem = Spell(GOLEM, "Golem", Jobs.ID.INVOKER, 6, SELF)
    private val raiku = Spell(RAIKU, "Raiku", Jobs.ID.INVOKER, 8, SELF)
    private val wendigo = Spell(WENDIGO, "Wendigo", Jobs.ID.INVOKER, 5, SELF)
    private val deadKing = Spell(DEAD_KING, "Dead King", Jobs.ID.INVOKER, 12, SELF)

    // Rogue spells
    private val dagger = Spell(DAGGER, "Dagger", Jobs.ID.ROGUE, 5, SINGLE)
    private val escape = Spell(ESCAPE, "Escape", Jobs.ID.ROGUE, 0, SELF)
    private val rob = Spell(ROB, "Rob", Jobs.ID.ROGUE, 3, SINGLE)
    private val burgle = Spell(BURGLE, "Burgle", Jobs.ID.ROGUE, 3, SINGLE)
    private val bomb = Spell(BOMB, "Bomb", Jobs.ID.ROGUE, 6, SINGLE)
    private val bribe = Spell(BRIBE, "Bribe", Jobs.ID.ROGUE, 1, SINGLE)

    // Bard spells
    private val sing = Spell(SING, "Sing", Jobs.ID.BARD, 2, SINGLE)
    private val bewitch = Spell(BEWITCH, "Bewitch", Jobs.ID.BARD, 2, SINGLE)
    private val hide = Spell(HIDE, "Hide", Jobs.ID.BARD, 2, SELF)
    private val fireworks = Spell(FIREWORKS, "Fireworks", Jobs.ID.BARD, 8, ENEMIES)
    private val wine = Spell(WINE, "Wine", Jobs.ID.BARD, 2, SINGLE)
    private val sausage = Spell(SAUSAGE, "Sausage", Jobs.ID.BARD, 2, SINGLE)

    // Geomancer spells
    private val earthquake = Spell(EARTHQUAKE, "Earthquake", Jobs.ID.GEOMANCER, 6, ENEMIES)
    private val predict = Spell(PREDICT, "Predict", Jobs.ID.GEOMANCER, 1, SINGLE)
    private val tornado = Spell(TORNADO, "Tornado", Jobs.ID.GEOMANCER, 5, SINGLE)

    // Monster spells
    private val ultrasound = Spell(ULTRASOUND, "Ultrasound", Jobs.ID.NONE, 3, SINGLE) { f1, f2 ->
        f1.attack(f2) +
                f1.addBuff(Stat.DEF, -4, 1, f2) +
                text("${f2.name}'s defense fell.")
    }

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
            TORNADO to tornado,

            ULTRASOUND to ultrasound,

            ATTACK to attack,
            DEFENSE to defense
    ).withDefault { wait }

    fun getSpell(spell: Actions.ID): Spell = map.getValue(spell)

    fun baseSpell(job: Jobs.ID): Spell = when (job) {
        Jobs.ID.BARD -> map.getValue(SING)
        Jobs.ID.GEOMANCER -> map.getValue(TORNADO)
        Jobs.ID.INVOKER -> map.getValue(FAIRY)
        Jobs.ID.MAGE -> map.getValue(POISON)
        Jobs.ID.MONK -> map.getValue(FOCUS)
        Jobs.ID.PRIEST -> map.getValue(HEAL)
        Jobs.ID.ROGUE -> map.getValue(ROB)
        Jobs.ID.WARRIOR -> map.getValue(DOUBLE)
        else -> map.getValue(WAIT)
    }

    fun text(move: Fight.Move): String = when (move.spell.id) {
        ATTACK -> "${move.fighter.name} attacks ${move.targets.first().name}!"
        DEFENSE -> "${move.fighter.name} is defending."
        WAIT -> "${move.fighter.name} waits."
        else -> "${move.fighter.name} uses ${move.spell.name}."
    }
}