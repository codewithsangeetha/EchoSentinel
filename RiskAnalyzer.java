import java.util.ArrayList;
import java.util.List;

public class RiskAnalyzer {

    public String identifyPossibleCause(AcousticReading reading) {
        double sound = reading.getSoundLevel();
        double frequency = reading.getFrequency();
        double repetition = reading.getRepetitionRate();

        List<String> possibleCauses = new ArrayList<>();

        if (sound >= 90) {
            possibleCauses.add("Elevated sound level");
        }

        if (frequency >= 5000) {
            possibleCauses.add("Possible high-frequency friction or vibration");
        }

        if (repetition >= 10) {
            possibleCauses.add("Repeated acoustic bursts");
        }

        if (possibleCauses.isEmpty()) {
            return "No obvious abnormal pattern under current rules";
        }

        return String.join("; ", possibleCauses);
    }

    public String getRecommendation(AcousticReading reading) {
        String level = reading.getRiskLevel();

        switch (level) {
            case "LOW":
                return "Continue routine monitoring.";

            case "MEDIUM":
                return "Record another reading and monitor for changes.";

            case "HIGH":
                return "Arrange a maintenance inspection soon.";

            case "CRITICAL":
                return "Arrange prompt inspection and follow site safety procedures.";

            default:
                return "Review the measurements.";
        }
    }

    public String generateReport(AcousticReading reading) {
        return "ECHOSENTINEL RISK ASSESSMENT REPORT\n"
                + "----------------------------------\n"
                + "Machine: " + reading.getMachineName() + "\n"
                + "Sound Level: " + reading.getSoundLevel() + " dB\n"
                + "Frequency: " + reading.getFrequency() + " Hz\n"
                + "Duration: " + reading.getDuration() + " seconds\n"
                + "Repetition Rate: " + reading.getRepetitionRate() + "\n"
                + "Risk Score: " + reading.getRiskScore() + "/100\n"
                + "Risk Level: " + reading.getRiskLevel() + "\n"
                + "Possible Pattern: " + identifyPossibleCause(reading) + "\n"
                + "Recommendation: " + getRecommendation(reading) + "\n"
                + "----------------------------------\n"
                + "Note: This is a rule-based screening result. "
                + "It does not confirm an actual machine defect.";
    }
}