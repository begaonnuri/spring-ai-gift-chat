package gift.recommendation;

import gift.prompt.PromptLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    private final ChatClient client;

    public RecommendationService(PromptLoader promptLoader, ChatClient.Builder builder) {
        this.client = builder
            .defaultSystem(promptLoader.loadSystemPrompt())
            .build();
    }

    public RecommendationResponse recommend(RecommendationRequest request) {
        try {
            var content = client.prompt().user(request.message()).call().content();
            return new RecommendationResponse("requestId", content, 0L);
        } catch (RuntimeException e) {
            throw new ChatClientException(e);
        }
    }
}
