package com.sivalabs.springai.showcase.rag;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rag/web-qa")
class WebUrlsRagController {
    private static final Logger log = LoggerFactory.getLogger(WebUrlsRagController.class);

    private final ChatClient chatClient;

    public WebUrlsRagController(ChatClient.Builder builder,
                                VectorStore vectorStore) {
        this.chatClient = builder
        // .defaultSystem("你是一个百科全书老师,回答问题时优先从向量数据库中寻找答案,没有匹配的答案时,再自己回答")
        //         .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore, SearchRequest.defaults()))
                .defaultSystem("你是一个专业的助手。在回答问题时，请严格遵循以下规则：\n" +
                        "1. 优先使用向量数据库中的相关内容来回答问题\n" +
                        "2. 如果向量数据库中找到相关内容，请基于这些内容构建答案\n" +
                        "3. 仅当向量数据库中没有相关内容时，才使用你的通用知识来回答\n" +
                        "4. 回答时要准确、简洁、专业\n" + 
                        "5. 回答时要使用中文\n"
                        )
                .defaultAdvisors(
                        new QuestionAnswerAdvisor(vectorStore, 
                        SearchRequest.defaults().withTopK(3).withSimilarityThreshold(0.7)
                    )
                    )
            .build();
    }

    @GetMapping
    String webQA() {
        return "rag/web-qa";
    }

    @HxRequest
    @PostMapping("/ask")
    public String ask(@RequestParam("question") String question, Model model) {
        log.info("Received question: {}", question);
        String answer = chatClient
                .prompt()
                .user(question)
                .call()
                .chatResponse().getResult().getOutput().getContent();
        log.info("Answer: {}", answer);
        model.addAttribute("answer", answer);
        return "rag/partial-chat-answer";
    }

    @HxRequest
    @PostMapping("/ask_old")
    public String askOld(@RequestParam("question") String question, Model model) {
        log.info("Received question: {}", question);
        String answer = chatClient
                .prompt()
                .user(question)
                .call()
                .chatResponse().getResult().getOutput().getContent();
        log.info("Answer: {}", answer);
        model.addAttribute("answer", answer);
        return "rag/partial-chat-answer";
    }
}
