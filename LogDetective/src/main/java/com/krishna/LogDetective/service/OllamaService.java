package com.krishna.LogDetective.service;

import com.krishna.LogDetective.config.LogStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OllamaService {

    @Value("${ollama.base-url}")
    private String baseUrl;

    @Value("${ollama.model}")
    private String model;

    @Autowired
    private EmbeddingService embeddingService;

    @Autowired
    private LogStore logStore;

    private final RestTemplate restTemplate = new RestTemplate();

    public String analyzeLog(String log) {

        // ✅ 0. Validate input
        if (log == null || log.trim().isEmpty()) {
            return "Please provide a valid log";
        }

        // 1️⃣ Create embedding
        List<Double> queryEmbedding = embeddingService.getEmbedding(log);

        // 2️⃣ Store log
        logStore.add(log, queryEmbedding);

        // 3️⃣ Retrieve similar logs
        List<String> similarLogs = logStore.search(queryEmbedding, 3);

        // ✅ Handle empty context
        String context = similarLogs.isEmpty()
                ? "No previous logs available"
                : String.join("\n", similarLogs);

        // 4️⃣ Build prompt (WITH CONTEXT 🔥)
        String prompt = """
You are a backend log analyzer.

STRICT RULES:
- Only return final answer
- No greetings
- No explanations
- No questions
- Do not repeat input

Use this previous context:
""" + context + """

Analyze this log:
""" + log + """

Return EXACTLY:

Error Type:
Root Cause:
Fix:
""";

        try {
            String url = baseUrl + "/api/generate";

            Map<String, Object> body = Map.of(
                    "model", model,
                    "prompt", prompt,
                    "stream", false
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            String response = restTemplate.postForObject(url, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response, Map.class);

            String result = (String) json.get("response");

            // ✅ Clean unwanted model output
            result = result.replaceAll("(?i)hello.*", "")
                    .replaceAll("(?i)sorry.*", "")
                    .trim();

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Ollama call failed", e);
        }
    }
}