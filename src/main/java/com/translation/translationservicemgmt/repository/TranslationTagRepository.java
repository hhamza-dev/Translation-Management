package com.translation.translationservicemgmt.repository;


import com.translation.translationservicemgmt.entity.TranslationTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationTagRepository extends JpaRepository<TranslationTag, Long> {
}
