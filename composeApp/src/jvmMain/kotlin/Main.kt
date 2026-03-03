import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.raymondoyondi.decompose.DefaultComponentContext
import com.raymondoyondi.decompose.ExperimentalDecomposeApi
import com.raymondoyondi.decompose.extensions.compose.lifecycle.LifecycleController
import com.raymondoyondi.essenty.lifecycle.LifecycleRegistry
import com.raymondoyondi.minesweeper.root.DefaultRootComponent
import com.raymondoyondi.minesweeper.root.RootContent
import com.raymondoyondi.mvikotlin.timetravel.server.TimeTravelServer
import com.raymondoyondi.mvikotlin.timetravel.store.TimeTravelStoreFactory
import minesweeper.composeapp.generated.resources.Res
import minesweeper.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import javax.swing.SwingUtilities

fun main() {
    TimeTravelServer(runOnMainThread = { SwingUtilities.invokeLater(it) })
        .start()

    val lifecycle = LifecycleRegistry()

    val root =
        DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
            storeFactory = TimeTravelStoreFactory(),
        )

    application {
        val windowState = rememberWindowState()

        Window(onCloseRequest = ::exitApplication, title = stringResource(Res.string.app_name), state = windowState) {
            RootContent(component = root)
        }

        @OptIn(ExperimentalDecomposeApi::class)
        LifecycleController(lifecycle, windowState)
    }
}
