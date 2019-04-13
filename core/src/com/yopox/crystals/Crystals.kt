package com.yopox.crystals

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import ktx.app.KtxGame
import ktx.freetype.registerFreeTypeFontLoaders
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter

data class Assets(val assetManager: AssetManager, val titleFont: BitmapFont, val font: BitmapFont)

/**
 * Main class.
 *
 * Loads the fonts, initialize the different game states and start the title screen.
 *
 * TODO: Move assets loading elsewhere
 * TODO: Move map creation
 */
class Crystals : KtxGame<Screen>() {

    companion object {
        lateinit var assetManager: AssetManager
        lateinit var events: Array<Array<Int>>
    }

    override fun create() {
        assetManager = initiateAssetManager()
        with(assetManager) {
            load("aseprite/icons.png", Texture::class.java)
            finishLoading()
        }

        val generator = FreeTypeFontGenerator(Gdx.files.internal("babyblocks.ttf"))
        val generator2 = FreeTypeFontGenerator(Gdx.files.internal("bubbleTime.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 8
        val parameter2 = FreeTypeFontParameter()
        parameter2.size = 21
        Util.font = generator.generateFont(parameter)
        Util.bigFont = generator2.generateFont(parameter2)

        generator.dispose()
        generator2.dispose()

        events = Array(5) { Array(3) {(Math.random() * 5).toInt()} }

        addScreen(TitleScreen())
        addScreen(CharacterSelection())
        addScreen(Trip())
        setScreen<CharacterSelection>()
    }

    private fun initiateAssetManager(): AssetManager {
        val assetManager = AssetManager()
        assetManager.registerFreeTypeFontLoaders()
        return assetManager
    }

}
