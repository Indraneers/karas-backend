package com.twistercambodia.karasbackend.storage.service;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService {
    private final MinioClient minioClient;
    private final Tika tika;
    private volatile boolean initialized = false; // volatile for thread safety

    @Value("${minio.bucket.name}")
    private String bucketName;

    public StorageService(@Lazy MinioClient minioClient) {
        this.minioClient = minioClient;
        this.tika = new Tika();
    }

    private synchronized void ensureBucketExists() {
        if (initialized) return; // Already initialized, skip
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Bucket initialization failed: " + e.getMessage());
        }
    }

    public String generatePresignedUrl(String fileName, Duration expiration) {
        ensureBucketExists(); // Lazy init on first actual use
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName.substring(1))
                            .expiry((int) expiration.toSeconds(), TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    public void uploadFile(String fileName, InputStream inputStream) {
        ensureBucketExists(); // Lazy init on first actual use
        try {
            byte[] data = inputStream.readAllBytes();
            String contentType = tika.detect(new ByteArrayInputStream(data), fileName);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        ensureBucketExists(); // Lazy init on first actual use
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
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
}