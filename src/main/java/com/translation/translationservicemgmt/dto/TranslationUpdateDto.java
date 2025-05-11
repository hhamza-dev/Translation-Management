package com.translation.translationservicemgmt.dto;

import jakarta.validation.constraints.NotBlank;


import java.util.List;

public class TranslationUpdateDto {
    private String key;
    @NotBlank
    private String content;
    private List<String> tags;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public @NotBlank String getContent() {
        return content;
    }

    public void setContent(@NotBlank String content) {
        this.content = content;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
