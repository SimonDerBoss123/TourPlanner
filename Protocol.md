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

The ViewModel layer (Custom Hooks) handles all state management, API calls, and business logic. Views are kept purely declarative.

### Backend – Layered Architecture

```
Controller → Service → Repository → Database
```

| Layer | Responsibility |
|-------|---------------|
| **Controller** | HTTP endpoints, request/response mapping |
| **Service** | Business logic, OpenRouteService integration |
| **Repository** | Database access via Spring Data JPA |
| **Domain** | JPA entities (Tour, TourLog, User) |
| **DTO** | Data transfer objects (TourDto, RouteInfo, UserResponse) |
| **Mapper** | AbstractMapper pattern for entity/DTO conversion |

---

## Design Pattern

The application implements the **AbstractMapper** pattern for entity/DTO conversion:

```java
public abstract class AbstractMapper<E, D> {
    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);
}
```

`TourMapper extends AbstractMapper<Tour, TourDto>` provides the concrete implementation. This ensures a consistent, reusable structure for all mappers and cleanly separates domain objects from API responses.

---

## Features

### Tours
- Create, edit, and delete tours
- Tour attributes: name, description, from/to location, transport type
- Distance and estimated time automatically calculated via OpenRouteService API
- Route geometry stored and displayed on an interactive Leaflet map
- Location autocomplete using ORS Geocoding API

### Computed Tour Attributes
- **Popularity** – derived from the number of tour logs
- **Child-friendliness** – derived from average difficulty (≤2), distance (≤10km) and duration (≤60min) of logs

### Tour Logs
- Create, edit, and delete tour logs per tour
- Tour log attributes: comment, difficulty (1–5), rating (1–5), total distance, total time, date/time
- Input validation on all fields

### Authentication
- User registration and login
- JWT-based authentication (24h token expiry)
- Protected routes – unauthenticated users are redirected to login
- Token validation on app start via `/api/users/validate`

### Map
- Interactive Leaflet map on tour detail page
- Real route drawn using encoded polyline from OpenRouteService
- Decoded using `@mapbox/polyline` and rendered as a `Polyline` component

### Search
- Client-side tour search by name on the dashboard

---

## Project Structure

```
tourplanner/
├── tourplanner-backend/
│   └── src/main/java/com/example/tourplannerbackend/
│       ├── controller/        # REST controllers
│       ├── domain/            # JPA entities (Tour, TourLog, User)
│       ├── repository/        # Spring Data repositories
│       ├── service/           # Business logic + OpenRouteService
│       ├── security/          # JWT filter, security config
│       ├── dto/               # RouteInfo, TourDto, UserResponse
│       └── mapper/            # AbstractMapper, TourMapper
│
└── tourplanner-frontend/
    └── src/
        ├── routes/            # TanStack Router file-based routes
        │   ├── _authenticated/
        │   │   ├── dashboard.tsx
        │   │   └── tours/$tourId.tsx
        │   ├── login.tsx
        │   └── register.tsx
        ├── components/        # Reusable UI components
        │   └── dashboard/
        │       ├── Navbar.tsx
        │       ├── TourCard.tsx
        │       ├── SearchBar.tsx
        │       ├── NewTourModal.tsx
        │       ├── EditTourModal.tsx
        │       ├── NewTourLogModal.tsx
        │       └── EditTourLogModal.tsx
        ├── hooks/             # ViewModel layer
        │   ├── useDashboard.ts
        │   └── useTourDetail.ts
        ├── services/          # Model layer
        │   ├── tourService.tsx
        │   └── tourLogService.tsx
        └── auth.tsx           # AuthContext
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

---

## UX Flow

```
Register / Login
      ↓
   Dashboard
   - View all tours
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
  - DB connection via `application.properties` (not committed)
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

- **TourServiceTest** – getAllTours, createTour, deleteTour, getTourById, updateTour (8 tests)
- **TourLogServiceTest** – createTourLog, deleteTourLog, getTourLogsByTourId, getTourLogById, updateTourLog (8 tests)
- **UserServiceTest** – registerUser, loginUser (6 tests)

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

## Security

- All API endpoints (except register/login/validate) require a valid JWT Bearer token
- CORS is configured to allow requests from `http://localhost:5173`
- Passwords are stored hashed via Spring Security's `BCryptPasswordEncoder`
- Tours and logs are user-scoped – users can only access their own data
- API keys stored as environment variables, never in source code
