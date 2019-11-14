package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.Text
import com.yopox.crystals.logic.Event
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.ui.*
import ktx.graphics.use
import kotlin.random.Random

class Shop(game: Crystals) : Screen(Util.TEXT_SHOP, game) {

    private val bench = ArrayList<Icon>()
    private val items = ArrayList<Tile>()
    var goldX = 0f

    private var itemTransition = false
    private var itemFrame = 0
    var selectedItem: Tile? = null
    var nextItem: Tile? = null
    var shopkeeper: Icon
    var text = arrayListOf<String>()
    var textX = listOf<Float>()

    private val benchX = 4f
    private val benchY = 24f

    init {
        shopkeeper = Icon(Icons.ID.SHOPKEEPER, Pair(109f, 28f))

        buttons.add(TextButton(79f - 40, 5f, Util.TEXT_SELL) { sell() })
        buttons.add(TextButton(79f, 5f, Util.TEXT_BUY) { buy() })
        buttons.add(TextButton(79f + 40, 5f, Util.TEXT_LEAVE) {
            state = ScreenState.ENDING
            blockInput = true
        })
        bench.add(Icon(Icons.ID.BENCH_LEFT, Pair(benchX, benchY)))
        bench.add(Icon(Icons.ID.BENCH_MIDDLE, Pair(benchX + 14, benchY)))
        bench.add(Icon(Icons.ID.BENCH_MIDDLE, Pair(benchX + 2 * 14, benchY)))
        bench.add(Icon(Icons.ID.BENCH_MIDDLE, Pair(benchX + 3 * 14, benchY)))
        bench.add(Icon(Icons.ID.BENCH_LEFT, Pair(benchX + 4 * 14, benchY)).apply { flip() })
    }

    private fun sell() {
        Gdx.app.log("Shop", "Sell")
    }

    private fun buy() {
        selectedItem?.let {
            val cost = it.treasure!!.value
            if (cost <= Progress.gold) {
                Progress.gold -= cost
                Progress.addItem(it.treasure!!)
                goldX = Util.WIDTH - 5f - Util.textSize("${Progress.gold} GOLD", Util.font).first
                nextItem = null
                itemTransition = true
                selectedItem!!.apply { hide(); clickable = false }
                itemFrame = 0
                blockInput = true
            }
        }
    }

    fun setup(event: Event) {
        // Generate shop items
        val itemsNb = Random.nextInt(2) + 2
        val spray = (16 * 5) / (itemsNb + 1)
        val itemX = benchX - 4

        repeat(itemsNb) {
            items.add(Tile.genShopTile(Pair(itemX + (it + 1) * spray - 8, benchY + 17)) { selectItem(items[it]) })
        }

        goldX = Util.WIDTH - 5f - Util.textSize("${Progress.gold} GOLD", Util.font).first
        text = arrayListOf(Text.shopkeeperWelcome.random())
        textX = text.map { Util.textOffsetX(Util.font, it, 76f) }
    }

    private fun selectItem(tile: Tile) {
        if (selectedItem != tile) {
            nextItem = tile
            itemTransition = true
            itemFrame = 0
            blockInput = true
        }
    }

    override fun render(delta: Float) {
        super.render(delta)

        // Shop items
        nextItem?.let {
            Util.drawRect(shapeRenderer, it.pos.first - 1, it.pos.second - 1, 18f, 18f)
        }
        items.forEach { it.draw(shapeRenderer, batch) }

        // UI (bench & buttons)
        bench.forEach { it.draw(shapeRenderer, batch) }

        // Item box
        Util.drawRect(shapeRenderer, 79f, 25f, 76f, 46f)
        shopkeeper.draw(shapeRenderer, batch)

        batch.use {
            for (i in 0 until text.size) {
                Util.font.draw(it, text[i], 79f + textX[i], 62f - 10 * i)
            }
        }

        if (itemTransition) {
            if (itemFrame < Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer, 79f, 25f, 76f, 46f, itemFrame) {
                    selectedItem = nextItem
                    text.clear()
                    updateBoxText()
                }
                itemFrame++
            } else {
                itemFrame = 0
                itemTransition = false
                blockInput = false
            }
        }

        drawTransitions()
    }

    private fun updateBoxText() {
        if (selectedItem != null) {
            text.add(nextItem!!.treasure!!.name)
            text.add(nextItem!!.treasure!!.description)
            text.add("${nextItem!!.treasure!!.value} GOLD")
            textX = text.map { Util.textOffsetX(Util.font, it, 76f) }
            shopkeeper.hide()
        } else {
            text = arrayListOf(Text.shopkeeperThanks.random())
            textX = text.map { Util.textOffsetX(Util.font, it, 76f) }
            shopkeeper.show()
        }
    }

    override fun resetState() {
        state = ScreenState.OPENING
        items.clear()
        text.clear()
        selectedItem = null
        nextItem = null
        shopkeeper.show()
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.forEach { it.lift(x, y) }
        items.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.forEach { it.touch(x, y) }
        items.forEach { it.touch(x, y) }
    }

}