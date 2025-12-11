# 📝 QoQ Notes Service

A RESTful service for managing notes with features including creation, editing, tag filtering, and text analysis. The project is built on **Spring Boot** and uses **MongoDB** for data persistence.

The entire infrastructure is containerized and can be deployed automatically using **Docker Compose**.

---

## 🛠 Tech Stack

* **Java:** 21 (Eclipse Temurin)
* **Framework:** Spring Boot 
* **Database:** MongoDB 
* **Build Tool:** Maven
* **Containerization:** Docker & Docker Compose
* **Testing:** JUnit 5, Mockito, MockMvc

---

## 🚀 Quick Start Guide

Follow these steps to deploy the application on any machine.

### Prerequisites
Ensure you have the following installed:
1.  **Docker Desktop** (or Docker Engine + Compose).
2.  **Git** (to clone the repository).

### Step 1: Clone the Repository
Open your terminal and run:

```bash
git clone [https://github.com/your-username/qoq-notes-service.git](https://github.com/your-username/qoq-notes-service.git)
cd qoq-notes-service
```
### Step 2: Configure Environment Variables
Create a file named `.env` in the root directory of the project (where `docker-compose.yml` is located).
Add the following configuration for the database credentials:

```properties
MONGO_USERNAME=username
MONGO_PASSWORD=secret
```

> **⚠️ Important:** If you skip this step, the database might fail to initialize correctly or start without password protection.

### Step 3: Build and Run
Run the following command to build the application and start the containers:

```bash
docker-compose up --build
```

**What happens next:**
1.  Maven builds the Java application.
2.  Docker pulls MongoDB and JDK images.
3.  MongoDB starts on port `27017`.
4.  The Application starts on port `8080`.

Wait until you see the log message: `Started QoQTestApplication in ... seconds`.

### Step 4: Stopping the Application
To stop the services, press `Ctrl+C` or run:

```bash
docker-compose down
```

> **Note:** To completely remove the database volume (reset data), add the `-v` flag: `docker-compose down -v`.

---

## 🔌 API Endpoints

Base URL: `http://localhost:8080`

### 📝 Note Operations

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **GET** | `/api/v1/note` | Get all notes (paginated).<br>Params: `?page=0&size=10` |
| **POST** | `/api/v1/note` | Create a new note. |
| **PUT** | `/api/v1/note` | Update an existing note. |
| **DELETE**| `/api/v1/note/{id}` | Delete a note by ID. |
| **GET** | `/api/v1/note/search` | Filter notes by tag.<br>Params: `?tag=BUSINESS` |
| **GET** | `/api/v1/note/{id}/text`| Get raw text content of a note. |

### 🧠 Analysis Operations

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| **POST** | `/api/v1/note/analyze` | Count word frequency in a text.<br>Body: `Plain Text`. |

---

## 🧪 Usage Examples (cURL)

**1. Create a Note:**
```bash
curl -X POST http://localhost:8080/api/v1/note \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Title",
    "text": "Text",
    "tag": "BUSINESS"
  }'
```

**2. Get Notes (Page 0, Size 10):**
```bash
curl "http://localhost:8080/api/v1/note?page=0&size=10"
```

**3. Search by Tag:**
```bash
curl "http://localhost:8080/api/v1/note/search?tag=BUSINESS"
```

**4. Analyze Text:**
```bash
curl -X POST http://localhost:8080/api/v1/note/analyze \
  -H "Content-Type: text/plain" \
  -d "Java Spring Java Docker"
```

---

## 🏗 Project Structure

```text
src/
├── main/
│   ├── java/org/example/qoq_test/
│   │   ├── controller/       # REST Controllers
│   │   ├── service/          # Business Logic
│   │   ├── repository/       # Data Access Layer (Mongo)
│   │   ├── entity/           # Database Models
│   │   ├── dto/              # Data Transfer Objects
│   │   ├── mapper/           # DTO <-> Entity mapping
│   │   └── enumeration/      # Enums (Tags, etc.)
│   └── resources/
│       └── application.yml   # Spring Configuration
├── test/                     # Unit & Integration Tests
├── Dockerfile                # Docker build instructions
└── docker-compose.yml        # Container orchestration
```

---

## ✅ Testing

The project includes two levels of testing:

1.  **Unit Tests:** Testing business logic in `NoteService` using **Mockito**.
2.  **Integration Tests:** Testing the REST API layer in `NoteController` using **MockMvc**.

To run tests locally (requires Maven installed):
```bash
mvn test
```
