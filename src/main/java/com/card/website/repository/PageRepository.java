package com.card.website.repository;

import com.card.website.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface PageRepository extends CrudRepository<Page, Integer> {

    List<Page> findByLang(Page.Lang lang, Sort sort);

    List<Page> findByLangAndHiddenIsFalse(Page.Lang lang, Sort sort);

    Page findByLangAndLangId(Page.Lang lang, Integer langId);

    Page findByLangAndMenuSequence(Page.Lang lang, Integer menuSequence);

    boolean existsByLangAndLangId(Page.Lang lang, Integer langId);

    // when old Sequence great then current => pushing + 1
    @Modifying
    @Query("update Page p SET p.menuSequence = p.menuSequence+1 WHERE p.menuSequence >=?1 AND p.lang = ?2")
    int pushSequenceOneStep(int mSequence, Page.Lang lang);

    @Modifying
    @Query("update Page p SET p.menuSequence = p.menuSequence+1 WHERE p.menuSequence >=?1 AND p.menuSequence < ?2  AND p.lang = ?3")
    int pushSequenceOneStep(int mSequence, int oldSequence, Page.Lang lang);

    // when old Sequence less then current => pushing - 1
    @Modifying
    @Query("update Page p SET p.menuSequence = p.menuSequence-1 WHERE p.menuSequence <=?1 AND p.lang = ?2")
    int pushSequenceOneStepToBack(int mSequence, Page.Lang lang);

    @Modifying
    @Query("update Page p SET p.menuSequence = p.menuSequence-1 WHERE p.menuSequence < ?1 AND p.menuSequence > ?2  AND p.lang = ?3")
    int pushSequenceOneStepToBack(int mSequence, int oldSequence, Page.Lang lang);
}