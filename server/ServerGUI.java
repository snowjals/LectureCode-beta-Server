package beta.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import beta.client.FileList;

public class ServerGUI {

	//Runnable listener;

	static String path;

	private ArrayList<Thread> threads = new ArrayList<Thread>();
	protected ArrayList<Socket> sockets = new ArrayList<Socket>();
	JFrame frame;

	JFileChooser chooser;
	JButton browseBnt;
	JTextField fileText;
	JButton startButton;
	JButton stopButton;

	//Listener listener;

	JRadioButton java;
	JRadioButton otherRadio;
	JTextField other;
	ButtonGroup group;

	JButton connectedUsers;
	private String usersOnline = "0";


	public void rigFrame() {

		JFrame frame = new JFrame("LectureCode Server");

		this.frame = frame;

		this.sockets = new ArrayList<Socket>();

		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JLabel label = new JLabel("File Path:");
		fileText = new JTextField(40);
		browseBnt = new JButton("Browse...");
		browseBnt.addActionListener(new Browse());


		startButton = new JButton("Start");
		startButton.setVisible(false);
		startButton.addActionListener(new Start());
		startButton.setPreferredSize(new Dimension(75,20));

		stopButton = new JButton("Stop server");
		stopButton.setVisible(false);
		stopButton.addActionListener(new Stop());

		JPanel selections = new JPanel();
		this.group = new ButtonGroup();
		java = new JRadioButton(".java",true);
		otherRadio = new JRadioButton();
		JLabel otherText = new JLabel("Other");
		this.other = new JTextField(7);
		other.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				otherRadio.setSelected(true);
			}
		});
		JLabel text = new JLabel("File format: ");

		JLabel connectedText = new JLabel("Users");
		this.connectedUsers = new JButton("" + sockets.size());
		connectedUsers.setPreferredSize(new Dimension(40,20));
		connectedUsers.addActionListener(new DisplayUsers());


		group.add(java);
		group.add(otherRadio);
		selections.add(text,"wrap");
		selections.add(java);
		selections.add(otherRadio);
		selections.add(otherText);
		selections.add(other);


		jp.add(label,BorderLayout.NORTH);
		jp.add(fileText,BorderLayout.NORTH);
		jp.add(browseBnt,BorderLayout.NORTH);
		jp.add(connectedText,"wrap");
		jp.add(connectedUsers);
		jp.add(selections);
		jp.add(startButton,BorderLayout.SOUTH);
		jp.add(stopButton,BorderLayout.SOUTH);
		frame.add(jp);



		frame.setVisible(true);
		frame.setSize(600,100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



		loop();
	}

	public String deceideFormat() {
		String selected = "";
		for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				selected =  button.getText();
			}
		}
		if (selected.equals("")) {
			selected = other.getText();
			if(selected.equals("")) {
				throw new IllegalArgumentException("You must enter a filetype");
			}
		}
		return selected;
	}

	public void loop() {
		try {
			Thread.sleep(1000*3);
			//System.out.println("loop");
		} catch (Exception e) {

		}
		updateButton();
		loop();
	}

	public void updateButton() {
		connectedUsers.setText("" + sockets.size());
	}

	public void addConnection() {
		try {
			int current = Integer.parseInt(usersOnline);
			current++;
			usersOnline = "" + current;
		} catch (Exception e) {

		}
	}

	class Browse implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("File selecter");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				//System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
				//System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
			} else {
				System.out.println("No Selection ");
			}
			ServerGUI.path = chooser.getSelectedFile().toString();
			fileText.setText(chooser.getSelectedFile().toString());
			startButton.setVisible(true);

		}
	}



	class Start implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setState(Frame.ICONIFIED);
			stopButton.setVisible(true);


			startButton.setVisible(false);
			browseBnt.setVisible(false);
			chooser.setBackground(Color.GREEN);
			frame.setBackground(Color.GREEN);
			//stopButton.setVisible(true); 

			//System.out.println(ServerGUI.path);
			if (ServerGUI.path != null) {
				String format = deceideFormat();
				System.out.println(format);
				Runnable listener = new Listener(ServerGUI.path,format,sockets);
				Thread thread = new Thread(listener);
				threads.add(thread);
				thread.start();
				connectedUsers.setBackground(Color.GREEN);
			}


		}

	}

	public void stop() {
		System.exit(0);
		frame.dispose();
		for (Thread t : threads) {
			t.interrupt();
			System.out.println("interrupting");

			//Listener.interrupted = true;

		}
	}

	class Stop implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			stop();
		}
	}

	class DisplayUsers implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			UIManager.put("Tree.rendererFillBackground", false);

			JFrame window = new JFrame("Connected");


			DefaultMutableTreeNode root = new DefaultMutableTreeNode("Connected");
			ArrayList<Socket> socs = getSockets();
			for (Socket soc : socs) {
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(soc.toString());
				root.add(node);
			}
			//DefaultTreeModel treeModel = new DefaultTreeModel(root);
			//tree.setModel(treeModel);
			JTree tree = new JTree(root);
			JPanel pn = new JPanel();
			pn.setPreferredSize(new Dimension(300,300));
			pn.add(tree);
			window.add(pn);

			tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
				@Override
				public void valueChanged(TreeSelectionEvent e) {
					//DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
					//System.out.println(selectedNode.getUserObject().toString());
					//removeSocket(selectedNode.getUserObject());
					int s = tree.getMinSelectionRow();
					if (s > 0) {
						removeSocket(s - 1);

						DefaultMutableTreeNode root = new DefaultMutableTreeNode("Connected");
						ArrayList<Socket> socs = getSockets();
						for (Socket soc : socs) {
							DefaultMutableTreeNode node = new DefaultMutableTreeNode(soc.toString());
							if (soc.isClosed()) {
								sockets.remove(soc);
							} else {
								root.add(node);
								window.remove(tree);
								JTree newTree = new JTree(root);
								window.add(newTree);
								window.repaint();
							}
						}

					}
				}
			}); 


			window.setVisible(true);
			window.setSize(400,400);
			window.setDefaultCloseOperation(JFrame.ICONIFIED);

		}

	}

	public ArrayList<Socket> getSockets() {
		return this.sockets;
	}

	public void removeSocket(Object socket) {
		if (socket instanceof Socket && this.sockets.contains(socket)) {
			this.sockets.remove(socket);
		} else if (socket instanceof Integer) {
			Socket s = this.sockets.get((int)socket);
			try {
				s.close();
			} catch (IOException e) {
			}
			this.sockets.remove((int)socket);
		}

	}


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
			//UIManager.put("TextArea.font", "Arial");
			ServerGUI gui = new ServerGUI();
			gui.rigFrame();

			//	System.out.println("Her?" + gui.path);

		} catch (Exception e) {
			System.err.println("Unable to build");
		}
	}
}
