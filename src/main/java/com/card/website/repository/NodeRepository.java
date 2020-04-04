package com.card.website.repository;

import com.card.website.domain.Node;
import com.card.website.domain.Parent;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalTime;


public interface NodeRepository extends CrudRepository<Node, Integer> {

    Iterable<Node> findAllByParentId(String parentId);

    Iterable<Node> findAllByParent(Parent p);

    Iterable<Node> findAllByStartDailyTimeBeforeAndEndDailyTimeAfter(LocalTime time,LocalTime time2);

    Iterable<Node> findAllByStartDailyTimeBeforeAndEndDailyTimeAfterAndOpenedFalse(LocalTime time,LocalTime time2);

    Iterable<Node> findAllByStartDailyTimeAfterOrEndDailyTimeBeforeAndOpenedIsTrue(LocalTime time,LocalTime time2);

    void deleteByParent(String parentId);

    void deleteByParent(Parent parentId);

    void deleteAllByParent(String parentId);

    void deleteAllByParent(Parent parentId);

    Iterable<Parent> deleteAllByParentId(String p);


}