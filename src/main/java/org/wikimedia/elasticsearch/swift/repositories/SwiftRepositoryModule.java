package org.wikimedia.elasticsearch.swift.repositories;

import org.elasticsearch.common.inject.AbstractModule;

/**
 * Swift repository module. Binds us to things.
 */
public class SwiftRepositoryModule extends AbstractModule {
    /**
     * Do the binding.
     */
    @Override
    protected void configure() {
//        bind(Repository.class).to(SwiftRepository.class).asEagerSingleton();
//        bind(IndexShardRepository.class).to(BlobStoreIndexShardRepository.class).asEagerSingleton();
//        bind(SwiftService.class).asEagerSingleton();
    }
}
