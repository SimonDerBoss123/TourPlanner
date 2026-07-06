# TourPlanner
**Repository:** https://github.com/SimonDerBoss123/TourPlanner

A full-stack web application for planning and managing tours and tour logs. Built with Spring Boot and React as part of a semester project.

---

## Tech Stack

### Backend
- **Java 17** with **Spring Boot 4.0.6**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **PostgreSQL** (via Docker Compose)
- **Maven**
- **OpenRouteService API** for route calculation and geocoding

### Frontend
- **React** with **TypeScript**
- **Vite** as build tool
- **TanStack Router v7** for file-based routing
- **shadcn/ui** + **Tailwind CSS** for UI components
- **React Leaflet** + **@mapbox/polyline** for interactive map and route visualization
- **Lucide React** for icons

---

## Architecture

The application follows the **MVVM (Model-View-ViewModel)** pattern on the frontend and a layered architecture on the backend.

### Frontend – MVVM

| Layer | Implementation |
|-------|---------------|
| **Model** | `tourService.ts`, `tourLogService.ts`, `auth.tsx` |
| **ViewModel** | `useDashboard.ts`, `useTourDetail.ts` (Custom Hooks) |
| **View** | `dashboard.tsx`, `$tourId.tsx` (pure JSX, no logic) |

The ViewModel layer (Custom Hooks) handles all state management, API calls, and business logic. Views are kept purely declarative – they only render what the ViewModel provides.

### Backend – Layered Architecture

```
Request → JwtFilter → Controller → Service → Repository → Database
```

| Layer | Responsibility |
|-------|---------------|
| **Controller** | HTTP endpoints, extracts JWT token, delegates to Service |
| **Service** | Business logic, authorization checks, OpenRouteService calls |
| **Repository** | Database access via Spring Data JPA |
| **Domain** | JPA entities (Tour, TourLog, User) |
| **DTO** | Data transfer objects (TourDto, RouteInfo, UserResponse) |
| **Mapper** | AbstractMapper pattern for entity/DTO conversion |
| **Security** | JwtFilter, JwtUtil, SecurityConfig |

---

## Authentication & Security

### JWT Flow
1. User registers or logs in → Backend generates JWT token containing the username as Subject
2. Frontend stores token in `localStorage` as `auth-token`
3. Every subsequent request includes `Authorization: Bearer <token>` header
4. `JwtFilter` runs before every request – validates token and sets the authenticated user in Spring Security's `SecurityContextHolder`
5. Controllers extract username from token via `jwtUtil.extractUsername(token)` and pass it to the Service layer

### Why JWT instead of Sessions?
JWT is **stateless** – the server does not need to store session data. The token itself contains all necessary information (username, expiry). This makes the application easier to scale.

### Password Security
Passwords are never stored in plain text. Spring Security's `BCryptPasswordEncoder` hashes passwords before saving. On login, the entered password is compared against the stored hash.

### Authorization
Every write operation checks if the authenticated user owns the resource:
```java
if (!tour.getUser().getUsername().equals(username)) {
    throw new RuntimeException("Unauthorized");
}
```
This applies to Tour delete/update and TourLog create/update/delete.

### CORS
`@CrossOrigin(origins = "http://localhost:5173")` is configured on all controllers to allow requests from the frontend origin. Without this, the browser would block requests between different ports.

### SQL Injection Prevention
Hibernate uses parameterized queries – user input is never directly embedded in SQL strings. This prevents SQL injection by design.

---

## Design Pattern – AbstractMapper

The application implements the **AbstractMapper** pattern for entity/DTO conversion:

```java
public abstract class AbstractMapper<E, D> {
    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);
}
```

`TourMapper extends AbstractMapper<Tour, TourDto>` provides the concrete implementation.

**Why:**
- Separates Domain objects from API responses – sensitive fields like passwords are not accidentally exposed
- Consistent structure for all mappers – adding a `TourLogMapper` is trivial
- The Controller returns `TourDto` instead of the full `Tour` entity

---

## OpenRouteService Integration

When a Tour is created or updated, the backend automatically:

**Step 1 – Geocoding** (`/geocode/search`):
- Converts location name (e.g. "Wien") to coordinates `[longitude, latitude]`
- Called for both `fromLocation` and `toLocation`

**Step 2 – Directions** (`/v2/directions/{profile}/json`):
- Sends both coordinate pairs to ORS
- Receives `distance` (meters), `duration` (seconds), and `geometry` (encoded Polyline)
- Distance is converted to km, duration to minutes before storing

**Route on Map:**
- `geometry` is stored as TEXT in the database
- Frontend decodes the encoded Polyline using `@mapbox/polyline`
- React Leaflet renders the route as a `Polyline` component on the map

**Transport Profiles:**
- `driving-car` – Car
- `cycling-regular` – Bike
- `foot-walking` – Walking
- `hiking` – Hiking

**Location Autocomplete:**
- As the user types in From/To fields, the frontend calls ORS `/geocode/search`
- Up to 5 suggestions are shown as a dropdown
- Only triggered after 2+ characters to avoid excessive API calls

---

## Computed Tour Attributes

These attributes are **not stored in the database** – they are recalculated on every `getTourById` and `getAllTours` call to always reflect the current state.

### Popularity
```java
tour.setPopularity(tourLogRepository.countByTourId(id));
```
Simply counts the number of TourLogs for this tour.

### Child-friendliness
A tour is considered child-friendly if ALL of the following are true:
- Average difficulty of all logs ≤ 2
- Tour distance ≤ 10 km
- Tour duration ≤ 60 min

```java
boolean easyDifficulty = avgDifficulty <= 2;
boolean shortDistance = tourDistance != null && tourDistance <= 10;
boolean shortTime = estimatedTime != null && estimatedTime <= 60;
return easyDifficulty && shortDistance && shortTime;
```

---

## Features

### Tours
- Create, edit, and delete tours
- Distance and estimated time automatically calculated via OpenRouteService
- Route geometry stored and displayed on interactive Leaflet map
- Location autocomplete for From/To fields

### Tour Logs
- Create, edit, and delete tour logs per tour
- Attributes: comment, difficulty (1–5), rating (1–5), total distance, total time, date/time
- Input validation on all fields (numeric range checks, required fields)

### Authentication
- User registration and login
- JWT-based authentication with 24h token expiry
- Protected routes – unauthenticated users are redirected to login
- Token validated on app start via `/api/users/validate`

### Search
- Client-side tour search by name on the dashboard

---

## Project Structure

```
tourplanner/
├── tourplanner-backend/
│   └── src/main/java/com/example/tourplannerbackend/
│       ├── controller/        # REST controllers (Tour, TourLog, User)
│       ├── domain/            # JPA entities (Tour, TourLog, User)
│       ├── repository/        # Spring Data repositories
│       ├── service/           # Business logic + OpenRouteService
│       ├── security/          # JwtFilter, JwtUtil, SecurityConfig
│       ├── dto/               # RouteInfo, TourDto, UserResponse
│       └── mapper/            # AbstractMapper, TourMapper
│
└── tourplanner-frontend/
    └── src/
        ├── routes/
        │   ├── _authenticated/
        │   │   ├── dashboard.tsx      # View
        │   │   └── tours/$tourId.tsx  # View
        │   ├── login.tsx
        │   └── register.tsx
        ├── components/dashboard/      # Reusable UI components
        ├── hooks/                     # ViewModel layer
        │   ├── useDashboard.ts
        │   └── useTourDetail.ts
        ├── services/                  # Model layer
        │   ├── tourService.tsx
        │   └── tourLogService.tsx
        └── auth.tsx                   # AuthContext
```

---

## Routing

| Route | Description |
|-------|-------------|
| `/login` | Login page – redirects to dashboard if already authenticated |
| `/register` | Registration page |
| `/_authenticated` | Layout route – redirects to `/login` if not authenticated |
| `/_authenticated/dashboard` | Tour overview |
| `/_authenticated/tours/$tourId` | Tour detail with logs and map |

The `_authenticated` layout route uses `beforeLoad` to protect all child routes. If the user is not authenticated, they are redirected to `/login` with the original URL saved as a redirect parameter.

---

## UX Flow

```
Register / Login
      ↓
   Dashboard
   - View all tours (distance, duration, logs count)
   - Search tours by name
   - Create new tour (with location autocomplete)
      ↓
  Tour Detail
  - View tour info (transport, distance, duration, popularity, child-friendliness)
  - Interactive Leaflet map with real route
  - View, create, edit, delete tour logs
  - Edit or delete tour
```

---

## Configuration

All sensitive configuration is stored outside the source code:

- **Backend:** Environment variables via IntelliJ Run Configuration
  - `OPENROUTE_API_KEY` – OpenRouteService API key
- **Frontend:** `.env` file (not committed to git)
  - `VITE_ORS_API_KEY` – OpenRouteService API key for geocoding autocomplete

---

## Setup

### Prerequisites
- Java 17+
- Node.js 18+
- Docker (for PostgreSQL)

### Backend

Set the environment variable `OPENROUTE_API_KEY` in your run configuration, then:

```bash
cd tourplanner-backend
./mvnw spring-boot:run
```

Docker Compose starts PostgreSQL automatically on `localhost:5432`.

### Frontend

Create a `.env` file in `tourplanner-frontend/`:
```
VITE_ORS_API_KEY=your_key_here
```

Then:
```bash
cd tourplanner-frontend
npm install
npm run dev
```

The frontend runs on `http://localhost:5173`.

---

## Unit Tests

22 unit tests covering the core service layer, written with JUnit 5 and Mockito:

- **TourServiceTest** (8 tests) – getAllTours, createTour (with RouteInfo mock), deleteTour (with auth check), getTourById, updateTour
- **TourLogServiceTest** (8 tests) – createTourLog (with auth check), deleteTourLog, getTourLogsByTourId, getTourLogById, updateTourLog
- **UserServiceTest** (6 tests) – registerUser, loginUser, invalid credentials

All tests use Mockito to mock dependencies – no real database or API calls are made.

Run tests:
```bash
cd tourplanner-backend
./mvnw test
```

---

## API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users/register` | Register new user |
| POST | `/api/users/login` | Login, returns JWT token |
| GET | `/api/users/validate` | Validate JWT token |

### Tours
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tours` | Get all tours for authenticated user |
| POST | `/api/tours` | Create new tour (triggers ORS route calculation) |
| GET | `/api/tours/{id}` | Get tour by ID |
| PUT | `/api/tours/{id}` | Update tour (triggers ORS route recalculation) |
| DELETE | `/api/tours/{id}` | Delete tour |

### Tour Logs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/tours/{tourId}/tourlogs` | Get all logs for a tour |
| POST | `/api/tours/{tourId}/tourlogs` | Create new log |
| GET | `/api/tours/{tourId}/tourlogs/{id}` | Get log by ID |
| PUT | `/api/tours/{tourId}/tourlogs/{id}` | Update log |
| DELETE | `/api/tours/{tourId}/tourlogs/{id}` | Delete log |

---

## Lessons Learned

- **JWT stateless auth** is simpler to implement than session-based auth for REST APIs
- **OpenRouteService** requires two separate API calls (geocoding + directions) – not just one
- **Hibernate DDL auto** does not alter existing columns – manual SQL migration needed for column type changes
- **React Leaflet z-index** conflicts with modals – requires explicit `zIndex: 0` on the map container
- **Computed attributes** should not be stored in the database if they depend on related data that changes frequently
