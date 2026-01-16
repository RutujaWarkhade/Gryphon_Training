package mysql_co;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class JDBC_4 extends JFrame implements ActionListener {   

    // UI
    JLabel l1, l2, l3;
    JTextField tf1, tf2, tf3;
    JButton b1, b2, b3, b4;

    // JDBC
    Connection con;
    Statement st;
    ResultSet rs;

    JDBC_4() {
        setLayout(null);

        // Labels
        l1 = new JLabel("Roll Number");
        l2 = new JLabel("Name");
        l3 = new JLabel("Stipend");

        // TextFields
        tf1 = new JTextField();
        tf2 = new JTextField();
        tf3 = new JTextField();

        // Buttons
        b1 = new JButton("First");
        b2 = new JButton("Next");
        b3 = new JButton("Prev");
        b4 = new JButton("Last");

        // Positioning
        l1.setBounds(100, 100, 100, 30);  tf1.setBounds(220, 100, 100, 30);
        l2.setBounds(100, 140, 100, 30);  tf2.setBounds(220, 140, 100, 30);
        l3.setBounds(100, 180, 100, 30);  tf3.setBounds(220, 180, 100, 30);

        b1.setBounds(100, 220, 100, 30);  b2.setBounds(220, 220, 100, 30);
        b3.setBounds(100, 260, 100, 30);  b4.setBounds(220, 260, 100, 30);

        add(l1); add(tf1);
        add(l2); add(tf2);
        add(l3); add(tf3);

        add(b1); add(b2); add(b3); add(b4);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);

        // JDBC setup
        String url = "jdbc:mysql://localhost:3306/SCOE_CS_B?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Vrunda@#123";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String selectQuery = "SELECT * FROM stds_scoe_cs_b";
            rs = st.executeQuery(selectQuery);

            // Display first record if exists
            if(rs.next()) {
                tf1.setText(rs.getString(1));
                tf2.setText(rs.getString(2));
                tf3.setText(rs.getString(3));
            }

        } catch(Exception e) {
            e.printStackTrace();
        } 
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            JButton B = (JButton) ae.getSource();

            if(B == b1)
                rs.first();
            else if(B == b2 && !rs.isLast())
                rs.next();
            else if(B == b3 && !rs.isFirst())
                rs.previous();
            else if(B == b4)
                rs.last();

            tf1.setText(rs.getString(1));
            tf2.setText(rs.getString(2));
            tf3.setText(rs.getString(3));

        } catch(Exception e) {
            e.printStackTrace();
        } 
    }

    public static void main(String[] args) {
        JDBC_4 op = new JDBC_4();
        op.setVisible(true);
        op.setSize(500, 400);
        op.setTitle("Student Record Navigator");

        op.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                try {
                    if(op.rs != null) op.rs.close();
                    if(op.st != null) op.st.close();
                    if(op.con != null) op.con.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
    }
}
