package benakka.ebankbot.agents;

import jakarta.servlet.http.HttpSession;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EbankAiAgent {
    private ChatClient chatClient;

    public EbankAiAgent(ChatClient.Builder chatClient, ChatMemory chatMemory, ToolCallbackProvider tools) {
        this.chatClient = chatClient
                .defaultSystem("""
                Vous êtes un assistant qui se charge de répondre aux questions
                de l'utilisateur à propos des clients et des comptes bancaires, en fonction du contexte
                Si aucun contexte n'est fourni, répond avec JE NE SAIS PAS
                """)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultTools(tools)
                .build();
    }

    public String chat(String query) {

        return chatClient.prompt()
                .user(query)
                .options(OpenAiChatOptions.builder().extraBody(Map.of("include_reasoning", false)))
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, "1"))
                .call()
                .content();
    }
}


