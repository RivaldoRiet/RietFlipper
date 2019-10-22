package RietFlipper;
import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.awt.List;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.dreambot.api.methods.MethodProvider;

import javax.swing.JCheckBox;

public class kankergui {
	private boolean isRunning = false;
	private JFrame frmRietflipperGui;
	private JTextField textField;
	private JTextField textField_1;
	List list = new List();
	List list_1 = new List();
	private JTextField textField_2;
	JCheckBox chckbxNewCheckBox = new JCheckBox("Member items?");
	ArrayList<String> saveList = new ArrayList<String>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					kankergui window = new kankergui();
					window.getFrmRietflipperGui().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public kankergui() {
		list.setMultipleMode(true);
		list_1.setMultipleMode(true);
		
		initialize();
		
	}

	public static boolean isNumeric(String str) {
	    if (str == null) {
	        return false;
	    }
	    int sz = str.length();
	    for (int i = 0; i < sz; i++) {
	        if (Character.isDigit(str.charAt(i)) == false) {
	            return false;
	        }
	    }
	    return true;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		setFrmRietflipperGui(new JFrame());
		getFrmRietflipperGui().setTitle("Rietflipper GUI");
		getFrmRietflipperGui().setBounds(100, 100, 721, 504);
		getFrmRietflipperGui().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrmRietflipperGui().getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 705, 465);
		getFrmRietflipperGui().getContentPane().add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Items", null, panel_1, null);
		panel_1.setLayout(null);
		list.setBounds(10, 32, 177, 166);
		panel_1.add(list);
		
		
		list_1.setBounds(370, 32, 187, 156);
		panel_1.add(list_1);
		
		JButton button = new JButton(">");
		button.setBounds(232, 42, 89, 23);
		panel_1.add(button);
		button.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 19));
		button.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	for (int i = 0; i < list.getSelectedItems().length; i++) {
					list_1.add(list.getSelectedItems()[i]);
				}
	        	
	        	// java awt list is buggy as shit so this to make sure everything gets added correctly
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list.getSelectedIndexes().length; i++) {
					list.remove(list.getSelectedIndexes()[i]);
				}
	        	/*
	        	for (int i = 0; i < list.getSelectedItems().length; i++) {
	        		System.out.println(list.getSelectedItems()[i]);
					list.remove(list.getSelectedItems()[i]);
				}
	        	
	        	for (int i = 0; i < list_1.getSelectedItems().length; i++) {
	        		for (int j = 0; j < list.getSelectedItems().length; j++) {
						if(list_1.getSelectedItems()[i].equals(list.getSelectedItems()[j])) {
							list.remove(list.getSelectedItems()[j]);
						}
					}
				}
	        	if(list.getSelectedItem() != null) {
	        		list_1.add(list.getSelectedItem());
	        		list.remove(list.getSelectedItem());
	        	}*/
	        }

	    });
		
		
		
		JButton button_1 = new JButton("<");
		button_1.setBounds(232, 77, 89, 23);
		panel_1.add(button_1);
		button_1.setFont(new Font("Arial", Font.BOLD, 19));
		button_1.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	for (int i = 0; i < list_1.getSelectedItems().length; i++) {
					list.add(list_1.getSelectedItems()[i]);
				}
	        	
	        	// java awt list is buggy as shit so this to make sure everything gets added correctly
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	        	for (int i = 0; i < list_1.getSelectedIndexes().length; i++) {
					list_1.remove(list_1.getSelectedIndexes()[i]);
				}
	      
	        	/*
	        	for (int i = 0; i < list.getSelectedItems().length; i++) {
	        		System.out.println(list.getSelectedItems()[i]);
					list.remove(list.getSelectedItems()[i]);
				}
	        	
	        	for (int i = 0; i < list_1.getSelectedItems().length; i++) {
	        		for (int j = 0; j < list.getSelectedItems().length; j++) {
						if(list_1.getSelectedItems()[i].equals(list.getSelectedItems()[j])) {
							list.remove(list.getSelectedItems()[j]);
						}
					}
				}
	        	if(list.getSelectedItem() != null) {
	        		list_1.add(list.getSelectedItem());
	        		list.remove(list.getSelectedItem());
	        	}*/
	        }

	    });
		
		
		
		
		JLabel label = new JLabel("Minimum item price:");
		label.setBounds(10, 215, 130, 14);
		panel_1.add(label);
		
		textField = new JTextField();
		textField.setBounds(138, 212, 86, 20);
		panel_1.add(textField);
		textField.setText("0");
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(138, 254, 86, 20);
		panel_1.add(textField_1);
		textField_1.setText("0");
		textField_1.setColumns(10);
		
		JLabel lblMinimumPrice = new JLabel("Maximum item price:");
		lblMinimumPrice.setBounds(10, 257, 133, 14);
		panel_1.add(lblMinimumPrice);
		
		JButton btnScrape = new JButton("Scrape!");
		btnScrape.setBounds(10, 320, 218, 70);
		panel_1.add(btnScrape);
		btnScrape.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent e)
	        {
	        	scrape();
	        }

	    });
		
		JLabel lblScrapedItems = new JLabel("Scraped items");
		lblScrapedItems.setBounds(31, 11, 100, 14);
		panel_1.add(lblScrapedItems);
		
		
		chckbxNewCheckBox.setBounds(10, 290, 155, 23);
		panel_1.add(chckbxNewCheckBox);
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.setBounds(558, 211, 89, 23);
		panel_1.add(btnNewButton);
		
		textField_2 = new JTextField();
		textField_2.setBounds(433, 212, 109, 20);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblAddItemBy = new JLabel("Add item by name:");
		lblAddItemBy.setBounds(316, 215, 128, 14);
		panel_1.add(lblAddItemBy);
		
		JLabel lblForExampleRune = new JLabel("For example: Rune kiteshield");
		lblForExampleRune.setBounds(355, 236, 187, 14);
		panel_1.add(lblForExampleRune);
		
		JLabel lblFlippingItems = new JLabel("Flipping items");
		lblFlippingItems.setBounds(409, 11, 100, 14);
		panel_1.add(lblFlippingItems);
		
		JButton btnExportItemList = new JButton("Export item list");
		btnExportItemList.setBounds(370, 290, 139, 23);
		panel_1.add(btnExportItemList);
		
		btnExportItemList.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	saveList.clear();
		        for (int i = 0; i < getList_1().getItems().length; i++) {
		        	saveList.add(getList_1().getItems()[i]);
				}
		        try {
					save();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	        }

	    });
		
		
		
		
		JButton btnImportItemList = new JButton("Import item list");
		btnImportItemList.setBounds(534, 290, 133, 23);
		panel_1.add(btnImportItemList);
		btnImportItemList.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	try {
					read();
				} catch (FileNotFoundException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
	        }

	    });
		
		
		JButton btnQuickExport = new JButton("Quick export");
		btnQuickExport.setBounds(370, 340, 139, 23);
		panel_1.add(btnQuickExport);
		btnQuickExport.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	saveList.clear();
		        for (int i = 0; i < getList_1().getItems().length; i++) {
		        	saveList.add(getList_1().getItems()[i]);
				}
		        try {
					saveQuick("Rietflipper_items.riet");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	        }

	    });
		
		
		JButton btnNewButton_1 = new JButton("Quick import");
		btnNewButton_1.setBounds(534, 340, 133, 23);
		panel_1.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	try {
					readQuick("Rietflipper_items.riet");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
	        }

	    });
		
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(578, 32, 89, 23);
		panel_1.add(btnClear);
		btnClear.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	list_1.removeAll();
	        }

	    });
		JButton btnStartScript = new JButton("Start script");
		btnStartScript.setBounds(287, 403, 130, 23);
		panel_1.add(btnStartScript);
		btnStartScript.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent e)
	        {
	        	for (int i = 0; i < getList_1().getItems().length; i++) {
	        		//add to array
	        	}
	        	isRunning = true;
	        }

	    });
		
		
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Settings", null, panel, null);
		panel.setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.setText("10");
		textPane.setBounds(183, 35, 35, 20);
		panel.add(textPane);
		
		JCheckBox chckbxSellItemsIf = new JCheckBox("sell items if not sold after");
		chckbxSellItemsIf.setBounds(6, 35, 171, 23);
		panel.add(chckbxSellItemsIf);
		
		JLabel lblMinutes = new JLabel("minutes");
		lblMinutes.setBounds(228, 41, 46, 14);
		panel.add(lblMinutes);
	}
	
	public void scrape() {
		if(isNumeric(textField.getText()) && isNumeric(textField_1.getText())) {
			list.removeAll();
			int minimumPrice = Integer.parseInt(textField.getText());
			int maximumPrice = Integer.parseInt(textField_1.getText());
			boolean ismember = chckbxNewCheckBox.isSelected();
			
			OSBuddy.init();
	    	java.util.List<OSBuddyItem> item = OSBuddy.getItemAll(p -> p.getOverallAverage() > minimumPrice &&  p.getOverallAverage() < maximumPrice && p.isMember() == ismember);
	    	MethodProvider.log("Length: " + item.size());
	    	
	    	
	    	for (int i = 0; i < item.size(); i++) {
				OSBuddyItem item1 = (OSBuddyItem) item.get(i);
				MethodProvider.log("" + item1.getId() + " - " + item1.getName());
				list.add(item1.getName());
			}
			
			/*
			ArrayList <String> data = this.scraper.getData(minimumPrice, maximumPrice, ismember);
			
			for (String dataline : data) {
				list.add(dataline);
			}
			*/

		}else {
			JOptionPane.showMessageDialog(null, "We could not parse the numbers you have provided!", "Rietflipper info", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public List getList() {
		return this.list;
	}
	
	public List getList_1() {
		return this.list_1;
	}

	
	
	public void saveQuick(String fileName) throws FileNotFoundException {
		try {
			FileOutputStream fout= new FileOutputStream (fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(saveList);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void readQuick(String fileName) throws FileNotFoundException {
		try {
			FileInputStream fin= new FileInputStream (fileName);
			ObjectInputStream ois = new ObjectInputStream(fin);
			ArrayList<String> importedArray = (ArrayList<String>)ois.readObject();
			for (int i = 0; i < importedArray.size(); i++) {
				list_1.add(importedArray.get(i));
			}
			fin.close();
	        
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	
	
	
	public void save() throws FileNotFoundException {
		FileOutputStream f = null;
        DataOutputStream h = null;
        FileDialog d = new FileDialog(new JFrame(), "Save", FileDialog.SAVE);
        d.setVisible(true);
        String dir;
        dir = d.getDirectory();
        File oneFile = new File(dir + d.getFile() + ".riet");
        try {
            oneFile.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            f = new FileOutputStream(oneFile);
            ObjectOutputStream oos = new ObjectOutputStream(f);
            oos.writeObject(saveList);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            try {
                
                f.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
		/*
		try {
			FileOutputStream fout= new FileOutputStream (fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(saveList);
			fout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	public void read() throws FileNotFoundException, ClassNotFoundException {
		try {
			
			FileDialog fd = new FileDialog(getFrmRietflipperGui(), "Choose a file", FileDialog.LOAD);
			fd.setVisible(true);
			String filename = fd.getFile();
			FileInputStream fin= new FileInputStream (fd.getDirectory() + fd.getFile());
			ObjectInputStream ois = new ObjectInputStream(fin);
			ArrayList<String> importedArray = (ArrayList<String>)ois.readObject();
			for (int i = 0; i < importedArray.size(); i++) {
				list_1.add(importedArray.get(i));
			}
			fin.close();
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JFrame getFrmRietflipperGui() {
		return frmRietflipperGui;
	}

	public void setFrmRietflipperGui(JFrame frmRietflipperGui) {
		this.frmRietflipperGui = frmRietflipperGui;
	}
}
