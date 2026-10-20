CREATE DATABASE IF NOT EXISTS echo_sentinel;

USE echo_sentinel;

CREATE TABLE IF NOT EXISTS acoustic_readings (
    reading_id INT AUTO_INCREMENT PRIMARY KEY,
    machine_name VARCHAR(100) NOT NULL,
    sound_level DOUBLE NOT NULL,
    frequency DOUBLE NOT NULL,
    duration DOUBLE NOT NULL,
    repetition_rate DOUBLE NOT NULL,
    risk_score INT NOT NULL,
    risk_level VARCHAR(20) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);