# 🍽️ Restaurant Booking System - Microservices Project

A complete restaurant booking system built with Spring Boot microservices architecture, featuring user management, restaurant management, and booking services with ActiveMQ message broker integration.

**📦 GitHub Repository:** https://github.com/GurjotKaur1501/AGS-Restaurant


##  Overview

This project demonstrates a microservices architecture with three independent services:

1. **User Service** (Port 8081) - Manages user accounts and preferences
2. **Restaurant Service** (Port 8082) - Manages restaurants and tables
3. **Booking Service** (Port 8083) - Handles reservations with facade pattern

Additional components:
- **ActiveMQ** (Ports 61616, 8161) - Message broker for inter-service communication
- **Frontend** (Port 3000) - React-based user interface
- **H2 Database** - In-memory database for each service


## Technologies

### Backend
- **Java 21** - Programming language
- **Spring Boot 3.5.8** - Framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Apache ActiveMQ** - Message broker
- **Lombok** - Code generation
- **Maven** - Build tool

### Frontend
- **HTML5 / CSS3** - Structure and styling
- **JavaScript (ES6+)** - Interactivity
- **NGINX** - Web server

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

---

## 🎨 Design Patterns

### **Repository Pattern**
Used in all services for data access abstraction.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```

### **Facade Pattern** (Booking Service)
Simplifies complex booking operations into a single interface.

```java
@Service
public class BookingFacade {
    public BookingResponseDTO createCompleteBooking(BookingRequestDTO request) {
        // Validates, checks availability, reserves table, creates booking
        // All in one method call!
    }
}
```

### **Strategy Pattern** (Restaurant Service)
Different table allocation strategies (First Fit, Best Fit).

```java
public interface TableAllocationStrategy {
    Optional<Table> allocate(List<Table> tables, int partySize);
}

@Component("firstFit")
public class FirstFitStrategy implements TableAllocationStrategy { }

@Component("bestFit")
public class BestFitStrategy implements TableAllocationStrategy { }
```

### **Builder Pattern** (All DTOs)
Clean object construction using Lombok.

```java
BookingDTO booking = BookingDTO.builder()
    .userId(1L)
    .tableId(1L)
    .bookingTime(LocalDateTime.now())
    .numberOfGuests(4)
    .build();
```

---

## 🚀 Getting Started

### Prerequisites

- **Docker Desktop** - [Download here](https://www.docker.com/products/docker-desktop)
- **Maven 3.8+** - [Download here](https://maven.apache.org/download.cgi)
- **Java 21** - [Download here](https://adoptium.net/)
- **Git** - [Download here](https://git-scm.com/downloads)

### Quick Start

**Clone the repository**
   ```bash
   git clone https://github.com/GurjotKaur1501/AGS-Restaurant.git
   cd AGS-Restaurant
   ```

**Run the startup script**

**Linux/Mac/Git Bash:**
   ```bash
   chmod +x start.sh
   ./start.sh
   ```

**Access the application**
- Frontend: http://localhost:3000

---

## 📡 API Endpoints

### 🔵 User Service (Port 8081)

#### Health Check
```bash
# HTTPie
http GET http://localhost:8081/api/users/health

# Insomnia/Postman
GET http://localhost:8081/api/users/health
```

#### Create User
```bash
# HTTPie
http POST http://localhost:8081/api/users \
  firstName="John" \
  lastName="Doe" \
  email="john.doe@example.com" \
  password="password123" \
  phone="+46701234567"

# Insomnia/Postman
POST http://localhost:8081/api/users
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "phone": "+46701234567"
}
```

#### Get User by ID
```bash
# HTTPie
http GET http://localhost:8081/api/users/1

# Insomnia/Postman
GET http://localhost:8081/api/users/1
```

#### Get User by Email
```bash
# HTTPie
http GET http://localhost:8081/api/users/email/john.doe@example.com

# Insomnia/Postman
GET http://localhost:8081/api/users/email/john.doe@example.com
```

#### Get All Users
```bash
# HTTPie
http GET http://localhost:8081/api/users

# Insomnia/Postman
GET http://localhost:8081/api/users
```

#### Update User
```bash
# HTTPie
http PUT http://localhost:8081/api/users/1 \
  firstName="Jane" \
  lastName="Doe" \
  email="jane.doe@example.com" \
  password="newpassword123" \
  phone="+46701234567"

# Insomnia/Postman
PUT http://localhost:8081/api/users/1
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "password": "newpassword123",
  "phone": "+46701234567"
}
```

#### Delete User
```bash
# HTTPie
http DELETE http://localhost:8081/api/users/1

# Insomnia/Postman
DELETE http://localhost:8081/api/users/1
```

---

### 🟢 Restaurant Service (Port 8082)

#### Create Restaurant
```bash
# HTTPie
http POST http://localhost:8082/api/restaurants \
  name="La Petite Bistro" \
  address="Östra Hamngatan 5, Göteborg" \
  cuisineType="FRENCH"

# Insomnia/Postman
POST http://localhost:8082/api/restaurants
Content-Type: application/json

{
  "name": "La Petite Bistro",
  "address": "Östra Hamngatan 5, Göteborg",
  "cuisineType": "FRENCH"
}
```

**Available Cuisine Types:** ITALIAN, SWEDISH, ASIAN, FRENCH, MEXICAN, OTHER

#### Get Restaurant by ID
```bash
# HTTPie
http GET http://localhost:8082/api/restaurants/1

# Insomnia/Postman
GET http://localhost:8082/api/restaurants/1
```

#### Get All Restaurants
```bash
# HTTPie
http GET http://localhost:8082/api/restaurants

# Insomnia/Postman
GET http://localhost:8082/api/restaurants
```

#### Update Restaurant
```bash
# HTTPie
http PUT http://localhost:8082/api/restaurants/1 \
  name="La Petite Bistro Deluxe" \
  address="Östra Hamngatan 5, Göteborg" \
  cuisineType="FRENCH"

# Insomnia/Postman
PUT http://localhost:8082/api/restaurants/1
Content-Type: application/json

{
  "name": "La Petite Bistro Deluxe",
  "address": "Östra Hamngatan 5, Göteborg",
  "cuisineType": "FRENCH"
}
```

#### Delete Restaurant
```bash
# HTTPie
http DELETE http://localhost:8082/api/restaurants/1

# Insomnia/Postman
DELETE http://localhost:8082/api/restaurants/1
```

#### Create Table
```bash
# HTTPie
http POST http://localhost:8082/api/tables \
  tableNumber:=1 \
  seats:=4 \
  available:=true \
  restaurantId:=1

# Insomnia/Postman
POST http://localhost:8082/api/tables
Content-Type: application/json

{
  "tableNumber": 1,
  "seats": 4,
  "available": true,
  "restaurantId": 1
}
```

#### Get Table by ID
```bash
# HTTPie
http GET http://localhost:8082/api/tables/1

# Insomnia/Postman
GET http://localhost:8082/api/tables/1
```

#### Get Tables by Restaurant
```bash
# HTTPie
http GET http://localhost:8082/api/tables/restaurant/1

# Insomnia/Postman
GET http://localhost:8082/api/tables/restaurant/1
```

#### Update Table
```bash
# HTTPie
http PUT http://localhost:8082/api/tables/1 \
  tableNumber:=1 \
  seats:=6 \
  available:=true \
  restaurantId:=1

# Insomnia/Postman
PUT http://localhost:8082/api/tables/1
Content-Type: application/json

{
  "tableNumber": 1,
  "seats": 6,
  "available": true,
  "restaurantId": 1
}
```

#### Delete Table
```bash
# HTTPie
http DELETE http://localhost:8082/api/tables/1

# Insomnia/Postman
DELETE http://localhost:8082/api/tables/1
```

#### Allocate Table (Strategy Pattern Demo)
```bash
# HTTPie - First Fit Strategy
http POST "http://localhost:8082/api/tables/allocate?restaurantId=1&partySize=4&strategy=firstFit"

# HTTPie - Best Fit Strategy
http POST "http://localhost:8082/api/tables/allocate?restaurantId=1&partySize=4&strategy=bestFit"

# Insomnia/Postman - First Fit
POST http://localhost:8082/api/tables/allocate?restaurantId=1&partySize=4&strategy=firstFit

# Insomnia/Postman - Best Fit
POST http://localhost:8082/api/tables/allocate?restaurantId=1&partySize=4&strategy=bestFit
```

**Strategies:**
- `firstFit` - Returns the first available table that fits the party size
- `bestFit` - Returns the smallest available table that fits the party size

---

### 🟡 Booking Service (Port 8083)

#### Health Check
```bash
# HTTPie
http GET http://localhost:8083/api/bookings/health

# Insomnia/Postman
GET http://localhost:8083/api/bookings/health
```

#### Create Booking (Facade Pattern Demo)
```bash
# HTTPie
http POST http://localhost:8083/api/bookings \
  userId:=1 \
  tableId:=1 \
  bookingTime="2025-12-25T19:00:00" \
  numberOfGuests:=4 \
  specialRequests="Window seat please"

# Insomnia/Postman
POST http://localhost:8083/api/bookings
Content-Type: application/json

{
  "userId": 1,
  "tableId": 1,
  "bookingTime": "2025-12-25T19:00:00",
  "numberOfGuests": 4,
  "specialRequests": "Window seat please"
}
```

**Response includes:**
- Booking details
- Confirmation code
- Next steps
- Additional information

#### Get Booking by ID
```bash
# HTTPie
http GET http://localhost:8083/api/bookings/1

# Insomnia/Postman
GET http://localhost:8083/api/bookings/1
```

#### Get Booking Details (Enhanced with Facade)
```bash
# HTTPie
http GET http://localhost:8083/api/bookings/details/1

# Insomnia/Postman
GET http://localhost:8083/api/bookings/details/1
```

#### Get All Bookings
```bash
# HTTPie
http GET http://localhost:8083/api/bookings

# Insomnia/Postman
GET http://localhost:8083/api/bookings
```

#### Get Bookings by User
```bash
# HTTPie
http GET http://localhost:8083/api/bookings/user/1

# Insomnia/Postman
GET http://localhost:8083/api/bookings/user/1
```

#### Update Booking
```bash
# HTTPie
http PUT http://localhost:8083/api/bookings/1 \
  userId:=1 \
  tableId:=2 \
  bookingTime="2025-12-25T20:00:00" \
  numberOfGuests:=6 \
  specialRequests="Birthday celebration"

# Insomnia/Postman
PUT http://localhost:8083/api/bookings/1
Content-Type: application/json

{
  "userId": 1,
  "tableId": 2,
  "bookingTime": "2025-12-25T20:00:00",
  "numberOfGuests": 6,
  "specialRequests": "Birthday celebration"
}
```

#### Cancel Booking
```bash
# HTTPie
http POST http://localhost:8083/api/bookings/1/cancel

# Insomnia/Postman
POST http://localhost:8083/api/bookings/1/cancel
```

#### Delete Booking
```bash
# HTTPie
http DELETE http://localhost:8083/api/bookings/1

# Insomnia/Postman
DELETE http://localhost:8083/api/bookings/1
```

---

#### Test Frontend
Open browser: http://localhost:3000
- ✅ Should show all restaurants
- ✅ Can select restaurant
- ✅ Can choose date and time
- ✅ Can make booking
- ✅ End-to-end flow works

---

## 🔍 Troubleshooting

### Services Not Responding

**Check logs:**
```bash
docker-compose logs -f user-service
docker-compose logs -f restaurant-service
docker-compose logs -f booking-service
```

### Port Already in Use

**Stop containers:**
```bash
docker-compose down
./start.sh
```

### Demo Data Missing

**Manually verify:**
```bash
http GET http://localhost:8082/api/restaurants
```

If empty, wait longer (services may still be initializing) or check restaurant-service logs.

---

## 🛑 Stopping the System

```bash
# Stop all containers
docker-compose down

# Stop and remove volumes (clean restart)
docker-compose down -v
```

---

## 📝 Useful Commands

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f user-service
```

### Check Container Status
```bash
docker-compose ps
```

### Restart a Service
```bash
docker-compose restart user-service
```



---

## 📄 License

Educational project.

---

## 👥 Authors

Anton, Gurjot, Sharina

---

**Last Updated:** December 2025