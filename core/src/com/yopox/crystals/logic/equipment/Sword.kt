package com.yopox.crystals.logic.equipment

import com.yopox.crystals.def.Icons
import kotlin.random.Random

class Sword(name: String, description: String, value: Int) : Item(name, description, value, Icons.ID.SWORD) {

    data class Combination(
            val blade: Int = Random.nextInt(10),
            val handle: Int = Random.nextInt(10),
            val guard: Int = Random.nextInt(10))

    val swordId = Combination()

    init {
    }

}