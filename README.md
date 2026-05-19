# CT Scan Request Service
### Microservices with Spring Boot — Persistence, Reliability and Advanced Java Concepts

A three-stage evolution of a CT scan request management system, demonstrating the migration from a legacy monolithic Java application to a production-ready RESTful microservice backed by PostgreSQL.

---

## The Three Stages

| Stage | Description |
|-------|-------------|
| **Stage 1** | Monolithic Java app — everything in one class, data in memory, no API |
| **Stage 2** | Spring Boot microservice — REST API, layered architecture, in-memory storage |
| **Stage 3** | Spring Boot microservice + PostgreSQL — full persistence, data survives restarts |

---

## REST API Endpoints

| Method | Endpoint | What it does |
|--------|----------|-------------|
| POST | `/api/scans` | Create a scan request |
| GET | `/api/scans` | Get all scan requests |
| GET | `/api/scans/{id}` | Get one scan by ID |
| PUT | `/api/scans/{id}/status` | Update scan status (PENDING, COMPLETED, CANCELLED) |
| DELETE | `/api/scans/{id}` | Delete a scan |



## Tech Stack
Java 17 · Spring Boot · Spring Data JPA · Hibernate · PostgreSQL · Maven

---

## What's Next
- Spring Security for authentication
- Docker for containerization
- Multiple microservices with an API Gateway
- Cloud deployment (AWS / Azure)
