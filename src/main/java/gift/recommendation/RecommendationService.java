package gift.recommendation;

import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    public RecommendationResponse recommend(RecommendationRequest request) {
        return new RecommendationResponse("requestId", request.message(), 0L);
    }
}
