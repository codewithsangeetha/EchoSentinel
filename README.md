# EchoSentinel | Acoustic Risk Assessment

### A Java-Based Acoustic Risk Assessment System

EchoSentinel is a desktop application developed using Java Swing and MySQL to assess potential acoustic risks associated with industrial machine noise. It records sound-related measurements, calculates risk scores using predefined rules, and classifies the results into risk levels.

## Features

- Record acoustic readings for different machines.
- Enter sound level, frequency, duration, and repetition rate.
- Calculate acoustic risk scores using predefined rules.
- Classify results into LOW, MEDIUM, HIGH, and CRITICAL risk levels.
- Store acoustic readings in a MySQL database.
- View reading history and risk reports.
- Use a graphical user interface built with Java Swing.

## Technologies Used

- Java
- Java Swing
- JDBC
- MySQL
- MySQL Connector/J 8.4.0

## Risk Classification

| Risk Score | Risk Level |
|---|---|
| 0–24 | LOW |
| 25–49 | MEDIUM |
| 50–74 | HIGH |
| 75–100 | CRITICAL |

## Database

Database name: `echo_sentinel`

Table name: `acoustic_readings`

The table stores machine details, acoustic measurements, calculated risk scores, risk levels, and recording timestamps.

## How It Works

1. Enter the machine name and acoustic measurements.
2. Submit the reading for analysis.
3. The system calculates a risk score using predefined rules.
4. The application displays the corresponding risk level.
5. Readings are stored in MySQL for history and reporting.

## How to Run

### Prerequisites

- Java Development Kit (JDK)
- MySQL Server
- MySQL Connector/J 8.4.0
- A Java IDE or terminal

### Compile and Run on Windows

Place the MySQL Connector/J JAR inside a folder named `lib` in the project directory.

Compile:

```bash
javac -cp ".;lib/mysql-connector-j-8.4.0.jar" *.java
```

Run:

```bash
java -cp ".;lib/mysql-connector-j-8.4.0.jar" Main
```

Configure the database connection in the project according to your local MySQL settings before running the application.

## Important Note

EchoSentinel uses predefined, rule-based calculations for educational and project demonstration purposes. It does not use machine learning or live acoustic sensors, and its risk classifications should not be treated as professional industrial safety assessments.

## Project Objective

To demonstrate how Java, object-oriented programming, database connectivity, and rule-based analysis can be combined to build an acoustic risk assessment application.

## Author

**Sangeetha J.**  
B.E. Computer Science and Engineering  
Madras Institute of Technology  
Anna University
