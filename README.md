### Environment Setup

1. **Install JDK 11 locally** This project was developed with Java 11 which is also required by the version of 
SpringBoot and Gradle used in this project.
2. **Clone the code from GitHub** https://github.com/larry-han-au/student-enrollment-larry

### How to run and test the project

1. **How to run**  `./gradlew bootRun`
2. **How to test**  `./gradlew test`
3. **How to run test coverage** `./gradlew jacocoTestReport` You can check the test coverage report
in `./build/reports/jacoco/test/html/index.html`

### Some Restrictions 

Courses data was seeded by `data.sql` and can't be manipulated. Current work is focused on implementing `Student`
resource so there is no way to fetch `Courses` via api. To check the schema of courses, look at the
`/src/main/resources/data.sql` file.

### Use cases

To make it easy to understand the allowed schema, you can access the apis from the swagger-ui via

`http://localhost:8080/swagger-ui.html`

### Future work

The following of works will make the project more prod ready but can't be implemented within the limited time.

1. CRUD for `Course` resource
2. Separation of profiles to `dev`, `test`, `prod`
3. Separation of DB and disable swagger in prod
4. Containerization