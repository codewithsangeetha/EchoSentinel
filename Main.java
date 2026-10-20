import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {

    private static final Color BACKGROUND = new Color(15, 23, 42);
    private static final Color SIDEBAR = new Color(11, 18, 35);
    private static final Color CARD = new Color(30, 41, 59);
    private static final Color TEAL = new Color(20, 184, 166);
    private static final Color WHITE = new Color(241, 245, 249);
    private static final Color MUTED = new Color(148, 163, 184);

    private static JFrame frame;
    private static JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createDashboard);
    }

    private static void createDashboard() {
        frame = new JFrame("EchoSentinel | Acoustic Risk Assessment");
        frame.setSize(1150, 720);
        frame.setMinimumSize(new Dimension(950, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(BACKGROUND);

        frame.add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        contentPanel.add(createHeader(), BorderLayout.NORTH);
        contentPanel.add(createOverview(), BorderLayout.CENTER);
        contentPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(245, 0));
        sidebar.setBackground(SIDEBAR);
        sidebar.setBorder(new EmptyBorder(25, 16, 20, 16));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("ECHO");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo.setForeground(TEAL);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel logo2 = new JLabel("SENTINEL");
        logo2.setFont(new Font("Segoe UI", Font.BOLD, 30));
        logo2.setForeground(WHITE);
        logo2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("ACOUSTIC RISK MONITOR");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        tagline.setForeground(MUTED);
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(logo2);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(tagline);
        sidebar.add(Box.createVerticalStrut(40));

        addNavButton(sidebar, "▦   Dashboard", true, "Dashboard");
        addNavButton(sidebar, "＋   Acoustic Data Entry", false, "Data Entry");
        addNavButton(sidebar, "◉   Risk Assessment", false, "Risk Assessment");
        addNavButton(sidebar, "◷   Reading History", false, "History");
        addNavButton(sidebar, "▤   Risk Reports", false, "Reports");

        sidebar.add(Box.createVerticalGlue());

        JLabel system = new JLabel("SYSTEM STATUS");
        system.setFont(new Font("Segoe UI", Font.BOLD, 11));
        system.setForeground(MUTED);
        system.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel status = new JLabel("●  Application Running");
        status.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        status.setForeground(TEAL);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(system);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(status);

        return sidebar;
    }

    private static void addNavButton(
            JPanel sidebar, String text, boolean active, String page) {

        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(active ? TEAL : WHITE);
        button.setBackground(active ? CARD : SIDEBAR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setPreferredSize(new Dimension(210, 45));
        button.setBorder(new EmptyBorder(0, 12, 0, 8));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.addActionListener(e -> showPage(page));

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(8));
    }

    private static JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Dashboard");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(WHITE);

        JLabel subtitle = new JLabel(
                "Monitor acoustic readings and assess machinery risk.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        titles.add(heading);
        titles.add(Box.createVerticalStrut(5));
        titles.add(subtitle);

        JLabel badge = new JLabel("  ● SYSTEM ONLINE  ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(TEAL);
        badge.setOpaque(true);
        badge.setBackground(CARD);
        badge.setBorder(new EmptyBorder(10, 8, 10, 8));

        header.add(titles, BorderLayout.WEST);
        header.add(badge, BorderLayout.EAST);

        return header;
    }

    private static JPanel createOverview() {
    JPanel overview = new JPanel();
    overview.setOpaque(false);
    overview.setLayout(new BoxLayout(overview, BoxLayout.Y_AXIS));

    JPanel stats = new JPanel(new GridLayout(1, 3, 15, 0));
    stats.setOpaque(false);
    stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 125));

    AcousticDAO dao = new AcousticDAO();
    java.util.List<AcousticReading> readings = dao.getAllReadings();

    int totalMachines = dao.getMachineCount();
    int totalReadings = dao.getReadingCount();
    int highRiskAlerts = 0;

    for (AcousticReading reading : readings) {
        String risk = reading.getRiskLevel();

        if (risk.equalsIgnoreCase("HIGH")
                || risk.equalsIgnoreCase("CRITICAL")) {
            highRiskAlerts++;
        }
    }

    stats.add(createStatCard(
            "TOTAL MACHINES",
            String.format("%02d", totalMachines),
            "Unique machines recorded"));

    stats.add(createStatCard(
            "READINGS ANALYZED",
            String.valueOf(totalReadings),
            "Saved in database"));

    stats.add(createStatCard(
            "HIGH-RISK ALERTS",
            String.format("%02d", highRiskAlerts),
            "HIGH and CRITICAL readings"));

    overview.add(stats);
    overview.add(Box.createVerticalStrut(25));

    JLabel section = new JLabel("MACHINE RISK OVERVIEW");
    section.setFont(new Font("Segoe UI", Font.BOLD, 16));
    section.setForeground(WHITE);
    section.setAlignmentX(Component.CENTER_ALIGNMENT);

    overview.add(section);
    overview.add(Box.createVerticalStrut(15));

    JPanel machineList = new JPanel();
    machineList.setBackground(CARD);
    machineList.setLayout(new BoxLayout(machineList, BoxLayout.Y_AXIS));
    machineList.setBorder(new EmptyBorder(10, 18, 10, 18));

    if (readings.isEmpty()) {
        JLabel emptyLabel = new JLabel("No machine readings available yet.");
        emptyLabel.setForeground(MUTED);
        emptyLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        machineList.add(emptyLabel);
    } else {
        java.util.Map<String, AcousticReading> latestReadings =
                new java.util.LinkedHashMap<>();

        for (AcousticReading reading : readings) {
            latestReadings.putIfAbsent(
                    reading.getMachineName(), reading);
        }

        int count = 0;

        for (java.util.Map.Entry<String, AcousticReading> entry
                : latestReadings.entrySet()) {

            AcousticReading reading = entry.getValue();
            String machineName = reading.getMachineName();
            String risk = reading.getRiskLevel();
            Color riskColor;

            switch (risk.toUpperCase()) {
                case "LOW":
                    riskColor = new Color(74, 222, 128);
                    break;
                case "MEDIUM":
                    riskColor = new Color(250, 204, 21);
                    break;
                case "HIGH":
                    riskColor = new Color(251, 146, 60);
                    break;
                default:
                    riskColor = new Color(248, 113, 113);
                    break;
            }

            if (count > 0) {
                machineList.add(createDivider());
            }

            machineList.add(createMachineRow(
                    machineName, "Recorded Machine", risk, riskColor));

            count++;
        }
    }

    overview.add(machineList);
    overview.add(Box.createVerticalStrut(20));

    JPanel riskLegend = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 8));
    riskLegend.setOpaque(false);

    riskLegend.add(createLegend("LOW", new Color(74, 222, 128)));
    riskLegend.add(createLegend("MEDIUM", new Color(250, 204, 21)));
    riskLegend.add(createLegend("HIGH", new Color(251, 146, 60)));
    riskLegend.add(createLegend("CRITICAL", new Color(248, 113, 113)));

    overview.add(riskLegend);

    return overview;
}
    private static JPanel createStatCard(
            String title, String value, String description) {

        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(16, 18, 14, 12));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(MUTED);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Consolas", Font.BOLD, 30));
        valueLabel.setForeground(TEAL);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descriptionLabel.setForeground(MUTED);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(descriptionLabel);

        return card;
    }

    private static JPanel createMachineRow(
            String name, String location, String risk, Color riskColor) {

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(12, 0, 12, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel machineName = new JLabel(name);
        machineName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        machineName.setForeground(WHITE);

        JLabel machineLocation = new JLabel(location);
        machineLocation.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        machineLocation.setForeground(MUTED);

        details.add(machineName);
        details.add(Box.createVerticalStrut(4));
        details.add(machineLocation);

        JLabel riskBadge = new JLabel("  " + risk + "  ");
        riskBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        riskBadge.setForeground(riskColor);
        riskBadge.setOpaque(true);
        riskBadge.setBackground(BACKGROUND);
        riskBadge.setBorder(new EmptyBorder(7, 5, 7, 5));

        row.add(details, BorderLayout.CENTER);
        row.add(riskBadge, BorderLayout.EAST);

        return row;
    }

    private static JComponent createDivider() {
        JPanel divider = new JPanel();
        divider.setBackground(new Color(51, 65, 85));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(1, 1));
        return divider;
    }

    private static JLabel createLegend(String text, Color color) {
        JLabel label = new JLabel("● " + text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 11));
        label.setForeground(color);
        return label;
    }

    private static JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JLabel message = new JLabel(
                "EchoSentinel  |  Acoustic Anomaly & Defect Risk Assessment");
        message.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        message.setForeground(MUTED);

        footer.add(message, BorderLayout.WEST);
        return footer;
    }

    private static void showPage(String page) {
    if (page.equals("Dashboard")) {
        createDashboardRefresh();
        return;
    }

    if (page.equals("Data Entry")) {
        contentPanel.removeAll();

        DataEntry dataEntry = new DataEntry();
        contentPanel.add(dataEntry, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
        return;
    }

    if (page.equals("Risk Assessment")) {
        contentPanel.removeAll();

        RiskAssessment assessment = new RiskAssessment();
        contentPanel.add(assessment, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
        return;
    }

    if (page.equals("History")) {
        contentPanel.removeAll();

        ReadingHistory history = new ReadingHistory();
        contentPanel.add(history, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
        return;
    }

    if (page.equals("Reports")) {
        contentPanel.removeAll();

        RiskReports reports = new RiskReports();
        contentPanel.add(reports, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
        return;
    }
}
    private static void createDashboardRefresh() {
        frame.getContentPane().removeAll();
        frame.add(createSidebar(), BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        contentPanel.add(createHeader(), BorderLayout.NORTH);
        contentPanel.add(createOverview(), BorderLayout.CENTER);
        contentPanel.add(createFooter(), BorderLayout.SOUTH);

        frame.add(contentPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }
}