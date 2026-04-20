package gift.recommendation;

import gift.prompt.PromptLoader;
import gift.request.RequestIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private RequestIdGenerator requestIdGenerator;

    @Mock
    private PromptLoader promptLoader;

    @Mock
    private ChatClient.Builder builder;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ChatClient chatClient;

    private RecommendationService sut;

    @BeforeEach
    void setUp() {
        when(promptLoader.loadSystemPrompt()).thenReturn("system prompt");
        when(builder.defaultSystem("system prompt")).thenReturn(builder);
        when(builder.build()).thenReturn(chatClient);

        sut = new RecommendationService(requestIdGenerator, promptLoader, builder);
    }

    @Test
    void recommend() {
        var message = "친구 생일 선물 추천해줘";
        var request = new RecommendationRequest("sessionId", message);
        var response = "선물 추천 응답";

        when(requestIdGenerator.generate()).thenReturn("requestId");
        when(chatClient.prompt().user(message).call().content()).thenReturn(response);
        RecommendationResponse recommend = sut.recommend(request);

        assertThat(recommend.message()).isEqualTo(response);
    }

    @Test
    void recommend_chatClientError() {
        var message = "친구 생일 선물 추천해줘";
        var request = new RecommendationRequest("sessionId", message);

        when(requestIdGenerator.generate()).thenReturn("requestId");
        when(chatClient.prompt().user(message).call().content()).thenThrow(new RuntimeException("ChatClient error"));

        assertThatThrownBy(() -> sut.recommend(request))
            .isInstanceOf(ChatClientException.class);
    }
}