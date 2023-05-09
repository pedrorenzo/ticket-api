# API README

This API provides endpoints to manage a IT-support ticket system.

## Project Assumptions

To make development simpler for this test, I considered the company id and author id as simple Long fields, so I wouldn't need to create APIs/tables/entities/relationships for both Company and Author.

## Endpoints

### POST /tickets
Create a new ticket for a company.

#### Request
```shell
curl --location --request POST 'http://localhost:8081/tickets' \
--header 'Content-Type: application/json' \
--data-raw '{
    "companyId": 5,
    "description": "Create a DB user for Pedro Vicentin",
    "status": "IN_PROGRESS"
}'
```
* *companyId*: the ID of the company associated with the ticket (required).
* *description*: the description of the ticket (required).
* *status*: the status of the ticket (required).

#### Response
```json
{
    "id": 10,
    "companyId": 5,
    "description": "Create a DB user for Pedro Vicentin",
    "status": "IN_PROGRESS"
}
```
* Returns 201 and a JSON object with the ID, company ID, description, and status of the created ticket.
* Returns 400 if any of the parameters in the request body are not provided.

### GET /companies/:companyId/tickets
Get a list of all tickets associated with a company (from the requirements: As a support agent I want to see a list of all tickets of any customer/company).

#### Request
```shell
curl --location --request GET 'http://localhost:8081/companies/5/tickets'
```
* *companyId*: the ID of the company (required).

#### Response
```json
{
    "companyId": 5,
    "tickets": [
        {
            "ticketId": 1,
            "description": "Create a DB user for Pedro Vicentin",
            "status": "IN_PROGRESS"
        },
        {
            "ticketId": 2,
            "description": "Restart server",
            "status": "IN_QUEUE"
        },
        {
            "ticketId": 3,
            "description": "Ticket 3 Description",
            "status": "RESOLVED"
        }
    ]
}
```
* Returns 200 and a JSON object with the company ID and a list of tickets associated with it.
* Returns an empty list if the companyId is not found in the database (Please check project assumptions).

### GET /tickets/:ticketId
Get information about a specific ticket (from the requirements: As a support agent I want to see all the information about a ticket including all comments in a
detail view).

#### Request
```shell
curl --location --request GET 'http://localhost:8081/tickets/4?include=comments'
```
* *ticketId*: the ID of the ticket (required).
* *include*: an optional parameter to include comments in the response.

#### Response
```json
{
    "id": 4,
    "companyId": 5,
    "description": "Create a DB user for Pedro Vicentin",
    "status": "RESOLVED",
    "comments": [
        {
            "commentId": 2,
            "message": "I started working on this ticket",
            "authorId": 2
        },
        {
            "commentId": 3,
            "message": "Ticket in progress again",
            "authorId": 5
        },
        {
            "commentId": 4,
            "message": "Ticket resolved now, finally",
            "authorId": 5
        }
    ]
}
```
* Returns 200 and a JSON object with information about the ticket, including any comments if the "include" parameter is present.
* Returns 404 if the ID is not found.

### POST /tickets/:ticketId/comments
Add a comment to a ticket (from the requirements: As a support agent I want to add comments to an existing ticket, so that I can ask the customer
about missing information or give them feedback).

#### Request
```shell
curl --location --request POST 'http://localhost:8081/tickets/4/comments' \
--header 'Content-Type: application/json' \
--data-raw '{
    "message": "I started working on this ticket",
    "authorId": 2
}'
```
* *ticketId*: the ID of the ticket (required).
* *message*: the message (required).
* *authorId*: the id of the author (required).

#### Response
```json
{
    "id": 7,
    "ticketId": 4,
    "message": "I started working on this ticket",
    "authorId": 2
}
```
* Returns 201 and a JSON object with the ID, ticket ID, message, and author.
* Returns 400 if any of the parameters in the request body are not provided or 404 if the ID is not found.

### PATCH /tickets/:ticketId/status
Update the ticket status, with or without a comment (from the requirements: As a support agent I want to update the status of a ticket and add more information to it in a
comment at the same time, so that the customer can see the status and understands it).

#### Request
```shell
curl --location --request PATCH 'http://localhost:8081/tickets/4/status' \
--header 'Content-Type: application/json' \
--data-raw '{
    "status": "RESOLVED",
    "comment": {
        "message": "Ticket resolved now, finally",
        "authorId": "5"
    }
}'
```
* *ticketId*: the ID of the ticket (required).
* *status*: the status of the ticket (required).
* *comment*: the comment to be added.
  * *comment.message*: the message (required if comment is present).
  * *comment.authorId*: the id of the author (required if comment is present).

#### Response
* Returns 204 if update was successfull.
* Returns 400 if any required parameter in the request body are not provided.
* Returns 404 if the ID is not found.

## Technologies/frameworks used

* Java 17
* Maven
* Spring Boot
* Spring Data
* Docker/Docker Compose
* MySQL
* JUnit
* Mockito
* Mock MVC

### To run the application:

To run the application you will need at least to have installed [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/).

Using your terminal, go to the /ticket-api/docker folder and run on your cmd:
* *docker build -t java-api .*
* *docker-compose build --no-cache*
* *docker-compose up*

With this commands, your application will be running.

### If you are a developer:

You will need to have installed [Git](https://git-scm.com/downloads), [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html), [Docker](https://docs.docker.com/get-docker/), [Docker Compose](https://docs.docker.com/compose/install/), [Maven](https://maven.apache.org/download.cgi), [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows) (or any other IDE of your choice) and [MySQL](https://www.mysql.com/downloads/) (optional, you can work with your docker containers :)).

To test your modifications using Docker containers, you will have to run, using your terminal:
* In /ticket-api/docker folder: *docker-compose up -d mysqldb* - Now you have your MySQL database up and running!
* In /ticket-api/ folder: *mvn clean install* (this command will also run all tests developed in the application)
* Back to /ticket-api/docker folder:
  * *docker build -t java-api .*
  * *docker-compose build --no-cache*
  * *docker-compose up*