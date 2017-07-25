package org.wikimedia.elasticsearch.swift.repositories;

import org.elasticsearch.cluster.metadata.RepositoryMetaData;
import org.elasticsearch.common.blobstore.BlobPath;
import org.elasticsearch.common.blobstore.BlobStore;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Setting;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.repositories.RepositoryException;
import org.elasticsearch.repositories.blobstore.BlobStoreRepository;
import org.javaswift.joss.model.Account;
import org.wikimedia.elasticsearch.swift.repositories.blobstore.SwiftBlobStore;

/**
 * The blob store repository. A glorified settings wrapper.
 */
public class SwiftRepository extends BlobStoreRepository {
    // The internal "type" for Elasticsearch
    public final static String TYPE = "swift";
    
    public interface Swift {
        Setting<String> CONTAINER_SETTING = Setting.simpleString("swift_container");
        Setting<String> URL_SETTING = Setting.simpleString("swift_url");
        Setting<String> AUTHMETHOD_SETTING = Setting.simpleString("swift_authmethod");
        Setting<String> PASSWORD_SETTING = Setting.simpleString("swift_password");
        Setting<String> TENANTNAME_SETTING = Setting.simpleString("swift_tenantname");
        Setting<String> USERNAME_SETTING = Setting.simpleString("swift_username");
        Setting<String> PREFERRED_REGION_SETTING = Setting.simpleString("swift_preferred_region");
        Setting<ByteSizeValue> CHUNK_SIZE_SETTING = Setting.byteSizeSetting("chunk_size", new ByteSizeValue(5, ByteSizeUnit.GB));
        Setting<Boolean> COMPRESS_SETTING = Setting.boolSetting("compress", false);
    }

    // Our blob store instance
    private final SwiftBlobStore blobStore;

    // Base path for blobs
    private final BlobPath basePath;

    // Chunk size.
    private final ByteSizeValue chunkSize;

    // Are we compressing our snapshots?
    private final boolean compress;

    /**
     * Constructs new BlobStoreRepository
     * 
     * @param metadata
     *            repository meta data
     * @param settings
     *            global settings
     * @param namedXContentRegistry
     *            an instance of NamedXContentRegistry
     * @param swiftService
     *            an instance of SwiftService
     */
    @Inject
    public SwiftRepository(RepositoryMetaData metadata, Settings settings,
            NamedXContentRegistry namedXContentRegistry, SwiftService swiftService) {
        super(metadata, settings, namedXContentRegistry);
        
        String url = Swift.URL_SETTING.get(metadata.settings());
        if (url == null) {
            throw new RepositoryException(metadata.name(), "No url defined for swift repository");
        }

        String container = Swift.CONTAINER_SETTING.get(metadata.settings());
        if (container == null) {
            throw new RepositoryException(metadata.name(), "No container defined for swift repository");
        }

        String username = Swift.USERNAME_SETTING.get(metadata.settings());
        String password = Swift.PASSWORD_SETTING.get(metadata.settings());
        String tenantName = Swift.TENANTNAME_SETTING.get(metadata.settings());
        String authMethod = Swift.AUTHMETHOD_SETTING.get(metadata.settings());
        String preferredRegion = Swift.PREFERRED_REGION_SETTING.get(metadata.settings());
        Account account = SwiftAccountFactory.createAccount(swiftService, url, username, password, tenantName, authMethod, preferredRegion);

        blobStore = new SwiftBlobStore(settings, account, container);
        this.chunkSize = Swift.CHUNK_SIZE_SETTING.get(metadata.settings());
        this.compress = Swift.COMPRESS_SETTING.get(metadata.settings());
        this.basePath = BlobPath.cleanPath();
    }

    /**
     * Get the blob store
     */
    @Override
    protected BlobStore blobStore() {
        return blobStore;
    }

    /**
     * Get the base blob path
     */
    @Override
    protected BlobPath basePath() {
        return basePath;
    }

    /**
     * Get the chunk size
     */
    @Override
    protected ByteSizeValue chunkSize() {
        return chunkSize;
    }

    /**
     * Are we compressing our snapshots?
     */
    @Override
    protected boolean isCompress() {
        return compress;
    }
}
