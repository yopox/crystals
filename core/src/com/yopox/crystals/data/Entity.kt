package com.yopox.crystals.data

data class Stats(var hp: Int = 40,
                 var mp: Int = 10,
                 var atk: Int = 10,
                 var wis: Int = 10,
                 var def: Int = 10,
                 var spd: Int = 10)

open class Entity {
    var baseStats = Stats()
    var stats = Stats()
    var gold = 321
}