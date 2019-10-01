package com.yopox.crystals.def

import com.yopox.crystals.def.Icons.ID.*
import kotlin.random.Random

/**
 * RNG probabilities & functions are defined here.
 */
object RNG {

    private val sums = mutableMapOf<Any, Int>()

    fun <A> computeSum(map: Map<A, Int>): Int {
        if (sums[map] == null) {
            val sum = map.values.reduce { i1, i2 -> i1 + i2 }
            sums[map] = sum
        }
        return sums[map]!!
    }

    operator fun invoke(proba: Double) = Random.nextDouble() <= proba

    /**
     * Spell unlocking probability
     */
    const val UNLOCK_2ND_SPELL = 0.15
    const val UNLOCK_3RD_SPELL = 0.04

    /**
     * Treasure probability for tiles.
     */
    private val treasure = mapOf(
            CHEST_CLOSED to 1.0,
            BOOKSHELF to 0.25,
            CLOSET1 to 0.1,
            CLOSET2 to 0.1,
            CLOSET3 to 0.1,
            CLOSET4_CLOSED to 0.2,
            CHEST_OPENED to 0.0,
            CLOSET4_OPENED to 0.0
    ).withDefault { 0.01 }

    // Tiles frequency

    val inn = mapOf(
            CHEST_CLOSED to 5,
            CHEST_OPENED to 1,
            BED to 5,
            BOOKSHELF to 6,
            CLOSET1 to 1,
            CLOSET2 to 1,
            CLOSET3 to 1,
            CLOSET4_CLOSED to 2
    )

    val innBad = mapOf(
            CHEST_OPENED to 4,
            BED to 1,
            BOOKSHELF_SKULL to 4,
            WEB to 20
    )

    val innGood = mapOf(
            CHEST_CLOSED to 5,
            BOOKSHELF to 2,
            CLOSET4_CLOSED to 5
    )

    private val treasures = mapOf(
            BOOKSHELF to mapOf(
                    Items.ID.SCROLL to 1
            )
    ).withDefault {
        mapOf(
                Items.ID.POTION to 1
        )
    }

    /**
     * Generate a treasure (or not) for a given tile.
     */
    fun genTreasure(id: Icons.ID): Items.ID? {
        return if (Math.random() > treasure.getValue(id)) {
            null
        } else {
            treasures.getValue(id).weighedRandom()
        }
    }

}

fun <A> Map<A, Int>.weighedRandom(): A {
    // Computes the sum of weights
    val sum = RNG.computeSum(this)

    // Picks a random number
    val randomItem = Random.nextInt(1, sum + 1)

    // Returns the corresponding value
    val intValues = this.values.toIntArray()
    var weightSum = intValues.first()
    var i = 0
    while (randomItem > weightSum) {
        i++; weightSum += intValues[i]
    }

    return this.keys.elementAt(i)
}