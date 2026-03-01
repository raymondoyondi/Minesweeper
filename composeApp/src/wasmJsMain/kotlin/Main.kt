import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import com.raymondoyondi.decompose.DefaultComponentContext
import com.raymondoyondi.essenty.lifecycle.LifecycleRegistry
import com.raymondoyondi.essenty.lifecycle.resume
import com.raymondoyondi.essenty.lifecycle.stop
import com.raymondoyondi.essenty.statekeeper.StateKeeperDispatcher
import com.raymondoyondi.minesweeper.decodeSerializableContainer
import com.raymondoyondi.minesweeper.encodeToString
import com.raymondoyondi.minesweeper.root.DefaultRootComponent
import com.raymondoyondi.minesweeper.root.RootContent
import com.raymondoyondi.mvikotlin.main.store.DefaultStoreFactory
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.w3c.dom.Document
import org.w3c.dom.get
import org.w3c.dom.set

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val stateKeeper = StateKeeperDispatcher(savedState = localStorage[KEY_SAVED_STATE]?.decodeSerializableContainer())

    val root =
        DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle, stateKeeper = stateKeeper),
            storeFactory = DefaultStoreFactory(),
        )

    lifecycle.attachToDocument()

    window.onbeforeunload =
        {
            localStorage[KEY_SAVED_STATE] = stateKeeper.save().encodeToString()
            null
        }

    // TODO: Take the title from resources after https://youtrack.jetbrains.com/issue/KT-49981
    CanvasBasedWindow(title = "Minesweeper", canvasElementId = "ComposeTarget") {
        RootContent(root)
    }
}

private const val KEY_SAVED_STATE = "saved_state"

private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (visibilityState(document) == "visible") {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

// Workaround for Document#visibilityState not available in Wasm
@JsFun("(document) => document.visibilityState")
private external fun visibilityState(document: Document): String
