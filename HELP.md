# Git Repo Reader

Git Repo Reader is a Spring Boot application that fetches and displays non-forked repositories of a GitHub user along with the branches of each repository.

## Features

* Fetches all repositories of a given GitHub user.
* Filters out forked repositories.
* Retrieves and displays branches for each non-forked repository.
* Returns data in a simplified DTO format.

## Prerequisites

Before you begin, ensure you have the following installed:

* Java 17+ (compatible with your Java version)
* Maven for dependency management and building the project
* Git for version control

## Installation

#### 1. Clone the repository:

`git clone https://github.com/irainbovvi/git-repo-reader.git`

#### 2. Build the project:

Navigate to the project root and run:

`mvn clean install`

## Usage

#### 1. Run the application:

After building the project, you can run the Spring Boot application using:

`mvn spring-boot:run`

#### 2. Access the service:

The application provides a RESTful endpoint. You can access it using a tool like curl or Postman.

Endpoint:

`GET /api/repos/{username}`

Example Request:

`curl http://localhost:8080/api/repos/testuser`

Example Response:

```
{
    "name": "test-application",
    "ownerLogin": "user",
    "branches": [
        {
            "name": "master",
            "commit": {
                "sha": "103259s2183af013b07a0f15c06c2af2fadcb05"
            }
        }
    ]
}
```

## Running Tests

You can run the unit tests using Maven:

`mvn test`

Tests are located in the src/test/java/com/example/git_repo_reader directory and are used to ensure that the service behaves as expected.

## Technologies Used
* **Spring Boot:** Framework for building the application
* **RestTemplate:** For making REST API calls to GitHub
* **JUnit 5:** For writing and running tests
* **Mockito:** For mocking dependencies in tests
* **Maven:** For dependency management and build automation

## Contact

For any questions or suggestions, feel free to reach out:
* Email: vladarius23@gmail.com
* GitHub: irainbovvi
