package mysql_co;

import java.awt.*;
import java.awt.event.*;

// Thread class for booking seats
class BookingSystem_scoe extends Thread {

    static int available = 50; // initial total seats
    int seat;
    String user;

    BookingSystem_scoe(int seat, String user) {
        this.seat = seat;
        this.user = user;
    }

    public void run() {
        synchronized (BookingSystem_scoe.class) {
            if (available >= seat) {
                JDBC_6.ta.append(user + " booked " + seat + " seats.\n");
                available -= seat;
            } else {
                JDBC_6.ta.append(user + " : Ooo Noo! NOT ENOUGH SEATS available\n");
            }
        }
    }
}

// Main UI class
public class JDBC_6 extends Frame implements ActionListener {

    Label l1, l2, l3;
    TextField tfName, tfSeats;
    Button b1;
    static TextArea ta;

    JDBC_6() {

        setTitle("Multithreaded Seat Booking System");
        setSize(500, 400);
        setLayout(new FlowLayout());

        // Labels
        l1 = new Label("Enter User Name:");
        l2 = new Label("Enter seats to book (comma separated):");
        l3 = new Label("Example: 2,3,1,4");

        // TextFields
        tfName = new TextField(15);
        tfSeats = new TextField(20);

        // Button
        b1 = new Button("Book Seats");

        // TextArea
        ta = new TextArea(10, 45);
        ta.setEditable(false);

        // Add components
        add(l1);
        add(tfName);

        add(l2);
        add(tfSeats);

        add(b1);
        add(l3);
        add(ta);

        // Button action
        b1.addActionListener(this);

        // Window close
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            String baseName = tfName.getText().trim();
            String[] seatArray = tfSeats.getText().trim().split(",");

            for (int i = 0; i < seatArray.length; i++) {
                int seat = Integer.parseInt(seatArray[i].trim());
                String threadName = baseName + "_Thread" + (i + 1);

                BookingSystem_scoe op = new BookingSystem_scoe(seat, threadName);
                op.start();
            }

            l3.setText("Booking request sent for " + seatArray.length + " threads");

        } catch (NumberFormatException e) {
            l3.setText("Error: Enter valid numbers separated by commas");
        }
    }

    public static void main(String[] args) {
        new JDBC_6();
    }
}
