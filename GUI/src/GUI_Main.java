import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
		txtSpacing.setBounds(53, 30, 86, 20);
		txtSpacing.setText("\r\n");
		txtSpacing.setHorizontalAlignment(SwingConstants.LEFT);
		left_panel.add(txtSpacing);
		txtSpacing.setColumns(10);
		
		JButton btnSpacing = new JButton("Accept Spacing");
		btnSpacing.setBounds(151, 29, 135, 23);
		btnSpacing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				spacing = Double.parseDouble(txtSpacing.getText().trim()); //may throw exception if input is not a valid integer
				lblCurrentSpacing.setText("Current Spacing: " + spacing);
			}
		});
		left_panel.add(btnSpacing);
		
		final JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(22, 7, 27, 14);
		left_panel.add(lblTitle);
		
		txtTitle = new JTextField();
		txtTitle.setBounds(53, 4, 86, 20);
		left_panel.add(txtTitle);
		txtTitle.setColumns(10);
		
		JButton btnAcceptTitle = new JButton("Accept Title");
		btnAcceptTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				title = txtTitle.getText();
				lblCurrentTitle.setText("Current Title: " + title);
			}
		});
		btnAcceptTitle.setBounds(151, 4, 135, 23);
		left_panel.add(btnAcceptTitle);
		
		JPanel panel_spaces = new JPanel();
		panel_spaces.setBounds(26, 58, 56, 47);
		left_panel.add(panel_spaces);
		
		JPanel panel_harmonics = new JPanel();
		panel_harmonics.setBounds(92, 116, 56, 47);
		left_panel.add(panel_harmonics);
		
		JPanel panel_slide = new JPanel();
		panel_slide.setBounds(155, 116, 56, 47);
		left_panel.add(panel_slide);
		
		JPanel panel_repeatStart = new JPanel();
		panel_repeatStart.setBounds(221, 116, 56, 47);
		left_panel.add(panel_repeatStart);
		
		JPanel panel_barLine = new JPanel();
		panel_barLine.setBounds(26, 116, 56, 47);
		left_panel.add(panel_barLine);
		
		JPanel panel_notes = new JPanel();
		panel_notes.setBounds(92, 58, 56, 47);
		left_panel.add(panel_notes);
		
		JPanel panel_hammerOn = new JPanel();
		panel_hammerOn.setBounds(155, 58, 56, 47);
		left_panel.add(panel_hammerOn);
		
		JPanel panel_pullOff = new JPanel();
		panel_pullOff.setBounds(221, 58, 56, 47);
		left_panel.add(panel_pullOff);
		
		JPanel panel_repeatEnd = new JPanel();
		panel_repeatEnd.setBounds(26, 174, 56, 47);
		left_panel.add(panel_repeatEnd);
		
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
}
