package mysql_co;



import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class StudentPercentage extends JFrame implements ActionListener {

    JLabel l1, l2, l3, l4, l5, l6, l7;
    JTextField tfName, tfSub1, tfSub2, tfSub3, tfSub4, tfSub5, tfPercentage;
    JButton bCalculate, bSave;

    Connection con;
    PreparedStatement pst;

    StudentPercentage() {
        setLayout(null);

        // Labels
        l1 = new JLabel("Student Name:");
        l2 = new JLabel("Subject 1:");
        l3 = new JLabel("Subject 2:");
        l4 = new JLabel("Subject 3:");
        l5 = new JLabel("Subject 4:");
        l6 = new JLabel("Subject 5:");
        l7 = new JLabel("Percentage:");

        // TextFields
        tfName = new JTextField();
        tfSub1 = new JTextField();
        tfSub2 = new JTextField();
        tfSub3 = new JTextField();
        tfSub4 = new JTextField();
        tfSub5 = new JTextField();
        tfPercentage = new JTextField();
        tfPercentage.setEditable(false);

        // Buttons
        bCalculate = new JButton("Calculate");
        bSave = new JButton("Save to DB");

        // Set positions
        l1.setBounds(50, 50, 120, 30);   tfName.setBounds(180, 50, 150, 30);
        l2.setBounds(50, 90, 120, 30);   tfSub1.setBounds(180, 90, 50, 30);
        l3.setBounds(50, 130, 120, 30);  tfSub2.setBounds(180, 130, 50, 30);
        l4.setBounds(50, 170, 120, 30);  tfSub3.setBounds(180, 170, 50, 30);
        l5.setBounds(50, 210, 120, 30);  tfSub4.setBounds(180, 210, 50, 30);
        l6.setBounds(50, 250, 120, 30);  tfSub5.setBounds(180, 250, 50, 30);
        l7.setBounds(50, 290, 120, 30);  tfPercentage.setBounds(180, 290, 100, 30);

        bCalculate.setBounds(50, 340, 120, 30);
        bSave.setBounds(200, 340, 130, 30);

        add(l1); add(tfName);
        add(l2); add(tfSub1);
        add(l3); add(tfSub2);
        add(l4); add(tfSub3);
        add(l5); add(tfSub4);
        add(l6); add(tfSub5);
        add(l7); add(tfPercentage);
        add(bCalculate); add(bSave);

        bCalculate.addActionListener(this);
        bSave.addActionListener(this);

        // JDBC connection
        try {
            String url = "jdbc:mysql://localhost:3306/SCOE_CS_B?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "root";

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement(
                "INSERT INTO student_marks(student_name, subject1, subject2, subject3, subject4, subject5) VALUES (?, ?, ?, ?, ?, ?)"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == bCalculate) {
                // Get marks
                double s1 = Double.parseDouble(tfSub1.getText());
                double s2 = Double.parseDouble(tfSub2.getText());
                double s3 = Double.parseDouble(tfSub3.getText());
                double s4 = Double.parseDouble(tfSub4.getText());
                double s5 = Double.parseDouble(tfSub5.getText());

                // Calculate percentage
                double percentage = (s1 + s2 + s3 + s4 + s5) / 5;
                tfPercentage.setText(String.format("%.2f", percentage));
            } else if (ae.getSource() == bSave) {
                // Save to DB
                pst.setString(1, tfName.getText());
                pst.setDouble(2, Double.parseDouble(tfSub1.getText()));
                pst.setDouble(3, Double.parseDouble(tfSub2.getText()));
                pst.setDouble(4, Double.parseDouble(tfSub3.getText()));
                pst.setDouble(5, Double.parseDouble(tfSub4.getText()));
                pst.setDouble(6, Double.parseDouble(tfSub5.getText()));

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data Saved Successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentPercentage sp = new StudentPercentage();
        sp.setVisible(true);
        sp.setSize(400, 450);
        sp.setTitle("Student Percentage Calculator");
        sp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
