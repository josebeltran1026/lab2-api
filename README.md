# CST-339 Lab 5 — Spring Security and Authorization

**Author:** Jose Beltran Veles

This project secures the Chinook Album API and the Lab 4 user-management
pages with Spring Security. Spring Security authenticates users against
PostgreSQL, verifies passwords with BCrypt, and enforces the `USER` and
`ADMIN` roles.

## Requirements

- Java 21
- PostgreSQL with the Chinook schema and sample data
- Git
- Internet access for Maven dependencies and Bootstrap CDN assets

The repository includes the Maven wrapper.

## Local Setup

1. Load the Chinook schema and sample data into PostgreSQL.
2. Run `sql_scripts/users.sql` against the same database to create the
   `users` table. Hibernate schema generation is disabled by default.
3. Set the database connection variables in PowerShell and start the app.

```powershell
$env:DB_URL = 'jdbc:postgresql://localhost:5432/chinook'
$env:DB_USERNAME = 'postgres'
$secureDbPassword = Read-Host 'Enter your PostgreSQL password' -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $secureDbPassword).Password
Remove-Variable secureDbPassword

.\mvnw.cmd spring-boot:run
```

Use the URL and username for your own PostgreSQL instance. The application
listens on port `8080` unless `PORT` is set. Keep database credentials,
passwords, and bootstrap hashes out of Git.

## Initial Administrator

On a database without an `ADMIN` account, set `LAB5_BOOTSTRAP_ADMIN_HASH`
to a valid BCrypt hash of a private temporary password before starting
the app. Supply the hash through the environment. The SQL script creates
the table, and the application creates the initial `admin` account.

The first admin sign-in requires a password change. Enter the temporary
password as the current password, choose a new password, and sign in
again. Once the account is working, remove the one-time bootstrap setting.
The administrator and its updated password hash are stored in PostgreSQL.

## Pages and API Permissions

| Route or operation | Access |
| --- | --- |
| `/`, `/register`, `/login` | Public |
| `/swagger-ui/index.html`, `/v3/api-docs` | Public documentation |
| `/users` | Signed-in users |
| `/change-password` | Signed-in users |
| `/admin/users`, `/admin/users/{id}/edit` | `ADMIN` only |
| Album API `GET` | `USER` or `ADMIN` |
| Album API `POST`, `PUT`, `DELETE` | `ADMIN` with a valid CSRF token |

Browser pages use form login and an authenticated session. API clients
can use HTTP Basic authentication. Accounts flagged for a password change
must complete it before accessing protected pages or the Album API.

## Demo Walkthrough

1. Open Swagger at `http://localhost:8080/swagger-ui/index.html` and
   execute `GET /api/albums/1` without credentials. Expect `401`.
2. Register a new account at `http://localhost:8080/register`. Verify in
   PostgreSQL that its role is `USER` and its password is stored in BCrypt
   format. Show the format check result without displaying the hash.
3. Sign in as the new user. Confirm that `/admin/users` returns `403`.
   Sign out and confirm that `/users` redirects to `/login`.
4. In Swagger, select **Authorize** with the new user's credentials.
   Repeat the album GET and expect `200`. USER write requests are denied.
5. Sign in as `admin`. On a fresh database, demonstrate the required
   password change. Sign in with the new password and open `/admin/users`.
6. Use a disposable album for the admin write demo. Create it with POST,
   update it with PUT, then remove it with DELETE. Supply a valid CSRF token
   for each write and keep the associated session. A `403` from a write
   without a valid CSRF token does not demonstrate role authorization.

Lab 4 handled login with a manual credential check. Lab 5 uses Spring
Security to authenticate users, establish sessions, and enforce roles at
the HTTP request boundary. A public Swagger page can describe endpoints
whose execution requires authentication.

## Tests

Point the database variables at a local PostgreSQL test database with
the expected Chinook and user schemas, then run:

```powershell
.\mvnw.cmd test
```

The Lab 5 verification run passed all 34 tests. The suite covers public
routes, database authentication, role-based MVC and API access, disabled
accounts, and the forced password-change flow.

## Deployment

The app is deployed on Render using the repository's Dockerfile and a
hosted Neon PostgreSQL database. Load the Chinook schema and run
`sql_scripts/users.sql` on the hosted database before first startup.
Configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` in the Render service
environment. On an empty user database, use `LAB5_BOOTSTRAP_ADMIN_HASH`
for the one-time administrator setup described above.

[Hosted application](https://lab2-api-8o7s.onrender.com) |
[Hosted Swagger](https://lab2-api-8o7s.onrender.com/swagger-ui/index.html)

## Git Workflow

Lab 5 was implemented in ten feature branches following the lab guide.
The pull requests and merge history record the work from adding Spring
Security through hosted verification.

Lab 4 was implemented through nine feature branches, each submitted
through a pull request and merged into `main`:

1. `feature/lab4-thymeleaf-bootstrap`
2. `feature/lab4-user-entity`
3. `feature/lab4-user-repository`
4. `feature/lab4-user-service-viewmodels`
5. `feature/lab4-registration`
6. `feature/lab4-login`
7. `feature/lab4-user-administration`
8. `feature/lab4-responsive-navigation`
9. `feature/lab4-bootstrap-password-change`