package com.tmaruni.javaslackhelloworld;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class JavaSlackHelloWorldApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JavaSlackHelloWorldApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("EXECUTING : Sending a message to Slack!");

        Slack slack = Slack.getInstance();

        // Load an env variable
        // If the token is a bot token, it starts with `xoxb-` while if it's a user token, it starts with `xoxp-`
        String token = System.getenv("SLACK_TOKEN");

        // Initialize an API Methods client with the given token
        MethodsClient methods = slack.methods(token);

        // Build a request object
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel("#hello-world")
                .text(":wave: Hello World!")
                .build();

        // Get a response as a Java object
        ChatPostMessageResponse response = null;
        try {
            response = methods.chatPostMessage(request);

            // Check if the message sent successfully
            if (response.isOk()) {
                Message postedMessage = response.getMessage();
                log.info("Success! Message posted to slack: {}", postedMessage);
            } else {
                String errorCode = response.getError(); // e.g., "invalid_auth", "channel_not_found"
                log.error("Error while posting chat message in slack: {}", errorCode);
            }
        } catch (Exception e) {
            log.error("Error while sending a message to Slack. @error={}", e.getMessage());
        }
    }
}
