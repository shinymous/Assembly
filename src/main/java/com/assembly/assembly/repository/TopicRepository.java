package com.assembly.assembly.repository;

import com.assembly.assembly.model.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TopicRepository extends CrudRepository<Topic, Long> {
    @Query(value = "select t from Topic t where t.name like %?1% " +
            "and t.endDate > ?2 " +
            "and t.closed is false ")
    List<Topic> findAllValidByLikeName(String name, LocalDateTime now);

    @Query(value = "select t from Topic t where t.endDate > ?1 and t.endDate < ?2 and t.closed is false")
    List<Topic> findAllOpenTopicByRangeDate(LocalDateTime start, LocalDateTime end);
}
