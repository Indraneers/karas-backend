package com.twistercambodia.karasbackend.karas.controller;

import com.twistercambodia.karasbackend.karas.entity.AppConfig;
import com.twistercambodia.karasbackend.karas.repository.AppConfigRepository;
import com.twistercambodia.karasbackend.storage.service.StorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/config")
public class ConfigController {
    private AppConfigRepository appConfigRepository;
    private StorageService storageService;

    public ConfigController(AppConfigRepository appConfigRepository, StorageService storageService) {
        this.appConfigRepository = appConfigRepository;
        this.storageService = storageService;
    }

    @GetMapping
    public AppConfig getAppConfig() {
        List<AppConfig> appConfigs = appConfigRepository.findAll();

        if (appConfigs.isEmpty()) {
            createDefaultAppConfig();
            return appConfigRepository.findAll().get(0);
        }

        return appConfigs.get(0);
    }

    @PutMapping
    public AppConfig setAppConfig(
            @RequestPart("data") AppConfig karasAppConfig,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {

        List<AppConfig> appConfigs = appConfigRepository.findAll();

        if (appConfigs.isEmpty()) {
            createDefaultAppConfig();
        }

        if (file != null) {
            storageService.uploadFile(
                    "/karas/logo.png",
                    file.getInputStream(),
                    "image/png"
            );
        }

        return this.appConfigRepository.save(karasAppConfig);
    }

    private void createDefaultAppConfig() {
        AppConfig defaultAppConfig = new AppConfig();
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("010 898 009");
        defaultAppConfig.setPhoneNumbers(phoneNumbers);
        this.appConfigRepository.save(defaultAppConfig);
    }
}
