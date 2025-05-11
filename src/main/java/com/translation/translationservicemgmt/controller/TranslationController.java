package com.translation.translationservicemgmt.controller;

import com.translation.translationservicemgmt.dto.TranslationCreateDto;
import com.translation.translationservicemgmt.dto.TranslationResponseDto;
import com.translation.translationservicemgmt.dto.TranslationSearchDto;
import com.translation.translationservicemgmt.dto.TranslationUpdateDto;
import com.translation.translationservicemgmt.entity.Translation;
import com.translation.translationservicemgmt.service.TranslationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {
    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    public ResponseEntity<TranslationResponseDto> create(@Valid @RequestBody TranslationCreateDto dto) {
        TranslationResponseDto translation = translationService.create(dto);
        return new ResponseEntity<>(translation, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TranslationResponseDto> update(@PathVariable Long id, @Valid @RequestBody TranslationUpdateDto dto) {
        Translation translation = translationService.findById(id);
        TranslationResponseDto updated = translationService.update(translation, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TranslationResponseDto> get(@PathVariable Long id) {
        TranslationResponseDto translation = translationService.getById(id);
        return ResponseEntity.ok(translation);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TranslationResponseDto>> search(@ModelAttribute TranslationSearchDto dto) {
        List<TranslationResponseDto> translations = translationService.search(dto);
        return ResponseEntity.ok(translations);
    }

    @GetMapping("/export/{locale}")
    public ResponseEntity<Map<String, String>> export(@PathVariable String locale) {
        Map<String, String> translations = translationService.export(locale);
        return ResponseEntity.ok(translations);
    }
}
