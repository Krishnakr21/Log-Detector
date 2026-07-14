package com.krishna.LogDetective.controller;


import com.krishna.LogDetective.service.OllamaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logs")
public class LogController {

    private final OllamaService service;

    public LogController(OllamaService service) {
        this.service = service;
    }

    @PostMapping("/analyze")
    public String analyze(@RequestBody String log) {
        return service.analyzeLog(log);
    }
}