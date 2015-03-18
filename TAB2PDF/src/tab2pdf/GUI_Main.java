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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
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

		// the main frame stuff
		createMainFrame();

		// text components
		JPanel panel_Text_Component = new JPanel();
		panel_Text_Component.setBorder(new TitledBorder(null, "",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmTabpdf.getContentPane().add(panel_Text_Component);
		GridBagLayout gbl_panel_Text_Component = new GridBagLayout();
		gbl_panel_Text_Component.columnWeights = new double[] { 0.0 };
		gbl_panel_Text_Component.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		panel_Text_Component.setLayout(gbl_panel_Text_Component);

		JPanel panel_LoadText =  new JPanel();
		GridBagConstraints gbc_panel_LoadText = new GridBagConstraints();
		gbc_panel_LoadText.fill = GridBagConstraints.BOTH;
		gbc_panel_LoadText.weighty = 1.0;
		gbc_panel_LoadText.weightx = 1.0;
		gbc_panel_LoadText.gridx = 0;
		gbc_panel_LoadText.gridy = 0;
		panel_Text_Component.add(panel_LoadText, gbc_panel_LoadText);

		JPanel panel_title = new JPanel();
		{
			GridBagConstraints gbc_panel_title = new GridBagConstraints();
			gbc_panel_title.weighty = 0.5;
			gbc_panel_title.weightx = 0.5;
			gbc_panel_title.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_title.gridx = 0;
			gbc_panel_title.gridy = 1;
			panel_Text_Component.add(panel_title, gbc_panel_title);
			panel_title.setLayout(new BorderLayout(0, 0));
		}

		final JLabel lblTitle = new JLabel("Title: ");
		{
			lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
			panel_title.add(lblTitle, BorderLayout.NORTH);
		}

		final JTextField txtTitle = new JTextField();
		{
			txtTitle.setHorizontalAlignment(SwingConstants.CENTER);
			txtTitle.setText("<insert title here>");
			panel_title.add(txtTitle);
			txtTitle.setColumns(10);
		}

		JPanel panel_subtitle = new JPanel();
		{
			GridBagConstraints gbc_panel_subtitle = new GridBagConstraints();
			gbc_panel_subtitle.weighty = 0.5;
			gbc_panel_subtitle.weightx = 0.5;
			gbc_panel_subtitle.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_subtitle.gridx = 0;
			gbc_panel_subtitle.gridy = 2;
			panel_Text_Component.add(panel_subtitle, gbc_panel_subtitle);
			panel_subtitle.setLayout(new BorderLayout(0, 0));
		}

		final JLabel lblSubtitle = new JLabel("Subtitle:");
		{
			lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
			panel_subtitle.add(lblSubtitle, BorderLayout.NORTH);
		}
		final JTextField txtSubtitle = new JTextField();
		{
			txtSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
			txtSubtitle.setText("<insert subtitle here>");
			txtSubtitle.setColumns(10);
			panel_subtitle.add(txtSubtitle, BorderLayout.CENTER);
		}
		JPanel panel_SliderSpacing = new JPanel();
		{
			GridBagConstraints gbc_panel_SliderSpacing = new GridBagConstraints();
			gbc_panel_SliderSpacing.weighty = 1.0;
			gbc_panel_SliderSpacing.weightx = 1.0;
			gbc_panel_SliderSpacing.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel_SliderSpacing.gridx = 0;
			gbc_panel_SliderSpacing.gridy = 3;
			panel_Text_Component.add(panel_SliderSpacing,
					gbc_panel_SliderSpacing);
			panel_SliderSpacing.setLayout(new BorderLayout(0, 0));
		}

		final JLabel lblSpacing = new JLabel("Spacing=" + spacing);
		{
			lblSpacing.setHorizontalAlignment(SwingConstants.CENTER);
			panel_SliderSpacing.add(lblSpacing, BorderLayout.NORTH);

		}
		final JSlider sldrSpacing = new JSlider();
		{
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
		}
		// pdf components
		JSplitPane panel_PDF_SplitPanel = new JSplitPane();
		panel_PDF_SplitPanel.setEnabled(false);
		panel_PDF_SplitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
		frmTabpdf.getContentPane().add(panel_PDF_SplitPanel);

		JPanel panel_PDF_buttons = new JPanel();
		panel_PDF_SplitPanel.setLeftComponent(panel_PDF_buttons);

		{
			panel_PDF_buttons.setBorder(new TitledBorder(null, "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
		}
		panel_PDF_buttons.setLayout(new GridLayout(1, 2, 0, 0));
		JPanel panel_ConvertPDF = new JPanel();
		{
			panel_PDF_buttons.add(panel_ConvertPDF);
		}
		panel_ConvertPDF.setLayout(new BorderLayout(0, 0));
		final JButton btnConvertToPdf = new JButton("Convert PDF");
		{
			panel_ConvertPDF.add(btnConvertToPdf);
		}
		JPanel panel_SavePDF = new JPanel();
		{
			panel_PDF_buttons.add(panel_SavePDF);
		}
		panel_SavePDF.setLayout(new BorderLayout(0, 0));

		final JButton btnSavePDF = new JButton("Save PDF");
		{
			panel_SavePDF.add(btnSavePDF);
		}

		JMenuBar menuBar = new JMenuBar();
		frmTabpdf.setJMenuBar(menuBar);
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		JMenuItem mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		JMenuItem mntmUserManual = new JMenuItem("User Manual");
		mnHelp.add(mntmUserManual);
		JPanel panel_PDF_Preview = new JPanel();
		panel_PDF_SplitPanel.setRightComponent(panel_PDF_Preview);
						GridBagLayout gbl_panel_LoadText = new GridBagLayout();
						gbl_panel_LoadText.columnWidths = new int[] {100, 25, 100};
						gbl_panel_LoadText.rowHeights = new int[] {60, 30, 60};
						gbl_panel_LoadText.columnWeights = new double[]{0.0, 0.0};
						gbl_panel_LoadText.rowWeights = new double[]{0.0, 0.0};
						panel_LoadText.setLayout(gbl_panel_LoadText);
								
										JButton btnLoadTextFile = new JButton("Load file");
										GridBagConstraints gbc_btnLoadTextFile = new GridBagConstraints();
										gbc_btnLoadTextFile.fill = GridBagConstraints.BOTH;
										gbc_btnLoadTextFile.gridheight = 2;
										gbc_btnLoadTextFile.gridwidth = 3;
										gbc_btnLoadTextFile.insets = new Insets(0, 0, 5, 5);
										gbc_btnLoadTextFile.gridx = 0;
										gbc_btnLoadTextFile.gridy = 0;
										panel_LoadText.add(btnLoadTextFile, gbc_btnLoadTextFile);
										setActionListeners(btnLoadTextFile, lblTitle, txtTitle, lblSubtitle,
												txtSubtitle, lblSpacing, sldrSpacing, btnConvertToPdf,
												btnSavePDF, mntmAbout, mntmUserManual, panel_PDF_Preview,
												panel_PDF_SplitPanel);
		setButtons(btnConvertToPdf, btnSavePDF);

	}

	private void setActionListeners(JButton btnLoadTextFile,
			final JLabel lblTitle, final JTextField txtTitle,
			final JLabel lblSubtitle, final JTextField txtSubtitle,
			final JLabel lblSpacing, final JSlider sldrSpacing,
			final JButton btnConvertToPdf, final JButton btnSavePDF,
			JMenuItem mntmAbout, JMenuItem mntmUserManual,
			final JPanel panel_PDF_Preview,
			final JSplitPane panel_PDF_SplitPanel) {
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// openPdf("font_types.pdf");
			}
		});

		mntmUserManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// openPdf(userManualDest);
			}
		});

		btnLoadTextFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				src = openFile();
				isInputProvided = true;
				setDefaultValues(btnConvertToPdf, btnSavePDF, sldrSpacing,
						lblSpacing, lblTitle, lblSubtitle, txtTitle,
						txtSubtitle);
				setButtons(btnConvertToPdf, btnSavePDF);
				try {
					output = new DrawOutput(src, dest, useCustomTitle,
							useCustomSubtitle, useCustomSpacing, title,
							subtitle, spacing);
					JOptionPane.showMessageDialog(null, "You have successfully loaded the file at: " + src);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
					output.createPdf(true);
					isConverted = true;
					setButtons(btnConvertToPdf, btnSavePDF);
					openPdf(output.getLocalDest(), panel_PDF_Preview,
							panel_PDF_SplitPanel);

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
					output.createPdf(false);

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
				lblSubtitle.setText("Subtitle: " + subtitle);
				if (useCustomSubtitle == false) {
					useCustomSubtitle = true;
				}

			}
		});

		txtTitle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				title = txtTitle.getText();
				lblTitle.setText("Title: " + title);
				if (useCustomTitle == false) {
					useCustomTitle = true;
				}

			}
		});
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

	// the below methods should be private but are made public for testing
	// purposes.
	public void openPdf(String localDest, JPanel viewerComponentPanel,
			JSplitPane panel) {
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
		viewerComponentPanel = factory.buildViewerPanel();
		// Now that the GUI is all in place, we can try opening a PDF
		controller.openDocument(localDest);
		panel.setRightComponent(viewerComponentPanel);
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

	public void setDefaultValues(JButton btnConvert, JButton btnSave,
			JSlider sliderSpacing, JLabel lblSpacing, JLabel lblTitle,
			JLabel lblSubtitle, JTextField txtTitle, JTextField txtSubtitle) {
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
	}

	public void setButtons(JButton btnConvert, JButton btnSave) {
		if (isInputProvided == true) {
			btnConvert.setEnabled(true);
			if (isConverted == true) {
				btnSave.setEnabled(true);
			}
		} else {
			btnConvert.setEnabled(false);
			btnSave.setEnabled(false);
		}
	}
}
