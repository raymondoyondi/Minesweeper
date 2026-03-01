package com.raymondoyondi.minesweeper.settings

import com.raymondoyondi.decompose.value.Value
import com.raymondoyondi.minesweeper.game.GameSettings

internal interface EditSettingsComponent {

    val model: Value<Model>

    fun onWidthChanged(text: String)
    fun onHeightChanged(text: String)
    fun onMaxMinesChanged(text: String)
    fun onConfirmClicked()
    fun onDismissRequested()

    data class Model(
        val width: String,
        val height: String,
        val maxMines: String,
    )

    fun interface Factory {
        operator fun invoke(
            settings: GameSettings,
            onConfirmed: (GameSettings) -> Unit,
            onCancelled: () -> Unit,
        ): EditSettingsComponent
    }
}
