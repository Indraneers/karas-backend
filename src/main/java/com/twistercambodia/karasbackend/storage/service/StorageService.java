package com.twistercambodia.karasbackend.storage.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public StorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct  // Runs after dependency injection is complete
    public void init() {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                // Set policy if needed
            }
        } catch (Exception e) {
            throw new RuntimeException("Bucket initialization failed: " + e.getMessage());
        }
    }

    public String generatePresignedUrl(String fileName, Duration expiration) {
        try {
            return  minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName.substring(1))
                            .expiry((int)expiration.toSeconds(), TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    public static String getExtension(String filename) {
        if (filename == null) return null;

        // Regex to match the extension at the end of the string
        var matcher = java.util.regex.Pattern.compile("\\.([^.]+)$").matcher(filename);
        if (matcher.find()) {
            return matcher.group(1);  // The captured extension without the dot
        }
        return ""; // No extension found
    }

    public void uploadFile(String fileName, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(
                                    inputStream,
                                    inputStream.available(),
                                    -1
                            )
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();

            // Delete the object
            minioClient.removeObject(args);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }
}
