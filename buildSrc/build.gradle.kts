plugins {
    `kotlin-dsl`
}

/**
 * Resolves a plugin alias from the version catalog to its plugin-marker coordinates, which is how a
 * precompiled script plugin gets a third-party plugin onto its own classpath.
 */
fun pluginMarker(plugin: Provider<PluginDependency>) =
    plugin.map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }

dependencies {
    implementation(pluginMarker(libs.plugins.kotlin.jvm))
    implementation(pluginMarker(libs.plugins.ktlint.plugin))
    implementation(pluginMarker(libs.plugins.detekt))
}
