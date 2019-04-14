package com.yopox.crystals.data

import com.yopox.crystals.Util

class Job(val name: String, val desc: String, val hp: Int, val mp: Int, val atk: Int, val wis: Int, val def: Int, val spd: Int) {

    fun statsDescription(): Array<String> {
        val stats = arrayOf(hp, mp, atk, wis, def, spd)
        val sS = stats.map { Util.signedInt(it) }
        return arrayOf("HP ${sS[0]}\nMP ${sS[1]}",
                "ATK ${sS[2]}\nWIS ${sS[3]}",
                "DEF ${sS[4]}\nSPD ${sS[5]}")
    }

}