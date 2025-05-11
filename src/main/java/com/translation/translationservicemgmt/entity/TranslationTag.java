package com.translation.translationservicemgmt.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "translation_tags", indexes = {
        @Index(name = "idx_tag", columnList = "tag"),
        @Index(name = "idx_translation_tag", columnList = "translation_id,tag")
})
public class TranslationTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translation_id", nullable = false)
    private Translation translation;

    @Column(nullable = false)
    private String tag;

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


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Translation getTranslation() { return translation; }
    public void setTranslation(Translation translation) { this.translation = translation; }
    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }


}