package com.translation.translationservicemgmt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "translations", indexes = {
        @Index(name = "idx_key", columnList = "translation_key"),
        @Index(name = "idx_locale", columnList = "locale"),
        @Index(name = "idx_key_locale", columnList = "translation_key,locale")
})
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translation_id")
    private Long translation_id;

    @Column(name = "translation_key", nullable = false)
    private String key;

    @Column(name = "locale" ,nullable = false, length = 10)
    private String locale;

    @Column(name =  "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "translation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TranslationTag> tags;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getTranslation_id() {
        return translation_id;
    }

    public void setTranslation_id(Long id) {
        this.translation_id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TranslationTag> getTags() {
        return tags;
    }

    public void setTags(List<TranslationTag> tags) {
        this.tags = tags;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


}
