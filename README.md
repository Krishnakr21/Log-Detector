# 🚀 Log Detective – AI Powered Log Analysis System

## 📌 Overview
Log Detective is an AI-powered backend system that analyzes application logs using a local LLM (Ollama).  
It uses embeddings and a basic Retrieval-Augmented Generation (RAG) pipeline to provide intelligent debugging insights.

---

## ⚙️ Tech Stack
- Java 17  
- Spring Boot 3  
- REST APIs  
- Ollama (Local LLM)  
- Embeddings  
- Custom Vector Store (LogStore)  
- Maven  

---

## 🧠 Features
- Accepts logs via REST API  
- Converts logs into embeddings  
- Stores logs in vector-based memory  
- Retrieves similar logs using similarity search  
- Uses RAG pipeline for context-aware responses  
- Generates structured output:
  - Error Type  
  - Root Cause  
  - Fix  

---

## 🔄 Architecture

User → Controller → Service → Embedding Service → Vector Store → LLM (Ollama) → Response

---

## 📦 API Endpoint

### POST `/logs/analyze`

Request:
```json
{
  "log": "NullPointerException at UserService.java:45"
}
