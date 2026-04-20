package gift.recommendation;

import gift.prompt.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecommendationService {

    private final ChatClient client;

    public RecommendationService(PromptLoader promptLoader, ChatClient.Builder builder) {
        this.client = builder
            .defaultSystem(promptLoader.loadSystemPrompt())
            .build();
    }

    public RecommendationResponse recommend(RecommendationRequest request) {
        var content = client.prompt().user(request.message()).call().content();

        return new RecommendationResponse("requestId", content, 0L);
    }
}
