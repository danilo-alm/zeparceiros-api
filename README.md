# ZeParceiros API

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Starting the application](#starting-the-application)
- [API Endpoints](#api-endpoints)
- [Postman](#postman)

## Introduction

ZeParceiros is a service that provides an API using REST to manage partners. This API is a solution to [Zé Backend Challenge](https://github.com/ab-inbev-ze-company/ze-code-challenges/blob/master/backend.md).

## Features

1. **Create a Partner**: Save a partner in the database.
2. **Load Partner by ID**: Retrieve a specific partner by its ID.
3. **Search Partner**: Find the nearest partner whose coverage area includes a given location.

## Technologies Used

- Programming Language: Java
- Database: PostgreSQL
- Framework: Spring Boot (for REST API)
- GeoJSON Handling: JTS (Java Topology Suite) & [PostGIS](https://postgis.net/)
- Containerization: Docker

## Getting Started

### Prerequisites

- Docker
- Docker Compose

### Starting the application

1. Clone the repository:
   ```sh
   git clone https://github.com/danilo-alm/zeparceiros-api.git && cd zeparceiros-api
   ```

2. Build and start the services using Docker Compose:
   ```sh
   docker-compose up --build
   ```

3. The API should be accessible at `http://localhost:8080`.

## API Endpoints

### Create a Partner
- **URL**: `/partners`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "tradingName": "Adega da Cerveja - Pinheiros",
    "ownerName": "Zé da Silva",
    "document": "1432132123891/0001",
    "coverageArea": {
      "type": "MultiPolygon",
      "coordinates": [
        [[[30, 20], [45, 40], [10, 40], [30, 20]]],
        [[[15, 5], [40, 10], [10, 20], [5, 10], [15, 5]]]
      ]
    },
    "address": {
      "type": "Point",
      "coordinates": [-46.57421, -21.785741]
    }
  }
  ```
- **Response**: Partner with ID.

### Create Multiple Partners
- **URL**: `/partners/batch`
- **Method**: `POST`
- **Request Body**: [pdvs.json](https://raw.githubusercontent.com/ab-inbev-ze-company/ze-code-challenges/master/files/pdvs.json)
- **Response**: List of `Partner`

### Load Partner by ID
- **URL**: `/partners/{id}`
- **Method**: `GET`
- **Response**: `Partner`

### Search Partner
- **URL**: `/partners/search`
- **Method**: `GET`
- **Query Parameters**:
    - `lat`: Latitude of the location
    - `lon`: Longitude of the location
- **Response**: `Partner`

## Postman

You can easily make requests using [this Postman request collection](https://www.postman.com/aviation-candidate-54355508/workspace/my-public-workspace/collection/28291732-99ace203-6804-4282-b676-21f7e52fb22e?action=share&creator=28291732&active-environment=28291732-0f919800-be8f-4a5e-bbde-d9f3e6790b3a).
