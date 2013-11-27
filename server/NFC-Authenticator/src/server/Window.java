package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RootPaneContainer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Window extends JPanel implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame frame;
	private UsersDB users;
	private JLabel title;
	private JLabel textUsers;
	private JLabel textAuths;
	private JLabel textAuthsUser;
	private JButton start;
	private JButton addUser;
	private JButton remUser;
	private JButton addAuth;
	private JButton remAuth;
	private JButton addAuthUser;
	private JButton remAuthUser;
	private JButton genQR;
	private JButton showMap;
	private JList userList;
	private JList authUserList;
	private JList authList;
	private JTextPane log;
	private JScrollPane scrollUsers;
	private JScrollPane scrollAuthUser;
	private JScrollPane scrollAuth;
	private JScrollPane scrollLog;
	private StringBuilder textLog;
	private JFrame mapFrame;
	private Map map;
	
	
	private Aim aim;
	
	private int port;
	
	static final int C1 = 10; // posizione orizzontale prima colonna
	static final int C2 = 200; // posizione orizzontale seconda colonna
	static final int C3 = 400; // posizione orizzonatale terza colonna
	static final int R1 = 50; // posizione orizzontale prima riga
	static final int R2 = 80; // posizione orizzontale seconda riga
	static final int R3 = 400; // posizione orizzonatale terza riga
	static final int SP = 10;
	
	static final int WIDTH = C3 + 190; // larghezza finestra principale
	static final int HEIGHT = R3 + 200; // altezza finestra principale

	private boolean isStart;
	static private Listener ls;
	static private Thread listener;
	static private Window window;
	
	public Window (JFrame frame, UsersDB users, int port) {
		this.frame = frame;
		this.users = users;
		this.port = port;
		isStart = false;
		window = this;
		map = new Map();
		mapFrame = new JFrame();
		mapFrame.setSize(500,500);
		mapFrame.setLocationRelativeTo(null);
		mapFrame.setResizable(false);
		mapFrame.getContentPane().add(map);
		mapFrame.setVisible(false);
	}
	
	public void signal(int n, String s, String text) throws IOException
	{
		switch(n)
		{
			case 0:
				textLog.append("<font family=arial size=3>"+text+"</font><br>");
				log.setText(textLog.toString());
				break;
			case 1:	
				textLog.append("<font color=green family=arial size=3>"+text+"</font><br>");
				log.setText(textLog.toString());
				MyTimer t = new MyTimer(s, map, n);
    			Thread thread = new Thread(t);
    			thread.start();
    			mapFrame.setVisible(true);
    			System.out.println("N" +n);
    			break;
			case 2:	
				textLog.append("<font color=red family=arial size=3>"+text+"</font><br>");
				log.setText(textLog.toString());
				MyTimer t2 = new MyTimer(s, map, n);
    			Thread thread2 = new Thread(t2);
    			thread2.start();
    			mapFrame.setVisible(true);
    			System.out.println("N3" +n);
    			break;
		}
	}


	public void run () {
		frame.setSize(WIDTH, HEIGHT);
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setLayout(null);
        frame.setResizable(false);
        
        title = new JLabel("<html><h3>NFC Locator Server</h3></html>");
        title.setBounds(C1, 10, (WIDTH/2), 20);
        
        textUsers = new JLabel("<html><i> Users: </i></html> ");
        textUsers.setBounds(C1, R1, C2-C1-SP, 20);  
       
        textAuths = new JLabel("<html><i> Authorizations: </i></html> ");
        textAuths.setBounds(C3, R1, C2-C1-SP, 20);
        
        start = new JButton("<html><font color=green>Start Server</font>");
        start.setBounds(200, 12, 120, 20);
        
        showMap = new JButton("<html><font color=green>Show Map</font>");
        showMap.setBounds(C3+30, 12, 120, 20);

        addUser = new JButton("+");
        addUser.setBounds(C1, R3, 45, 20);
        
        remUser = new JButton("-");
        remUser.setBounds(C1 + 55, R3, 45, 20);
        
        addAuthUser = new JButton("<html><h5>Add Auth</h5></html>");
        addAuthUser.setBounds(C2,R3, 100, 20);
        addAuthUser.setMargin(new Insets(1,1,1,1));
        
        remAuthUser = new JButton("<html><h5>Remove Auth</h5></html>");
        remAuthUser.setBounds(C2,R3+30, 100, 20);
        remAuthUser.setMargin(new Insets(1,1,1,1));
        
        addAuth = new JButton("+");
        addAuth.setBounds(C3, R3, 45, 20);
        addAuth.setMargin(new Insets(1,1,1,1));
        
        remAuth = new JButton("-");
        remAuth.setBounds(C3 + 55, R3, 45, 20);
        remAuth.setMargin(new Insets(1,1,1,1));
        
        genQR = new JButton("<html><h4 color=#46333 align=center>Generate<br>QR Code</h4></html>");
        genQR.setBounds(C1, R3+30, 100, 40);
      
		userList = new JList(users.getUsers());
        userList.setFixedCellWidth(20);
        userList.setFixedCellHeight(20);
        userList.setSelectedIndex(0);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollUsers = new JScrollPane(userList);
        scrollUsers.setBounds(C1, R2, C2-C1-SP-20, R3-R2-SP);
        
        authUserList = new JList(users.getAuthsUsers((String) userList.getSelectedValue()));
        authUserList.setFixedCellWidth(20);
        authUserList.setFixedCellHeight(20);
        authUserList.setSelectedIndex(0);
        authUserList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollAuthUser = new JScrollPane(authUserList);
        scrollAuthUser.setBounds(C2, R2, C3-C2-5*SP, R3-R2-SP);
        
        authList = new JList(users.getAuths());
        authList.setFixedCellWidth(20);
        authList.setFixedCellHeight(20);
        authList.setSelectedIndex(0);
        authList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollAuth = new JScrollPane(authList);
        scrollAuth.setBounds(C3, R2, C2-C1-SP, R3-R2-SP);
        
        if(userList.getSelectedValue() == null)
        	 textAuthsUser = new JLabel ("<html><i><h5>  No User Selected </h5></i></html> ");
        else
        	 textAuthsUser = new JLabel ("<html><i><h5> Authorizations of user :<br><font size=4 color=red>&nbsp;"  +  userList.getSelectedValue() + "</font> </h5></i></html> ");
        textAuthsUser.setBounds(C2, R1, C3-C2, 30);
 
        aim = new Aim(30, 20);
        aim.setBounds(C2-30,R1+20,30,R3);
        
        textLog = new StringBuilder();
        log = new JTextPane();
        log.setEditable(false);
        log.setContentType("text/html");
        scrollLog = new JScrollPane(log);
        scrollLog.setBounds(C1, R3+90, 500, 100);
       
        panel.add(aim);
        panel.add(title);
        panel.add(textUsers);
        panel.add(textAuths);
        panel.add(textAuthsUser);
        panel.add(scrollAuth);
        panel.add(scrollAuthUser);
        panel.add(scrollUsers);
        panel.add(start);
        panel.add(showMap);
        panel.add(addUser);
        panel.add(remUser);
        panel.add(addAuthUser);
        panel.add(remAuthUser);
        panel.add(addAuth);
        panel.add(remAuth);
        panel.add(genQR);
        panel.add(scrollLog);

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        userList.addListSelectionListener(new ListSelectionListener() {
        	public void valueChanged(ListSelectionEvent e) {
        		
        		String username = (String) userList.getSelectedValue();
        		textAuthsUser.setText("<html><i><h5> Authorizations of user :<br> <font size=4 color=red>&nbsp;"  +  userList.getSelectedValue() + "</font></h5></i></html> ");
        		authUserList.setListData(users.getAuthsUsers(username));
        		aim.move((userList.getSelectedIndex()+1)*20 - scrollUsers.getVerticalScrollBar().getValue() );
        		aim.repaint();
        		
        	}
        });
        
        scrollUsers.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent ae) {
            	 aim.move((userList.getSelectedIndex()+1)*20 - scrollUsers.getVerticalScrollBar().getValue() );
            	 aim.repaint();
            }   	
        });
        
        start.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		if(!isStart)
        		{
        			ls = new Listener(port, users, window);
        			listener = new Thread(ls);
        			listener.start();
        			start.setText("Stop Server");
        			isStart = true;
        					
        		}
        		else
        		{
        			ls.stop();
        			start.setText("Start Server");
        			isStart = false		
        			        ;
        		}
        	}
        });
        
        showMap.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mapFrame.setVisible(true);
    		}
        	
    	});
    
        addUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		JFrame add = new JFrame("Insert a new User");
        		new WindowAddUser(add);
    	        frame.addWindowListener(new WindowAdapter() {
    		        @Override
    		        public void windowClosing(WindowEvent e) {
    		        	frame.setVisible(false);
    		            System.exit(0);
    		        }
    	        });
        	}
        		
        });
        
        remUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if(userList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No user selected");
        		else
        		{
	        		String userRemoved = users.removeUser((String) userList.getSelectedValue());
	        		userList.setListData(users.getUsers());
	        		System.out.println("-- Account '" + userRemoved + "' REMOVED!");
	        		userList.setSelectedIndex(userList.getLastVisibleIndex());
	        		if(userList.getSelectedValue() == null)
	        			textAuthsUser.setText("<html><i><h5>  No User Selected </h5></i></html> ");
	        			
        		}
        	}
        });
        
        addAuth.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		JFrame newAuth = new JFrame("Add an Authorization");
        		new WindowAddAuth(newAuth);
        	}
        });
        
        remAuth.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if(users.getAuths().length == 0)
        			JOptionPane.showMessageDialog(frame, "There are no authorizations");
        		else if(authList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No authorization selected");
        		else if(users.getUsersAuth((String) authList.getSelectedValue()).length == 0 || 
        				JOptionPane.showConfirmDialog(null, "There are several users which use this authorization. \n Are you sure to cancel this authorization?", 
        						"Cancel " + authList.getSelectedValue(), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	        	{
		        	users.removeAuth((String) authList.getSelectedValue());
		        	authList.setListData(users.getAuths());
		        	authUserList.setListData(users.getAuthsUsers((String)userList.getSelectedValue()));
	        	}
        	}
        });
        
        addAuthUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if(userList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No user selected");
        		else if(users.getAuths().length == 0 || users.getAuthsNOUser((String) userList.getSelectedValue()).length == 0)
        			JOptionPane.showMessageDialog(frame, "There are no authorizations");
        		else
        		{
	        		JFrame addAuthUser = new JFrame("Add an Authorization to user" + userList.getSelectedValue());
	        		new WindowAddAuthUser(addAuthUser, (String)userList.getSelectedValue());
        		}
        	}
        }); 
        
        remAuthUser.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		if(authUserList.getSelectedValue() == null)
        			JOptionPane.showMessageDialog(frame, "No authorization selected");
        		else
        		{
	        		users.remAuthToUser((String)userList.getSelectedValue(), (String) authUserList.getSelectedValue());
	        		authUserList.setListData(users.getAuthsUsers((String)userList.getSelectedValue()));
        		}
        	}
        });
        
        genQR.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e)
        	{
        		try {
        			if(userList.getSelectedValue() == null)
	        			JOptionPane.showMessageDialog(frame, "No user selected");
        			else {
        				JFrame qr = new JFrame();
        				JLabel user = new JLabel("<html><h2>&nbsp;"+userList.getSelectedValue()+ "</h2></html>");
        				
        				qr.setSize(260, 350);
        				qr.setBackground(Color.DARK_GRAY);
        				qr.setLocationRelativeTo(null);
        				qr.setResizable(false);
        				user.setBounds(10, 10, 240, 30);
        				user.setBackground(Color.white);
        				
        				
        				user.setOpaque(true);
        				user.setForeground(new Color(23453));
        				
        				qr.add(user);
        				qr.add(new QRCodeWindow("address:"+InetAddress.getLocalHost().getHostAddress()+" port:"+port+ " username:"+userList.getSelectedValue()));
        				qr.setVisible(true); 
        				System.out.println(user.getWidth() + "");
 
	        		}
	        	} catch(UnknownHostException e1){
	        		e1.printStackTrace();
	        		
	        	}
        	}
        });
	}
	
	class WindowAddUser extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JFrame frame;
		public WindowAddUser (JFrame fram) {

			this.frame = fram;
			frame.setSize(330, 120);
			frame.setResizable(false);
	        JPanel panel = (JPanel) frame.getContentPane();
	        panel.setLayout(null);
	        JLabel labelUsername = new JLabel("Username:");
	        final JTextField textUsername = new JTextField();
	        JLabel labelPassword = new JLabel("Password:");
	        final JTextField textPassword = new JTextField();
	        JLabel labelNotice = new JLabel("<html><h5><font color = red>Username must be long max 10 characters</font></h5></html>");
	        final JButton addButton = new JButton("Ok");
	        final JButton cancelButton = new JButton("Cancel");
	        
	        labelUsername.setBounds(10, 10, 100, 20);
	        textUsername.setBounds(90, 10, 200, 20);
	        labelPassword.setBounds(10, 60, 100, 20);
	        textPassword.setBounds(90, 60, 200, 20);
	        labelNotice.setBounds(10, 30, 300, 20);
	        addButton.setBounds(50, 90 , 100, 20);
	        cancelButton.setBounds(160, 90 , 100, 20);

	        panel.add(labelUsername);
	        panel.add(textUsername);
	        panel.add(labelPassword);
	        panel.add(textPassword);
	        panel.add(addButton);
	        panel.add(cancelButton);
	        panel.add(labelNotice);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        frame.getRootPane().setDefaultButton(addButton);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		if(textUsername.getText().equals(""))
	        			JOptionPane.showMessageDialog(frame, "Username field is empty");
	        		else if(textPassword.getText().equals(""))
        				JOptionPane.showMessageDialog(frame, "Password field is empty");
	        		else if(textUsername.getText().length() > 10)
        				JOptionPane.showMessageDialog(frame, "Username is longer than 10 characters");
	        		else
	        		{
	        			users.addUser(textUsername.getText(), textPassword.getText());
	        			userList.setListData(users.getUsers());
		        		frame.setEnabled(false);
		        		frame.setVisible(false);
		        		userList.setSelectedIndex(userList.getLastVisibleIndex());

	        		}
	        	}
	        });
	        
	        cancelButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        	}
	        });
	        
	        
		}
	}
	
	class WindowAddAuth extends JFrame {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JFrame frame;
		public WindowAddAuth (JFrame fram) {

			this.frame = fram;
			frame.setSize(330, 120);
			frame.setResizable(false);
	        JPanel panel = (JPanel) frame.getContentPane();
	        panel.setLayout(null);
	        JLabel labelResource = new JLabel("Resource: ");
	        JLabel labelAddress = new JLabel("Address: ");
	        final JTextField textResource = new JTextField();
	        final JTextField textAddress = new JTextField();
	        final JButton addButton = new JButton("Add");
	        final JButton cancelButton = new JButton("Cancel");
	        
	        labelResource.setBounds(10, 10, 100, 20);
	        textResource.setBounds(90, 10, 200, 20);
	        labelAddress.setBounds(10, 30, 100, 20);
	        textAddress.setBounds(90, 30, 200, 20);
	        addButton.setBounds(50, 60 , 100, 20);
	        cancelButton.setBounds(160, 60 , 100, 20);

	        panel.add(labelResource);
	        panel.add(textResource);
	        panel.add(labelAddress);
	        panel.add(textAddress);
	        panel.add(addButton);
	        panel.add(cancelButton);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        frame.getRootPane().setDefaultButton(addButton);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		if(textResource.getText().equals(""))
	        			JOptionPane.showMessageDialog(frame, "Resource field is empty");
	        		else if(textAddress.getText().equals(""))
        				JOptionPane.showMessageDialog(frame, "Address field is empty");
	        		else
	        		{
		        		users.addAuth(textResource.getText(), textAddress.getText());
		        		authList.setListData(users.getAuths());
		        		
		        		frame.setEnabled(false);
		        		frame.setVisible(false);
		        		System.out.println("-- Authorization '" + textResource.getText() + " ADDED");
		        		
	        		}
	        	}
	     
	        });
	        
	        cancelButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{
	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        	}
	        });

		}
	}
	
	class WindowAddAuthUser extends JFrame {
		
		private static final long serialVersionUID = 1L;
		private JFrame frame;
		public WindowAddAuthUser (JFrame fram, final String username) {

			final JComboBox auths = new JComboBox(users.getAuthsNOUser(username));
			auths.setBounds(10, 50, 150, 20);
			this.frame = fram;
			frame.setSize(300, 110);
			frame.setResizable(false);
	        JPanel panel = (JPanel) frame.getContentPane();
	        panel.setLayout(null);
	        JLabel labelAuth = new JLabel("<html>Authorization for <font color = red>" + username +"</font>:<html>");
	        final JButton addButton = new JButton("Add");
	        
	        labelAuth.setBounds(10, 20, 300, 20);
	       
	        addButton.setBounds(200, 50 , 70, 20);

	        panel.add(labelAuth);
	      
	        panel.add(addButton);
	        panel.add(auths);
	        
	        frame.setLocationRelativeTo(null);
	        frame.setVisible(true);
	        frame.setEnabled(true);
	        frame.getRootPane().setDefaultButton(addButton);
	        
	        addButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e)
	        	{

	        		frame.setEnabled(false);
	        		frame.setVisible(false);
	        		users.addAuthToUser(username, (String) auths.getSelectedItem());
	        		authUserList.setListData(users.getAuthsUsers(username));
	        		System.out.println("-- Authorization '" + auths.getSelectedItem() + "' ADDED at user " + username);
	        	}
	        });

		}
	}
	
	class Timer implements Runnable
	{
		private String place;
		public Timer(String s)
		{
			place = s;
		}
		public void run()
		{
			try
			{
				map.setOn(place);
				Thread.currentThread().sleep(500L);
				map.setOff(place);
				Thread.currentThread().sleep(500L);
				map.setOn(place);
				Thread.currentThread().sleep(500L);
				map.setOff(place);
				Thread.currentThread().sleep(500L);
				map.setOn(place);
				Thread.currentThread().sleep(500L);
				map.setOff(place);

			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}		
}