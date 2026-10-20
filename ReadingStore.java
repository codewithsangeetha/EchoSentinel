import java.util.ArrayList;
import java.util.List;

public class ReadingStore {

    private static final List<AcousticReading> readings = new ArrayList<>();

    public static void addReading(AcousticReading reading) {
        readings.add(reading);
    }

    public static List<AcousticReading> getReadings() {
        return new ArrayList<>(readings);
    }
}