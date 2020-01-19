package recognizer1;


// ������ ������������� ���������� ���� ������ � ������� � ������������

import javax.swing.*;
// import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;

public class JFileChooserTest extends JFrame
{
	private static final long serialVersionUID = 1L;

	private  JButton      btnSaveFile   = null;
	private  JButton      btnOpenDir    = null;
	private  JButton      btnFileFilter = null;
	                                      
	private  JFileChooser fileChooser   = null;

	private final String[][] FILTERS = {{"docx", "����� Word (*.docx)"},
			                            {"pdf" , "Adobe Reader(*.pdf)"}};	
	public JFileChooserTest() {
		super("������ FileChooser");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// ������ �������� ����������� ���� ��� ������ ����������
		btnOpenDir = new JButton("������� ����������");
		// ������ �������� ����������� ���� ��� ���������� �����
		btnSaveFile = new JButton("��������� ����");
		// ������ �������� ����������� ���� ��� ���������� �����
		btnFileFilter = new JButton("���������� ������");
		
		// �������� ���������� JFileChooser 
		fileChooser = new JFileChooser();
		// ����������� ���������� � �������
		addFileChooserListeners();
		// ���������� ������ � ����������
		JPanel contents = new JPanel();
		contents.add(btnOpenDir   );
		contents.add(btnSaveFile  );
		contents.add(btnFileFilter);
		setContentPane(contents);
		// ����� ���� �� �����
		setSize(360, 110);
		setVisible(true);
	}
	private void addFileChooserListeners()
	{
		btnOpenDir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("����� ����������");
				// ����������� ������ - ������ �������
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(JFileChooserTest.this);
				// ���� ���������� �������, ������� �� � ���������
				if (result == JFileChooser.APPROVE_OPTION )
                    JOptionPane.showMessageDialog(JFileChooserTest.this, 
							                      fileChooser.getSelectedFile());
			}
		});
		btnSaveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("���������� �����");
				// ����������� ������ - ������ ����
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showSaveDialog(JFileChooserTest.this);
				// ���� ���� ������, �� ���������� ��� � ���������
				if (result == JFileChooser.APPROVE_OPTION )
					JOptionPane.showMessageDialog(JFileChooserTest.this, 
			                          "���� '" + fileChooser.getSelectedFile() + 
			                          " ) ��������");
			}
		});
		btnFileFilter.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fileChooser.setDialogTitle("�������� ����");
				// ���������� ������� ����� ������
                for (int i = 0; i < FILTERS[0].length; i++) {
    				FileFilterExt eff = new FileFilterExt(FILTERS[i][0], 
    						                              FILTERS[i][1]);
    				fileChooser.addChoosableFileFilter(eff);
                }
//                FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                        "Word & Excel", "docx", "xlsx");
//                fileChooser.setFileFilter(filter);

				// ����������� ������ - ������ ����
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(JFileChooserTest.this);
				// ���� ���� ������, ������� ��� � ���������
				if (result == JFileChooser.APPROVE_OPTION )
					JOptionPane.showMessageDialog(JFileChooserTest.this, 
							                      "������ ���� ( " + 
				                          fileChooser.getSelectedFile() + " )");
			}
		});
	}
	// ������ ������ ������ ������������� ����
	class FileFilterExt extends javax.swing.filechooser.FileFilter 
	{
		String extension  ;  // ���������� �����
		String description;  // �������� ���� ������

		FileFilterExt(String extension, String descr)
		{
			this.extension = extension;
			this.description = descr;
		}
		@Override
		public boolean accept(java.io.File file)
		{
			if(file != null) {
				if (file.isDirectory())
					return true;
				if( extension == null )
					return (extension.length() == 0);
				return file.getName().endsWith(extension);
			}
			return false;
		}
		// ������� �������� ����� ������
		@Override
		public String getDescription() {
			return description;
		}
	}

	public static void main(String[] args)
	{
		// ����������� ����������� ���� JFileChooser  
		UIManager.put("FileChooser.saveButtonText"      , "���������"             );
		UIManager.put("FileChooser.openButtonText"      , "�������"               );
		UIManager.put("FileChooser.cancelButtonText"    , "������"                );
		UIManager.put("FileChooser.fileNameLabelText"   , "������������ �����"    );
		UIManager.put("FileChooser.filesOfTypeLabelText", "���� ������"           );
		UIManager.put("FileChooser.lookInLabelText"     , "����������"            );
		UIManager.put("FileChooser.saveInLabelText"     , "��������� � ����������");
		UIManager.put("FileChooser.folderNameLabelText" , "���� ����������"       );

		new JFileChooserTest();
	}
}
