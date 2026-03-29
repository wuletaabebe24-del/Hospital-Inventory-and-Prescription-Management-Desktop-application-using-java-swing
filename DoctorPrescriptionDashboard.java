package hospital_Dashboard;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class DoctorPrescriptionDashboard extends JFrame {

    private final Color secondary = new Color(52,73,94);
    private final Color accent = new Color(41,128,185);
    private final Color bg = new Color(236,240,241);

    private JTable prescriptionTable;
    private DefaultTableModel prescriptionModel;

    Connection conn;

    public DoctorPrescriptionDashboard() {
        connectDatabase();

        setTitle("Doctor Prescription Dashboard");
        setSize(1000,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(topBar(),BorderLayout.NORTH);
        add(mainPanel(),BorderLayout.CENTER);
        add(statusBar(),BorderLayout.SOUTH);

        loadPrescriptions();
    }

    /* DATABASE CONNECTION */
    private void connectDatabase(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pharmacy",
                    "root",
                    ""
            );
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* TOP BAR */
    private JPanel topBar(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(secondary);
        p.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));

        JLabel title=new JLabel("🩺 Doctor Panel - Prescriptions");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,20));

        p.add(title,BorderLayout.WEST);
        return p;
    }

    /* MAIN PANEL */
    private JPanel mainPanel(){
        JPanel panel=new JPanel(new BorderLayout(10,10));
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        String[] columns={
                "ID","Patient","Medicine","Dosage","Instructions","Status"
        };

        prescriptionModel=new DefaultTableModel(columns,0);
        prescriptionTable=new JTable(prescriptionModel);

        panel.add(new JScrollPane(prescriptionTable),BorderLayout.CENTER);

        JPanel buttons=new JPanel();
        buttons.add(btn("Add Prescription",this::addPrescription));
        buttons.add(btn("Edit",this::editPrescription));
        buttons.add(btn("Delete",this::deletePrescription));

        panel.add(buttons,BorderLayout.SOUTH);

        return panel;
    }

    /* LOAD DATA */
    private void loadPrescriptions(){
        try{
            prescriptionModel.setRowCount(0);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM prescriptions");

            while(rs.next()){
                prescriptionModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("patient"),
                        rs.getString("medicine"),
                        rs.getString("dosage"),
                        rs.getString("instructions"),
                        rs.getString("status")
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* ADD */
    private void addPrescription(){
        JTextField patient=new JTextField();
        JTextField medicine=new JTextField();
        JTextField dosage=new JTextField();
        JTextArea instructions=new JTextArea(3,20);

        Object[] msg={
                "Patient:",patient,
                "Medicine:",medicine,
                "Dosage:",dosage,
                "Instructions:",new JScrollPane(instructions)
        };

        int ok=JOptionPane.showConfirmDialog(
                this,msg,"New Prescription",
                JOptionPane.OK_CANCEL_OPTION
        );

        if(ok==JOptionPane.OK_OPTION){
            try{
                Statement st = conn.createStatement();
                st.executeUpdate(
                        "INSERT INTO prescriptions " +
                                "(patient,medicine,dosage,instructions,status) VALUES ('"
                                +patient.getText()+"','"
                                +medicine.getText()+"','"
                                +dosage.getText()+"','"
                                +instructions.getText()+"','Pending')"
                );
                loadPrescriptions();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /* EDIT */
    private void editPrescription(){
        int row=prescriptionTable.getSelectedRow();
        if(row==-1){
            alert("Select prescription first");
            return;
        }

        int id=(int)prescriptionModel.getValueAt(row,0);

        JTextField patient=new JTextField(prescriptionModel.getValueAt(row,1).toString());
        JTextField medicine=new JTextField(prescriptionModel.getValueAt(row,2).toString());
        JTextField dosage=new JTextField(prescriptionModel.getValueAt(row,3).toString());
        JTextField instructions=new JTextField(prescriptionModel.getValueAt(row,4).toString());

        Object[] msg={
                "Patient:",patient,
                "Medicine:",medicine,
                "Dosage:",dosage,
                "Instructions:",instructions
        };

        int ok=JOptionPane.showConfirmDialog(
                this,msg,"Edit Prescription",
                JOptionPane.OK_CANCEL_OPTION
        );

        if(ok==JOptionPane.OK_OPTION){
            try{
                Statement st = conn.createStatement();
                st.executeUpdate(
                        "UPDATE prescriptions SET patient='"+patient.getText()+
                                "', medicine='"+medicine.getText()+
                                "', dosage='"+dosage.getText()+
                                "', instructions='"+instructions.getText()+
                                "' WHERE id="+id
                );
                loadPrescriptions();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /* DELETE */
    private void deletePrescription(){
        int row=prescriptionTable.getSelectedRow();
        if(row==-1){
            alert("Select prescription");
            return;
        }

        int id=(int)prescriptionModel.getValueAt(row,0);

        try{
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM prescriptions WHERE id="+id);
            loadPrescriptions();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private JButton btn(String t,Runnable r){
        JButton b=new JButton(t);
        b.setFocusPainted(false);
        b.setBackground(accent);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.addActionListener(e->r.run());
        return b;
    }

    private void alert(String msg){
        JOptionPane.showMessageDialog(this,msg);
    }

    private JPanel statusBar(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(secondary);

        JLabel l=new JLabel(" © 2026 Hospital Portal ");
        l.setForeground(Color.WHITE);

        JLabel r=new JLabel(" v1.0 ");
        r.setForeground(Color.WHITE);

        p.add(l,BorderLayout.WEST);
        p.add(r,BorderLayout.EAST);

        return p;
    }

}
