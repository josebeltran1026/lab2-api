# CST-339 Lab 4 — User Registration, Login, and Administration

**Author:** Jose Beltran Veles

This project extends the existing Album REST API with Thymeleaf pages,
Bootstrap styling, and PostgreSQL-backed user management.

## Lab 4 Security Scope

This lab intentionally stores and compares plaintext passwords.
Spring Security authentication and role authorization are not implemented.
Administration routes remain unprotected.

The session attribute `demoUserId` supports the password-change demonstration;
it is not a complete authentication system. Security will be added in Lab 5.

## Requirements

- Java 21
- PostgreSQL with the Chinook database
- Git
- Internet access for Maven dependencies and Bootstrap CDN assets

The repository includes the Maven wrapper.

## Database Setup

The default database connection is:

- URL: `jdbc:postgresql://localhost:5432/chinook`
- Username: `postgres`

Run `sql_scripts/users.sql` against the Chinook database to create the
users table and insert the initial administrator if its username is absent.

To demonstrate application startup creation instead, execute only the
table-creation portion of the script on a fresh database before starting
the application.

Hibernate schema generation is disabled, so the table must exist first.

## Environment Variables

Supported variables:

| Variable | Default |
| --- | --- |
| DB_URL | jdbc:postgresql://localhost:5432/chinook |
| DB_USERNAME | postgres |
| DB_PASSWORD | Must be supplied |
| PORT | 8080 |

Set the database password in each PowerShell terminal used to run the
application or database-backed tests:

```powershell
$securePassword = Read-Host "Enter your PostgreSQL password" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $securePassword).Password
Remove-Variable securePassword
```

Do not commit database credentials.

## Run the Application

From the project directory:

```powershell
.\mvnw.cmd spring-boot:run
```

Open `http://localhost:8080/users`.

## Pages and API

| Path | Purpose |
| --- | --- |
| /users | User management landing page |
| /register | Register a USER account |
| /login | Manual credential validation |
| /change-password | Change the demonstration login account's password |
| /admin/users | List account summaries without passwords |
| /admin/users/{id}/edit | Update role and account flags |
| /swagger-ui.html | Album API documentation |
| /api/albums | Existing Album REST API |

All user pages use shared responsive Bootstrap navigation.

## Initial Administrator

At startup, the application checks whether any ADMIN account exists.
If none exists, it creates:

- Username: `admin`
- Initial password: `password`
- Role: ADMIN
- Enabled: true
- Password change required: true

Existing administrators are preserved, including their passwords.
If no ADMIN exists but a non-admin account already owns username `admin`,
startup fails with a clear message rather than promoting that account.

A flagged account is redirected to the password-change page after login.
A successful change clears the flag and redirects to login again.
Use the new password for subsequent logins.

## Validation and Business Rules

- Registration requires a unique username and matching passwords.
- Registration always creates USER accounts.
- Disabled accounts cannot pass login validation.
- Account updates accept only ADMIN or USER roles.
- The last enabled administrator cannot be disabled or demoted.
- Password changes verify the current password and require matching,
  different new passwords.
- Form-specific ViewModels limit the fields accepted from each form.

## Tests

Set DB_PASSWORD in the test terminal, then run:

```powershell
.\mvnw.cmd test
```

The suite contains 22 tests covering application startup, Album API
integration, user persistence, service rules, and administrator bootstrap.

Database-backed tests require PostgreSQL and the expected schema.
Bootstrap unit tests use a mocked repository.

Browser checks also covered registration, login, account updates,
responsive navigation, and the password-change workflow.

## Git Workflow

Lab 4 was implemented through nine feature branches, each submitted
through a pull request and merged into main:

1. feature/lab4-thymeleaf-bootstrap
2. feature/lab4-user-entity
3. feature/lab4-user-repository
4. feature/lab4-user-service-viewmodels
5. feature/lab4-registration
6. feature/lab4-login
7. feature/lab4-user-administration
8. feature/lab4-responsive-navigation
9. feature/lab4-bootstrap-password-change