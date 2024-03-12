import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable

@Immutable
data class AppBarState(
    val key: String = "",
    val title: String = "",
    val showBackAction: Boolean = false,
    val showTopAppBar: Boolean = true,
    val showBottomBar: Boolean = false,
    val showLogo: Boolean = false,
    val showContrastColor: Boolean = false,
    val actions: (@Composable RowScope.() -> Unit)? = null,
    val popBackStack: (() -> Unit)? = null
)