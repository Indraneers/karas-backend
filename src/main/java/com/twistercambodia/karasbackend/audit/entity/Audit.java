package com.twistercambodia.karasbackend.audit.entity;

import com.twistercambodia.karasbackend.auth.entity.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Instant;

@Entity
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String resourceName;

    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL
    private ServiceEnum service;

    @Enumerated(EnumType.STRING)  // or EnumType.ORDINAL
    private HttpMethod httpMethod;

    @Column(nullable = false)
    private String requestUrl;

    @Column(length = 10000)
    private String oldValue;

    @Column(length = 10000)
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
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
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", name='" + name + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", service=" + service +
                ", httpMethod=" + httpMethod +
                ", requestUrl='" + requestUrl + '\'' +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                ", user=" + user +
                '}';
    }
}
