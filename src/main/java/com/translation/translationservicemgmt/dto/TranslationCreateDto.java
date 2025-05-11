package com.translation.translationservicemgmt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


import java.util.List;


public class TranslationCreateDto {
    @NotBlank
    private String key;

    @NotBlank
    @Size(max = 10)
    private String locale;

    @NotBlank
    private String content;

    private List<String> tags;

    public @NotBlank String getKey() {
        return key;
    }

    public void setKey(@NotBlank String key) {
        this.key = key;
    }

    public @NotBlank @Size(max = 10) String getLocale() {
        return locale;
    }

    public void setLocale(@NotBlank @Size(max = 10) String locale) {
        this.locale = locale;
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
