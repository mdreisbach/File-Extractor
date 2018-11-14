package v1;

import java.awt.*; // Using AWT container and component classes
import java.awt.event.*; // Using AWT event classes and listener interfaces
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

public class FilePathGui extends JFrame implements ActionListener {

	private Label lblInput; // Declare input Label
	private Label lblOutput; // Declare output Label
	private TextField tfInput; // Declare input TextField
	private TextField tfOutput; // Declare output TextField
	private TextField fileType;
	private JComboBox<String> formatList;
	private Boolean isSpecifiedType = false;
	private String fileTypeText = new String();
	String[] formatStrings = { "Forward Slash (R/Java)", "Double Back Slash (Java)", "Back Slash (Windows/VBA)" };
	public JRadioButton r1=new JRadioButton("All File Types");    
	public JRadioButton r2=new JRadioButton("Specific File Type (.pdf, .txt, .xls)");
	public String localUserName = System.getProperty("user.name");
	String preferenceTextDirectory = "C:\\user_preferences";
	String defaultDestinationDirectory = "C:\\"+localUserName+"\\";
	public String sourcePath = new String();
	public String destPath = new String();

	// Constructor to setup the GUI components and event handlers
	public FilePathGui() {

		setLayout(new FlowLayout());
// 
		lblInput = new Label("Source Directory: "); // Construct Label
		add(lblInput); // "super" Frame container adds Label component

		tfInput = new TextField(100); // Construct TextField
		add(tfInput); // "super" Frame adds TextField

		tfInput.addActionListener(this);

		lblOutput = new Label("Destination Directory: "); // allocate Label
		add(lblOutput); // "super" Frame adds Label

		tfOutput = new TextField(100); // allocate TextField
		add(tfOutput); // "super" Frame adds TextField

		setDefaultCloseOperation(this.EXIT_ON_CLOSE);


		r1.setBounds(75,50,100,30);    
		r2.setBounds(75,100,100,30);    
		ButtonGroup bg=new ButtonGroup();    
		bg.add(r1);bg.add(r2);    
		add(r1);
		add(r2);      
		fileType = new TextField(20); // allocate TextField
		add(fileType);


		setTitle("File Extractor"); // "super" Frame sets title
		setSize(1800, 130); // "super" Frame sets initial window size
		setVisible(true); // "super" Frame shows

		if(hasPreferences()){
			setGuiPreferences();
		} else {
			r1.setSelected(true);
			tfOutput.setText(defaultDestinationDirectory);
		}

		JButton extractBtn = new JButton("Extract");
		add(extractBtn);
		extractBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JButton b = new JButton();
				b.setBackground(Color.red);
				b.setBorder(new LineBorder(Color.black, 2));
				b.setPreferredSize(new Dimension(600, 10));

				if(r1.isSelected()) {
					isSpecifiedType = false;
				} else {
					isSpecifiedType = true;
				}

				fileTypeText = fileType.getText();
				sourcePath = tfInput.getText();
				destPath = tfOutput.getText();

				if(r2.isSelected() & fileTypeText.equals("")){ // if specific file type is selected but text field is empty
					JOptionPane.showMessageDialog(null, "Enter the file type in text field", "Hey there Buster!", JOptionPane.WARNING_MESSAGE);
				}
				else if(!r1.isSelected() & !r2.isSelected()){ // if no radio button is selected
					JOptionPane.showMessageDialog(null, "Select a radio button", "Hey there Buster!", JOptionPane.WARNING_MESSAGE);
				}
				else if(tfInput.getText().equals("") | tfOutput.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Enter a Source and Destination directory path", "Hey there Buster!", JOptionPane.WARNING_MESSAGE);
				}
				else {
					createTextReference(tfInput.getText(), tfOutput.getText(), getRadioButttonInteger(), fileTypeText);
					try {
						Extractor extractor = new Extractor();
						extractor.setGUI(FilePathGui.this);
						extractor.beginExtraction();
					} catch (Exception exc) {
						JOptionPane.showMessageDialog(null, "Invalid file paths or entries", "Hey there Buster!", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
	}

	public Integer getRadioButttonInteger() {
		if(r2.isSelected()){
			return 2;
		} else {
			return 1;
		}		
	}

	public void setGuiPreferences() {
		File preferences = new File(preferenceTextDirectory, localUserName+".txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(preferences));
			String text = br.readLine();
			String[] strArray = text.split("\\?");
			tfInput.setText(strArray[0]);
			tfOutput.setText(strArray[1]);
			fileType.setText(strArray[3]);
			if(strArray[2].equals("2")){
				r2.setSelected(true);
			} else {
				r1.setSelected(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {

	}

	public void createTextReference(String sourceDirectory, String destinationDirectory, Integer buttonIndex, String fileType) {
		File dir = new File (preferenceTextDirectory);
		File userPreferences = new File (dir, localUserName+".txt");
		PrintWriter writer;
		try {
			writer = new PrintWriter(userPreferences, "UTF-8");
			writer.println(sourceDirectory+"?"+destinationDirectory+"?"+buttonIndex+"?"+fileType);
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Boolean hasPreferences(){
		Boolean hasPreferences =  new File(preferenceTextDirectory, localUserName+".txt").exists();
		return hasPreferences;
	}

	public Boolean getIsSpecifiedType(){
		return isSpecifiedType;
	}

	public String getFileTypeText() {
		return fileTypeText;
	}

	public JRadioButton getR1() {
		return r1;
	}

	public JRadioButton getR2() {
		return r2;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	public void setFileTypeText(String fileTypeText) {
		this.fileTypeText = fileTypeText;
	}


}
