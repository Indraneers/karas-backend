package com.twistercambodia.karasbackend.audit.entity;

import com.twistercambodia.karasbackend.auth.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL
    private ServiceEnum service;

    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL
    private HttpMethod httpMethod;

    @Column(nullable = false)
    private String requestUrl;

    @Column
    private String oldValue;

    @Column
    private String newValue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceEnum getService() {
        return service;
    }

    public void setService(ServiceEnum service) {
        this.service = service;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "newValue='" + newValue + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", httpMethod=" + httpMethod +
                ", service=" + service +
                ", name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", id='" + id + '\'' +
                ", user=" + user +
                '}';
    }
}
