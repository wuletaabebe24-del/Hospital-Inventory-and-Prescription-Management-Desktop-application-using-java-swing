package hospital_Dashboard;


import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class PharmacyDashboard extends JFrame {

    private final Color secondary = new Color(52,73,94);
    private final Color accent = new Color(46,204,113);
    private final Color bg = new Color(236,240,241);

    private JTable inventoryTable;
    private JTable prescriptionTable;

    private DefaultTableModel inventoryModel;
    private DefaultTableModel prescriptionModel;

    Connection conn;

    public PharmacyDashboard() {
        connectDatabase();

        ImageIcon frameIcon = new ImageIcon("resources/logo.png");
        setIconImage(frameIcon.getImage());

        setTitle("PharmaTrack Pro");
        setSize(1150,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(topBar(),BorderLayout.NORTH);
        add(mainArea(),BorderLayout.CENTER);
        add(statusBar(),BorderLayout.SOUTH);

        loadInventory();
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

        JLabel title=new JLabel("💊 PharmaTrack Pro");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,22));

        p.add(title,BorderLayout.WEST);
        return p;
    }

    /* MAIN AREA */
    private JSplitPane mainArea(){

        JPanel sidebar = new JPanel();
        sidebar.setBackground(secondary);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));

        sidebar.add(sideBtn("Dashboard"));
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(sideBtn("Inventory"));
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(sideBtn("Prescriptions"));

        JPanel right=new JPanel(new BorderLayout(10,10));
        right.setBackground(bg);
        right.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        right.add(centerCards(),BorderLayout.CENTER);

        JSplitPane split=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sidebar,right);
        split.setDividerLocation(180);
        split.setDividerSize(0);

        return split;
    }

    private JButton sideBtn(String name){
        JButton b=new JButton(name);
        b.setMaximumSize(new Dimension(160,40));
        b.setFocusPainted(false);
        b.setBackground(accent);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,14));
        return b;
    }

    private JPanel centerCards(){
        JPanel g=new JPanel(new GridLayout(1,2,15,15));
        g.setOpaque(false);

        g.add(card("Inventory", inventoryPanel()));
        g.add(card("Prescriptions", prescriptionPanel()));

        return g;
    }

    /* ================= INVENTORY ================= */

    private JPanel inventoryPanel(){
        String[] columns={"ID","Medicine","Stock","Price","Status"};
        inventoryModel=new DefaultTableModel(columns,0);
        inventoryTable=new JTable(inventoryModel);

        JPanel panel=new JPanel(new BorderLayout());
        panel.add(new JScrollPane(inventoryTable),BorderLayout.CENTER);

        JPanel buttons=new JPanel();
        buttons.add(btn("Add",this::addInventory));
        buttons.add(btn("Edit",this::editInventory));
        buttons.add(btn("Delete",this::deleteInventory));

        panel.add(buttons,BorderLayout.SOUTH);

        return panel;
    }

    private void loadInventory(){
        try{
            inventoryModel.setRowCount(0);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM inventory");

            while(rs.next()){
                inventoryModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("medicine"),
                        rs.getInt("stock"),
                        rs.getDouble("price"),
                        rs.getString("status")
                });
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void addInventory(){
        JTextField med=new JTextField();
        JTextField stock=new JTextField();
        JTextField price=new JTextField();

        Object[] msg={
                "Medicine:",med,
                "Stock:",stock,
                "Price:",price
        };

        int ok=JOptionPane.showConfirmDialog(this,msg,"Add Inventory",JOptionPane.OK_CANCEL_OPTION);

        if(ok==JOptionPane.OK_OPTION){
            try{
                Statement st = conn.createStatement();
                st.executeUpdate(
                        "INSERT INTO inventory (medicine,stock,price,status) VALUES ('"
                                +med.getText()+"',"+stock.getText()+","+price.getText()+",'Available')"
                );
                loadInventory();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void editInventory(){
        int row=inventoryTable.getSelectedRow();
        if(row==-1){
            alert("Select inventory item");
            return;
        }

        int id=(int)inventoryModel.getValueAt(row,0);

        JTextField med=new JTextField(inventoryModel.getValueAt(row,1).toString());
        JTextField stock=new JTextField(inventoryModel.getValueAt(row,2).toString());
        JTextField price=new JTextField(inventoryModel.getValueAt(row,3).toString());

        Object[] msg={
                "Medicine:",med,
                "Stock:",stock,
                "Price:",price
        };

        int ok=JOptionPane.showConfirmDialog(this,msg,"Edit Inventory",JOptionPane.OK_CANCEL_OPTION);

        if(ok==JOptionPane.OK_OPTION){
            try{
                Statement st = conn.createStatement();
                st.executeUpdate(
                        "UPDATE inventory SET medicine='"+med.getText()+
                                "', stock="+stock.getText()+
                                ", price="+price.getText()+
                                " WHERE id="+id
                );
                loadInventory();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void deleteInventory(){
        int row=inventoryTable.getSelectedRow();
        if(row==-1){
            alert("Select inventory item");
            return;
        }

        int id=(int)inventoryModel.getValueAt(row,0);

        try{
            Statement st = conn.createStatement();
            st.executeUpdate("DELETE FROM inventory WHERE id="+id);
            loadInventory();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* ================= PRESCRIPTIONS ================= */

    private JPanel prescriptionPanel(){

        String[] columns={
                "ID","Patient","Medicine","Dosage","Instructions","Status"
        };

        prescriptionModel=new DefaultTableModel(columns,0);
        prescriptionTable=new JTable(prescriptionModel);

        JPanel panel=new JPanel(new BorderLayout());
        panel.add(new JScrollPane(prescriptionTable),BorderLayout.CENTER);

        JPanel buttons=new JPanel();
        buttons.add(btn("Delete",this::deletePrescription));
        buttons.add(btn("Change Status",this::changeStatus));

        panel.add(buttons,BorderLayout.SOUTH);

        return panel;
    }

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

    private void deletePrescription(){

        int row=prescriptionTable.getSelectedRow();
        if(row==-1){
            alert("Select prescription first");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this prescription?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if(confirm==JOptionPane.YES_OPTION){

            int id=(int)prescriptionModel.getValueAt(row,0);

            try{
                Statement st = conn.createStatement();
                st.executeUpdate("DELETE FROM prescriptions WHERE id="+id);
                loadPrescriptions();
                alert("Prescription deleted successfully");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private void changeStatus(){

        int row=prescriptionTable.getSelectedRow();
        if(row==-1){
            alert("Select prescription first");
            return;
        }

        int id=(int)prescriptionModel.getValueAt(row,0);

        String[] statuses={"Pending","Processing","Ready","Dispensed"};

        String newStatus=(String)JOptionPane.showInputDialog(
                this,
                "Select new status:",
                "Change Prescription Status",
                JOptionPane.PLAIN_MESSAGE,
                null,
                statuses,
                statuses[0]
        );

        if(newStatus!=null){
            try{
                Statement st = conn.createStatement();
                st.executeUpdate(
                        "UPDATE prescriptions SET status='"+newStatus+
                                "' WHERE id="+id
                );
                loadPrescriptions();
                alert("Status updated successfully");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /* UTILITIES */

    private JButton btn(String t,Runnable r){
        JButton b=new JButton(t);
        b.setFocusPainted(false);
        b.setBackground(accent);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.addActionListener(e->r.run());
        return b;
    }

    private JPanel card(String title,Component content){
        JPanel p=new JPanel(new BorderLayout());
        p.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(8,8,8,8)
        ));

        JLabel l=new JLabel(title);
        l.setFont(new Font("Segoe UI",Font.BOLD,15));

        p.add(l,BorderLayout.NORTH);
        p.add(content,BorderLayout.CENTER);

        return p;
    }

    private void alert(String msg){
        JOptionPane.showMessageDialog(this,msg);
    }

    private JPanel statusBar(){
        JPanel p=new JPanel(new BorderLayout());
        p.setBackground(secondary);

        JLabel l=new JLabel("© 2026 All Right Reserved!");
        l.setForeground(Color.WHITE);

        JLabel r=new JLabel(" PharmaTrack Pro v1.0 ");
        r.setForeground(Color.WHITE);

        p.add(l,BorderLayout.WEST);
        p.add(r,BorderLayout.EAST);

        return p;
    }
}
