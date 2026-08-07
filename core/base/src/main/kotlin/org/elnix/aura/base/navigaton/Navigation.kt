package org.elnix.aura.base.navigaton

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.elnix.aura.i18n.R

@Suppress("EqualsOrHashCode")
@Serializable
public sealed class NavigationRoute : NavKey {

    @get:StringRes
    public abstract val resId: Int

    @get:DrawableRes
    public abstract val icon: Int

    @SerialName("Main")
    @Serializable
    public data object Main : NavigationRoute() {
        override val resId: Int = R.string.main_screen
        override val icon: Int = R.drawable.home
    }

    @Serializable
    @SerialName("Welcome")
    public data object Welcome : NavigationRoute() {
        override val resId: Int = R.string.welcome_screen
        override val icon: Int = R.drawable.rocket_launch
    }


    @Serializable
    @SerialName("Settings")
    public data object Settings : NavigationRoute() {
        override val resId: Int = R.string.settings
        override val icon: Int = R.drawable.settings
    }

    @Serializable
    @SerialName("AppDisplay")
    public data object AppDisplay : NavigationRoute() {
        override val resId: Int = R.string.app_display
        override val icon: Int = R.drawable.display_settings
    }

    @Serializable
    @SerialName("Appearance")
    public data object Appearance : NavigationRoute() {
        override val resId: Int = R.string.appearance
        override val icon: Int = R.drawable.routine
    }

    @Serializable
    @SerialName("Behavior")
    public data object Behavior : NavigationRoute() {
        override val resId: Int = R.string.behavior
        override val icon: Int = R.drawable.question_mark
    }



    @Serializable
    @SerialName("Changelogs")
    public data object Changelogs : NavigationRoute() {
        override val resId: Int = R.string.changelogs
        override val icon: Int = R.drawable.source_notes
    }

    @Serializable
    @SerialName("Debug")
    public data object Debug : NavigationRoute() {
        override val resId: Int = R.string.debug
        override val icon: Int = R.drawable.bug_report
    }

    @Serializable
    @SerialName("Logs")
    public data object Logs : NavigationRoute() {
        override val resId: Int = R.string.logs
        override val icon: Int = R.drawable.source_notes
    }

    @Serializable
    @SerialName("LogsViewer")
    public data class LogsViewer(
        val filename: String
    ) : NavigationRoute() {
        override fun hashCode(): Int = super.hashCode()
        override val resId: Int = R.string.logs
        override val icon: Int = R.drawable.source_notes
    }

    @Serializable
    @SerialName("LockScreen")
    public data object LockScreen : NavigationRoute() {
        override val resId: Int = R.string.lock
        override val icon: Int = R.drawable.lock
    }

    override fun hashCode(): Int = System.identityHashCode(this)

    public companion object {
        public val settingsRoutes: List<NavigationRoute> by lazy {
            listOf(
                Settings,
                Appearance,
                Behavior,
                Changelogs,
                Debug,
                Logs,
            )
        }

    }
}