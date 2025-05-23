package com.translation.translationservicemgmt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.translation.translationservicemgmt.config.TestSecurityConfig;
import com.translation.translationservicemgmt.dto.TranslationCreateDto;
import com.translation.translationservicemgmt.dto.TranslationResponseDto;
import com.translation.translationservicemgmt.dto.TranslationUpdateDto;
import com.translation.translationservicemgmt.entity.Translation;
import com.translation.translationservicemgmt.exception.GlobalExceptionHandler;
import com.translation.translationservicemgmt.exception.TranslationNotFoundException;
import com.translation.translationservicemgmt.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TranslationController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
public class TranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    @MockBean
    private com.translation.translationservicemgmt.config.JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private TranslationResponseDto translation;
    private TranslationCreateDto createDto;
    private TranslationUpdateDto updateDto;
    private Translation translationObject;
    @BeforeEach
    void setUp() {
        translation = new TranslationResponseDto();
        translation.setId(1L);
        translation.setKey("test.key");
        translation.setLocale("en");
        translation.setContent("Test content");

        createDto = new TranslationCreateDto();
        createDto.setKey("test.key");
        createDto.setLocale("en");
        createDto.setContent("Test content");
        createDto.setTags(Collections.singletonList("mobile"));

        updateDto = new TranslationUpdateDto();
        updateDto.setContent("Updated content");
        updateDto.setTags(Collections.singletonList("desktop"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateTranslation() throws Exception {
        when(translationService.create(any(TranslationCreateDto.class))).thenReturn(translation);

        mockMvc.perform(post("/api/translations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.key").value("test.key"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateTranslation() throws Exception {
        when(translationService.findById(1L)).thenReturn(translationObject);
        when(translationService.update(any(Translation.class), any(TranslationUpdateDto.class))).thenReturn(translation);

        mockMvc.perform(put("/api/translations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("Test content"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateTranslationNotFound() throws Exception {
        when(translationService.findById(999L)).thenThrow(new TranslationNotFoundException(999L));

        mockMvc.perform(put("/api/translations/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Translation not found with id: 999"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTranslation() throws Exception {
        when(translationService.findById(1L)).thenReturn(translationObject);

        mockMvc.perform(get("/api/translations/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.key").value("test.key"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetTranslationNotFound() throws Exception {
        when(translationService.findById(999L)).thenThrow(new TranslationNotFoundException(999L));

        mockMvc.perform(get("/api/translations/999")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Translation not found with id: 999"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSearchTranslations() throws Exception {
        when(translationService.search(any())).thenReturn(Collections.<TranslationResponseDto>singletonList(translation));

        mockMvc.perform(get("/api/translations/search?locale=en")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].key").value("test.key"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testExportTranslations() throws Exception {
        when(translationService.export("en")).thenReturn(Collections.singletonMap("test.key", "Test content"));

        mockMvc.perform(get("/api/translations/export/en")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['test.key']").value("Test content"));
    }
}