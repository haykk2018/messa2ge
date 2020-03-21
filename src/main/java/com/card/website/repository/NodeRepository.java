package com.card.website.repository;

import com.card.website.domain.Node;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalTime;


public interface NodeRepository extends CrudRepository<Node, Integer> {


    Iterable<Node> findAllByStartDailyTimeBeforeAndEndDailyTimeAfter(LocalTime time,LocalTime time2);

    Iterable<Node> findAllByStartDailyTimeBeforeAndEndDailyTimeAfterAndOpenedFalse(LocalTime time,LocalTime time2);

    Iterable<Node> findAllByStartDailyTimeAfterOrEndDailyTimeBeforeAndOpenedIsTrue(LocalTime time,LocalTime time2);


}