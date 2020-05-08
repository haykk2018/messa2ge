package com.card.website.repository;

import com.card.website.domain.Regime;
import org.springframework.data.repository.CrudRepository;

public interface RegimeRepository extends CrudRepository<Regime, Integer> {

    Regime findRegimeByRegimeId(String regimeId);

    Iterable<Regime> findAllByParentId(String parentId);

}
