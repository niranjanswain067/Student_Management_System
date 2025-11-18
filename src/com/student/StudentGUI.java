package com.student;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentGUI extends JFrame {
    JTextField idField, nameField, courseField, addressField, contactField;
    JButton insertBtn, showBtn, deleteBtn, updateBtn;
    JTextArea displayArea;

    String url = "jdbc:mysql://localhost:3306/MSB";
    String userName = "root";
    String password = "Admin@123";

    public StudentGUI() {
        setTitle("Student Management System");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel for form input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Font labelFont = new Font("Arial", Font.BOLD, 13);

        // Labels & Fields
        String[] labels = {"Student ID:", "Name:", "Course:", "Address:", "Contact:"};
        JTextField[] fields = {idField = new JTextField(), nameField = new JTextField(), courseField = new JTextField(), addressField = new JTextField(), contactField = new JTextField()};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            inputPanel.add(new JLabel(labels[i]) {{
                setFont(labelFont);
            }}, gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            inputPanel.add(fields[i], gbc);
        }

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        insertBtn = new JButton("Insert");
        showBtn = new JButton("Show All");
        deleteBtn = new JButton("Delete");
        updateBtn = new JButton("Update");

        buttonPanel.add(insertBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(showBtn);

        // Display area
        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add to main frame
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Event handling
        insertBtn.addActionListener(e -> insertStudent());
        showBtn.addActionListener(e -> showStudents());
        deleteBtn.addActionListener(e -> deleteStudent());
        updateBtn.addActionListener(e -> updateStudent());

        setVisible(true);
    }

    // Insert Student
    public void insertStudent() {
        try (Connection con = DriverManager.getConnection(url, userName, password)) {
            String query = "INSERT INTO STUDENT(ID, NAME, COURSE, ADDRESS, CONTACT) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idField.getText()));
            pstmt.setString(2, nameField.getText());
            pstmt.setString(3, courseField.getText());
            pstmt.setString(4, addressField.getText());
            pstmt.setString(5, contactField.getText());
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Inserted Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Insert Error: " + ex.getMessage());
        }
    }

    // Show Students
    public void showStudents() {
        displayArea.setText("");
        try (Connection con = DriverManager.getConnection(url, userName, password)) {
            String query = "SELECT * FROM STUDENT";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            displayArea.append("ID\t\tNAME\t\tCOURSE\t\t\tADDRESS\t\tCONTACT\n");
            displayArea.append("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            while (rs.next()) {
                displayArea.append(
                        rs.getInt("ID") + "\t\t" +
                                rs.getString("NAME") + "\t\t" +
                                rs.getString("COURSE") + "\t\t\t" +
                                rs.getString("ADDRESS") + "\t\t" +
                                rs.getString("CONTACT") + "\n"
                );
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Show Error: " + ex.getMessage());
        }
    }

    // Delete Student
    public void deleteStudent() {
        try (Connection con = DriverManager.getConnection(url, userName, password)) {
            String query = "DELETE FROM STUDENT WHERE ID = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, Integer.parseInt(idField.getText()));
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");
            else
                JOptionPane.showMessageDialog(this, "No Student Found with Given ID");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Delete Error: " + ex.getMessage());
        }
    }

    // Update Student
    public void updateStudent() {
        try (Connection con = DriverManager.getConnection(url, userName, password)) {
            String query = "UPDATE STUDENT SET NAME=?, COURSE=?, ADDRESS=?, CONTACT=? WHERE ID=?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, courseField.getText());
            pstmt.setString(3, addressField.getText());
            pstmt.setString(4, contactField.getText());
            pstmt.setInt(5, Integer.parseInt(idField.getText()));
            int rows = pstmt.executeUpdate();
            if (rows > 0)
                JOptionPane.showMessageDialog(this, "Student Updated Successfully!");
            else
                JOptionPane.showMessageDialog(this, "No Student Found with Given ID");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "MySQL Driver not found.");
            return;
        }
        new StudentGUI();
    }
}
