package com.card.website.repository;

import com.card.website.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Transactional
public interface PageRepository extends CrudRepository<Page, Integer> {

    List<Page> findByLang(Page.Lang lang, Sort sort);

    List<Page> findByLangAndHiddenIsFalse(Page.Lang lang, Sort sort);

     Page findByLangAndLangId(Page.Lang lang, Integer langId);

     boolean existsByLangAndLangId(Page.Lang lang, Integer langId);


    @Modifying
    @Query("update Page p SET p.menuSequence = p.menuSequence+1 WHERE p.menuSequence >=?1 AND p.lang = ?2")
    int pushSequenceOneStep(int mSequence, Page.Lang lang);

}