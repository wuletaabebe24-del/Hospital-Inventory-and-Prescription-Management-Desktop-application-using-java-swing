package hospital_Dashboard;

import java.sql.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class Hospital_Dashboard extends JFrame {
	private static final String url="jdbc:mysql://localhost:3306/pharmacy";
	private static final String username="root";
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confpasswordField;
    private JTextField RoleField;
    private void Register() throws ClassNotFoundException, SQLException {
   	 Class.forName("com.mysql.cj.jdbc.Driver");
   	 Connection con=DriverManager.getConnection(url,username,"");
		Statement stmt=con.createStatement();
       JFrame jf=new JFrame();
       jf.setSize(600,400);
       jf.setLocation(400,180);
       jf.setLayout(new GridBagLayout());
       jf.setResizable(false);
       jf.setBackground(new Color(240, 248, 255));
       ImageIcon frameIcon = new ImageIcon("resources/logo.png"); 
       jf.setIconImage(frameIcon.getImage());
       //JPanel mainPanel = new JPanel(new GridBagLayout());
       jf.setBackground(new Color(240, 248, 255));
       GridBagConstraints gbc = new GridBagConstraints();
       gbc.insets = new Insets(10, 10, 10, 10);
       
       // Title
       JLabel titleLabel = new JLabel("Hospital Registration Form");
       titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
       titleLabel.setForeground(new Color(25, 25, 112));
       gbc.gridx = 0;
       gbc.gridy = 0;
       gbc.gridwidth = 2;
      jf.add(titleLabel, gbc);
       
       // User name
       gbc.gridwidth = 1;
       gbc.gridy = 1;
       gbc.gridx = 0;
       jf.add(new JLabel("Username:"), gbc);
       
       usernameField = new JTextField(15);
       gbc.gridx = 1;
      jf.add(usernameField, gbc);
       
       // Password
       gbc.gridy = 2;
       gbc.gridx = 0;
       jf.add(new JLabel("Password:"), gbc);
       
       passwordField = new JPasswordField(15);
       gbc.gridx = 1;
      jf.add(passwordField, gbc);
       
      //confirm  Password
       gbc.gridy = 3;
       gbc.gridx = 0;
       jf.add(new JLabel("Confirm Password:"), gbc);
       
       confpasswordField = new JPasswordField(15);
       gbc.gridx = 1;
      jf.add(confpasswordField, gbc);
      gbc.gridy = 4;
      gbc.gridx = 0;
      jf.add(new JLabel("Role:"), gbc);
      
      RoleField = new JTextField(15);
      gbc.gridx = 1;
     jf.add( RoleField, gbc);
     
     String languge[]={"Select Role","doctor","pharmacist"};
     JComboBox cb=new JComboBox(languge);
     cb.setBounds(350,290,130,40);
     cb.setBackground(Color.white);
     gbc.gridx = 2;
     jf.add(cb,gbc);
     cb.addActionListener(new ActionListener(){ 
         @Override
         public void actionPerformed(ActionEvent e) {
         String st=cb.getSelectedItem().toString();
         RoleField.setText(st);
         }
     });
       //RegistrationButton
       JButton RegistrationButton = new JButton("Register");
       RegistrationButton.setBackground(new Color(70, 130, 180));
       RegistrationButton.setForeground(Color.WHITE);
       RegistrationButton.setFont(new Font("Arial", Font.BOLD, 12));
       
       gbc.gridy = 5;
       gbc.gridx = 0;
       gbc.gridwidth = 2;
       jf.add(RegistrationButton, gbc);
       RegistrationButton.addActionListener(new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e) {
           String username=usernameField.getText(); 
           String password=new String(passwordField.getPassword());
           String role=RoleField.getText();
           if(username.isEmpty()|| password.isBlank()) {
        	   JOptionPane.showMessageDialog(jf,"Please Enter All Empty Field!","Error",JOptionPane.ERROR_MESSAGE);
        	   return;
           }
           String inse="insert into users(username,password,role) values('"+username+"','"+password+"','"+role+"')";
           try {
				stmt.executeUpdate(inse);
				JOptionPane.showMessageDialog(jf, "Sucessful Registered");
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
           }
              
          });
       //jf.add(mainPanel);
       jf.setVisible(true);
   }
     private void Login() throws ClassNotFoundException ,SQLException {
    	 Class.forName("com.mysql.cj.jdbc.Driver");
    	 Connection con=DriverManager.getConnection(url,username,"");
    	 Statement stmt=con.createStatement();
        JFrame jf=new JFrame();
        jf.setSize(400,400);
        jf.setLocation(400,170);
        jf.setLayout(new GridBagLayout());
        jf.setResizable(false);
        jf.setBackground(new Color(240, 248, 255));
        ImageIcon frameIcon = new ImageIcon("resources/logo.png"); 
        jf.setIconImage(frameIcon.getImage());
        //JPanel mainPanel = new JPanel(new GridBagLayout());
        //jf.setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Hospital Login Form");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(25, 25, 112));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
       jf.add(titleLabel, gbc);
        
        //Username
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        jf.add(new JLabel("Username:"), gbc);
        
        usernameField = new JTextField(15);
        gbc.gridx = 1;
       jf.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        jf.add(new JLabel("Password:"), gbc);
        
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
       jf.add(passwordField, gbc);
        // Login Button
        JButton loginButton = new JButton(" Login ");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        jf.add(loginButton, gbc);
        
        // Register Button
        JLabel reglabel=new JLabel("New User?");
        reglabel.setForeground(new Color(70, 130, 180));
        JButton registerButton = new JButton(" Register");
        registerButton.setBorderPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.setForeground(new Color(70, 130, 180));
        gbc.gridy = 5;
        gbc.gridx = 0;
        jf.add(reglabel, gbc);
        
        gbc.gridy = 5;
        gbc.gridx = 2;
        jf.add(registerButton, gbc);
        registerButton.addActionListener(new ActionListener() {
        	@Override
        public void actionPerformed(ActionEvent e) {
        		try {
        			
        			Register();	
        			jf.setVisible(false);
        		}catch(ClassNotFoundException |SQLException e2) {
        			
        		}
        	}
        	
        });
        loginButton.addActionListener(new ActionListener() {
        	@Override
        public void actionPerformed(ActionEvent e) {
        		
        		try {
        			String username=usernameField.getText();
        		String password =new String(passwordField.getPassword());
        		String sql="select * from users where username='"+username+"'AND password='"+password+"'";
        		ResultSet rs=stmt.executeQuery(sql);
        		if (rs.next()) {
        		    String role = rs.getString("role");

        		    if ("pharmacist".equals(role)) {
        		        new PharmacyDashboard().setVisible(true);
        		        setVisible(false);
        		        jf.setVisible(false);
        		    } else if ("doctor".equals(role)) {
        		        new DoctorPrescriptionDashboard().setVisible(true);
        		        setVisible(false);
        		        jf.setVisible(false);
        		    } else {
        		        JOptionPane.showMessageDialog(null, "Unknown role!");
        		    }
        		} 
        		else if(username.isEmpty()|password.isEmpty()) {
        			 JOptionPane.showMessageDialog(jf,"Please Enter All Empty Field!","Empty Field",JOptionPane.ERROR_MESSAGE);
        		}
        		else {
        			 JOptionPane.showMessageDialog(jf,"User dose not exist!","Error",JOptionPane.ERROR_MESSAGE);
        			 return;
        		}
        		}catch(Exception e3) {}
        	}
        	
        });
        jf.setVisible(true);
    }
    
public Hospital_Dashboard(){
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("HOSPITAL INVPRE TRACKING PORTAL ");
        setBounds(65,60,1150,600);
        ImageIcon frameIcon = new ImageIcon("resources/logo.png"); 
        setIconImage(frameIcon.getImage());
        JPanel jpanel1=new JPanel();
        jpanel1.setBackground(new Color(240, 248, 255));
        jpanel1.setPreferredSize(new Dimension(0, 60));
        JPanel jp2=new JPanel();
        jp2.setBackground(new Color(195, 207, 226));
        jp2.setPreferredSize(new Dimension(300, 0));
        
        JPanel jp3=new JPanel();
        jp3.setBackground(new Color(195, 207, 226));
        JPanel jpanel4=new JPanel();
        jpanel4.setBackground(new Color(240, 248, 255));
        jpanel4.setPreferredSize(new Dimension(0, 60));
        
        JPanel jpanel5=new JPanel();
        jpanel5.setBackground(new Color(240, 248, 255));
        jpanel5.setPreferredSize(new Dimension(60, 0));
        JLabel l=new JLabel("<html><body><br> &copy; 2026 All Right Reserved! &nbsp;&nbsp;</body></html> ");
        l.setForeground(new Color(41, 128, 185));

        JLabel r=new JLabel("<html><body> <br> &nbsp;&nbsp;   HospitalTrack Pro v1.0  </body></htm>");
        r.setForeground(new Color(41, 128, 185));

        jpanel4.add(l,BorderLayout.WEST);
        jpanel4.add(r,BorderLayout.EAST);
        //Login and Registration  button
        JButton login=new JButton("      Login    ");
        login.setFont(new Font("Arial",Font.BOLD,24));
        login.setForeground(Color.white);
        login.setBackground(new Color(41, 128, 185));
       
        JButton register=new JButton("   Register   ");
        register.setFont(new Font("Arial",Font.BOLD,24));
        register.setForeground(Color.white);
        register.setBackground(new Color(41, 128, 185));
        
        login.addActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
           try {
			Login();
		   } catch (Exception e1) {
			
			e1.printStackTrace();
		   } 
         }
            
        });
        register.addActionListener(new ActionListener(){
         @Override
         public void actionPerformed(ActionEvent e) {
           try {
			Register();
		   } catch (ClassNotFoundException | SQLException e1) {
			
			e1.printStackTrace();
			
		   } 
         }
            
        });
        add(jpanel1,BorderLayout.NORTH);
        add(jp2,BorderLayout.WEST);
        add(jp3,BorderLayout.CENTER);
        add(jpanel4,BorderLayout.SOUTH);
        add(jpanel5,BorderLayout.EAST);
       jp2.setLayout(new GridBagLayout()); 
        GridBagConstraints gbc =new GridBagConstraints();
        gbc.insets=new Insets(10,10,15,15);

        gbc.gridx=3;
        gbc.gridy=3;
        jp2.add(login,gbc);
        gbc.gridx=3;
        gbc.gridy=4;
        jp2.add(register,gbc);
        JLabel jl=new JLabel("Hospital Inventor And Prescription Tracking Portal");
        JLabel jl2=new JLabel("<html><h1 style='margin-top:70px; font-size:24px;'>Welcom To Hospital Digital<br> Inventory And Prescription<br>&nbsp;&nbsp;&nbsp; Tracking Portal</h1></html>");
        jl2.setForeground(new Color(25, 25, 112));
        jl.setFont(new Font("Arial",Font.BOLD,30));
        jpanel1.add(jl);
        jp3.add(jl2);
        setVisible(true);
}
    public static void main(String[] args) {
    	
       new Hospital_Dashboard(); 
       
    }
}


