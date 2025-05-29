package com.twistercambodia.karasbackend.audit.dto;

import com.twistercambodia.karasbackend.audit.entity.Audit;
import com.twistercambodia.karasbackend.audit.entity.HttpMethod;
import com.twistercambodia.karasbackend.audit.entity.ServiceEnum;
import com.twistercambodia.karasbackend.auth.entity.User;

public class AuditDTO {
    private String id;
    private String timestamp;
    private String name;
    private ServiceEnum service;
    private HttpMethod httpMethod;
    private String requestUrl;
    private String oldValue;;
    private String newValue;
    private User user;

    public AuditDTO(Audit audit) {
        this.id = audit.getId();
        this.timestamp = audit.getTimestamp().toString();
        this.name = audit.getName();
        this.httpMethod = audit.getHttpMethod();
        this.requestUrl = audit.getRequestUrl();
        this.service = audit.getService();
        this.oldValue = audit.getOldValue();
        this.newValue = audit.getNewValue();
        this.user = audit.getUser();
    }

    public AuditDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
}
