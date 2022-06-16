package com.ecom.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Application constants.
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_TEST = "test";

    // Kafka
    public static final String TOPIC_NAME = "PRODUCT-EVENT";
    public static final String SCHEMA_REGISTRY_URL = "schema.registry.url";

    public static final String ORIGIN_URL = "http://localhost:3001";
}
