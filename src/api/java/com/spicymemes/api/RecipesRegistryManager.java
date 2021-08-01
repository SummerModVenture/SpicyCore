package com.spicymemes.api;

import java.util.HashMap;

public class RecipesRegistryManager {
    private static HashMap<String, RecipesBase> registries = new HashMap<>();

    private static String createRegistryName(String modId, String registryName) {
        return modId + ":" + registryName;
    }

    /**
     * Adds a recipe registry to the registry manager
     * @param modId Your mod id
     * @param registryName Your registry name
     * @param registry The registry to add
     */
    public static void addRegistry(String modId, String registryName, RecipesBase registry) {
        registries.put(createRegistryName(modId, registryName), registry);
    }

    /**
     * Gets a recipe registry from the registry manager
     * @param modId The mod id of the registry
     * @param registryName The name of the registry
     * @return The registry, or null if it isn't found
     */
    public static RecipesBase getRegistry(String modId, String registryName) {
        String name = createRegistryName(modId, registryName);
        if (registries.containsKey(name)) {
            return registries.get(name);
        }
        return null;
    }
}
