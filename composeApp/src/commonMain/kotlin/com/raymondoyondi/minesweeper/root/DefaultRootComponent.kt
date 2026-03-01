package com.raymondoyondi.minesweeper.root

import com.raymondoyondi.decompose.ComponentContext
import com.raymondoyondi.decompose.router.children.SimpleNavigation
import com.raymondoyondi.decompose.router.slot.ChildSlot
import com.raymondoyondi.decompose.router.slot.SlotNavigation
import com.raymondoyondi.decompose.router.slot.activate
import com.raymondoyondi.decompose.router.slot.childSlot
import com.raymondoyondi.decompose.router.slot.dismiss
import com.raymondoyondi.decompose.value.Value
import com.raymondoyondi.minesweeper.child
import com.raymondoyondi.minesweeper.game.DefaultGameComponent
import com.raymondoyondi.minesweeper.game.GameComponent
import com.raymondoyondi.minesweeper.game.GameSettings
import com.raymondoyondi.minesweeper.settings.DefaultEditSettingsComponent
import com.raymondoyondi.minesweeper.settings.EditSettingsComponent
import com.raymondoyondi.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.Dispatchers

internal class DefaultRootComponent(
    componentContext: ComponentContext,
    gameComponentFactory: GameComponent.Factory,
    editSettingsComponentFactory: EditSettingsComponent.Factory,
) : RootComponent, ComponentContext by componentContext {

    private var settings = GameSettings()

    private val gameNav = SimpleNavigation<GameSettings>()
    override val gameComponent: Value<GameComponent> =
        child(
            source = gameNav,
            serializer = GameSettings.serializer(),
            initialConfiguration = { settings },
            childFactory = { settings, ctx -> gameComponentFactory(componentContext = ctx, settings = settings) },
        )

    private val editSettingsNav = SlotNavigation<GameSettings>()
    override val editSettingsComponent: Value<ChildSlot<*, EditSettingsComponent>> =
        childSlot(
            source = editSettingsNav,
            serializer = null,
            childFactory = { settings, _ ->
                editSettingsComponentFactory(
                    settings = settings,
                    onConfirmed = {
                        this.settings = it
                        editSettingsNav.dismiss()
                        gameNav.navigate(it)
                    },
                    onCancelled = editSettingsNav::dismiss,
                )
            },
        )

    override fun onEditSettingsClicked() {
        editSettingsNav.activate(settings)
    }
}

internal fun DefaultRootComponent(componentContext: ComponentContext, storeFactory: StoreFactory): DefaultRootComponent =
    DefaultRootComponent(
        componentContext = componentContext,
        gameComponentFactory = { ctx, settings ->
            DefaultGameComponent(
                componentContext = ctx,
                storeFactory = storeFactory,
                settings = settings,
                mainCoroutineContext = Dispatchers.Main.immediate
            )
        },
        editSettingsComponentFactory = ::DefaultEditSettingsComponent,
    )
