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

public class GUI_Main {

	private JFrame frmTabpdf;
	private String src;
	private String dest;

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
		frmTabpdf.getContentPane().setLayout(null);

		JButton btnConvertToPdf = new JButton("Convert and Display PDF");
		btnConvertToPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openPdf();
			}

		});
		btnConvertToPdf.setBounds(414, 135, 228, 163);
		frmTabpdf.getContentPane().add(btnConvertToPdf);
		JButton btnLoadTextFile = new JButton("Load text file");
		btnLoadTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				src = openFile();
			}
		});
		btnLoadTextFile.setBounds(114, 135, 228, 163);
		frmTabpdf.getContentPane().add(btnLoadTextFile);

		frmTabpdf.setBounds(100, 100, 450, 300);
		frmTabpdf.setSize(800, 600);
		frmTabpdf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmTabpdf.setJMenuBar(menuBar);
	}

	// the below methods should be private but are made public for testing
	// purposes.
	public void openPdf() {
		// check if extension is .pdf
		dest = saveFile();
		try {
			new drawOutput(src, dest);
		} catch (IOException | DocumentException e) {
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
		applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
