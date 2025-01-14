plugins {
    id("naveenapps.plugin.android.library")
    id("naveenapps.plugin.kotlin.basic")
    id("naveenapps.plugin.hilt")
}

android {
    namespace = "com.naveenapps.expensemanager.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.joda.time)
}