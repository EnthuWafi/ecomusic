# Ecomusic Platform

A Java-based web application for music streaming and artist subscriptions built with Jakarta EE. This platform includes user management, music catalog, play history tracking, and subscription features, powered by a robust backend and modern architecture.

## 📦 Tech Stack

- **Jakarta EE (Servlets, JSP)**
- **Oracle 11g**
- **Tomcat**
- **Stripe API for subscriptions**
- **MapStruct for DTO mapping**
- **PlantUML + PlantUML Maven Plugin for class diagrams**

## 📐 UML Class Diagram

The diagram below shows a snapshot of the core domain model, automatically generated from compiled Java `.class` files using [GenUML](https://github.com/samuller/genuml):

![Class Diagram](target/uml/class-diagram.png)


## 🚀 Features

- User registration & authentication
- Music upload & streaming
- Like, play history, and daily stats tracking
- Artist subscription via Stripe
- Admin role support
- REST-style service layer

## 🛠️ Dev Setup

```bash
# Generate UML
genuml insert --class-dir target/ecomusic-0.0.1-SNAPSHOT/WEB-INF/classes template.puml > output.puml 
java -jar plantuml.jar target/uml/output.puml
java -jar plantuml.jar target/uml/class-diagram.puml 
