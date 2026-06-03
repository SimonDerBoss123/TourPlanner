# TourPlanner

A full-stack web application for planning and managing tours and tour logs. Built with Spring Boot and React as part of a semester project.

---

## Tech Stack

### Backend
- **Java 17** with **Spring Boot 4.0.6**
- **Spring Security** with JWT authentication
- **Spring Data JPA** with Hibernate
- **PostgreSQL** (via Docker Compose)
- **Maven**

### Frontend
- **React** with **TypeScript**
- **Vite** as build tool
- **TanStack Router v7** for file-based routing
- **shadcn/ui** + **Tailwind CSS** for UI components
- **React Leaflet** for map placeholder
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
| **Service** | Business logic, validation |
| **Repository** | Database access via Spring Data JPA |
| **Domain** | JPA entities (Tour, TourLog, User) |

---

## Features

### Tours
- Create, edit, and delete tours
- Tour attributes: name, description, from/to location, transport type, distance, estimated time
- Tour detail view with map placeholder (React Leaflet)

### Tour Logs
- Create, edit, and delete tour logs per tour
- Tour log attributes: comment, difficulty (1–5), rating (1–5), total distance, total time, date/time
- Input validation on all fields

### Authentication
- User registration and login
- JWT-based authentication (24h token expiry)
- Protected routes – unauthenticated users are redirected to login
- Token validation on app start via `/api/users/validate`

### Search
- Client-side tour search by name

---

## Project Structure

```
tourplanner/
├── tourplanner-backend/
│   └── src/main/java/com/example/tourplannerbackend/
│       ├── controller/        # REST controllers
│       ├── domain/            # JPA entities
│       ├── repository/        # Spring Data repositories
│       ├── service/           # Business logic
│       ├── security/          # JWT filter, security config
│       └── dto/               # Data transfer objects
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

TanStack Router v7 with file-based routing is used for navigation.

| Route | Description |
|-------|-------------|
| `/login` | Login page – redirects to dashboard if already authenticated |
| `/register` | Registration page |
| `/_authenticated` | Layout route – redirects to `/login` if not authenticated |
| `/_authenticated/dashboard` | Tour overview |
| `/_authenticated/tours/$tourId` | Tour detail with logs and map |

The `_authenticated` layout route uses `beforeLoad` to protect all child routes. If `context.auth.isAuthenticated` is false, the user is redirected to `/login` with the original URL saved as a redirect parameter.

---

## UX Flow

```
Register / Login
      ↓
   Dashboard
   - View all tours
   - Search tours
   - Create new tour
      ↓
  Tour Detail
  - View tour info (transport, distance, duration)
  - Map placeholder
  - View, create, edit, delete tour logs
  - Edit or delete tour
```

---

## Setup

### Prerequisites
- Java 17+
- Node.js 18+
- Docker (for PostgreSQL)

### Backend

```bash
cd tourplanner-backend
./mvnw spring-boot:run
```

Docker Compose starts PostgreSQL automatically on `localhost:5432`.

### Frontend

```bash
cd tourplanner-frontend
npm install
npm run dev
```

The frontend runs on `http://localhost:5173`.

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
| POST | `/api/tours` | Create new tour |
| GET | `/api/tours/{id}` | Get tour by ID |
| PUT | `/api/tours/{id}` | Update tour |
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
