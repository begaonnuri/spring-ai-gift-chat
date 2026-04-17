package gift.recommendation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecommendationServiceTest {

    private RecommendationService sut = new RecommendationService();

    @Test
    void recommend() {
        var message = "친구 생일 선물 추천해줘";
        var request = new RecommendationRequest("sessionId", message);

        RecommendationResponse recommend = sut.recommend(request);

        assertThat(recommend.message()).isEqualTo(message);
    }
}