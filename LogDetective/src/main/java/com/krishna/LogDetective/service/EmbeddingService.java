package com.krishna.LogDetective.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@Service
public class EmbeddingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Double> getEmbedding(String text) {
        try {
            String url = "http://localhost:11434/api/embeddings";

            Map<String, Object> body = Map.of(
                    "model", "nomic-embed-text",
                    "prompt", text
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            String response = restTemplate.postForObject(url, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response, Map.class);

            return (List<Double>) json.get("embedding");

        } catch (Exception e) {
            throw new RuntimeException("Embedding failed", e);
        }
    }
}