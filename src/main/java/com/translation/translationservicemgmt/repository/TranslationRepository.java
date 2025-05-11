package com.translation.translationservicemgmt.repository;


import com.translation.translationservicemgmt.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByLocale(String locale);

    @Query("SELECT t FROM Translation t " +
            "LEFT JOIN t.tags tag " +
            "WHERE (:key IS NULL OR t.key LIKE %:key%) " +
            "AND (:locale IS NULL OR t.locale = :locale) " +
            "AND (:content IS NULL OR t.content LIKE %:content%) " +
            "AND (:tags IS NULL OR tag.tag IN :tags)")
    List<Translation> search(String key, String locale, String content, List<String> tags);
}
