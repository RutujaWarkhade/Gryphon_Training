/*
 This application is designed for a big cloth shop to analyze employee salary patterns using SQL window functions 
 like RANK, ROW_NUMBER, AVG OVER, and LAG. Java AWT is used to design the frontend and JDBC connects MySQL database.
 */


package mysql_co;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/*
 APPLICATION NAME : Cloth Shop Employee Analytics
 DATABASE CONCEPTS USED :
 1) WINDOW FUNCTIONS
 2) RANK()
 3) ROW_NUMBER()
 4) AVG() OVER()
 5) LAG()
 */

public class ClothShopAnalytics extends Frame implements ActionListener {

    Button b1, b2, b3, b4, b5;

    TextArea ta;

    Connection con;
    Statement st;
    ResultSet rs;


    ClothShopAnalytics() {

        setLayout(null);

        Label title = new Label("CLOTH SHOP - EMPLOYEE ANALYTICS");
        title.setBounds(80, 40, 350, 30);
        add(title);

        b1 = new Button("Salary Rank");                 
        b2 = new Button("Top 2 Salary Dept-wise");      
        b3 = new Button("First Hired Dept-wise");       
        b4 = new Button("Dept Avg Salary");             
        b5 = new Button("Salary Trend");                

        b1.setBounds(50, 90, 200, 30);
        b2.setBounds(50, 130, 200, 30);
        b3.setBounds(50, 170, 200, 30);
        b4.setBounds(50, 210, 200, 30);
        b5.setBounds(50, 250, 200, 30);

        ta = new TextArea();
        ta.setBounds(280, 90, 300, 230);

        add(b1); add(b2); add(b3); add(b4); add(b5);
        add(ta);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/SCOE_CS_B?useSSL=false&serverTimezone=UTC",
                "root",
                "root"
            );

            st = con.createStatement();

        } catch (Exception e) {
            ta.setText(e.toString());
        }
    }

    /*
     * actionPerformed()
     * Executes query based on button clicked
     */
    public void actionPerformed(ActionEvent ae) {

        ta.setText("");

        try {

            /*
             * Q1) RANK employees by salary (Highest salary first)
             * TYPE : WINDOW FUNCTION – RANK()
             */
            if (ae.getSource() == b1) {

                rs = st.executeQuery(
                    "SELECT emp_name, salary, " +
                    "RANK() OVER (ORDER BY salary DESC) rnk " +
                    "FROM cloth_employees"
                );

                ta.append("NAME\tSALARY\tRANK\n");

                while (rs.next()) {
                    ta.append(rs.getString(1) + "\t" +
                              rs.getInt(2) + "\t" +
                              rs.getInt(3) + "\n");
                }
            }

            /*
             Q2) Find TOP 2 highest paid employees in each department
             TYPE : WINDOW FUNCTION – ROW_NUMBER() + PARTITION BY
             */
            if (ae.getSource() == b2) {

                rs = st.executeQuery(
                    "SELECT * FROM (" +
                    "SELECT emp_name, dept, salary, " +
                    "ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC) rn " +
                    "FROM cloth_employees) X WHERE rn<=2"
                );

                ta.append("NAME\tDEPT\tSALARY\n");

                while (rs.next()) {
                    ta.append(rs.getString(1) + "\t" +
                              rs.getString(2) + "\t" +
                              rs.getInt(3) + "\n");
                }
            }

            /* 
             Q3) Find FIRST hired employee in each department
             TYPE : WINDOW FUNCTION – ROW_NUMBER() + ORDER BY join_date
              */
            if (ae.getSource() == b3) {

                rs = st.executeQuery(
                    "SELECT * FROM (" +
                    "SELECT emp_name, dept, join_date, " +
                    "ROW_NUMBER() OVER (PARTITION BY dept ORDER BY join_date) rn " +
                    "FROM cloth_employees) X WHERE rn=1"
                );

                ta.append("NAME\tDEPT\tJOIN DATE\n");

                while (rs.next()) {
                    ta.append(rs.getString(1) + "\t" +
                              rs.getString(2) + "\t" +
                              rs.getDate(3) + "\n");
                }
            }

            /* 
              Q4) Calculate DEPARTMENT-WISE AVERAGE SALARY
             TYPE : WINDOW FUNCTION – AVG() OVER()
            */
            if (ae.getSource() == b4) {

                rs = st.executeQuery(
                    "SELECT emp_name, dept, " +
                    "AVG(salary) OVER (PARTITION BY dept) avg_sal " +
                    "FROM cloth_employees"
                );

                ta.append("NAME\tDEPT\tAVG SALARY\n");

                while (rs.next()) {
                    ta.append(rs.getString(1) + "\t" +
                              rs.getString(2) + "\t" +
                              rs.getInt(3) + "\n");
                }
            }

            /* 
              Q5) Detect SALARY TREND (UP / DOWN / SAME)
             TYPE : WINDOW FUNCTION – LAG()
             */
            if (ae.getSource() == b5) {

                rs = st.executeQuery(
                    "SELECT emp_name, " +
                    "CASE " +
                    "WHEN salary > LAG(salary) OVER (ORDER BY join_date) THEN 'UP' " +
                    "WHEN salary < LAG(salary) OVER (ORDER BY join_date) THEN 'DOWN' " +
                    "ELSE 'SAME' END trend " +
                    "FROM cloth_employees"
                );

                ta.append("NAME\tTREND\n");

                while (rs.next()) {
                    ta.append(rs.getString(1) + "\t" +
                              rs.getString(2) + "\n");
                }
            }

        } catch (Exception e) {
            ta.setText(e.toString());
        }
    }

 
    public static void main(String[] args) {

        ClothShopAnalytics f = new ClothShopAnalytics();
        f.setSize(620, 380);
        f.setTitle("Big Cloth Shop Analytics");
        f.setVisible(true);

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }
}
