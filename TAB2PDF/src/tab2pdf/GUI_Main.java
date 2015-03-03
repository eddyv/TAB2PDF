package tab2pdf;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.PropertiesManager;

import com.itextpdf.text.DocumentException;
import javax.swing.JSlider;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GUI_Main {

	private JFrame frmTabpdf;
	private String src;
	private String dest;
	private double spacing = 50;

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
		frmTabpdf.getContentPane().setFont(
				new Font("Times New Roman", Font.PLAIN, 11));
				frmTabpdf.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));
				
				JPanel panel_PDF_Component = new JPanel();
				panel_PDF_Component.setBorder(new TitledBorder(null, "pdf components", TitledBorder.LEADING, TitledBorder.TOP, null, null));
				frmTabpdf.getContentPane().add(panel_PDF_Component);
						panel_PDF_Component.setLayout(new GridLayout(2, 1, 1, 1));
				
						JButton btnConvertToPdf = new JButton("Convert and Display PDF");
						panel_PDF_Component.add(btnConvertToPdf);
						
						JButton btnSavePDF = new JButton("Save PDF file");
						panel_PDF_Component.add(btnSavePDF);
						btnConvertToPdf.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								openPdf();
							}

						});
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "text components", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmTabpdf.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{380, 0};
		gbl_panel.rowHeights = new int[]{123, 0, 123, 0};
		gbl_panel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		JButton btnLoadTextFile = new JButton("Load text file");
		GridBagConstraints gbc_btnLoadTextFile = new GridBagConstraints();
		gbc_btnLoadTextFile.weighty = 2.0;
		gbc_btnLoadTextFile.weightx = 2.0;
		gbc_btnLoadTextFile.fill = GridBagConstraints.BOTH;
		gbc_btnLoadTextFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadTextFile.gridx = 0;
		gbc_btnLoadTextFile.gridy = 0;
		panel.add(btnLoadTextFile, gbc_btnLoadTextFile);
		btnLoadTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				src = openFile();
			}
		});
		
		JLabel lblSpacing = new JLabel("Spacing=50");
		GridBagConstraints gbc_lblSpacing = new GridBagConstraints();
		gbc_lblSpacing.insets = new Insets(0, 0, 5, 0);
		gbc_lblSpacing.gridx = 0;
		gbc_lblSpacing.gridy = 1;
		panel.add(lblSpacing, gbc_lblSpacing);
		
		JSlider sldrSpacing = new JSlider();
		sldrSpacing.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				
			}
		});
		GridBagConstraints gbc_sldrSpacing = new GridBagConstraints();
		gbc_sldrSpacing.weightx = 1.0;
		gbc_sldrSpacing.weighty = 1.0;
		gbc_sldrSpacing.fill = GridBagConstraints.BOTH;
		gbc_sldrSpacing.gridx = 0;
		gbc_sldrSpacing.gridy = 2;
		panel.add(sldrSpacing, gbc_sldrSpacing);
		sldrSpacing.setMinorTickSpacing(1);
		sldrSpacing.setMajorTickSpacing(10);
		sldrSpacing.setPaintLabels(true);
		sldrSpacing.setPaintTicks(true);

		frmTabpdf.setBounds(100, 100, 450, 300);
		frmTabpdf.setSize(800, 600);
		frmTabpdf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmTabpdf.setJMenuBar(menuBar);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		
		JMenuItem mntmUserManual = new JMenuItem("User Manual");
		mnHelp.add(mntmUserManual);
	}

	// the below methods should be private but are made public for testing
	// purposes.
	public void openPdf() {
		// check if extension is .pdf
		dest = saveFile();
		try {
			drawOutput output = new drawOutput(src, dest);
			output.createPdf();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// build a component controller
		SwingController controller = new SwingController();
		controller.setIsEmbeddedComponent(true);

		PropertiesManager properties = new PropertiesManager(
				System.getProperties(),
				ResourceBundle
						.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));

		properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1.75");

		SwingViewBuilder factory = new SwingViewBuilder(controller, properties);

		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(controller
						.getDocumentViewController()));
		JPanel viewerComponentPanel = factory.buildViewerPanel();
		JFrame applicationFrame = new JFrame();
		//changed from exit on close to dispose on close
		applicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		applicationFrame.getContentPane().add(viewerComponentPanel);
		// Now that the GUI is all in place, we can try opening a PDF
		controller.openDocument(dest);
		// show the component
		applicationFrame.pack();
		applicationFrame.setVisible(true);
	}

	public String saveFile() {
		JFileChooser fc = new JFileChooser();
		FileFilter pdfFilter = new ExtensionFilter("Pdf file", ".pdf");
		fc.setFileFilter(pdfFilter);
		fc.setAcceptAllFileFilterUsed(false);
		int status = fc.showSaveDialog(null);
		File in = null;
		if (status == JFileChooser.APPROVE_OPTION) {
			in = fc.getSelectedFile();
		}
		return in.getPath();
	}

	public String openFile() {
		JFileChooser fc = new JFileChooser();
		FileFilter textFilter = new ExtensionFilter("Text file", ".txt");
		// fc.addChoosableFileFilter(textFilter);
		fc.setFileFilter(textFilter);
		fc.setAcceptAllFileFilterUsed(false);
		int status = fc.showOpenDialog(null);
		File out = null;
		if (status == JFileChooser.APPROVE_OPTION) {
			out = fc.getSelectedFile();
			// DoStuff();
		}
		return out.getPath();
	}
}
