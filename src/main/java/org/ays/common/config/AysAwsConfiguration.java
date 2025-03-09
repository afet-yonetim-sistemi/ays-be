package org.ays.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisClient;

@Configuration
@Profile("aws")
class AysAwsConfiguration {

    @Value("${aws.credentials.access-key-id}")
    private String accessKey;

    @Value("${aws.credentials.secret-access-key}")
    private String secretKey;

    @Value("${aws.region}")
    private String region;

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
