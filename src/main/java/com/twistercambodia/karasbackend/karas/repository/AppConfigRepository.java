package com.twistercambodia.karasbackend.karas.repository;

import com.twistercambodia.karasbackend.karas.entity.AppConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AppConfigRepository extends JpaRepository<AppConfig, Long> {
}
