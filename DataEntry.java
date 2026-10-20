import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataEntry extends JPanel {

    private final JTextField machineField = new JTextField();
    private final JTextField soundField = new JTextField();
    private final JTextField frequencyField = new JTextField();
    private final JTextField durationField = new JTextField();
    private final JTextField repetitionField = new JTextField();

    private final List<AcousticReading> readings = new ArrayList<>();
    private final RiskAnalyzer analyzer = new RiskAnalyzer();

    private final Color BACKGROUND = new Color(15, 23, 42);
    private final Color CARD = new Color(30, 41, 59);
    private final Color TEAL = new Color(20, 184, 166);
    private final Color WHITE = new Color(241, 245, 249);
    private final Color MUTED = new Color(148, 163, 184);

    private AcousticReading latestReading;

    public DataEntry() {
        setLayout(new BorderLayout(20, 20));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Acoustic Data Entry");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(WHITE);

        JLabel subtitle = new JLabel(
                "Enter machinery measurements to calculate a preliminary risk score.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);
        header.add(Box.createVerticalStrut(7));
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 18, 18));
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(25, 25, 25, 25));

        addField(form, "Machine Name", machineField);
        addField(form, "Sound Level (dB)", soundField);
        addField(form, "Frequency (Hz)", frequencyField);
        addField(form, "Duration (seconds)", durationField);
        addField(form, "Repetition Rate (bursts/min)", repetitionField);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(form, BorderLayout.NORTH);

        JButton analyzeButton = new JButton("Calculate Risk");
        styleButton(analyzeButton);
        analyzeButton.addActionListener(e -> analyzeReading());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 18));
        buttons.setOpaque(false);
        buttons.add(analyzeButton);

        center.add(buttons, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, String label, JTextField field) {
        JLabel text = new JLabel(label);
        text.setFont(new Font("Segoe UI", Font.BOLD, 13));
        text.setForeground(WHITE);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(BACKGROUND);
        field.setForeground(WHITE);
        field.setCaretColor(TEAL);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105)),
                new EmptyBorder(8, 10, 8, 10)));

        panel.add(text);
        panel.add(field);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(TEAL);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 24, 12, 24));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void analyzeReading() {
        try {
            String machine = machineField.getText().trim();

            if (machine.isEmpty()) {
                throw new IllegalArgumentException(
                        "Please enter the machine name.");
            }

            double sound = parseValue(soundField, "Sound level");
            double frequency = parseValue(frequencyField, "Frequency");
            double duration = parseValue(durationField, "Duration");
            double repetition = parseValue(
                    repetitionField, "Repetition rate");

            if (sound < 0 || sound > 200) {
                throw new IllegalArgumentException(
                        "Sound level must be between 0 and 200 dB.");
            }

            if (frequency <= 0 || frequency > 100000) {
                throw new IllegalArgumentException(
                        "Frequency must be greater than 0 and at most 100000 Hz.");
            }

            if (duration <= 0 || duration > 86400) {
                throw new IllegalArgumentException(
                        "Duration must be greater than 0 and at most 86400 seconds.");
            }

            if (repetition < 0 || repetition > 10000) {
                throw new IllegalArgumentException(
                        "Repetition rate must be between 0 and 10000.");
            }

            latestReading = new AcousticReading(
                machine, sound, frequency, duration, repetition);

            readings.add(latestReading);
            ReadingStore.addReading(latestReading);

            AcousticDAO dao = new AcousticDAO();
            boolean saved = dao.saveReading(latestReading);

            showAssessment(latestReading);

            if (saved) {
                JOptionPane.showMessageDialog(
                        this,
                        "Reading saved successfully to MySQL!",
                        "Database Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Reading analyzed, but database saving failed. Check the terminal.",
                        "Database Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid numeric values in all measurement fields.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private double parseValue(JTextField field, String fieldName) {
        String value = field.getText().trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be empty.");
        }

        double number = Double.parseDouble(value);

        if (!Double.isFinite(number)) {
            throw new IllegalArgumentException(
                    fieldName + " must be a finite number.");
        }

        return number;
    }

    private void showAssessment(AcousticReading reading) {
        String report = analyzer.generateReport(reading);

        JTextArea reportArea = new JTextArea(report, 15, 45);
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        reportArea.setBackground(BACKGROUND);
        reportArea.setForeground(WHITE);
        reportArea.setCaretPosition(0);
        reportArea.setBorder(new EmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(CARD));

        JButton saveButton = new JButton("Save Report");
        styleButton(saveButton);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(saveButton);
        buttons.add(closeButton);

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "EchoSentinel Risk Assessment",
                true);

        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(BACKGROUND);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveReport(report, dialog));
        closeButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setMinimumSize(new Dimension(580, 390));
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void saveReport(String report, JDialog dialog) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save EchoSentinel Report");
        chooser.setSelectedFile(
                new File("EchoSentinel_Report.txt"));

        int result = chooser.showSaveDialog(dialog);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (file.exists()) {
            int confirm = JOptionPane.showConfirmDialog(
                    dialog,
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
                    dialog,
                    "Report saved successfully!\n" + file.getAbsolutePath(),
                    "Report Saved",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Unable to save report: " + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<AcousticReading> getReadings() {
        return new ArrayList<>(readings);
    }
}