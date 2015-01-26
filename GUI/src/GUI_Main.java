import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.JSplitPane;
import javax.swing.JPanel;


public class GUI_Main {

	private JFrame frmTabpdf;
	private File in;
	private File out;

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
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		splitPane.setBounds(0, 31, 784, 509);
		frmTabpdf.getContentPane().add(splitPane);
		
		JLabel lblTiles = new JLabel("Tiles");
		lblTiles.setHorizontalAlignment(SwingConstants.CENTER);
		lblTiles.setVerticalAlignment(SwingConstants.TOP);
		splitPane.setLeftComponent(lblTiles);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		
		JLabel lblArrayOfPanels = new JLabel("Array of panels here.");
		panel.add(lblArrayOfPanels);
		
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
				JFileChooser fc = new JFileChooser();
				FileFilter textFilter = new ExtensionFilter("Text file", ".txt");
				//fc.addChoosableFileFilter(textFilter);
				fc.setFileFilter(textFilter);
				int status = fc.showOpenDialog(null);
				if(status == JFileChooser.APPROVE_OPTION)
				{
					out = fc.getSelectedFile();
					lblTemp.setText(out.getPath());
					//DoStuff();
				}
			}
		});
		
		mntmOpen.setHorizontalAlignment(SwingConstants.LEFT);
		mnFile.add(mntmOpen);
		
		JMenuItem mntmConvert = new JMenuItem("Convert to PDF");
		mntmConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				FileFilter pdfFilter = new ExtensionFilter("Pdf file", ".pdf");
				fc.setFileFilter(pdfFilter);
				int status = fc.showSaveDialog(null);
				if(status == JFileChooser.APPROVE_OPTION)
				{
					//ugly maybe i can fix this....
					//out = fc.getSelectedFile().getPath().concat(((ExtensionFilter) pdfFilter).getExtension());
					in = fc.getSelectedFile();
					lblTemp.setText(in.getPath());
				}
				//converting stuff to pdf.
			}
		});
		mnFile.add(mntmConvert);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
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
		}
		return out.getPath();
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
}
