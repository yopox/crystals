package com.yopox.crystals.def

import com.badlogic.gdx.Gdx
import com.yopox.crystals.def.Icons.ID.*
import kotlin.random.Random

/**
 * RNG probabilities & functions are defined here.
 */
object RNG {

    data class Item<K>(val value: K, val weight: Int)
    data class Proba<K>(val value: K, val proba: Double)

    private val sums = mutableMapOf<Any, Int>()

    fun <A> computeSum(list: List<Item<A>>): Int {
        if (sums[list] == null) {
            val sum = list.map { it.weight }.reduce { i1, i2 -> i1 + i2 }
            sums[list] = sum
        }
        return sums[list]!!
    }

    val treasure = listOf(
            Proba(CHEST_CLOSED, 1.0),
            Proba(BOOKSHELF, 0.25),
            Proba(CLOSET1, 0.15),
            Proba(CLOSET2, 0.15),
            Proba(CLOSET3, 0.15)
    )

    val inn = listOf(
            Item(CHEST_CLOSED, 5),
            Item(CHEST_OPENED, 1),
            Item(BED, 10),
            Item(BOOKSHELF, 6),
            Item(CLOSET1, 2),
            Item(CLOSET2, 2),
            Item(CLOSET3, 2)
    )

    val innBad = listOf(
            Item(CHEST_CLOSED, 1),
            Item(CHEST_OPENED, 5),
            Item(BED, 5),
            Item(BOOKSHELF, 1),
            Item(WEB, 20)
    )

    val innGood = listOf(
            Item(CHEST_CLOSED, 10),
            Item(BOOKSHELF, 8)
    )

}

fun <A> List<RNG.Item<A>>.weighedRandom(): A {
    // Computes the sum of weights
    val sum = RNG.computeSum(this)

    // Picks a random number
    val randomItem = Random.nextInt(1, sum + 1)

    // Returns the corresponding value
    var weightSum = this[0].weight
    var i = 0
    while (randomItem > weightSum) {
        i++; weightSum += this[i].weight
    }

    return this[i].value
}