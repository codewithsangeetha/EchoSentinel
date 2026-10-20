import java.time.LocalDateTime;

public class AcousticReading {
    private String machineName;
    private double soundLevel;
    private double frequency;
    private double duration;
    private double repetitionRate;
    private double riskScore;
    private String riskLevel;
    private LocalDateTime recordedAt;

    public AcousticReading(String machineName, double soundLevel,
                           double frequency, double duration,
                           double repetitionRate) {
        this.machineName = machineName;
        this.soundLevel = soundLevel;
        this.frequency = frequency;
        this.duration = duration;
        this.repetitionRate = repetitionRate;
        this.recordedAt = LocalDateTime.now();

        calculateRisk();
    }

    private void calculateRisk() {
        double soundRisk = Math.min(
                Math.max((soundLevel - 60) / 50.0 * 100, 0), 100);

        double frequencyRisk = Math.min(
                Math.max(frequency / 10000.0 * 100, 0), 100);

        double durationRisk = Math.min(
                Math.max(duration / 120.0 * 100, 0), 100);

        double repetitionRisk = Math.min(
                Math.max(repetitionRate / 20.0 * 100, 0), 100);

        riskScore = soundRisk * 0.40
                  + frequencyRisk * 0.25
                  + durationRisk * 0.15
                  + repetitionRisk * 0.20;

        riskScore = Math.round(riskScore);

        if (riskScore < 25) {
            riskLevel = "LOW";
        } else if (riskScore < 50) {
            riskLevel = "MEDIUM";
        } else if (riskScore < 75) {
            riskLevel = "HIGH";
        } else {
            riskLevel = "CRITICAL";
        }
    }

    public String getMachineName() {
        return machineName;
    }

    public double getSoundLevel() {
        return soundLevel;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getDuration() {
        return duration;
    }

    public double getRepetitionRate() {
        return repetitionRate;
    }

    public double getRiskScore() {
        return riskScore;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}