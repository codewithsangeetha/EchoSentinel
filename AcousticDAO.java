import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AcousticDAO {

    public boolean saveReading(AcousticReading reading) {

        String sql = "INSERT INTO acoustic_readings "
                + "(machine_name, sound_level, frequency, duration, "
                + "repetition_rate, risk_score, risk_level, recorded_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, reading.getMachineName());
            ps.setDouble(2, reading.getSoundLevel());
            ps.setDouble(3, reading.getFrequency());
            ps.setDouble(4, reading.getDuration());
            ps.setDouble(5, reading.getRepetitionRate());
            ps.setDouble(6, reading.getRiskScore());
            ps.setString(7, reading.getRiskLevel());
            ps.setTimestamp(8, Timestamp.valueOf(reading.getRecordedAt()));

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error saving acoustic reading: "
                    + e.getMessage());
            return false;
        }
    }

    public List<AcousticReading> getAllReadings() {

        List<AcousticReading> readings = new ArrayList<>();

        String sql = "SELECT machine_name, sound_level, frequency, "
                + "duration, repetition_rate, recorded_at "
                + "FROM acoustic_readings ORDER BY reading_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                AcousticReading reading = new AcousticReading(
                        rs.getString("machine_name"),
                        rs.getDouble("sound_level"),
                        rs.getDouble("frequency"),
                        rs.getDouble("duration"),
                        rs.getDouble("repetition_rate")
                );

                Timestamp timestamp = rs.getTimestamp("recorded_at");

                if (timestamp != null) {
                    reading.setRecordedAt(timestamp.toLocalDateTime());
                }

                readings.add(reading);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving readings: "
                    + e.getMessage());
        }

        return readings;
    }

    public int getReadingCount() {

        String sql = "SELECT COUNT(*) FROM acoustic_readings";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting readings: "
                    + e.getMessage());
        }

        return 0;
    }

    public int getMachineCount() {

        String sql = "SELECT COUNT(DISTINCT machine_name) "
                + "FROM acoustic_readings";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Error counting machines: "
                    + e.getMessage());
        }

        return 0;
    }
}