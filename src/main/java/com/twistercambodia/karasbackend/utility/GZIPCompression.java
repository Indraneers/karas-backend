package com.twistercambodia.karasbackend.utility;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GZIPCompression {

    public static String compress(final String str) throws IOException {
        if (str == null || str.isEmpty()) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
        }
        return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
    }

    public static String decompress(final String compressedStr) throws IOException {
        if (compressedStr == null || compressedStr.isEmpty()) {
            return "";
        }

        try {
            byte[] compressedBytes = Base64.getDecoder().decode(compressedStr);
            return decompressBytes(compressedBytes);
        } catch (IllegalArgumentException e) {
            // If not Base64, try direct decompression (backward compatibility)
            try {
                return decompressBytes(compressedStr.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e2) {
                throw new IOException("Failed to decompress: input is neither valid Base64 nor compressed data");
            }
        }
    }

    private static String decompressBytes(final byte[] compressedBytes) throws IOException {
        if (compressedBytes == null || compressedBytes.length == 0) {
            return "";
        }

        // Check if data is actually compressed
        if (!isCompressed(compressedBytes)) {
            return new String(compressedBytes, StandardCharsets.UTF_8);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedBytes));
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            return byteArrayOutputStream.toString(StandardCharsets.UTF_8.name());
        }
    }

    public static boolean isCompressed(final byte[] compressed) {
        if (compressed == null || compressed.length < 2) {
            return false;
        }
        return (compressed[0] == (byte) GZIPInputStream.GZIP_MAGIC) &&
                (compressed[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8));
    }
}