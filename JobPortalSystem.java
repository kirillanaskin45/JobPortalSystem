package jobportalsystem;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.border.TitledBorder;

class JobApplication {
    String name, email, jobTitle, qualification, phone, status, appliedDate;
    
    JobApplication(String name, String email, String jobTitle, String qualification, String phone, String status, String appliedDate) {
        this.name = name;
        this.email = email;
        this.jobTitle = jobTitle;
        this.qualification = qualification;
        this.phone = phone;
        this.status = status;
        this.appliedDate = appliedDate;
    }
}

public class JobPortalSystem extends JFrame {
    
    private JTextField tfName, tfEmail, tfJobTitle, tfQualification, tfPhone, tfSearch;
    private JComboBox<String> cbStatus;
    private JTable table;
    private DefaultTableModel model;
    private ArrayList<JobApplication> applications = new ArrayList<>();
    private JLabel statusLabel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    
    public JobPortalSystem() {
        setTitle("Job Portal Management System");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Устанавливаем современный look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Верхняя панель с заголовком
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Основная панель с разделением
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Левая панель - форма
        splitPane.setLeftComponent(createFormPanel());
        
        // Правая панель - таблица
        splitPane.setRightComponent(createTablePanel());
        
        add(splitPane, BorderLayout.CENTER);
        
        // Нижняя панель
        add(createBottomPanel(), BorderLayout.SOUTH);
        
        // Добавляем тестовые данные
        addSampleData();
        updateTable();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(41, 128, 185), 
                                                      w, 0, new Color(142, 68, 173));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 80));
        header.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("JOB PORTAL MANAGEMENT SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 2),
            "Подать заявку",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16),
            new Color(41, 128, 185)
        ));
        
        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        // Поля ввода
        tfName = createStyledTextField();
        tfEmail = createStyledTextField();
        tfJobTitle = createStyledTextField();
        tfQualification = createStyledTextField();
        tfPhone = createStyledTextField();
        cbStatus = new JComboBox<>(new String[]{"Новая", "Рассматривается", "Интервью", "Принята", "Отклонена"});
        
        // Добавляем поля
        gbc.gridy = 0;
        addFormField(formGrid, gbc, "ФИО:", tfName);
        
        gbc.gridy = 1;
        addFormField(formGrid, gbc, "Email:", tfEmail);
        
        gbc.gridy = 2;
        addFormField(formGrid, gbc, "Телефон:", tfPhone);
        
        gbc.gridy = 3;
        addFormField(formGrid, gbc, "Должность:", tfJobTitle);
        
        gbc.gridy = 4;
        addFormField(formGrid, gbc, "Квалификация:", tfQualification);
        
        gbc.gridy = 5;
        addFormField(formGrid, gbc, "Cтатус:", cbStatus);
        
        // Кнопки
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setBackground(new Color(245, 245, 245));
        
        JButton btnApply = createStyledButton("Отправить заявку", new Color(46, 204, 113));
        JButton btnClear = createStyledButton("Очистить форму", new Color(52, 152, 219));
        
        btnApply.addActionListener(e -> applyJob());
        btnClear.addActionListener(e -> clearForm());
        
        buttonPanel.add(btnApply);
        buttonPanel.add(btnClear);
        
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formGrid.add(buttonPanel, gbc);
        
        panel.add(formGrid, BorderLayout.NORTH);
        
        return panel;
    }
    
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(jLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(field, gbc);
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(142, 68, 173), 2),
            "Список заявок",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 16),
            new Color(142, 68, 173)
        ));
        
        // Создаем таблицу с дополнительными колонками
        model = new DefaultTableModel(
            new String[]{"ФИО", "Email", "Телефон", "Должность", "Квалификация", "Статус", "Дата"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(142, 68, 173));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(187, 134, 252, 50));
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        
        // Чередование цветов строк
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                }
                
                // Цвет статуса
                if (column == 5) { // Колонка статуса
                    String status = value.toString();
                    switch (status) {
                        case "Новая":
                            c.setForeground(new Color(52, 152, 219));
                            break;
                        case "Рассматривается":
                            c.setForeground(new Color(241, 196, 15));
                            break;
                        case "Интервью":
                            c.setForeground(new Color(155, 89, 182));
                            break;
                        case "Принята":
                            c.setForeground(new Color(46, 204, 113));
                            break;
                        case "Отклонена":
                            c.setForeground(new Color(231, 76, 60));
                            break;
                    }
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Панель управления таблицей
        JPanel tableControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        tableControl.setBackground(Color.WHITE);
        
        JButton btnDelete = createStyledButton("️Удалить", new Color(231, 76, 60));
        JButton btnEdit = createStyledButton("Редактировать", new Color(241, 196, 15));
        JButton btnStatus = createStyledButton("Изменить статус", new Color(155, 89, 182));
        
        btnDelete.addActionListener(e -> deleteApplication());
        btnEdit.addActionListener(e -> editApplication());
        btnStatus.addActionListener(e -> changeStatus());
        
        tableControl.add(btnEdit);
        tableControl.add(btnStatus);
        tableControl.add(btnDelete);
        
        panel.add(tableControl, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Панель поиска
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(245, 245, 245));
        
        searchPanel.add(new JLabel("Поиск:"));
        tfSearch = createStyledTextField();
        tfSearch.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(tfSearch);
        
        JButton btnSearch = createStyledButton("Найти", new Color(155, 89, 182));
        JButton btnShowAll = createStyledButton("Показать все", new Color(52, 152, 219));
        JButton btnStats = createStyledButton("Статистика", new Color(46, 204, 113));
        
        btnSearch.addActionListener(e -> searchApplications());
        btnShowAll.addActionListener(e -> {
            tfSearch.setText("");
            updateTable();
        });
        btnStats.addActionListener(e -> showStatistics());
        
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);
        searchPanel.add(btnStats);
        
        // Статус панель
        statusLabel = new JLabel("Всего заявок: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(new Color(41, 128, 185));
        
        panel.add(searchPanel, BorderLayout.WEST);
        panel.add(statusLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void applyJob() {
        String name = tfName.getText().trim();
        String email = tfEmail.getText().trim();
        String phone = tfPhone.getText().trim();
        String jobTitle = tfJobTitle.getText().trim();
        String qualification = tfQualification.getText().trim();
        String status = cbStatus.getSelectedItem().toString();
        
        // Валидация
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || jobTitle.isEmpty() || qualification.isEmpty()) {
            showMessage("Пожалуйста, заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isValidEmail(email)) {
            showMessage("Введите корректный email!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!isValidPhone(phone)) {
            showMessage("Введите корректный телефон!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String appliedDate = java.time.LocalDate.now().toString();
        
        applications.add(new JobApplication(name, email, jobTitle, qualification, phone, status, appliedDate));
        updateTable();
        clearForm();
        
        showMessage("Заявка успешно отправлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return Pattern.matches(emailRegex, email);
    }
    
    private boolean isValidPhone(String phone) {
        return phone.matches("\\+?[0-9]{10,13}");
    }
    
    private void updateTable() {
        model.setRowCount(0);
        for (JobApplication ja : applications) {
            model.addRow(new Object[]{
                ja.name, 
                ja.email, 
                ja.phone, 
                ja.jobTitle, 
                ja.qualification, 
                ja.status,
                ja.appliedDate
            });
        }
        updateStatus();
    }
    
    private void updateStatus() {
        statusLabel.setText("Всего заявок: " + applications.size() + 
                           " | Новых: " + countByStatus("Новая") +
                           " | Принято: " + countByStatus("Принята"));
    }
    
    private int countByStatus(String status) {
        return (int) applications.stream().filter(a -> a.status.equals(status)).count();
    }
    
    private void clearForm() {
        tfName.setText("");
        tfEmail.setText("");
        tfPhone.setText("");
        tfJobTitle.setText("");
        tfQualification.setText("");
        cbStatus.setSelectedIndex(0);
    }
    
    private void deleteApplication() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("Выберите заявку для удаления!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Вы уверены, что хотите удалить эту заявку?",
            "Подтверждение",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            applications.remove(row);
            updateTable();
            showMessage("Заявка успешно удалена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void editApplication() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("Выберите заявку для редактирования!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JobApplication ja = applications.get(row);
        
        tfName.setText(ja.name);
        tfEmail.setText(ja.email);
        tfPhone.setText(ja.phone);
        tfJobTitle.setText(ja.jobTitle);
        tfQualification.setText(ja.qualification);
        cbStatus.setSelectedItem(ja.status);
        
        applications.remove(row);
        updateTable();
    }
    
    private void changeStatus() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("Выберите заявку!", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String[] options = {"Новая", "Рассматривается", "Интервью", "Принята", "Отклонена"};
        String newStatus = (String) JOptionPane.showInputDialog(this,
            "Выберите новый статус:",
            "Изменение статуса",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            applications.get(row).status);
            
        if (newStatus != null) {
            applications.get(row).status = newStatus;
            updateTable();
            showMessage("Статус обновлен!", "Успех", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void searchApplications() {
        String searchText = tfSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            updateTable();
            return;
        }
        
        model.setRowCount(0);
        for (JobApplication ja : applications) {
            if (ja.name.toLowerCase().contains(searchText) ||
                ja.jobTitle.toLowerCase().contains(searchText) ||
                ja.email.toLowerCase().contains(searchText)) {
                model.addRow(new Object[]{
                    ja.name, ja.email, ja.phone, ja.jobTitle, 
                    ja.qualification, ja.status, ja.appliedDate
                });
            }
        }
    }
    
    private void showStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append("СТАТИСТИКА ЗАЯВОК\n\n");
        stats.append("Всего заявок: ").append(applications.size()).append("\n\n");
        
        stats.append("По статусам:\n");
        stats.append("• Новая: ").append(countByStatus("Новая")).append("\n");
        stats.append("• Рассматривается: ").append(countByStatus("Рассматривается")).append("\n");
        stats.append("• Интервью: ").append(countByStatus("Интервью")).append("\n");
        stats.append("• Принята: ").append(countByStatus("Принята")).append("\n");
        stats.append("• Отклонена: ").append(countByStatus("Отклонена")).append("\n");
        
        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);
        
        JOptionPane.showMessageDialog(this, 
            new JScrollPane(textArea), 
            "Статистика", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
    
    private void addSampleData() {
        applications.add(new JobApplication("Иван Петров", "ivan@mail.com", "Java Developer", "B.Tech", "+79123456789", "Новая", "2024-01-15"));
        applications.add(new JobApplication("Анна Смирнова", "anna@mail.com", "UI/UX Designer", "BFA", "+79234567890", "Интервью", "2024-01-16"));
        applications.add(new JobApplication("Петр Сидоров", "petr@mail.com", "Project Manager", "MBA", "+79345678901", "Рассматривается", "2024-01-17"));
        applications.add(new JobApplication("Мария Иванова", "maria@mail.com", "Data Scientist", "PhD", "+79456789012", "Принята", "2024-01-18"));
        applications.add(new JobApplication("Алексей Козлов", "alex@mail.com", "DevOps Engineer", "MS", "+79567890123", "Новая", "2024-01-19"));
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new JobPortalSystem().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
