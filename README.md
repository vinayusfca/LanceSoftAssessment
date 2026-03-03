# Banking Notification Batch Application

## Overview

The application uses **Spring Batch** to perform two scheduled operations:

* At **2:00 AM** – Insert 100 Account records into the database.
* At **3:00 PM** – Read all accounts and send balance notification.


---

## Project Structure

```
com.example.lancesoft
 ├── LancesoftApplication
 └── notification
      ├── entity
      ├── repository
      ├── service
      ├── batch
      ├── scheduler
      └── controller
```

---

## How to Run the Application

### Using Terminal

```
mvn clean install
mvn spring-boot:run
```

---

## Application Behaviour

* Application starts and initializes Spring Batch.
* Insert Job runs at 2 AM and creates 100 account records.
<img width="1128" height="402" alt="Screenshot 2026-03-03 at 7 42 22 PM" src="https://github.com/user-attachments/assets/18470c1b-a943-49e3-a549-cab99c3ec869" />
* Verify the data using the endpoint - http://localhost:8080/accounts
<img width="1435" height="761" alt="Screenshot 2026-03-03 at 7 43 47 PM" src="https://github.com/user-attachments/assets/75807cd1-7e90-4c4e-bf27-cdb8c2eee8c8" />
* Notification Job runs at 3 PM and sends balance notifications.
<img width="1133" height="368" alt="Screenshot 2026-03-03 at 7 44 14 PM" src="https://github.com/user-attachments/assets/0be894f8-184b-4b17-8708-a03ad53ef6c8" />

