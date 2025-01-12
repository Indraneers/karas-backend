package com.twistercambodia.karasbackend.karas.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AppConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String branchNameEn = "KK LUBE EXPRESS";
    @Column(nullable = false)
    private String branchNameKh = "ខេខេ សេវាកម្មប្តូរប្រេងម៉ាស៊ីន រថយន្ត និងម៉ូតូគ្រប់ប្រភេទ";
    @Column(nullable = false)
    private String logo = "/karas/logo.png";
    @Column(nullable = false)
    private String addressEn =
            """
            #205, st 598, Tom Nop village, Phnom Penh Thmey Commu, Sen Sok District, Phnom Penh Kingdom""";

    @Column(nullable = false)
    private String addressKh =
            """
            ផ្ទះលេខ 205, ផ្លូវ 598, ភូមិ ទំនប់, សង្កាត់ភ្នំពេញថ្មី, ខណ្ឌ សែនសុខ, រាជធានីភ្នំពេញ""";

    @ElementCollection
    private List<String> phoneNumbers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBranchNameEn() {
        return branchNameEn;
    }

    public void setBranchNameEn(String branchNameEn) {
        this.branchNameEn = branchNameEn;
    }

    public String getBranchNameKh() {
        return branchNameKh;
    }

    public void setBranchNameKh(String branchNameKh) {
        this.branchNameKh = branchNameKh;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getAddressKh() {
        return addressKh;
    }

    public void setAddressKh(String addressKh) {
        this.addressKh = addressKh;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
