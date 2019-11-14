package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Util
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.equipment.Sword
import ktx.graphics.use

/**
 * Icon class : displays a 16*16 icon.
 *
 * @param id The icon ID, see [Def.Icons]
 * @param pos The icon position
 * @param onClick optional click callback
 */
open class Icon : Button {

    private var flip = false
    private var texture: CompositeTexture
    var id = Icons.ID.UNKNOWN
        get() = field
    override val size: Pair<Float, Float>
        get() = texture.size

    constructor(id: Icons.ID, pos: Pair<Float, Float>, onClick: () -> Unit = {}) : super(pos, true, onClick) {
        texture = CompositeTexture(Icons(id))
        this.id = id
    }

    constructor(id: Sword.Combination, pos: Pair<Float, Float>, onClick: () -> Unit = {}) : super(pos, true, onClick) {
        texture = CompositeTexture(id)
    }

    override fun draw(sR: ShapeRenderer, batch: SpriteBatch) {
        if (visible) {
            if (clicked) batch.shader = Util.invertShader

            batch.use {
                texture.draw(it, pos, flip)
            }
            batch.shader = Util.defaultShader
        }
    }

    fun setIcon(id: Icons.ID?) {
        this.id = id ?: Icons.ID.UNKNOWN
        texture.changeIcon(id)
    }

    fun setIcon(id: Sword.Combination) {
        texture = CompositeTexture(id)
        this.id = Icons.ID.SWORD
    }

    fun flip() {
        this.flip = !this.flip
    }

    override fun clickCallback() {
        super.clickCallback()
        if (id in Icons.changingIcons.keys) {
            setIcon(Icons.changingIcons[id])
        }
    }
}