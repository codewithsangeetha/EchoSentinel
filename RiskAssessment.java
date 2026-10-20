import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RiskAssessment extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextArea details;
    private final RiskAnalyzer analyzer = new RiskAnalyzer();

    public RiskAssessment() {
        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(15, 23, 42));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = new JLabel("Risk Assessment");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Review acoustic readings and identify potential risk patterns."
        );
        subtitle.setForeground(new Color(180, 195, 210));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 8));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);

        String[] columns = {
                "Machine", "Sound (dB)", "Frequency (Hz)",
                "Risk Score", "Risk Level"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setBackground(new Color(30, 41, 59));
        table.setForeground(new Color(241, 245, 249));
        table.setGridColor(new Color(71, 85, 105));
        table.setSelectionBackground(new Color(20, 184, 166));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(15, 118, 110));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(30, 41, 59));
        scrollPane.setBackground(new Color(30, 41, 59));

        details = new JTextArea(7, 30);
        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setFont(new Font("Arial", Font.PLAIN, 14));
        details.setBackground(new Color(30, 41, 59));
        details.setForeground(new Color(241,245,249));

        details.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        details.setText("Select a machine from the table to view its assessment.");

        JButton refreshButton = new JButton("Refresh");
        JButton assessButton = new JButton("View Assessment");

        refreshButton.addActionListener(e -> loadReadings());
        assessButton.addActionListener(e -> showAssessment());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showAssessment();
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        buttons.setOpaque(false);
        buttons.add(refreshButton);
        buttons.add(assessButton);

        JPanel bottom = new JPanel(new BorderLayout(0, 10));
        bottom.setOpaque(false);
        bottom.add(buttons, BorderLayout.NORTH);
        bottom.add(new JScrollPane(details), BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
                    reading.getRiskScore(),
                    reading.getRiskLevel()
            });
        }

        details.setText(
                readings.isEmpty()
                        ? "No readings available. Open Acoustic Data Entry and analyze a machine first."
                        : "Select a machine from the table to view its assessment."
        );
    }

    private void showAssessment() {
        int row = table.getSelectedRow();

        if (row < 0) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(row);
        List<AcousticReading> readings = new AcousticDAO().getAllReadings();

        if (modelRow >= readings.size()) {
            return;
        }

        AcousticReading reading = readings.get(modelRow);

        String report = analyzer.generateReport(reading);
        details.setText(report);
        details.setCaretPosition(0);
    }
}