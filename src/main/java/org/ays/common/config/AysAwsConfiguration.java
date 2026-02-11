package org.ays.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;

/**
 * <p>
 * Configuration class for AWS Kinesis integration within the application.
 * </p>
 * <p>
 * This configuration is activated when the {@code aws} profile is active and
 * defines the necessary beans and settings required to interact with AWS
 * services.
 * </p>
 * <p>
 * It includes the configuration of the {@link KinesisClient} using AWS
 * credentials and region information provided via the application
 * configuration.
 * </p>
 */
@Configuration
@Profile("aws")
class AysAwsConfiguration {

    @Value("${aws.credentials.access-key-id}")
    private String accessKey;

    @Value("${aws.credentials.secret-access-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

    /**
     * <p>
     * Creates and configures an instance of {@link KinesisClient}.
     * </p>
     * <p>
     * The client is initialized using AWS credentials and region
     * information defined in the application configuration.
     * </p>
     * @return a configured {@link KinesisClient} instance
     */
    @Bean
    KinesisClient kinesisClient() {

        final AwsBasicCredentials awsCredentials = AwsBasicCredentials
                .create(this.accessKey, this.secretKey);

        return KinesisClient.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .region(Region.of(this.region))
                .build();
    }

}
