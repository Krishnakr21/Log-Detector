package com.krishna.LogDetective.config;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LogStore {

    private final List<String> logs = new ArrayList<>();
    private final List<List<Double>> embeddings = new ArrayList<>();

    public void add(String log, List<Double> embedding) {
        logs.add(log);
        embeddings.add(embedding);
    }

    public List<String> search(List<Double> queryEmbedding, int topK) {

        List<Map.Entry<String, Double>> scores = new ArrayList<>();

        for (int i = 0; i < logs.size(); i++) {
            double similarity = cosine(queryEmbedding, embeddings.get(i));
            scores.add(Map.entry(logs.get(i), similarity));
        }

        scores.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        List<String> result = new ArrayList<>();
        for (int i = 0; i < Math.min(topK, scores.size()); i++) {
            result.add(scores.get(i).getKey());
        }

        return result;
    }

    private double cosine(List<Double> a, List<Double> b) {
        double dot = 0, normA = 0, normB = 0;

        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += Math.pow(a.get(i), 2);
            normB += Math.pow(b.get(i), 2);
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}