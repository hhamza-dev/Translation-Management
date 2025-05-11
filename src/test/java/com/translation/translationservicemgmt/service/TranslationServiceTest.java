package com.translation.translationservicemgmt.service;



import com.translation.translationservicemgmt.dto.TranslationCreateDto;
import com.translation.translationservicemgmt.dto.TranslationResponseDto;
import com.translation.translationservicemgmt.dto.TranslationSearchDto;
import com.translation.translationservicemgmt.dto.TranslationUpdateDto;
import com.translation.translationservicemgmt.entity.Translation;
import com.translation.translationservicemgmt.entity.TranslationTag;
import com.translation.translationservicemgmt.exception.TranslationNotFoundException;
import com.translation.translationservicemgmt.repository.TranslationRepository;
import com.translation.translationservicemgmt.repository.TranslationTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;

    @Mock
    private TranslationTagRepository tagRepository;

    @InjectMocks
    private TranslationService translationService;

    private Translation translation;
    private TranslationCreateDto createDto;
    private TranslationUpdateDto updateDto;
    private TranslationSearchDto searchDto;

    @BeforeEach
    void setUp() {
        translation = new Translation();
        translation.setTranslation_id(1L);
        translation.setKey("test.key");
        translation.setLocale("en");
        translation.setContent("Test content");

        createDto = new TranslationCreateDto();
        createDto.setKey("test.key");
        createDto.setLocale("en");
        createDto.setContent("Test content");
        createDto.setTags(Arrays.asList("mobile", "web"));

        updateDto = new TranslationUpdateDto();
        updateDto.setContent("Updated content");
        updateDto.setTags(Collections.singletonList("desktop"));

        searchDto = new TranslationSearchDto();
        searchDto.setLocale("en");
    }

    @Test
    void testCreateTranslationWithTags() {
        when(translationRepository.save(any(Translation.class))).thenReturn(translation);
        when(tagRepository.saveAll(any())).thenReturn(Collections.emptyList());

        TranslationResponseDto result = translationService.create(createDto);

        assertNotNull(result);
        assertEquals("test.key", result.getKey());
        assertEquals("en", result.getLocale());
        assertEquals("Test content", result.getContent());
        verify(translationRepository, times(1)).save(any(Translation.class));
        verify(tagRepository, times(1)).saveAll(any());
    }

    @Test
    void testCreateTranslationWithoutTags() {
        createDto.setTags(null);
        when(translationRepository.save(any(Translation.class))).thenReturn(translation);

        TranslationResponseDto result = translationService.create(createDto);

        assertNotNull(result);
        assertEquals("test.key", result.getKey());
        assertNull(result.getTags());
        verify(translationRepository, times(1)).save(any(Translation.class));
        verify(tagRepository, never()).saveAll(any());
    }

    @Test
    void testUpdateTranslation() {
        Translation existing = new Translation();
        existing.setTranslation_id(1L);
        existing.setKey("test.key");
        existing.setLocale("en");
        existing.setContent("Old content");
        existing.setTags(Collections.singletonList(new TranslationTag()));

        Translation updatedTranslation = new Translation();
        updatedTranslation.setTranslation_id(1L);
        updatedTranslation.setKey("test.key");
        updatedTranslation.setLocale("en");
        updatedTranslation.setContent("Updated content");

        when(translationRepository.save(any(Translation.class))).thenReturn(updatedTranslation);
        when(tagRepository.saveAll(any())).thenReturn(Collections.emptyList());

        TranslationResponseDto result = translationService.update(existing, updateDto);

        assertNotNull(result);
        assertEquals("test.key", result.getKey());
        assertEquals("Updated content", result.getContent());
        verify(tagRepository, times(1)).deleteAll(any());
        verify(tagRepository, times(1)).saveAll(any());
        verify(translationRepository, times(1)).save(any(Translation.class));
    }

    @Test
    void testUpdateTranslationWithoutTags() {
        Translation existing = new Translation();
        existing.setTranslation_id(1L);
        existing.setKey("test.key");
        existing.setLocale("en");
        existing.setContent("Old content");

        Translation updatedTranslation = new Translation();
        updatedTranslation.setTranslation_id(1L);
        updatedTranslation.setKey("test.key");
        updatedTranslation.setLocale("en");
        updatedTranslation.setContent("Updated content");

        updateDto.setTags(null);
        when(translationRepository.save(any(Translation.class))).thenReturn(updatedTranslation);

        TranslationResponseDto result = translationService.update(existing, updateDto);

        assertNotNull(result);
        assertEquals("Updated content", result.getContent());
        verify(tagRepository, never()).deleteAll(any());
        verify(tagRepository, never()).saveAll(any());
        verify(translationRepository, times(1)).save(any(Translation.class));
    }

    @Test
    void testFindByIdSuccess() {
        when(translationRepository.findById(1L)).thenReturn(Optional.of(translation));

        Translation result = translationService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getTranslation_id());
        assertEquals("test.key", result.getKey());
        verify(translationRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(translationRepository.findById(1L)).thenReturn(Optional.empty());

        TranslationNotFoundException exception = assertThrows(TranslationNotFoundException.class, () -> translationService.findById(1L));
        assertEquals("Translation not found with id: 1", exception.getMessage());
        verify(translationRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchTranslations() {
        when(translationRepository.search(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(translation));

        List<TranslationResponseDto> result = translationService.search(searchDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test.key", result.get(0).getKey());
        verify(translationRepository, times(1)).search(any(), any(), any(), any());
    }

    @Test
    void testSearchTranslationsWithAllCriteria() {
        searchDto.setKey("test");
        searchDto.setContent("content");
        searchDto.setTags(Arrays.asList("mobile", "web"));

        when(translationRepository.search(any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(translation));

        List<TranslationResponseDto> result = translationService.search(searchDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(translationRepository, times(1)).search("test", "en", "content", Arrays.asList("mobile", "web"));
    }

    @Test
    void testExportTranslations() {
        when(translationRepository.findByLocale("en"))
                .thenReturn(Collections.singletonList(translation));

        Map<String, String> result = translationService.export("en");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test content", result.get("test.key"));
        verify(translationRepository, times(1)).findByLocale("en");
    }

    @Test
    void testExportTranslationsEmpty() {
        when(translationRepository.findByLocale("en")).thenReturn(Collections.emptyList());

        Map<String, String> result = translationService.export("en");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(translationRepository, times(1)).findByLocale("en");
    }
}

