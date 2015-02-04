import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;


public class GUI_Main {

	private JFrame frmTabpdf;
	private File in;
	private File out;
	private JTextField txtSpacing;
	private double spacing;
	private String title;
	private JTextField txtTitle;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_Main window = new GUI_Main();
					window.frmTabpdf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_Main() { 
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTabpdf = new JFrame();
		frmTabpdf.setTitle("TAB2PDF");
		frmTabpdf.getContentPane().setFont(new Font("Times New Roman", Font.PLAIN, 11));
		frmTabpdf.getContentPane().setLayout(null);
		
		final JLabel lblTemp = new JLabel("<file location here>");
		lblTemp.setHorizontalAlignment(SwingConstants.CENTER);
		lblTemp.setBounds(326, 11, 161, 14);
		frmTabpdf.getContentPane().add(lblTemp);
		
		final JLabel lblCurrentTitle = new JLabel("Current Title: N/A");
		lblCurrentTitle.setBounds(17, 0, 338, 14);
		frmTabpdf.getContentPane().add(lblCurrentTitle);
		
		final JLabel lblCurrentSpacing = new JLabel("Current Spacing: N/A");
		lblCurrentSpacing.setBounds(0, 11, 355, 14);
		frmTabpdf.getContentPane().add(lblCurrentSpacing);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setBounds(0, 31, 784, 509);
		frmTabpdf.getContentPane().add(splitPane);
		
		JPanel left_panel = new JPanel();
		splitPane.setLeftComponent(left_panel);
		left_panel.setLayout(null);
		
		JLabel lblSpacing = new JLabel("Spacing:");
		lblSpacing.setBounds(0, 33, 56, 14);
		left_panel.add(lblSpacing);
		
		txtSpacing = new JTextField();
		txtSpacing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spacing = Double.parseDouble(txtSpacing.getText().trim()); //may throw exception if input is not a valid integer
				lblCurrentSpacing.setText("Current Spacing: " + spacing);
			}
		});
		txtSpacing.setBounds(53, 30, 86, 20);
		txtSpacing.setText("\r\n");
		txtSpacing.setHorizontalAlignment(SwingConstants.LEFT);
		left_panel.add(txtSpacing);
		txtSpacing.setColumns(10);
		
		final JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(22, 7, 27, 14);
		left_panel.add(lblTitle);
		
		txtTitle = new JTextField();
		txtTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				title = txtTitle.getText();
				lblCurrentTitle.setText("Current Title: " + title);
			}
		});
		txtTitle.setBounds(53, 4, 86, 20);
		left_panel.add(txtTitle);
		txtTitle.setColumns(10);
		
		
		JPanel panel_spaces = new JPanel();
		panel_spaces.setBounds(26, 58, 56, 47);
		left_panel.add(panel_spaces);
		
		JLabel imgSpacing = new JLabel("");
		ImageIcon img = new ImageIcon("images/test.png");
		CreateImage(panel_spaces,img,imgSpacing); //change other code to this...
		panel_spaces.add(imgSpacing);
		
		JPanel panel_harmonics = new JPanel();
		panel_harmonics.setBounds(92, 116, 56, 47);
		left_panel.add(panel_harmonics);
		
		JLabel imgHarmonics = new JLabel("");
		imgHarmonics.setIcon(img);
		panel_harmonics.add(imgHarmonics); //don't forget to add this to other panels...
		
		JPanel panel_slide = new JPanel();
		panel_slide.setBounds(155, 116, 56, 47);
		left_panel.add(panel_slide);
		
		JLabel imgSlide = new JLabel("");
		imgSlide.setIcon(img);
		panel_slide.add(imgSlide);
		
		JPanel panel_repeatStart = new JPanel();
		panel_repeatStart.setBounds(221, 116, 56, 47);
		left_panel.add(panel_repeatStart);
		
		JLabel imgRepeatStart = new JLabel("");
		imgRepeatStart.setIcon(img);
		panel_repeatStart.add(imgRepeatStart);
		
		JPanel panel_barLine = new JPanel();
		panel_barLine.setBounds(26, 116, 56, 47);
		left_panel.add(panel_barLine);
		
		JLabel imgBarLine = new JLabel("");
		imgBarLine.setIcon(img);
		panel_barLine.add(imgBarLine);
		
		JPanel panel_notes = new JPanel();
		panel_notes.setBounds(92, 58, 56, 47);
		left_panel.add(panel_notes);
		
		JLabel imgNotes = new JLabel("");
		imgNotes.setIcon(img);
		panel_notes.add(imgNotes);
		
		JPanel panel_hammerOn = new JPanel();
		panel_hammerOn.setBounds(155, 58, 56, 47);
		left_panel.add(panel_hammerOn);
		
		JLabel imgHammerOn = new JLabel("");
		imgHammerOn.setIcon(img);
		panel_hammerOn.add(imgHammerOn);
		
		JPanel panel_pullOff = new JPanel();
		panel_pullOff.setBounds(221, 58, 56, 47);
		left_panel.add(panel_pullOff);
		
		JLabel imgPullOff = new JLabel("");
		imgPullOff.setIcon(img);
		panel_pullOff.add(imgPullOff);
		
		JPanel panel_repeatEnd = new JPanel();
		panel_repeatEnd.setBounds(26, 174, 56, 47);
		left_panel.add(panel_repeatEnd);
		
		JLabel imgRepeatEnd = new JLabel("");
		imgRepeatEnd.setIcon(img);
		panel_repeatEnd.add(imgRepeatEnd);
		
		JButton btnConvertToPdf = new JButton("Convert to PDF");
		btnConvertToPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblTemp.setText(SaveFile());
			}
		});
		btnConvertToPdf.setBounds(65, 359, 161, 47);
		left_panel.add(btnConvertToPdf);
		
		JButton btnLoadTextFile = new JButton("Load text file");
		btnLoadTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblTemp.setText(OpenFile());
			}
		});
		btnLoadTextFile.setBounds(65, 417, 161, 47);
		left_panel.add(btnLoadTextFile);
		
		JScrollPane right_panel = new JScrollPane();
		splitPane.setRightComponent(right_panel);
		
		JButton btnCreateMoreSymbols = new JButton("Create row of symbols");
		btnCreateMoreSymbols.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		right_panel.setColumnHeaderView(btnCreateMoreSymbols);
		
		JPanel panel = new JPanel();
		right_panel.setViewportView(panel);
		right_panel.setPreferredSize(new Dimension(200,200));
		panel.setLayout(null);
		
		
		
		frmTabpdf.setBounds(100, 100, 450, 300);
		frmTabpdf.setSize(800,600);
		frmTabpdf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmTabpdf.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		//this is code for the menu opened option. a lot of it is auto generated from the design tab
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblTemp.setText(OpenFile());
			}
		});
		
		mntmOpen.setHorizontalAlignment(SwingConstants.LEFT);
		mnFile.add(mntmOpen);
		
		JMenuItem mntmConvert = new JMenuItem("Convert to PDF");
		mntmConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblTemp.setText(SaveFile());
				//converting stuff to pdf.
			}
		});
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		mnFile.add(mntmConvert);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
	}
	
	public String SaveFile()
	{
		JFileChooser fc = new JFileChooser();
		FileFilter pdfFilter = new ExtensionFilter("Pdf file", ".pdf");
		fc.setFileFilter(pdfFilter);
		int status = fc.showSaveDialog(null);
		if(status == JFileChooser.APPROVE_OPTION)
		{
			//ugly maybe i can fix this....
			//out = fc.getSelectedFile().getPath().concat(((ExtensionFilter) pdfFilter).getExtension());
			in = fc.getSelectedFile();
		}
		return in.getPath();
	}
	public String OpenFile()
	{
		JFileChooser fc = new JFileChooser();
		FileFilter textFilter = new ExtensionFilter("Text file", ".txt");
		//fc.addChoosableFileFilter(textFilter);
		fc.setFileFilter(textFilter);
		int status = fc.showOpenDialog(null);
		if(status == JFileChooser.APPROVE_OPTION)
		{
			out = fc.getSelectedFile();
			//DoStuff();
		}
		return out.getPath();
	}
	private void CreateImage(JPanel panel, ImageIcon img, JLabel imgLoc)
	{
		imgLoc.setIcon(img);
		imgLoc.setBounds(panel.getBounds());
	}
	private void CreateRow()
	{
		
	}
}
