package ru.otus.operations.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface OperDateMessage {
    @Gateway(requestChannel = "businessProcessExec", replyChannel = "resultChannel")
    void businessProcessExec(String sysName);
}
