# WiJungle Sales CRM — Backend only
Independent portfolio CRM prototype inspired by a cybersecurity vendor sales process; it is not WiJungle's official internal software.

## Features
- Spring Boot REST APIs, JPA/Hibernate, H2 local database, PostgreSQL driver support
- JWT authentication, BCrypt password hashing, Admin/Sales Manager/Sales Rep roles
- Leads with filtering, pagination, status, estimated value and follow-up date
- Sales activity history: calls, emails, meetings, demos and notes
- Dashboard summary, overdue follow-ups, Swagger documentation and validation

## Run
JDK 21 and Maven 3.9+: `mvn spring-boot:run`
Swagger: http://localhost:8080/swagger-ui.html

Local-only demo admin: `admin@local.test` / `ChangeMe123!`. Change `ADMIN_PASSWORD` and `JWT_SECRET` before deploying. Do not use default credentials or real customer data without a security review.

## Login
`POST /api/auth/login` body: `{"email":"admin@local.test","password":"ChangeMe123!"}`. Copy `accessToken`, then use Swagger Authorize with `Bearer <token>`. Public registration only creates SALES_REP accounts. Admin can create role-based users at `POST /api/auth/users`.

## Lead endpoints
- `GET /api/leads?page=0&size=20&search=university&status=QUALIFIED`
- `GET /api/leads/{id}`
- `POST /api/leads` body: `{"contactName":"Amit Sharma","companyName":"ABC University","email":"amit@example.com","industry":"Education","leadSource":"Website","status":"NEW","estimatedValue":800000,"assignedTo":"admin@local.test","nextFollowUpDate":"2026-10-25","notes":"Product demo requested"}`
- `PUT /api/leads/{id}`, `PATCH /api/leads/{id}/status` body `{"status":"DEMO_SCHEDULED"}`, `DELETE /api/leads/{id}`
- `GET /api/leads/{id}/activities`; `POST /api/leads/{id}/activities` body `{"type":"DEMO","summary":"Demo scheduled"}`
- `GET /api/dashboard/summary`

## PostgreSQL environment variables
Set `DB_URL=jdbc:postgresql://HOST:5432/DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `DB_DRIVER=org.postgresql.Driver`, `JWT_SECRET` (32+ random bytes), `ADMIN_PASSWORD`, and disable H2 console in production.

## Before real deployment
Add integration tests for auth and permissions, verify record-level ownership for every endpoint, add Flyway/Liquibase migrations, rate limiting, audit logs, secret management and database backups. This is a learning starter, not production-audited software.
