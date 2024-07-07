package libraryManagement;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class LibraryManagement {
            

	public static void main(String[] args) {
		login();
	}
	public static void login() {
	     
	    final JFrame f=new JFrame("Login");
	    JLabel l1,l2;  
	    l1=new JLabel("Username");  
	    l1.setBounds(30,15, 100,30);  
	     
	    l2=new JLabel("Password");  
	    l2.setBounds(30,50, 100,30);    
	     
	    final JTextField user = new JTextField();
	    user.setBounds(110, 15, 200, 30);
	         
	    final JPasswordField pass=new JPasswordField();
	    pass.setBounds(110, 50, 200, 30);
	       
	    JButton login_but=new JButton("Login");
	    login_but.setBounds(130,90,80,25);
	    login_but.addActionListener(new ActionListener() {  
	         
	        public void actionPerformed(ActionEvent e){ 
	 
	        String username = user.getText(); 
	        String password = new String(pass.getPassword()); 
	         
	        if(username.equals("")) 
	        {
	            JOptionPane.showMessageDialog(null,"Please enter username"); 
	        } 
	        else if(password.equals("")) 
	        {
	            JOptionPane.showMessageDialog(null,"Please enter password"); 
	        }
	        else { 
	            //System.out.println("Login connect");
	            Connection connection=connect();  
	            try
	            {
	            Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE ,ResultSet.CONCUR_READ_ONLY);
	              stmt.executeUpdate("USE LIBRARY"); 
	              String st = ("SELECT * FROM USERS WHERE USERNAME='"+username+"' AND PASSWORD='"+password+"'"); 
	              ResultSet rs = stmt.executeQuery(st);
	              if(rs.next()==false) { 
	                  System.out.print("No user");  
	                  JOptionPane.showMessageDialog(null,"Wrong Username/Password!"); 
	 
	              }
	              else {
	                  f.dispose();
	                rs.beforeFirst();  
	                while(rs.next())
	                {
	                  String admin = rs.getString("ADMIN"); 
	                  String UID = rs.getString("UID"); 
	                  if(admin.equals("1")) { 
	                      admin_menu(); 
	                  }
	                  else{
	                      user_menu(UID); 
	                  }
	              }
	              }
	            }
	            catch (Exception ex) {
	                 ex.printStackTrace();
	        }
	        }
	    }               
	    });
	 
	     
	    f.add(pass); 
	    f.add(login_but);
	    f.add(user); 
	    f.add(l1);  
	    f.add(l2); 
	     
	    f.setSize(400,180);
	    f.setLayout(null);
	    f.setVisible(true);
	    f.setLocationRelativeTo(null);
	     
	}
	public static Connection connect() {
		try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost/mysql?user=root&password=root");
	        
	        return con;
	 } 
	 catch (Exception ex) {
	        ex.printStackTrace();
	 }
		return null;
	}
	public static void create() {
	    try {
	    Connection connection=connect();
	    ResultSet resultSet = connection.getMetaData().getCatalogs();
	        while (resultSet.next()) {
	          
	          String databaseName = resultSet.getString(1);
	          if(databaseName.equals("library")) {
	             
	              Statement stmt = connection.createStatement();
	             
	              String sql = "DROP DATABASE library";
	              stmt.executeUpdate(sql);
	          }
	        }
	          Statement stmt = connection.createStatement();
	           
	          String sql = "CREATE DATABASE LIBRARY"; 
	          stmt.executeUpdate(sql); 
	          stmt.executeUpdate("USE LIBRARY"); 
	          String sql1 = "CREATE TABLE USERS(UID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(30), PASSWORD VARCHAR(30), ADMIN BOOLEAN)";
	          stmt.executeUpdate(sql1);
	          
	          stmt.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) VALUES('admin','admin',TRUE)");
	          
	          stmt.executeUpdate("CREATE TABLE BOOKS(BID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, BNAME VARCHAR(50), GENRE VARCHAR(20), PRICE INT)");
	          
	          stmt.executeUpdate("CREATE TABLE ISSUED(IID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, UID INT, BID INT, ISSUED_DATE VARCHAR(20), RETURN_DATE VARCHAR(20), PERIOD INT, FINE INT)");
	          
	          stmt.executeUpdate("INSERT INTO BOOKS (BNAME, GENRE, PRICE) VALUES"
	          		+ "('Book 1', 'Fiction', 20),"
	          		+ "    ('Book 2', 'Fantasy', 25),"
	          		+ "    ('Book 3', 'Mystery', 18),"
	          		+ "    ('Book 4', 'Science Fiction', 30),"
	          		+ "    ('Book 5', 'Thriller', 22),"
	          		+ "    ('Book 6', 'Romance', 15),"
	          		+ "    ('Book 7', 'Historical Fiction', 28),"
	          		+ "    ('Book 8', 'Horror', 24),"
	          		+ "    ('Book 9', 'Non-Fiction', 26),"
	          		+ "    ('Book 10', 'Biography', 21);");
	    }
	     catch (Exception ex) {
	         ex.printStackTrace();
	}
	}
	public static void user_menu(final String UID) {
	     
	     
	    JFrame f=new JFrame("User Functions"); 
	    JButton view_but=new JButton("View Books");
	    view_but.setBounds(20,20,120,25);
	    view_but.addActionListener(new ActionListener() { 
	        public void actionPerformed(ActionEvent e){
	             
	            JFrame f = new JFrame("Books Available"); 
	             
	             
	            Connection connection = connect();
	            String sql="select * from BOOKS"; 
	            try {
	                Statement stmt = connection.createStatement(); 
	                 stmt.executeUpdate("USE LIBRARY"); 
	                stmt=connection.createStatement();
	                ResultSet rs=stmt.executeQuery(sql);
	                JTable book_list= new JTable(); 
	                book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	                  
	                JScrollPane scrollPane = new JScrollPane(book_list); 
	 
	                f.add(scrollPane); 
	                f.setSize(800, 400); 
	                f.setVisible(true);
	                f.setLocationRelativeTo(null);
	            } catch (SQLException e1) {
	                
	                 JOptionPane.showMessageDialog(null, e1);
	            }               
	             
	    }
	    }
	    );
	     
	    JButton my_book=new JButton("My Books"); 
	    my_book.setBounds(150,20,120,25);
	    my_book.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e){
	             
	               
	            JFrame f = new JFrame("My Books"); 
	            
	            int UID_int = Integer.parseInt(UID); 
	 
	            
	            Connection connection = connect();
	         
	            String sql="SELECT issued.*, books.bname, books.genre, books.price "
	            		+ "from issued "
	            		+ "join books on issued.bid = books.bid "
	            		+ "where issued.uid = "+UID_int;
	            try {
	                Statement stmt = connection.createStatement();
	         
	                 stmt.executeUpdate("USE LIBRARY");
	                stmt=connection.createStatement();
	  
	                
	                 
	                ResultSet rs=stmt.executeQuery(sql);
	                JTable book_list= new JTable();
	                book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	                
	                JScrollPane scrollPane = new JScrollPane(book_list);
	 
	                f.add(scrollPane); 
	                f.setSize(800, 400);
	                f.setVisible(true);
	                f.setLocationRelativeTo(null);
	            } catch (SQLException e1) {
	                
	                 JOptionPane.showMessageDialog(null, e1);
	            }               
	                 
	    }
	    }
	    );
	    JButton my_query = new JButton("Contact us");
	    my_query.setBounds(150,20,120,25);
	    my_query.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    	}
	    }); 
	     
	    f.add(my_book); 
	    f.add(view_but); 
	    f.setSize(300,100);
	    f.setLayout(null);
	    f.setVisible(true);
	    f.setLocationRelativeTo(null);
	    }
	public static void admin_menu() {
	     
	     
	    JFrame f=new JFrame("Admin Functions"); 
	     
	     
	    JButton create_but=new JButton("Create/Reset");
	    create_but.setBounds(450,60,120,25);
	    create_but.addActionListener(new ActionListener() { 
	        public void actionPerformed(ActionEvent e){
	             
	            create(); 
	            JOptionPane.showMessageDialog(null,"Database Created/Reset!");
	             
	        }
	    });
	     
	     
	    JButton view_but=new JButton("View Books");
	    view_but.setBounds(20,20,120,25);
	    view_but.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e){
	             
	            JFrame f = new JFrame("Books Available"); 
	             
	             
	            Connection connection = connect(); 
	            String sql="select * from BOOKS"; 
	            try {
	                Statement stmt = connection.createStatement();
	                 stmt.executeUpdate("USE LIBRARY"); 
	                stmt=connection.createStatement();
	                ResultSet rs=stmt.executeQuery(sql);
	                JTable book_list= new JTable();
	                book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	             
	                JScrollPane scrollPane = new JScrollPane(book_list); 
	 
	                f.add(scrollPane);
	                f.setSize(800, 400); 
	                f.setVisible(true);
	                f.setLocationRelativeTo(null);
	            } catch (SQLException e1) {
	                
	                 JOptionPane.showMessageDialog(null, e1);
	            }               
	             
	    }
	    }
	    );
	     
	    JButton users_but=new JButton("View Users");
	    users_but.setBounds(150,20,120,25);
	    users_but.addActionListener(new ActionListener() { 
	        public void actionPerformed(ActionEvent e){
	                 
	                JFrame f = new JFrame("Users List");
	                
	                 
	                 
	                Connection connection = connect();
	                String sql="select * from users"; 
	                try {
	                    Statement stmt = connection.createStatement();
	                     stmt.executeUpdate("USE LIBRARY");
	                    stmt=connection.createStatement();
	                    ResultSet rs=stmt.executeQuery(sql);
	                    JTable book_list= new JTable();
	                    book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	                
	                    JScrollPane scrollPane = new JScrollPane(book_list);
	 
	                    f.add(scrollPane);
	                    f.setSize(800, 400);
	                    f.setVisible(true);
	                    f.setLocationRelativeTo(null);
	                } catch (SQLException e1) {
	                    
	                     JOptionPane.showMessageDialog(null, e1);
	                }       
	                 
	                 
	    }
	        }
	    );  
	     
	    JButton issued_but=new JButton("View Issued Books");
	    issued_but.setBounds(280,20,160,25);
	    issued_but.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e){
	                 
	                JFrame f = new JFrame("Users List");
	                
	                 
	                 
	                Connection connection = connect();
	                String sql="select * from issued";
	                try {
	                    Statement stmt = connection.createStatement();
	                     stmt.executeUpdate("USE LIBRARY");
	                    stmt=connection.createStatement();
	                    ResultSet rs=stmt.executeQuery(sql);
	                    JTable book_list= new JTable();
	                    book_list.setModel(DbUtils.resultSetToTableModel(rs)); 
	                     
	                    JScrollPane scrollPane = new JScrollPane(book_list);
	 
	                    f.add(scrollPane);
	                    f.setSize(800, 400);
	                    f.setVisible(true);
	                    f.setLocationRelativeTo(null);
	                } catch (SQLException e1) {
	                    
	                     JOptionPane.showMessageDialog(null, e1);
	                }       
	                             
	    }
	        }
	    );
	     
	     
	    JButton add_user=new JButton("Add User");
	    add_user.setBounds(20,60,120,25); 
	     
	    add_user.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e){
	                 
	                final JFrame g = new JFrame("Enter User Details"); 
	                
	                JLabel l1,l2;  
	                l1=new JLabel("Username");  
	                l1.setBounds(30,15, 100,30); 
	                 
	                 
	                l2=new JLabel("Password"); 
	                l2.setBounds(30,50, 100,30); 
	                 
	                
	                final JTextField F_user = new JTextField();
	                F_user.setBounds(110, 15, 200, 30);
	                 
	                
	                final JPasswordField F_pass=new JPasswordField();
	                F_pass.setBounds(110, 50, 200, 30);
	               
	                final JRadioButton a1 = new JRadioButton("Admin");
	                a1.setBounds(55, 80, 200,30);
	                
	                JRadioButton a2 = new JRadioButton("User");
	                a2.setBounds(130, 80, 200,30);
	              
	                ButtonGroup bg=new ButtonGroup();    
	                bg.add(a1);bg.add(a2);  
	                 
	                                 
	                JButton create_but=new JButton("Create");
	                create_but.setBounds(130,130,80,25);
	                create_but.addActionListener(new ActionListener() {
	                     
	                    public void actionPerformed(ActionEvent e){
	                     
	                    String username = F_user.getText();
	                    String password = new String(F_pass.getPassword());
	                    boolean admin = false;
	                     
	                    if(a1.isSelected()) {
	                        admin=true;
	                    }
	                     
	                    Connection connection = connect();
	                     
	                    try {
	                    Statement stmt = connection.createStatement();
	                     stmt.executeUpdate("USE LIBRARY");
	                     stmt.executeUpdate("INSERT INTO USERS(USERNAME,PASSWORD,ADMIN) VALUES ('"+username+"','"+password+"',"+admin+")");
	                     JOptionPane.showMessageDialog(null,"User added!");
	                     g.dispose();
	                      
	                    }
	                     
	                    catch (SQLException e1) {
	                        
	                         JOptionPane.showMessageDialog(null, e1);
	                    }   
	                     
	                    }
	                     
	                });
	                     
	                 
	                    g.add(create_but);
	                    g.add(a2);
	                    g.add(a1);
	                    g.add(l1);
	                    g.add(l2);
	                    g.add(F_user);
	                    g.add(F_pass);
	                    g.setSize(350,200);
	                    g.setLayout(null);
	                    g.setVisible(true);
	                    g.setLocationRelativeTo(null);
	                 
	                 
	    }
	    });
	         
	     
	    JButton add_book=new JButton("Add Book");
	    add_book.setBounds(150,60,120,25); 
	     
	    add_book.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e){
	                
	                final JFrame g = new JFrame("Enter Book Details");
	             
	                JLabel l1,l2,l3;  
	                l1=new JLabel("Book Name");
	                l1.setBounds(30,15, 100,30); 
	                 
	                 
	                l2=new JLabel("Genre");  
	                l2.setBounds(30,53, 100,30); 
	                 
	                l3=new JLabel("Price");
	                l3.setBounds(30,90, 100,30); 
	                 
	                
	                final JTextField F_bname = new JTextField();
	                F_bname.setBounds(110, 15, 200, 30);
	                 
	                
	                final JTextField F_genre=new JTextField();
	                F_genre.setBounds(110, 53, 200, 30);
	                
	                final JTextField F_price=new JTextField();
	                F_price.setBounds(110, 90, 200, 30);
	                         
	                 
	                JButton create_but=new JButton("Submit");
	                create_but.setBounds(130,130,80,25);
	                create_but.addActionListener(new ActionListener() {
	                     
	                    public void actionPerformed(ActionEvent e){
	                   
	                    String bname = F_bname.getText();
	                    String genre = F_genre.getText();
	                    String price = F_price.getText();
	                    
	                    int price_int = Integer.parseInt(price);
	                     
	                    Connection connection = connect();
	                     
	                    try {
	                    Statement stmt = connection.createStatement();
	                     stmt.executeUpdate("USE LIBRARY");
	                     stmt.executeUpdate("INSERT INTO BOOKS(BNAME,GENRE,PRICE) VALUES ('"+bname+"','"+genre+"',"+price_int+")");
	                     JOptionPane.showMessageDialog(null,"Book added!");
	                     g.dispose();
	                      
	                    }
	                     
	                    catch (SQLException e1) {
	                        
	                         JOptionPane.showMessageDialog(null, e1);
	                    }   
	                     
	                    }
	                     
	                });
	                                 
	                    g.add(l3);
	                    g.add(create_but);
	                    g.add(l1);
	                    g.add(l2);
	                    g.add(F_bname);
	                    g.add(F_genre);
	                    g.add(F_price);
	                    g.setSize(350,200);
	                    g.setLayout(null);
	                    g.setVisible(true);
	                    g.setLocationRelativeTo(null);
	                             
	    }
	    });
	     
	     
	    JButton issue_book=new JButton("Issue Book");
	    issue_book.setBounds(450,20,120,25); 
	     
	    issue_book.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e){
	               
	                final JFrame g = new JFrame("Enter Details");
	                JLabel l1,l2,l3,l4;  
	                l1=new JLabel("Book ID(BID)"); 
	                l1.setBounds(30,15, 100,30); 
	                 
	                 
	                l2=new JLabel("User ID(UID)"); 
	                l2.setBounds(30,53, 100,30); 
	                 
	                l3=new JLabel("Period(days)"); 
	                l3.setBounds(30,90, 100,30); 
	                 
	                l4=new JLabel("Issued Date(DD-MM-YYYY)");  
	                l4.setBounds(30,127, 150,30); 
	                 
	                final JTextField F_bid = new JTextField();
	                F_bid.setBounds(110, 15, 200, 30);
	                 
	                 
	                final JTextField F_uid=new JTextField();
	                F_uid.setBounds(110, 53, 200, 30);
	                 
	                final JTextField F_period=new JTextField();
	                F_period.setBounds(110, 90, 200, 30);
	                 
	                final JTextField F_issue=new JTextField();
	                F_issue.setBounds(180, 130, 130, 30);   
	 
	                 
	                JButton create_but=new JButton("Submit");  
	                create_but.setBounds(130,170,80,25); 
	                create_but.addActionListener(new ActionListener() {
	                     
	                    public void actionPerformed(ActionEvent e){
	                     
	                    String uid = F_uid.getText();
	                    String bid = F_bid.getText();
	                    String period = F_period.getText();
	                    String issued_date = F_issue.getText();
	 
	                    int period_int = Integer.parseInt(period);
	                     
	                    Connection connection = connect();
	                     
	                    try {
	                    Statement stmt = connection.createStatement();
	                     stmt.executeUpdate("USE LIBRARY");
	                     stmt.executeUpdate("INSERT INTO ISSUED(UID,BID,ISSUED_DATE,PERIOD) VALUES ('"+uid+"','"+bid+"','"+issued_date+"',"+period_int+")");
	                     JOptionPane.showMessageDialog(null,"Book Issued!");
	                     g.dispose();
	                      
	                    }
	                     
	                    catch (SQLException e1) {
	                        
	                         JOptionPane.showMessageDialog(null, e1);
	                    }   
	                     
	                    }
	                     
	                });
	                     
	                 
	                    g.add(l3);
	                    g.add(l4);
	                    g.add(create_but);
	                    g.add(l1);
	                    g.add(l2);
	                    g.add(F_uid);
	                    g.add(F_bid);
	                    g.add(F_period);
	                    g.add(F_issue);
	                    g.setSize(350,250);
	                    g.setLayout(null);
	                    g.setVisible(true);
	                    g.setLocationRelativeTo(null);
	                 
	                 
	    }
	    });
	     
	     
	    JButton return_book=new JButton("Return Book");
	    return_book.setBounds(280,60,160,25); 
	     
	    return_book.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e){
	                 
	                final JFrame g = new JFrame("Enter Details");
	                
	                JLabel l1,l4;  
	                l1=new JLabel("Issue ID(IID)"); 
	                l1.setBounds(30,15, 100,30); 
	                
	                 
	                l4=new JLabel("Return Date(DD-MM-YYYY)");  
	                l4.setBounds(30,50, 150,30); 
	                 
	                final JTextField F_iid = new JTextField();
	                F_iid.setBounds(110, 15, 200, 30);
	                 
	                 
	                final JTextField F_return=new JTextField();
	                F_return.setBounds(180, 50, 130, 30);
	             
	 
	                JButton create_but=new JButton("Return");
	                create_but.setBounds(130,170,80,25);
	                create_but.addActionListener(new ActionListener() {
	                     
	                    public void actionPerformed(ActionEvent e){                 
	                     
	                    String iid = F_iid.getText();
	                    String return_date = F_return.getText();
	                     
	                    Connection connection = connect();
	                     
	                    try {
	                    Statement stmt = connection.createStatement();
	                     stmt.executeUpdate("USE LIBRARY");
	                     
	                     String date1=null;
	                     String date2=return_date; 
	                     
	                  
	                     ResultSet rs = stmt.executeQuery("SELECT ISSUED_DATE FROM ISSUED WHERE IID="+iid);
	                     while (rs.next()) {
	                         date1 = rs.getString(1);
	                          
	                       }
	                      int days = 0;
	                     try {
	                            Date date_1=new SimpleDateFormat("dd-MM-yyyy").parse(date1);
	                            Date date_2=new SimpleDateFormat("dd-MM-yyyy").parse(date2);
	                        
	                            long diff = date_2.getTime() - date_1.getTime();
	                           
	                            days=(int)(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	                             
	                             
	                        } catch (ParseException e1) {
	                            
	                            e1.printStackTrace();
	                        }
	                      
	                     
	                  
	                     stmt.executeUpdate("UPDATE ISSUED SET RETURN_DATE='"+return_date+"' WHERE IID="+iid);
	                     g.dispose();
	                      
	 
	                     Connection connection1 = connect();
	                     Statement stmt1 = connection1.createStatement();
	                     stmt1.executeUpdate("USE LIBRARY");                
	                    ResultSet rs1 = stmt1.executeQuery("SELECT PERIOD FROM ISSUED WHERE IID="+iid); 
	                    String diff=null; 
	                    while (rs1.next()) {
	                         diff = rs1.getString(1);
	                          
	                       }
	                    int diff_int = Integer.parseInt(diff);
	                    if(days>diff_int) { 
	                        int fine = (days-diff_int)*10; 
	                        stmt1.executeUpdate("UPDATE ISSUED SET FINE="+fine+" WHERE IID="+iid);  
	                        String fine_str = ("Fine: Rs. "+fine);
	                        JOptionPane.showMessageDialog(null,fine_str);
	                         
	                    }
	 
	                     JOptionPane.showMessageDialog(null,"Book Returned!");
	                      
	                    }
	                             
	                     
	                    catch (SQLException e1) {
	                        
	                         JOptionPane.showMessageDialog(null, e1);
	                    }   
	                     
	                    }
	                     
	                }); 
	                    g.add(l4);
	                    g.add(create_but);
	                    g.add(l1);
	                    g.add(F_iid);
	                    g.add(F_return);
	                    g.setSize(350,250);
	                    g.setLayout(null);
	                    g.setVisible(true);
	                    g.setLocationRelativeTo(null);              
	    }
	    });
	     
	    f.add(create_but);
	    f.add(return_book);
	    f.add(issue_book);
	    f.add(add_book);
	    f.add(issued_but);
	    f.add(users_but);
	    f.add(view_but);
	    f.add(add_user);
	    f.setSize(600,200); 
	    f.setLayout(null);
	    f.setVisible(true);
	    f.setLocationRelativeTo(null);
	     
	    }
}
