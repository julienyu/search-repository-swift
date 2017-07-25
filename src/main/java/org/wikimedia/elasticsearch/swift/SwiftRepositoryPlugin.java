package org.wikimedia.elasticsearch.swift;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.env.Environment;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.plugins.RepositoryPlugin;
import org.elasticsearch.repositories.Repository;
import org.wikimedia.elasticsearch.swift.repositories.SwiftRepository;
import org.wikimedia.elasticsearch.swift.repositories.SwiftService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Our base plugin stuff.
 */
public class SwiftRepositoryPlugin extends Plugin implements RepositoryPlugin {
    
    // overridable for tests
    protected SwiftService createStorageService(Settings settings) {
        return new SwiftService(settings);
    }
    
    @Override
    public Map<String, Repository.Factory> getRepositories(Environment env, NamedXContentRegistry namedXContentRegistry) {
        return Collections.singletonMap(SwiftRepository.TYPE,
            (metadata) -> new SwiftRepository(metadata, env.settings(), namedXContentRegistry, createStorageService(env.settings())));
    }
    
    @Override
    public List<String> getSettingsFilter() {
        return Arrays.asList(
            SwiftRepository.Swift.PASSWORD_SETTING.getKey());
    }
}
