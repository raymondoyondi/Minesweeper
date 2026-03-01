package com.raymondoyondi.minesweeper.root

import com.raymondoyondi.decompose.router.slot.ChildSlot
import com.raymondoyondi.decompose.value.Value
import com.raymondoyondi.minesweeper.game.GameComponent
import com.raymondoyondi.minesweeper.settings.EditSettingsComponent

internal interface RootComponent {

    val gameComponent: Value<GameComponent>
    val editSettingsComponent: Value<ChildSlot<*, EditSettingsComponent>>

    fun onEditSettingsClicked()
}
