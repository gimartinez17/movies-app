package com.gmart.gmovies.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

data class DeviceScreenConfiguration(val size: DeviceScreenSize) {

    enum class DeviceScreenSize(val screenWidthRange: IntRange) {
        ExtraSmall(0..360),
        Small(361..599),
        Medium(600..839),
        Large(840..Int.MAX_VALUE);

        var actualWidth: Int = -1
            private set

        companion object {
            fun getScreenSize(height: Int, width: Int): DeviceScreenSize {
                val actualWidth = minOf(height, width)

                return values().firstOrNull {
                    it.screenWidthRange.contains(actualWidth)
                }?.apply DeviceScreenSize@{
                    this@DeviceScreenSize.actualWidth = actualWidth
                } ?: throw IllegalScreenSize()

            }

            private const val illegalScreenSizeMessage = "Screen size can not be less than 0"

            class IllegalScreenSize : Throwable(illegalScreenSizeMessage)
        }
    }

    enum class Config {
        ExtraSmallPortrait,
        SmallPortrait,
        MediumPortrait,
        NormalPortrait,
        BigPortrait,
    }
}

@Composable
fun rememberScreenConfiguration(): DeviceScreenConfiguration {
    val configuration = LocalConfiguration.current

    val screenWidth by remember(key1 = configuration.screenWidthDp) {
        mutableIntStateOf(configuration.screenWidthDp)
    }
    val screenHeight by remember(key1 = configuration.screenHeightDp) {
        mutableIntStateOf(configuration.screenHeightDp)
    }

    return DeviceScreenConfiguration(
        DeviceScreenConfiguration.DeviceScreenSize.getScreenSize(screenHeight, screenWidth),
    )
}
