package gift.recommendation;

import gift.prompt.PromptLoader;
import gift.request.RequestIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RecommendationService {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final RequestIdGenerator requestIdGenerator;
    private final ChatClient client;

    public RecommendationService(RequestIdGenerator requestIdGenerator, PromptLoader promptLoader, ChatClient.Builder builder) {
        this.requestIdGenerator = requestIdGenerator;
        this.client = builder
            .defaultSystem(promptLoader.loadSystemPrompt())
            .build();
    }

    public RecommendationResponse recommend(RecommendationRequest request) {
        var requestId = requestIdGenerator.generate();
        var startNs = System.nanoTime();
        try {
            var content = client.prompt().user(request.message()).call().content();
            var durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            log.info("Recommendation generated in {} ms. requestId: {}, message: {}", durationMs, requestId, request.message());

            return new RecommendationResponse(requestId, content, durationMs);
        } catch (RuntimeException e) {
            throw new ChatClientException(e);
        }
    }
}
