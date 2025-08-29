package com.example.curdUsingMongoDB.actionTriggers.impl;

import com.example.curdUsingMongoDB.actionTriggers.ActionTriggerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ActionTriggerImpl implements ActionTriggerService {

    private final WebClient webClient;
    private final String owner = "sonymystique";
    private final String repo = "crudUsingSpringReactive";
    private final String workflowFileName = "BuildAndTest.yml";
    private final String githubToken = "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";

    public ActionTriggerImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
    }
    public ActionTriggerImpl() {

        this(WebClient.builder());
    }


    @Override
    public Mono<Void> triggerWorkflow(String branch, String triggerReason) {
        String url = String.format("/repos/%s/%s/actions/workflows/%s/dispatches",
                owner, repo, workflowFileName);


        String requestBody = String.format("{\"ref\":\"%s\", \"inputs\": {\"reason\":\"%s\"}}", branch, triggerReason);

        return webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + githubToken)
                .header(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(Void.class);
    }
}
