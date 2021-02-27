package com.assembly.assembly.controller;

import com.assembly.assembly.dto.ResponseDTO;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(headers = {"Accept=application/json"}, produces = "application/json")
@ApiResponses(value = {@ApiResponse(code = 400, message = "Default Error", response = ResponseDTO.class)})
public interface AssemblyRestController {
}
