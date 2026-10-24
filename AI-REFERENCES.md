AI Reference 1
Prompt:
Help me begin Lab 4 in my existing Spring Boot project by adding Thymeleaf and a Bootstrap user management landing page, following the assignment guide and rubric.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-thymeleaf-bootstrap — Initial Setup and User Management Landing Page
How the Response Influenced the Work:
The response helped organize the assignment into feature branches and provided code for the Thymeleaf dependency, page controller, and Bootstrap landing page.
Student Modifications / Verification:
I added the dependency and page files, ran the application, and checked the landing page in the browser. I committed and pushed the changes, then merged pull request #14 into main.


AI Reference 2
Prompt:
Help me create the User entity, Role enum, and PostgreSQL users table for Lab 4, including the account status and password-change fields.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-user-entity — User Entity, Role Enum, and Database Script
How the Response Influenced the Work:
The response provided code for the User entity, the ADMIN and USER roles, and a PostgreSQL script defining the users table and initial administrator record.
Student Modifications / Verification:
I added the Java files and SQL script, worked through the database setup in pgAdmin, and ran Maven compilation successfully. I committed and pushed the changes, then merged pull request #15 into main.


AI Reference 3
Prompt:
Help me create a Spring Data JPA repository for user accounts and integration tests that verify its queries against PostgreSQL.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-user-repository — User Repository and Integration Tests
How the Response Influenced the Work:
The response provided the UserRepository interface and integration tests for user lookup, username existence, and administrator queries.
Student Modifications / Verification:
I added the repository and test files, then ran UserRepositoryTest against PostgreSQL. All three tests passed with no failures or errors. I committed and pushed the changes, then merged pull request #16 into main.


AI Reference 4
Prompt:
Help me create the user service and form ViewModels for registration, login, account updates, and password changes, with validation and business-rule tests.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-user-service-viewmodels — User Service, ViewModels, and Business Rules
How the Response Influenced the Work:
The response provided the UserService, five ViewModels, and tests. The service handles duplicate usernames, password matching, enabled-account checks, account updates, and protection of the last enabled administrator. User summaries exclude passwords.
Student Modifications / Verification:
I added the service, ViewModels, and test files. Maven compilation succeeded, and all 10 UserServiceTest tests passed with no failures or errors. I committed and pushed the changes, then merged pull request #17 into main.


AI Reference 5
Prompt:
Help me build a Bootstrap registration form and Thymeleaf controller that validate input, reject duplicate usernames, and always register new accounts with the USER role.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-registration — Registration Page and Validation
How the Response Influenced the Work:
The response provided RegistrationController and the registration template, including validation messages and a redirect to the login page after successful registration. It also restricted form binding to the registration fields.
Student Modifications / Verification:
I added the files and checked successful registration, mismatched passwords, and duplicate usernames. I submitted a request containing role=ADMIN and verified in PostgreSQL that the account was still created with the USER role. I committed and pushed the changes, then merged pull request #18 into main.


AI Reference 6
Prompt:
Help me build a manual login page that validates credentials, rejects disabled accounts, and redirects users who must change their password.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-login — Login Form and Password-Change Redirect
How the Response Influenced the Work:
The response provided LoginController and the Bootstrap login template. The controller delegates credential checks to UserService, displays a general failure message, and redirects successful logins according to the password-change flag. It uses demonstration session state without implementing Spring Security.
Student Modifications / Verification:
I added the files, updated the home page, and compiled successfully. I checked login behavior and used curl to confirm that an account requiring a password change redirected to /change-password. I committed and pushed the changes, then merged pull request #19 into main.


AI Reference 7
Prompt:
Help me create user administration pages that list accounts and allow updates to roles, enabled status, and password-change requirements, with server-side validation.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-user-administration — Account Listing and Editing
How the Response Influenced the Work:
The response provided UserAdminController and Bootstrap templates for listing and editing accounts. The pages use ViewModels, exclude passwords from the list, and display validation errors from account updates.
Student Modifications / Verification:
I added the controller and templates and compiled successfully. I checked account updates and submitted an invalid SUPERADMIN role through curl, which produced the validation message “Role must be ADMIN or USER.” I committed and pushed the changes, then merged pull request #20 into main.


AI Reference 8
Prompt:
Help me add shared Bootstrap navigation to the user pages, including a collapsible menu for mobile screens.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-responsive-navigation — Shared Responsive Navigation
How the Response Influenced the Work:
The response provided a reusable Thymeleaf navbar fragment and Bootstrap JavaScript fragment, with instructions for including them in the user pages.
Student Modifications / Verification:
I added both fragments to the five existing user pages and checked their inclusion using PowerShell. Compilation succeeded. I also verified that the menu expanded and displayed all four-navigation links at a mobile viewport of 375 × 812. The implementation was committed, pushed, and merged through pull request #21.


AI Reference 9
Prompt:
Help me automatically create an initial administrator when no ADMIN account exists and build a validated password-change workflow.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
feature/lab4-bootstrap-password-change — Administrator Bootstrap and Password Changes
How the Response Influenced the Work:
The response provided AdminBootstrap, PasswordChangeController, a Bootstrap password-change template, and bootstrap unit tests. The workflow checks the current password, validates the new password and confirmation, and clears the password-change requirement after success.
Student Modifications / Verification:
I added the files and compiled successfully. All three AdminBootstrapTest unit tests passed. Browser and PostgreSQL checks confirmed password validation, successful password changes, clearing of the password-change flag, and login with the updated password. These checks did not demonstrate administrator creation against an empty live database. I committed and pushed the implementation, then merged pull request #22 into main.


AI Reference 10
Prompt:
Help me diagnose the Maven test failure, document application setup and AI assistance, and organize screenshots with captions for my Lab 4 report.
AI System: ChatGPT
Date Used: October 24, 2026
Related Branch / Task:
docs/lab4-documentation — Troubleshooting, README, AI References, and Report
How the Response Influenced the Work:
The response helped identify a missing DB_PASSWORD environment variable in the test terminal. It also supplied README and AI-reference drafts, screenshot instructions, captions, and explanations of the intentional Lab 4 security limitations.
Student Modifications / Verification:
I set the database password in the correct PowerShell terminal and reran the tests. All 22 tests passed with no failures or errors. I saved the documentation drafts and added screenshots and captions to my Word report. I requested that the AI references follow the format used in my previous lab. Prompts labeled “paraphrased” summarize the interactions; the saved conversation preserves the original wording through October 22.

