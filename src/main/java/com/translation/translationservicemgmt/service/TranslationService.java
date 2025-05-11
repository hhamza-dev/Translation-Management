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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TranslationService {
    private final TranslationRepository translationRepository;
    private final TranslationTagRepository tagRepository;

    public TranslationService(TranslationRepository translationRepository, TranslationTagRepository tagRepository) {
        this.translationRepository = translationRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional
    @CacheEvict(value = "translations", key = "#dto.locale")
    public TranslationResponseDto create(TranslationCreateDto dto) {
        Translation translation = new Translation();
        translation.setKey(dto.getKey());
        translation.setLocale(dto.getLocale());
        translation.setContent(dto.getContent());

        Translation saved = translationRepository.save(translation);

        if (dto.getTags() != null) {
            List<TranslationTag> tags = dto.getTags().stream()
                    .map(tag -> {
                        TranslationTag translationTag = new TranslationTag();
                        translationTag.setTranslation(saved);
                        translationTag.setTag(tag);
                        return translationTag;
                    })
                    .collect(Collectors.toList());
            tagRepository.saveAll(tags);
            saved.setTags(tags);
        }

        TranslationResponseDto responseDto = new TranslationResponseDto();
        responseDto.setId(saved.getTranslation_id());
        responseDto.setKey(saved.getKey());
        responseDto.setLocale(saved.getLocale());
        responseDto.setContent(saved.getContent());
        responseDto.setTags(saved.getTags().stream().map(TranslationTag::getTag).collect(Collectors.toList()));
        return responseDto;
    }

    @Transactional
    @CacheEvict(value = "translations", key = "#translation.locale")
    public TranslationResponseDto update(Translation translation, TranslationUpdateDto dto) {
        if (dto.getKey() != null) {
            translation.setKey(dto.getKey());
        }
        if (dto.getContent() != null) {
            translation.setContent(dto.getContent());
        }

        if (dto.getTags() != null) {
            tagRepository.deleteAll(translation.getTags());
            List<TranslationTag> tags = dto.getTags().stream()
                    .map(tag -> {
                        TranslationTag translationTag = new TranslationTag();
                        translationTag.setTranslation(translation);
                        translationTag.setTag(tag);
                        return translationTag;
                    })
                    .collect(Collectors.toList());
            tagRepository.saveAll(tags);
            translation.setTags(tags);
        }
        TranslationResponseDto responseDto = new TranslationResponseDto();
        responseDto.setId(translation.getTranslation_id());
        responseDto.setKey(translation.getKey());
        responseDto.setLocale(translation.getLocale());
        responseDto.setContent(translation.getContent());
        responseDto.setTags(translation.getTags().stream().map(TranslationTag::getTag).collect(Collectors.toList()));
        return responseDto;

    }

    public TranslationResponseDto getById(Long id) {
        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new TranslationNotFoundException(id));
        TranslationResponseDto responseDto = new TranslationResponseDto();
        responseDto.setId(translation.getTranslation_id());
        responseDto.setKey(translation.getKey());
        responseDto.setLocale(translation.getLocale());
        responseDto.setContent(translation.getContent());
        responseDto.setTags(translation.getTags().stream().map(TranslationTag::getTag).collect(Collectors.toList()));
        return responseDto;
    }


    public Translation findById(Long id) {
        Translation translation = translationRepository.findById(id)
                .orElseThrow(() -> new TranslationNotFoundException(id));
        return translation;
    }

    public List<TranslationResponseDto> search(TranslationSearchDto dto) {
        List<Translation> translations = translationRepository.search(
                dto.getKey(),
                dto.getLocale(),
                dto.getContent(),
                dto.getTags()
        );
        return translations.stream().map(translation -> {
            TranslationResponseDto responseDto = new TranslationResponseDto();
            responseDto.setId(translation.getTranslation_id());
            responseDto.setKey(translation.getKey());
            responseDto.setLocale(translation.getLocale());
            responseDto.setContent(translation.getContent());
            responseDto.setTags(translation.getTags().stream().map(TranslationTag::getTag).collect(Collectors.toList()));
            return responseDto;
        }).collect(Collectors.toList());
    }

    @Cacheable(value = "translations", key = "#locale")
    public Map<String, String> export(String locale) {
        return translationRepository.findByLocale(locale)
                .stream()
                .collect(Collectors.toMap(
                        Translation::getKey,
                        Translation::getContent,
                        (existing, replacement) -> replacement
                ));
    }
}
