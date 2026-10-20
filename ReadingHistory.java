import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReadingHistory extends JPanel {

    private final Color BACKGROUND = new Color(15, 23, 42);
    private final Color CARD = new Color(30, 41, 59);
    private final Color TEAL = new Color(20, 184, 166);
    private final Color WHITE = new Color(241, 245, 249);
    private final Color MUTED = new Color(148, 163, 184);

    private final DefaultTableModel model;
    private final JTable table;

    public ReadingHistory() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Reading History");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(WHITE);

        JLabel subtitle = new JLabel(
                "Review machinery readings and their preliminary risk assessments.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.add(title);
        header.add(Box.createVerticalStrut(7));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        String[] columns = {
                "Machine Name",
                "Sound (dB)",
                "Frequency (Hz)",
                "Duration (sec)",
                "Repetition",
                "Risk Score",
                "Risk Level",
                "Recorded At"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setBackground(CARD);
        table.setForeground(WHITE);
        table.setGridColor(new Color(71, 85, 105));
        table.setSelectionBackground(TEAL);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(15, 118, 110));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(
                new Color(71, 85, 105)));

        add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh History");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshButton.setBackground(TEAL);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(new EmptyBorder(10, 20, 10, 20));

        refreshButton.addActionListener(e -> loadReadings());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        bottom.add(refreshButton);

        add(bottom, BorderLayout.SOUTH);

        loadReadings();
    }

    private void loadReadings() {
        model.setRowCount(0);

        List<AcousticReading> readings = new AcousticDAO().getAllReadings();

        for (AcousticReading reading : readings) {
            model.addRow(new Object[]{
                    reading.getMachineName(),
                    reading.getSoundLevel(),
                    reading.getFrequency(),
                    reading.getDuration(),
                    reading.getRepetitionRate(),
                    reading.getRiskScore(),
                    reading.getRiskLevel(),
                    getRecordedAt(reading)
            });
        }

        if (readings.isEmpty()) {
            model.addRow(new Object[]{
                    "No readings yet",
                    "-", "-", "-", "-", "-", "-", "-"
            });
        }
    }

    private String getRecordedAt(AcousticReading reading) {
        if (reading.getRecordedAt() == null) {
            return "Unavailable";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");

        return reading.getRecordedAt().format(formatter);
    }
}