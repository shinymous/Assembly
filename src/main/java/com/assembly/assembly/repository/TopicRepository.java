package com.assembly.assembly.repository;

import com.assembly.assembly.model.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TopicRepository extends CrudRepository<Topic, Long> {

    @Query(value = "select t from Topic t where t.name like %?1% and t.endDate > CURRENT_TIMESTAMP ")
    List<Topic> findAllValidByLikeName(String name);
}
