package com.yopox.crystals

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
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
 */
class Crystals : KtxGame<Screen>() {

    private lateinit var assetManager: AssetManager
    private lateinit var assets: Assets

    override fun create() {
        assetManager = initiateAssetManager()
        with(assetManager) {
            finishLoading()
        }

        val generator = FreeTypeFontGenerator(Gdx.files.internal("babyblocks.ttf"))
        val generator2 = FreeTypeFontGenerator(Gdx.files.internal("bubbleTime.ttf"))
        val parameter = FreeTypeFontParameter()
        parameter.size = 16
        val parameter2 = FreeTypeFontParameter()
        parameter2.size = 42
        assets = Assets(assetManager, generator2.generateFont(parameter2), generator.generateFont(parameter))

        generator.dispose()
        generator2.dispose()

        addScreen(TitleScreen(assets))
        setScreen<TitleScreen>()
    }

    private fun initiateAssetManager(): AssetManager {
        val assetManager = AssetManager()
        assetManager.registerFreeTypeFontLoaders()
        return assetManager
    }

}
