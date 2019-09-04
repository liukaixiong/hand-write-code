package com.lkx.code.netty.rpc.registry;

import com.lkx.code.netty.rpc.listening.NotifyListener;
import com.lkx.code.netty.rpc.protocol.RegisterMeta;

import java.rmi.registry.Registry;
import java.util.Collection;
import java.util.Map;

/**
 * Registry service.
 * <p>
 * jupiter
 * org.jupiter.registry
 *
 * @author jiachun.fjc
 */
public interface RegistryService extends Registry {

    /**
     * Register service to registry server.
     */
    void register(RegisterMeta meta);

    /**
     * Unregister service to registry server.
     */
    void unregister(RegisterMeta meta);

    /**
     * Subscribe a service from registry server.
     */
    void subscribe(RegisterMeta.ServiceMeta serviceMeta, NotifyListener listener);

    /**
     * Find a service in the local scope.
     */
    Collection<RegisterMeta> lookup(RegisterMeta.ServiceMeta serviceMeta);

    /**
     * List all consumer's info.
     */
    Map<RegisterMeta.ServiceMeta, Integer> consumers();

    /**
     * List all provider's info.
     */
    Map<RegisterMeta, RegisterState> providers();

    /**
     * Returns {@code true} if {@link RegistryService} is shutdown.
     */
    boolean isShutdown();

    /**
     * Shutdown.
     */
    void shutdownGracefully();

    enum RegistryType {
        DEFAULT("default"),
        ZOOKEEPER("zookeeper");

        private final String value;

        RegistryType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static RegistryType parse(String name) {
            for (RegistryType s : values()) {
                if (s.name().equalsIgnoreCase(name)) {
                    return s;
                }
            }
            return null;
        }
    }

    enum RegisterState {
        PREPARE,
        DONE
    }
}
