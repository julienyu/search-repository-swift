package org.wikimedia.elasticsearch.swift;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.RepositoryPlugin;
import org.elasticsearch.repositories.Repository;
import org.wikimedia.elasticsearch.swift.repositories.SwiftRepository;
import org.wikimedia.elasticsearch.swift.repositories.SwiftService;

import java.util.Collections;
import java.util.Map;

/**
 * Our base plugin stuff.
 */
public class SwiftRepositoryPlugin extends Plugin implements RepositoryPlugin {
//    // Elasticsearch settings
//    private final Settings settings;
//
//    /**
//     * Constructor. Sets settings to settings.
//     * @param settings Our settings
//     */
//    public SwiftRepositoryPlugin(Settings settings) {
//        this.settings = settings;
//    }
//
//    /**
//     * Plugin name, duh.
//     */
//    @Override
//    public String name() {
//        return "swift-repository";
//    }
//
//    /**
//     * A description. Also duh.
//     */
//    @Override
//    public String description() {
//        return "Swift repository plugin";
//    }
//
//    /**
//     * Register our services, if needed.
//     */
//
//    @SuppressWarnings("rawtypes")
//    @Override
//    public Collection<Class<? extends LifecycleComponent>> nodeServices() {
//        return Collections.<Class<? extends LifecycleComponent>>singleton(SwiftService.class);
//    }
//
//    /**
//     * Load our repository module into the list, if enabled
//     * @param repositoriesModule The repositories module to register ourselves with
//     */
//    public void onModule(RepositoriesModule repositoriesModule) {
//        if (settings.getAsBoolean("swift.repository.enabled", true)) {
//            repositoriesModule.registerRepository(SwiftRepository.TYPE, SwiftRepository.class, BlobStoreIndexShardRepository.class);
//        }
//    }
    
    // overridable for tests
    protected SwiftService createStorageService(Settings settings) {
        return new SwiftService(settings);
    }
    
    @Override
    public Map<String, Repository.Factory> getRepositories(Environment env, NamedXContentRegistry namedXContentRegistry) {
        return Collections.singletonMap(SwiftRepository.TYPE,
            (metadata) -> new SwiftRepository(metadata, env.settings(), namedXContentRegistry, createStorageService(env.settings())));
    }
}
