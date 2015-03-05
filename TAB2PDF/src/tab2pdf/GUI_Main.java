package tab2pdf;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.PropertiesManager;

import com.itextpdf.text.DocumentException;
import javax.swing.SwingConstants;

public class GUI_Main {

	private JFrame frmTabpdf;
	private String src;
	private String dest;
	private String localDest="result.pdf";
	private drawOutput output;
	private float spacing = 25;
	private boolean useCustomTitle;
	private boolean useCustomSubtitle;
	private boolean useCustomSpacing;
	private String title="";
	private String subtitle="";

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
		panel_PDF_Component.setBorder(new TitledBorder(null, "pdf components",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmTabpdf.getContentPane().add(panel_PDF_Component);
		panel_PDF_Component.setLayout(new GridLayout(3, 1, 1, 1));

		JButton btnConvertToPdf = new JButton("Convert PDF");
		btnConvertToPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//create a local version of the pdf file.
				try {
					output = new drawOutput(src, localDest,useCustomTitle,useCustomSubtitle,useCustomSpacing,title,subtitle,spacing);
					output.createPdf();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		panel_PDF_Component.add(btnConvertToPdf);
		
		JButton btnDisplayPdf = new JButton("Display PDF");
		btnDisplayPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openPdf();
			}
		});
		panel_PDF_Component.add(btnDisplayPdf);

		JButton btnSavePDF = new JButton("Save PDF");
		btnSavePDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dest=saveFile();
				try {
					output = new drawOutput(src, dest,useCustomTitle,useCustomSubtitle,useCustomSpacing,title,subtitle,spacing);
					output.createPdf();

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
				
		});
		panel_PDF_Component.add(btnSavePDF);
		

		JPanel panel_Text_Component = createTextPanel();

		JLabel lblCurrentTitle = new JLabel("Current Title: ");
		GridBagConstraints gbc_lblCurrentTitle = new GridBagConstraints();
		gbc_lblCurrentTitle.insets = new Insets(5, 0, 5, 0);
		gbc_lblCurrentTitle.gridx = 0;
		gbc_lblCurrentTitle.gridy = 1;
		panel_Text_Component.add(lblCurrentTitle, gbc_lblCurrentTitle);

		JPanel panel_title = new JPanel();
		GridBagConstraints gbc_panel_title = new GridBagConstraints();
		gbc_panel_title.insets = new Insets(0, 0, 5, 0);
		gbc_panel_title.fill = GridBagConstraints.BOTH;
		gbc_panel_title.gridx = 0;
		gbc_panel_title.gridy = 2;
		panel_Text_Component.add(panel_title, gbc_panel_title);
		panel_title.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_title.add(lblTitle);

		JTextField txtTitle = new JTextField();
		txtTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				title = txtTitle.getText();
				lblCurrentTitle.setText("Title: " + title);
			}
		});
		txtTitle.setHorizontalAlignment(SwingConstants.CENTER);
		txtTitle.setText("<insert title here>");
		panel_title.add(txtTitle);
		txtTitle.setColumns(10);

		JLabel lblCurrentSubtitle = new JLabel("Current Subtitle:");
		GridBagConstraints gbc_lblCurrentSubtitle = new GridBagConstraints();
		gbc_lblCurrentSubtitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblCurrentSubtitle.gridx = 0;
		gbc_lblCurrentSubtitle.gridy = 3;
		panel_Text_Component.add(lblCurrentSubtitle, gbc_lblCurrentSubtitle);

		JPanel panel_subtitle = new JPanel();
		GridBagConstraints gbc_panel_subtitle = new GridBagConstraints();
		gbc_panel_subtitle.insets = new Insets(0, 0, 5, 0);
		gbc_panel_subtitle.fill = GridBagConstraints.BOTH;
		gbc_panel_subtitle.gridx = 0;
		gbc_panel_subtitle.gridy = 4;
		panel_Text_Component.add(panel_subtitle, gbc_panel_subtitle);
		panel_subtitle.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblSubtitle = new JLabel("Subtitle:");
		lblSubtitle.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_subtitle.add(lblSubtitle);

		JTextField txtSubtitle = new JTextField();
		txtSubtitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subtitle = txtSubtitle.getText();
				lblCurrentSubtitle.setText("Subtitle: " + subtitle);
			}
		});
		txtSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		txtSubtitle.setText("<insert subtitle here>");
		panel_subtitle.add(txtSubtitle);
		txtSubtitle.setColumns(10);

		JCheckBox bxCustomTitle = new JCheckBox("Use custom title");
		bxCustomTitle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (bxCustomTitle.isSelected() == true) {
					useCustomTitle = true;
				} else {
					useCustomTitle = false;
				}
			}
		});
		GridBagConstraints gbc_bxCustomTitle = new GridBagConstraints();
		gbc_bxCustomTitle.insets = new Insets(0, 0, 5, 0);
		gbc_bxCustomTitle.gridx = 0;
		gbc_bxCustomTitle.gridy = 7;
		panel_Text_Component.add(bxCustomTitle, gbc_bxCustomTitle);

		JCheckBox bxCustomSubtitle = new JCheckBox("Use custom subtitle");
		bxCustomSubtitle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (bxCustomSubtitle.isSelected() == true) {
					useCustomSubtitle = true;
				} else {
					useCustomSubtitle = false;
				}
			}
		});
		GridBagConstraints gbc_bxCustomSubtitle = new GridBagConstraints();
		gbc_bxCustomSubtitle.insets = new Insets(0, 0, 5, 0);
		gbc_bxCustomSubtitle.gridx = 0;
		gbc_bxCustomSubtitle.gridy = 8;
		panel_Text_Component.add(bxCustomSubtitle, gbc_bxCustomSubtitle);

		JCheckBox bxCustomSpacing = new JCheckBox("Use custom spacing");
		bxCustomSpacing.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (bxCustomSpacing.isSelected() == true) {
					useCustomSpacing = true;
				} else {
					useCustomSpacing = false;
				}
			}
		});
		GridBagConstraints gbc_bxCustomSpacing = new GridBagConstraints();
		gbc_bxCustomSpacing.insets = new Insets(0, 0, 5, 0);
		gbc_bxCustomSpacing.gridx = 0;
		gbc_bxCustomSpacing.gridy = 9;
		panel_Text_Component.add(bxCustomSpacing, gbc_bxCustomSpacing);

		JLabel lblSpacing = new JLabel("Spacing=50");
		GridBagConstraints gbc_lblSpacing = new GridBagConstraints();
		gbc_lblSpacing.insets = new Insets(0, 0, 5, 0);
		gbc_lblSpacing.gridx = 0;
		gbc_lblSpacing.gridy = 5;
		panel_Text_Component.add(lblSpacing, gbc_lblSpacing);

		JSlider sldrSpacing = new JSlider();
		sldrSpacing.setMaximum(25);
		sldrSpacing.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				spacing = sldrSpacing.getValue();
				lblSpacing.setText("Spacing=" + spacing);

			}
		});

		GridBagConstraints gbc_sldrSpacing = new GridBagConstraints();
		gbc_sldrSpacing.weightx = 2.0;
		gbc_sldrSpacing.weighty = 2.0;
		gbc_sldrSpacing.fill = GridBagConstraints.BOTH;
		gbc_sldrSpacing.gridx = 0;
		gbc_sldrSpacing.gridy = 6;
		panel_Text_Component.add(sldrSpacing, gbc_sldrSpacing);
		sldrSpacing.setMinorTickSpacing(1);
		sldrSpacing.setMajorTickSpacing(5);
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

	private JPanel createTextPanel() {
		JPanel panel_Text_Component = new JPanel();
		panel_Text_Component.setBorder(new TitledBorder(null,
				"text components", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		frmTabpdf.getContentPane().add(panel_Text_Component);
		GridBagLayout gbl_panel_Text_Component = new GridBagLayout();
		gbl_panel_Text_Component.columnWidths = new int[] { 380 };
		gbl_panel_Text_Component.rowHeights = new int[] { 123, 0, 0, 0, 0, 0,
				0, 30, 30, 0 };
		gbl_panel_Text_Component.columnWeights = new double[] { 0.0 };
		gbl_panel_Text_Component.rowWeights = new double[] { 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		panel_Text_Component.setLayout(gbl_panel_Text_Component);
		JButton btnLoadTextFile = new JButton("Load text file");
		GridBagConstraints gbc_btnLoadTextFile = new GridBagConstraints();
		gbc_btnLoadTextFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnLoadTextFile.weighty = 2.0;
		gbc_btnLoadTextFile.weightx = 2.0;
		gbc_btnLoadTextFile.fill = GridBagConstraints.BOTH;
		gbc_btnLoadTextFile.gridx = 0;
		gbc_btnLoadTextFile.gridy = 0;
		panel_Text_Component.add(btnLoadTextFile, gbc_btnLoadTextFile);
		btnLoadTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				src = openFile();
			}
		});
		return panel_Text_Component;
	}

	// the below methods should be private but are made public for testing
	// purposes.
	public void openPdf() {
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
		// changed from exit on close to dispose on close
		applicationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		applicationFrame.getContentPane().add(viewerComponentPanel);
		// Now that the GUI is all in place, we can try opening a PDF
		controller.openDocument(localDest);
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
		return in.getPath() + ".pdf";
	}

	public String openFile() {
		JFileChooser fc = new JFileChooser();
		FileFilter textFilter = new ExtensionFilter("txt file", ".txt");
		fc.addChoosableFileFilter(textFilter);
		int status = fc.showOpenDialog(null);
		File out = null;
		if (status == JFileChooser.APPROVE_OPTION) {
			out = fc.getSelectedFile();
			// DoStuff();
		}
		return out.getPath();
	}
}
