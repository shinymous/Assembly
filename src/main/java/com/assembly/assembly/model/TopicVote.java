package com.assembly.assembly.model;


import com.assembly.assembly.validator.CpfValidatorConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "topic_vote")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TopicVote {

    public static final Short VOTE_SIM = 1;
    public static final Short VOTE_NAO = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CpfValidatorConstraint
    @Column(name = "associate_identifier")
    private String associateIdentifier;
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="topic",referencedColumnName = "id")
    private Topic topic;
    @NotNull
    private Short vote;
}
