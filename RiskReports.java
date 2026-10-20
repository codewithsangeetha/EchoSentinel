import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class RiskReports extends JPanel {

    private final Color BACKGROUND = new Color(15, 23, 42);
    private final Color CARD = new Color(30, 41, 59);
    private final Color TEAL = new Color(20, 184, 166);
    private final Color WHITE = new Color(241, 245, 249);
    private final Color MUTED = new Color(148, 163, 184);

    private final DefaultTableModel model;
    private final JTable table;
    private final RiskAnalyzer analyzer = new RiskAnalyzer();

    public RiskReports() {
        setLayout(new BorderLayout(15, 15));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Risk Reports");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(WHITE);

        JLabel subtitle = new JLabel(
                "Review risk assessments and export detailed machine reports.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(Box.createVerticalStrut(7));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        String[] columns = {
                "Machine Name", "Sound (dB)", "Frequency (Hz)",
                "Risk Score", "Risk Level"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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

        JButton refreshButton = new JButton("Refresh");
        JButton viewButton = new JButton("View Report");
        JButton exportButton = new JButton("Export Report");

        styleButton(refreshButton);
        styleButton(viewButton);
        styleButton(exportButton);

        refreshButton.addActionListener(e -> loadReports());
        viewButton.addActionListener(e -> viewReport());
        exportButton.addActionListener(e -> exportReport());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttons.setOpaque(false);
        buttons.add(refreshButton);
        buttons.add(viewButton);
        buttons.add(exportButton);

        add(buttons, BorderLayout.SOUTH);

        loadReports();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(TEAL);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void loadReports() {
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

        if (readings.isEmpty()) {
            model.addRow(new Object[]{
                    "No reports available", "-", "-", "-", "-"
            });
        }
    }

    private AcousticReading getSelectedReading() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a machine from the table.",
                    "No Selection",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

       List<AcousticReading> readings = new AcousticDAO().getAllReadings();

        if (row >= readings.size()) {
            return null;
        }

        return readings.get(row);
    }

    private void viewReport() {
        AcousticReading reading = getSelectedReading();

        if (reading == null) {
            return;
        }

        JTextArea reportArea = new JTextArea(
                analyzer.generateReport(reading), 18, 50);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setBackground(BACKGROUND);
        reportArea.setForeground(WHITE);
        reportArea.setCaretPosition(0);
        reportArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(reportArea),
                "Risk Assessment Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportReport() {
        AcousticReading reading = getSelectedReading();

        if (reading == null) {
            return;
        }

        String report = analyzer.generateReport(reading);

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Export Risk Report");
        chooser.setSelectedFile(new File(
                reading.getMachineName().replaceAll(
                        "[^a-zA-Z0-9_-]", "_") + "_Report.txt"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (file.exists()) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "This file already exists. Overwrite it?",
                    "Confirm Overwrite",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(report);

            JOptionPane.showMessageDialog(
                    this,
                    "Report exported successfully!\n" + file.getAbsolutePath(),
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to export report: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}