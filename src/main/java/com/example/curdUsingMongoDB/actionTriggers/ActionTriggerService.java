package com.example.curdUsingMongoDB.actionTriggers;

import reactor.core.publisher.Mono;

public interface ActionTriggerService {
    public Mono<Void> triggerWorkflow(String branch, String triggerReason);
}
