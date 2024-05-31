package com.sivalabs.aidemo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
class ChatController {

    private final ChatClient chatClient;

    ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/chat")
    Map<String,String> chat(@RequestParam String question) {
        var response = chatClient.prompt().user(question).call().content();
        return Map.of("question", question, "answer", response);
    }

    @GetMapping("/ai/chat-with-prompt")
    Map<String,String> chatWithPrompt(@RequestParam String subject) {
        PromptTemplate promptTemplate = new PromptTemplate("Tell me a joke about {subject}");
        Prompt prompt = promptTemplate.create(Map.of("subject", subject));
        String answer = chatClient.prompt(prompt).call().content();
        return Map.of( "answer", answer);
    }

    @GetMapping("/ai/chat-with-system-prompt")
    Map<String,String> chatWithSystemPrompt(@RequestParam String subject) {
        SystemMessage systemMessage = new SystemMessage("You are a sarcastic and funny chatbot");
        UserMessage userMessage = new UserMessage("Tell me a joke about " + subject);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        String answer = chatClient.prompt(prompt).call().content();
        return Map.of( "answer", answer);
    }

}

record JokeResponse(String response) {}
