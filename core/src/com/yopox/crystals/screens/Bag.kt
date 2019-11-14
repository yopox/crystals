package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.logic.equipment.Item
import com.yopox.crystals.logic.equipment.Sword
import com.yopox.crystals.ui.*
import ktx.graphics.use
import kotlin.math.floor

class Bag(game: Crystals) : Screen(Util.TEXT_BAG, game) {

    private val tiles = mutableListOf<Tile>()
    private var page = 0
    private var popupFrame = 0
    private var showPopup = false
    private var popupIn = false
    private var popupOut = false
    private var popupItem: Item? = null
    private var popupButtons = mutableListOf<TextButton>()
    private var popupIcon = CompositeTexture(Sword.Combination())
    private var popupName = ""
    private var popupDesc = ""
    private var selectedItem = -1

    init {
        buttons.add(TextButton(100f, 5f, Util.TEXT_CONTINUE) {
            blockInput = true
            state = ScreenState.ENDING
        })
        popupButtons.add(TextButton(22f, 15f, Util.TEXT_DISCARD) { discard() })
        popupButtons.add(TextButton(22f + Util.BUTTON_WIDTH + 3, 15f, Util.TEXT_EQUIP) { })
        popupButtons.add(TextButton(22f + 2 * (Util.BUTTON_WIDTH + 3), 15f, Util.TEXT_OK) { ok() })
        popupButtons.forEach { it.hide() }
        repeat(12) { i ->
            tiles.add(Tile(Icons.ID.DOT, Pair(25f + 19 * (i % 6), 43f - 19 * floor(i / 6f))) { select(i) })
        }
    }

    private fun discard() {
        Progress.removeItem(popupItem!!)
        popupName = "${popupItem!!.name} (x${Progress.items[popupItem!!] ?: 0})"
        if (Progress.items[popupItem!!] == null) {
            popupOut = true
            popupItem = null
            blockInput = true
            setup()
        }
    }

    private fun ok() {
        popupOut = true
        popupItem = null
        blockInput = true
    }

    private fun select(i: Int) {
        if (tiles[i].id != Icons.ID.DOT) {
            // Make background buttons not clickable
            tiles.forEach { it.clickable = false }
            buttons.forEach { it.clickable = false }
            popupButtons.forEach { it.show() }

            selectedItem = i
            state = ScreenState.POPUP
            popupIn = true
            blockInput = true
            showPopup = false

            val item = tiles[i].treasure!!
            popupItem = item
            popupName = "${item.name} (x${Progress.items[item]})"
            popupDesc = item.description
            when (item) {
                is Sword -> {
                    popupIcon = CompositeTexture(item.swordId)
                    popupButtons[1].editText("Equip")
                }
                else -> {
                    popupIcon = CompositeTexture(Icons(item.id))
                    popupButtons[1].editText("Use")
                }
            }
        }
    }

    fun setup() {
        println(Progress.items)
        tiles.forEach { it.setIcon(Icons.ID.DOT) ; it.treasure = null }
        Progress.items.keys.forEachIndexed { i, item ->
            if (i < 12) {
                if (item is Sword) {
                    tiles[i].apply {
                        setIcon(item.swordId)
                        treasure = item
                    }
                } else {
                    tiles[i].apply {
                        setIcon(item.id)
                        treasure = item
                    }
                }
            }
        }
    }

    override fun render(delta: Float) {
        super.render(delta)

        tiles.forEach { it.draw(shapeRenderer, batch) }
        batch.use {
            Util.font.draw(it, "Page ${page + 1}/1", 27f, 16f)
        }

        if (state == ScreenState.POPUP) {
            // Draw the popup
            if (showPopup) {
                Util.drawRect(shapeRenderer, 15f, 11f, 129f, 66f)
                batch.use {
                    popupIcon.draw(batch, Pair(21f, 56f))
                    Util.font.draw(it, popupName, 42f, 67f)
                    Util.font.draw(it, popupDesc, 22f, 51f)
                }
                popupButtons.forEach { it.draw(shapeRenderer, batch) }
            }

            if (popupIn && popupFrame <= Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer, 15f, 11f, 129f, 66f, popupFrame) {
                    showPopup = true
                }
                popupFrame++
            } else if (popupIn) {
                popupIn = false
                blockInput = false
                popupButtons.forEach { it.clickable = true }
                popupFrame = 0
            }

            if (popupOut && popupFrame <= Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer, 15f, 11f, 129f, 66f, popupFrame) {
                    showPopup = false
                }
                popupFrame++
            } else if (popupOut) {
                popupOut = false
                blockInput = false
                state = ScreenState.MAIN
                popupFrame = 0
                popupButtons.forEach { it.clickable = false }
                buttons.forEach { it.clickable = true }
                tiles.forEach { it.clickable = true }
            }

        }

        drawTransitions()
    }

    override fun resetState() {
        state = ScreenState.OPENING
        page = 0
        tiles.forEach { it.setIcon(Icons.ID.DOT) }
        popupIn = false
        popupOut = false
        popupFrame = 0
        showPopup = false
    }

    override fun inputUp(x: Int, y: Int) {
        tiles.forEach { it.lift(x, y) }
        buttons.forEach { it.lift(x, y) }
        popupButtons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        tiles.forEach { it.touch(x, y) }
        buttons.forEach { it.touch(x, y) }
        popupButtons.forEach { it.touch(x, y) }
    }

}