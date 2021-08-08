package com.spicymemes.api

object RecipesRegistryManager {

    private val registries: MutableMap<String, RecipesBase> = mutableMapOf()

    private fun createRegistryName(modId: String, registryName: String): String = "$modId:$registryName"

    /**
     * Adds a recipe registry to the registry manager
     * @param modId Your mod id
     * @param registryName Your registry name
     * @param registry The registry to add
     */
    @JvmStatic
    fun addRegistry(modId: String, registryName: String, registry: RecipesBase) {
        registries[createRegistryName(modId, registryName)] = registry
    }

    /**
     * Gets a recipe registry from the registry manager
     * @param modId The mod id of the registry
     * @param registryName The name of the registry
     * @return The registry, or null if it isn't found
     */
    @JvmStatic
    fun getRegistry(modId: String, registryName: String) =
        registries[createRegistryName(modId, registryName)]
}
