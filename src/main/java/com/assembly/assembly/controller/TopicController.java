package com.assembly.assembly.controller;

import com.assembly.assembly.dto.ResponseDTO;
import com.assembly.assembly.dto.TopicDTO;
import com.assembly.assembly.dto.VoteDTO;
import com.assembly.assembly.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/v1/topic")
@Api(tags = "Topic")
public class TopicController implements AssemblyRestController{

    private final TopicService topicService;

    @PostMapping
    @ApiOperation(value = "Create a topic")
    public ResponseEntity<ResponseDTO<?>> createTopic(@Valid @RequestBody TopicDTO topicDTO) {
        return new ResponseEntity<>(topicService.saveTopic(topicDTO), HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "Find topic by name")
    public ResponseEntity<ResponseDTO<?>> findTopicByName(@RequestParam String name){
        return new ResponseEntity<>(topicService.findAllValidTopicByName(name), HttpStatus.OK);
    }

    @PostMapping("/{id}/vote")
    @ApiOperation(value = "Vote on a topic")
    public ResponseEntity<ResponseDTO<?>> vote(@PathVariable Long id, @Valid @RequestBody VoteDTO voteDTO) {
        return new ResponseEntity<>(topicService.vote(id, voteDTO), HttpStatus.OK);
    }
}


