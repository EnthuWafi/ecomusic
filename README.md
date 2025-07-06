# Moosic: The Eco-Music Platform

A half-polished Java-based web application for music streaming and artist subscriptions — cobbled together with Jakarta EE, broken dreams, and occasional React components. Did I mention that I used Oracle 11g as the database for this project? The karma of being too lazy to use actual databases I suppose.

![Logo](src/main/webapp/assets/images/logo.svg)
(what is this logo even)
---

## ⚙️ Tech Stack 

- **Jakarta EE**
- **Oracle 11g**
- **Tomcat**
- **Stripe API**
- **MapStruct**
- **PlantUML + Maven Plugin**

---

## 🚀 Features

- User registration & authentication
- Music upload & streaming
- Like system + playback history + some stats
- Stripe-based subscription system (the only reliable thing in this project)
- Random mix of REST APIs and passing info from Servlets to JSPs because of reasons
- There's no test because why would I need any??
- Half-assed REST API (not going to bother show the routing)
- Business logic leaks into JSP like a pipe burst
- 60% of development time was spent "structuring" packages
- It's still ugly


> **Cool command:**
> ```
> stripe listen --forward-to localhost:8081/ecomusic/webhook/stripe
> ```


## 🧾 Config Setup

Edit this file with your own values before running:

> src/main/webapp/WEB-INF/classes/config.properties.test

This project started with good intentions. It ended with a commit history that looks like a cry for help.
