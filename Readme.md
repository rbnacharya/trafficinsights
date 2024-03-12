

# TrafficInsights

TrafficInsights is a demo project designed to demonstrate some APIs. It showcases how to build and run a Spring Boot application that interacts with an in-memory H2 database for development purposes. The application is easily configurable to connect to other types of databases.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java JDK 17 installed
- Gradle (compatible with the version used in the project, preferably the latest)
- Docker (optional, for containerization)

## Installing and Running TrafficInsights

Follow these steps to get your TrafficInsights up and running:

1. **Clone the repository**

    ```sh
    git clone https://github.com/rbnacharya/trafficinsights.git
    ```

2. **Navigate into the project directory**

    ```sh
    cd trafficinsights
    ```

3. **Build the project**

   Using Gradle:
    ```sh
    ./gradlew build
    ```

4. **Run the application**

   Using Gradle:
    ```sh
    ./gradlew bootRun
    ```

   Or running as a packaged application:
    ```sh
    java -jar build/libs/trafficinsights-0.0.1-SNAPSHOT.jar
    ```

   **Dockerization:**

   To containerize TrafficInsights, use the command as:
```sh
DOCKER_BUILDKIT=1 docker build --platform linux/amd64 -t trafficinsights:0.0.1-SNAPSHOT .
```
   This will create a Docker image, to run the image use the command as:
```sh
docker run -p 8080:8080 -t trafficinsights:0.0.1-SNAPSHOT
```
## Application Configuration

Update an `src/main/resources/application.properties` to update database configuration. For example, to use an in-memory H2 database, use the following properties:

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create
spring.h2.console.enabled=true
spring.jpa.defer-datasource-initialization=true
```
## API Usage

### Sending a Request

To send a request, use the following `curl` command:

```sh
curl --location 'http://localhost:8080/request' \
--header 'Content-Type: application/json' \
--data '{
    "customerID": 1,
    "tagID": 1,
    "userID": "abc",
    "timestamp": 1710055228,
    "remoteIP": "192.168.1.2",
    "userAgent": "Firefox/1201"
}'
```

#### Request Constraints:

- All fields are required.
- `customerID` should already exist in the database. (By default 1L is available, check src/main/resources/data.sql, to prepopulate data)
- `timestamp` should not be after the current time and should be within 1 year from the current time.
- If `remoteIP` or `userAgent` is blacklisted, the request will be marked as invalid.

### Retrieving Statistics

To get statistics for a single customer for a single day, use the following `curl` command:

```sh
curl --location 'http://localhost:8080/statistics?customerID=1&date=2024-03-10'
```

#### Sample Response Format:

```json
{
    "customerID": 1,
    "requestCount": 5,
    "invalidCount": 1,
    "date": "2024-03-10"
}
```

This response includes the number of requests and the number of invalid requests for the specified customer on the given date.

To connect to a different database, replace these properties with the appropriate ones for your database.

## Contact

If you want to contact me, you can reach me at rbn@gerer.io.
