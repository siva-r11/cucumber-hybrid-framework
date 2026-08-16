package com.framework.config;

import org.aeonbits.owner.ConfigCache;

/**
 * Single access point for framework configuration.
 *
 * <p>Owner's {@link ConfigCache} guarantees a single materialised instance per interface,
 * so every caller sees the same resolved values without re-reading files. Because config
 * is immutable at runtime, this is thread-safe for parallel execution.
 */
public final class ConfigManager {

    private ConfigManager() {
        // static utility
    }

    /**
     * @return the cached, fully-resolved {@link FrameworkConfig}.
     */
    public static FrameworkConfig get() {
        return ConfigCache.getOrCreate(FrameworkConfig.class);
    }
}
