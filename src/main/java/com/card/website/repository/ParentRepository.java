package com.card.website.repository;

import com.card.website.domain.Parent;
import org.springframework.data.repository.CrudRepository;

public interface ParentRepository extends CrudRepository<Parent, Integer> {

    Parent findParentByParentId(String parentId);

}
