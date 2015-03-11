package tab2pdf;

import java.awt.BorderLayout;
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
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.icepdf.ri.util.PropertiesManager;

import com.itextpdf.text.DocumentException;

public class GUI_Main {

	private JFrame frmTabpdf;
	private String src;
	private String dest;
	private final String localDest = "result.pdf";
	private final String userManualDest = "User_Manual.pdf";
	private DrawOutput output;
	private float spacing = 5.0f;
	private boolean useCustomTitle = false;
	private boolean useCustomSubtitle = false;
	private boolean useCustomSpacing = false;
	private boolean isInputProvided = false;
	private boolean isConverted = false;
	private String title = "";
	private String subtitle = "";

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

		//the main frame stuff
		createMainFrame();

		//text components
		JPanel panel_Text_Component = createTextComponentPanel();

		JPanel panel_LoadText = createLoadTextPanel(panel_Text_Component);

		JButton btnLoadTextFile = createLoadTextButton(panel_LoadText);

		JPanel panel_title = createTitlePanel(panel_Text_Component);

		final JLabel lblTitle = createTitleLabel(panel_title);

		final JTextField txtTitle = createTitleTextField(panel_title);

		JPanel panel_subtitle = createSubtitlePanel(panel_Text_Component);

		final JLabel lblSubtitle = createSubtitleLabel(panel_subtitle);

		final JTextField txtSubtitle = createSubtitleTextField(panel_subtitle);

		JPanel panel_SliderSpacing = createSpacingPanel(panel_Text_Component);

		final JLabel lblSpacing = createSpacingLabel(panel_SliderSpacing);

		final JSlider sldrSpacing = createSpacingSlider(panel_SliderSpacing);
		
		
		//pdf components
		JPanel panel_PDF_Component = createPdfComponentPanel();

		JPanel panel_ConvertPDF = createConvertPdfPanel(panel_PDF_Component);

		final JButton btnConvertToPdf = createConvertToPdfButton(panel_ConvertPDF);

		JPanel panel_SavePDF = createSavePdfPanel(panel_PDF_Component);

		final JButton btnSavePDF = createSavePdfButton(panel_SavePDF);

		JPanel panel_DisplayPDF = createDisplayPdfPanel(panel_PDF_Component);

		final JButton btnDisplayPdf = createDisplayPdfButton(panel_DisplayPDF);

		JMenuBar menuBar = new JMenuBar();
		frmTabpdf.setJMenuBar(menuBar);
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		JMenuItem mntmUserManual = new JMenuItem("User Manual");
		mnHelp.add(mntmUserManual);
		setDefaultValues(btnConvertToPdf, btnDisplayPdf, btnSavePDF,
				sldrSpacing, lblSpacing, lblTitle, lblSubtitle,
				txtTitle, txtSubtitle);

		setActionListeners(btnLoadTextFile, lblTitle, txtTitle,
				lblSubtitle, txtSubtitle, lblSpacing, sldrSpacing,
				btnConvertToPdf, btnSavePDF, btnDisplayPdf, mntmAbout,
				mntmUserManual);

	}

	private void setActionListeners(JButton btnLoadTextFile,
			final JLabel lblCurrentTitle, final JTextField txtTitle,
			final JLabel lblCurrentSubtitle, final JTextField txtSubtitle,
			final JLabel lblSpacing, final JSlider sldrSpacing,
			final JButton btnConvertToPdf, final JButton btnSavePDF,
			final JButton btnDisplayPdf, JMenuItem mntmAbout,
			JMenuItem mntmUserManual) {
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openPdf("font_types.pdf");
			}
		});

		mntmUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openPdf(userManualDest);
			}
		});
		btnDisplayPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openPdf(localDest);
			}
		});

		btnLoadTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				src = openFile();
				isInputProvided = true;
				setDefaultValues(btnConvertToPdf, btnDisplayPdf, btnSavePDF,
						sldrSpacing, lblSpacing, lblCurrentTitle,
						lblCurrentSubtitle, txtTitle, txtSubtitle);
			}
		});

		sldrSpacing.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				spacing = (float) (sldrSpacing.getValue() / 10.0f);
				lblSpacing.setText("Spacing=" + spacing);
				if (useCustomSpacing == false) {
					useCustomSpacing = true;
				}

			}
		});

		btnConvertToPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// create a local version of the pdf file.
				try {
					output = new DrawOutput(src, localDest, useCustomTitle,
							useCustomSubtitle, useCustomSpacing, title,
							subtitle, spacing);
					output.createPdf();
					isConverted = true;
					setButtons(btnConvertToPdf, btnDisplayPdf, btnSavePDF);

				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}

		});
		btnSavePDF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dest = saveFile();
				try {
					output = new DrawOutput(src, dest, useCustomTitle,
							useCustomSubtitle, useCustomSpacing, title,
							subtitle, spacing);
					output.createPdf();

				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}

		});
		txtSubtitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subtitle = txtSubtitle.getText();
				lblCurrentSubtitle.setText("Subtitle: " + subtitle);
				if (useCustomSubtitle == false) {
					useCustomSubtitle = true;
				}

			}
		});

		txtTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				title = txtTitle.getText();
				lblCurrentTitle.setText("Title: " + title);
				if (useCustomTitle == false) {
					useCustomTitle = true;
				}

			}
		});
	}

	private JButton createDisplayPdfButton(JPanel panel_DisplayPDF) {
		final JButton btnDisplayPdf = new JButton("Display PDF");
		GridBagConstraints gbc_btnDisplayPdf = new GridBagConstraints();
		gbc_btnDisplayPdf.ipady = 10;
		gbc_btnDisplayPdf.ipadx = 10;
		gbc_btnDisplayPdf.fill = GridBagConstraints.BOTH;
		gbc_btnDisplayPdf.gridx = 0;
		gbc_btnDisplayPdf.gridy = 0;
		panel_DisplayPDF.add(btnDisplayPdf, gbc_btnDisplayPdf);
		return btnDisplayPdf;
	}

	private JPanel createDisplayPdfPanel(JPanel panel_PDF_Component) {
		JPanel panel_DisplayPDF = new JPanel();
		panel_PDF_Component.add(panel_DisplayPDF);
		GridBagLayout gbl_panel_DisplayPDF = new GridBagLayout();
		gbl_panel_DisplayPDF.columnWidths = new int[] { 0 };
		gbl_panel_DisplayPDF.rowHeights = new int[] { 0 };
		gbl_panel_DisplayPDF.columnWeights = new double[] { 0.0 };
		gbl_panel_DisplayPDF.rowWeights = new double[] { 0.0 };
		panel_DisplayPDF.setLayout(gbl_panel_DisplayPDF);
		return panel_DisplayPDF;
	}

	private JButton createSavePdfButton(JPanel panel_SavePDF) {
		final JButton btnSavePDF = new JButton("Save PDF");
		GridBagConstraints gbc_btnSavePDF = new GridBagConstraints();
		gbc_btnSavePDF.ipady = 10;
		gbc_btnSavePDF.ipadx = 10;
		gbc_btnSavePDF.fill = GridBagConstraints.BOTH;
		gbc_btnSavePDF.gridx = 0;
		gbc_btnSavePDF.gridy = 0;
		panel_SavePDF.add(btnSavePDF, gbc_btnSavePDF);
		return btnSavePDF;
	}

	private JPanel createSavePdfPanel(JPanel panel_PDF_Component) {
		JPanel panel_SavePDF = new JPanel();
		panel_PDF_Component.add(panel_SavePDF);
		GridBagLayout gbl_panel_SavePDF = new GridBagLayout();
		gbl_panel_SavePDF.columnWidths = new int[] { 0 };
		gbl_panel_SavePDF.rowHeights = new int[] { 0 };
		gbl_panel_SavePDF.columnWeights = new double[] { 0.0 };
		gbl_panel_SavePDF.rowWeights = new double[] { 0.0 };
		panel_SavePDF.setLayout(gbl_panel_SavePDF);
		return panel_SavePDF;
	}

	private JButton createConvertToPdfButton(JPanel panel_ConvertPDF) {
		final JButton btnConvertToPdf = new JButton("Convert PDF");
		GridBagConstraints gbc_btnConvertToPdf = new GridBagConstraints();
		gbc_btnConvertToPdf.ipady = 10;
		gbc_btnConvertToPdf.ipadx = 10;
		gbc_btnConvertToPdf.fill = GridBagConstraints.BOTH;
		gbc_btnConvertToPdf.gridx = 0;
		gbc_btnConvertToPdf.gridy = 0;
		panel_ConvertPDF.add(btnConvertToPdf, gbc_btnConvertToPdf);
		return btnConvertToPdf;
	}

	private JPanel createConvertPdfPanel(JPanel panel_PDF_Component) {
		JPanel panel_ConvertPDF = new JPanel();
		panel_PDF_Component.add(panel_ConvertPDF);
		GridBagLayout gbl_panel_ConvertPDF = new GridBagLayout();
		gbl_panel_ConvertPDF.columnWidths = new int[] { 0 };
		gbl_panel_ConvertPDF.rowHeights = new int[] { 0 };
		gbl_panel_ConvertPDF.columnWeights = new double[] { 0.0 };
		gbl_panel_ConvertPDF.rowWeights = new double[] { 0.0 };
		panel_ConvertPDF.setLayout(gbl_panel_ConvertPDF);
		return panel_ConvertPDF;
	}

	private JPanel createPdfComponentPanel() {
		JPanel panel_PDF_Component = new JPanel();
		panel_PDF_Component.setBorder(new TitledBorder(null, "PDF COMPONENTS",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmTabpdf.getContentPane().add(panel_PDF_Component);
		panel_PDF_Component.setLayout(new GridLayout(3, 1, 0, 0));
		return panel_PDF_Component;
	}

	private JSlider createSpacingSlider(JPanel panel_SliderSpacing) {
		final JSlider sldrSpacing = new JSlider();
		panel_SliderSpacing.add(sldrSpacing);
		sldrSpacing.setMaximum(100);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("0.0"));
		labelTable.put(new Integer(100), new JLabel("10.0"));
		sldrSpacing.setLabelTable(labelTable);
		sldrSpacing.setMinorTickSpacing(1);
		sldrSpacing.setMajorTickSpacing(5);
		sldrSpacing.setPaintLabels(true);
		sldrSpacing.setPaintTicks(true);
		return sldrSpacing;
	}

	private JLabel createSpacingLabel(JPanel panel_SliderSpacing) {
		final JLabel lblSpacing = new JLabel("Spacing=" + spacing);
		lblSpacing.setHorizontalAlignment(SwingConstants.CENTER);
		panel_SliderSpacing.add(lblSpacing, BorderLayout.NORTH);
		return lblSpacing;
	}

	private JPanel createSpacingPanel(JPanel panel_Text_Component) {
		JPanel panel_SliderSpacing = new JPanel();
		GridBagConstraints gbc_panel_SliderSpacing = new GridBagConstraints();
		gbc_panel_SliderSpacing.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_SliderSpacing.gridx = 0;
		gbc_panel_SliderSpacing.gridy = 3;
		panel_Text_Component.add(panel_SliderSpacing, gbc_panel_SliderSpacing);
		panel_SliderSpacing.setLayout(new BorderLayout(0, 0));
		return panel_SliderSpacing;
	}

	private JTextField createSubtitleTextField(JPanel panel_subtitle) {
		final JTextField txtSubtitle = new JTextField();
		txtSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		txtSubtitle.setText("<insert subtitle here>");
		txtSubtitle.setColumns(10);
		panel_subtitle.add(txtSubtitle, BorderLayout.CENTER);
		return txtSubtitle;
	}

	private JLabel createSubtitleLabel(JPanel panel_subtitle) {
		final JLabel lblCurrentSubtitle = new JLabel("Subtitle:");
		lblCurrentSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
		panel_subtitle.add(lblCurrentSubtitle, BorderLayout.NORTH);
		return lblCurrentSubtitle;
	}

	private JPanel createSubtitlePanel(JPanel panel_Text_Component) {
		JPanel panel_subtitle = new JPanel();
		GridBagConstraints gbc_panel_subtitle = new GridBagConstraints();
		gbc_panel_subtitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_subtitle.gridx = 0;
		gbc_panel_subtitle.gridy = 2;
		panel_Text_Component.add(panel_subtitle, gbc_panel_subtitle);
		panel_subtitle.setLayout(new BorderLayout(0, 0));
		return panel_subtitle;
	}

	private JTextField createTitleTextField(JPanel panel_title) {
		final JTextField txtTitle = new JTextField();
		txtTitle.setHorizontalAlignment(SwingConstants.CENTER);
		txtTitle.setText("<insert title here>");
		panel_title.add(txtTitle);
		txtTitle.setColumns(10);
		return txtTitle;
	}

	private JLabel createTitleLabel(JPanel panel_title) {
		final JLabel lblCurrentTitle = new JLabel("Title: ");
		lblCurrentTitle.setHorizontalAlignment(SwingConstants.CENTER);
		panel_title.add(lblCurrentTitle, BorderLayout.NORTH);
		return lblCurrentTitle;
	}

	private void createMainFrame() {
		frmTabpdf = new JFrame();
		frmTabpdf.setTitle("TAB2PDF");
		frmTabpdf.getContentPane().setFont(
				new Font("Times New Roman", Font.PLAIN, 11));
		frmTabpdf.getContentPane().setLayout(new GridLayout(1, 2, 0, 0));
		frmTabpdf.setBounds(100, 100, 450, 300);
		frmTabpdf.setSize(800, 600);
		frmTabpdf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private JPanel createTitlePanel(JPanel panel_Text_Component) {
		JPanel panel_title = new JPanel();
		GridBagConstraints gbc_panel_title = new GridBagConstraints();
		gbc_panel_title.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_title.gridx = 0;
		gbc_panel_title.gridy = 1;
		panel_Text_Component.add(panel_title, gbc_panel_title);
		panel_title.setLayout(new BorderLayout(0, 0));
		return panel_title;
	}

	private JButton createLoadTextButton(JPanel panel_LoadText) {
		JButton btnLoadTextFile = new JButton("Load text file");
		GridBagConstraints gbc_btnLoadTextFile = new GridBagConstraints();
		gbc_btnLoadTextFile.fill = GridBagConstraints.BOTH;
		gbc_btnLoadTextFile.gridx = 0;
		gbc_btnLoadTextFile.gridy = 0;
		panel_LoadText.add(btnLoadTextFile, gbc_btnLoadTextFile);
		return btnLoadTextFile;
	}

	private JPanel createLoadTextPanel(JPanel panel_Text_Component) {
		JPanel panel_LoadText = new JPanel();
		GridBagConstraints gbc_panel_LoadText = new GridBagConstraints();
		gbc_panel_LoadText.gridx = 0;
		gbc_panel_LoadText.gridy = 0;
		panel_Text_Component.add(panel_LoadText, gbc_panel_LoadText);
		GridBagLayout gbl_panel_LoadText = new GridBagLayout();
		gbl_panel_LoadText.columnWidths = new int[] { 0 };
		gbl_panel_LoadText.rowHeights = new int[] { 0 };
		gbl_panel_LoadText.columnWeights = new double[] { 1.0 };
		gbl_panel_LoadText.rowWeights = new double[] { 1.0 };
		panel_LoadText.setLayout(gbl_panel_LoadText);
		return panel_LoadText;
	}

	private JPanel createTextComponentPanel() {
		JPanel panel_Text_Component = new JPanel();
		panel_Text_Component.setBorder(new TitledBorder(null,
				"TEXT COMPONENTS", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		frmTabpdf.getContentPane().add(panel_Text_Component);
		GridBagLayout gbl_panel_Text_Component = new GridBagLayout();
		gbl_panel_Text_Component.columnWidths = new int[] { 364 };
		gbl_panel_Text_Component.rowHeights = new int[] { 126, 63, 63, 126 };
		gbl_panel_Text_Component.columnWeights = new double[] { 1.0 };
		gbl_panel_Text_Component.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		panel_Text_Component.setLayout(gbl_panel_Text_Component);
		return panel_Text_Component;
	}

	// the below methods should be private but are made public for testing
	// purposes.
	public void openPdf(String localDest) {
		// build a component controller
		SwingController controller = new SwingController();
		controller.setIsEmbeddedComponent(true);

		PropertiesManager properties = new PropertiesManager(
				System.getProperties(),
				ResourceBundle
						.getBundle(PropertiesManager.DEFAULT_MESSAGE_BUNDLE));

		properties.set(PropertiesManager.PROPERTY_DEFAULT_ZOOM_LEVEL, "1.00");

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

	public void setDefaultValues(JButton btnConvert, JButton btnDisplay,
			JButton btnSave, JSlider sliderSpacing, JLabel lblSpacing,
			JLabel lblTitle, JLabel lblSubtitle, JTextField txtTitle,
			JTextField txtSubtitle) {
		useCustomTitle = false;
		useCustomSubtitle = false;
		useCustomSpacing = false;
		spacing = (float) (sliderSpacing.getValue() / 10.0f);
		sliderSpacing.setValue(50);// default value;
		lblSpacing.setText("Spacing= " + spacing);
		lblTitle.setText("Title: " + title);
		lblSubtitle.setText("Subtitle: " + subtitle);
		txtTitle.setText("<insert title here>");
		txtSubtitle.setText("<insert subtitle here>");
		setButtons(btnConvert, btnDisplay, btnSave);
	}

	public void setButtons(JButton btnConvert, JButton btnDisplay,
			JButton btnSave) {
		if (isInputProvided == true) {
			btnConvert.setEnabled(true);
			if (isConverted == true) {
				btnSave.setEnabled(true);
				btnDisplay.setEnabled(true);
			}
		} else {
			btnConvert.setEnabled(false);
			btnSave.setEnabled(false);
			btnDisplay.setEnabled(false);
		}
	}
}
