package com.raymondoyondi.minesweeper

import com.raymondoyondi.decompose.ComponentContext
import com.raymondoyondi.decompose.router.children.ChildNavState
import com.raymondoyondi.decompose.router.children.NavState
import com.raymondoyondi.decompose.router.children.NavigationSource
import com.raymondoyondi.decompose.router.children.SimpleChildNavState
import com.raymondoyondi.decompose.router.children.children
import com.raymondoyondi.decompose.value.Value
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

internal fun <C : Any, T : Any> ComponentContext.child(
    source: NavigationSource<C>,
    serializer: KSerializer<C>,
    initialConfiguration: () -> C,
    key: String = "child",
    childFactory: (C, ComponentContext) -> T,
): Value<T> =
    children(
        source = source,
        stateSerializer = SimpleNavState.serializer(typeSerial0 = serializer),
        initialState = { SimpleNavState(initialConfiguration()) },
        key = key,
        navTransformer = { _, config -> SimpleNavState(config) },
        stateMapper = { _, children -> requireNotNull(children.single().instance) },
        childFactory = childFactory,
    )

@Serializable
private data class SimpleNavState<C : Any>(
    private val config: C,
) : NavState<C> {
    override val children: List<ChildNavState<C>> =
        listOf(SimpleChildNavState(config, ChildNavState.Status.RESUMED))
}
