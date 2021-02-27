package com.assembly.assembly.repository;

import com.assembly.assembly.model.TopicVote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TopicVoteRepository extends CrudRepository<TopicVote, Long> {

    Optional<TopicVote> findByAssociateIdentifierAndTopic_Id(String associateIdentifier, Long topicId);
    List<TopicVote> findAllByTopic_Id(Long topicId);
}
