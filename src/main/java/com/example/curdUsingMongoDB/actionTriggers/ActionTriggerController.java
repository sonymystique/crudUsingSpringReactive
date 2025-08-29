package com.example.curdUsingMongoDB.actionTriggers.impl;

import com.example.curdUsingMongoDB.actionTriggers.ActionTriggerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@RestController
@AllArgsConstructor
public class ActionTriggerController {

    private final ActionTriggerService actionTriggerService;



    @PostMapping("/trigger-build")
    public Mono<ResponseEntity<String>> triggerGithubAction(
            @RequestParam(defaultValue = "master") String branch,
            @RequestParam(defaultValue = "Manual trigger from REST API") String reason) {

        return actionTriggerService.triggerWorkflow(branch, reason)
                .thenReturn(ResponseEntity.ok("GitHub Action triggered successfully."))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to trigger GitHub Action: " + e.getMessage())));
    }
}
