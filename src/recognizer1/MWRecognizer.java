package recognizer1;
//https://habr.com/ru/post/198268/

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import recognizer1.JFileChooserTest;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
/*
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
*/
import javax.swing.UIManager;





@SuppressWarnings("serial")
public class MWRecognizer extends JPanel implements ActionListener,MouseListener,MouseMotionListener {
	int maxSizeWindowX=1280; 
	int maxSizeWindowY=1024;
	int sizeWindowX=maxSizeWindowX/5*3; 
	int sizeWindowY=maxSizeWindowY/4*3;
	JButton buttonLearning,buttonOpenFile,buttonRecognize,buttonOptions,buttonClearAll,buttonStartLearning,buttonStopLearning,buttonOpenWeights,buttonSaveWeights;
	ActionListener learningAL,openFileAL,recognizeAL,optionsAL,clearAllAL,startLearningAL,stopLearningAL,openWeightsAL,saveWeightsAL;
	JFrame mainWindow;
	Graphics g;
	Timer time;
	Image fon;
	List<Integer>countNeironsInLayers=new ArrayList<Integer>();
	ClassNeironWeb NeironWeb;
	boolean mainWindowBool=true,learningBool=false;
	int[][]placePaint=new int [15][15];
	int dotSizeX,dotSizeY;
	JFileChooserTest chooser;
	JFileChooser fileChooser=null;
	
	public static void main(String args[]) throws IOException  {
		new MWRecognizer();
	}
	
	public MWRecognizer() throws IOException{
		
		mainWindow=new JFrame("Распознаватель");
		//mainwindow.setBackground(Color.black);
		mainWindow.setSize(sizeWindowX,sizeWindowY);
		mainWindow.setLocation(maxSizeWindowX/2-sizeWindowX/2,maxSizeWindowY/2-sizeWindowY/2);
		mainWindow.setFocusable(true);
		mainWindow.setLayout(null);
        mainWindow.setDefaultCloseOperation(mainWindow.EXIT_ON_CLOSE);
		mainWindow.setVisible(true);
		fon=new ImageIcon("fon.png").getImage();
		
		creatingAndAddingButtons();
		setPositionButtons();
		fileChooser= new JFileChooser();
		chooser=new JFileChooserTest();
		for (int i=0;i<15;i++) {for(int j=0;j<15;j++) {placePaint[j][i]=(i+j)%2*255;}}
                
        mainpanelshow();
        mainWindow.repaint();
        countNeironsInLayers.add(150);
        countNeironsInLayers.add(30);
        //countNeironsInLayers.add(15);
        countNeironsInLayers.add(11);
        NeironWeb=new ClassNeironWeb(countNeironsInLayers);
		NeironWeb.setImagePack();
        NeironWeb.handlerImage();
               
		time=new Timer(100,this);
		time.start();
	}
	public class JFileChooserTest extends JFrame
	{
		private static final long serialVersionUID = 1L;

		JFileChooser fileChooser   = null;

		
		public JFileChooserTest() {
			super("Пример FileChooser");
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			// Кнопка создания диалогового окна для выбора директории
			
			fileChooser = new JFileChooser();
		}
	}

		
	public void creatingAndAddingButtons() {
		
		buttonLearning=new JButton("Обучение");
		buttonStopLearning=new JButton("Завершить обучение");
		buttonOpenFile=new JButton("Открыть изображение");
		buttonRecognize=new JButton("Распознать");
		buttonOptions=new JButton("Настройки");
		buttonClearAll=new JButton("Очистить");
		buttonStartLearning=new JButton("Начать обучение");
		buttonSaveWeights=new JButton("Сохранить веса");
		buttonOpenWeights=new JButton("Открыть файл с весами");
		
		learningAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                            mainWindowBool=false;
                            learningpanelshow();
			}
		};
		
		openFileAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChooser.setDialogTitle("Выбор файла");
				// Определение режима - только каталог
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setSize(500, 800);
				int result = fileChooser.showOpenDialog(chooser);
				String path=fileChooser.getSelectedFile().getAbsolutePath();
				try {
					NeironWeb.setRecognirTarget(ImageIO.read(new File(path)));
				}catch(Exception d){ JOptionPane.showMessageDialog(fileChooser,path+"	dont find");}
				
				
				
				repaint();
				
			}
		};
		
		recognizeAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NeironWeb.recognizeMethod();
			}
		};
		
		optionsAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		};
		
		clearAllAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            
			}
		};
		
		startLearningAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            if(NeironWeb.learningMode==false&NeironWeb.learningProcess==false){
                                NeironWeb.learningMode=true;
                                buttonStartLearning.setText("Идет обучение...");
                                NeironWeb.learningMethod();
                                repaint();
                            }
                            else{
                                NeironWeb.learningMode=false;
                                buttonStartLearning.setText("Начать обучение");
                                repaint();
                            }
			}
		};
		openWeightsAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		};
		saveWeightsAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		};
		stopLearningAL=new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
                            if(NeironWeb.learningMode==false&NeironWeb.learningProcess==false){
				mainWindowBool=true;
                                mainpanelshow();
                            }
			}
		};		
		
		mainWindow.add(buttonOpenWeights);
		mainWindow.add(buttonStartLearning);
		mainWindow.add(buttonSaveWeights);
		mainWindow.add(buttonStopLearning);
		
		mainWindow.add(buttonLearning);
		mainWindow.add(buttonOpenFile);
		mainWindow.add(buttonRecognize);
		mainWindow.add(buttonOptions);
		mainWindow.add(buttonClearAll);
	}
	
	public void setPositionButtons() {
		buttonLearning.addActionListener(learningAL);
		buttonOpenFile.addActionListener(openFileAL);
		buttonRecognize.addActionListener(recognizeAL);
		buttonOptions.addActionListener(optionsAL);
		buttonClearAll.addActionListener(clearAllAL);
		buttonStartLearning.addActionListener(startLearningAL);
		buttonOpenWeights.addActionListener(openWeightsAL);;
		buttonStopLearning.addActionListener(stopLearningAL);
		
		buttonLearning.setSize((int)(sizeWindowX/2*0.8),(int) (sizeWindowY/16));
		buttonOpenFile.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		buttonRecognize.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		buttonOptions.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		buttonClearAll.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		
		buttonStartLearning.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		buttonSaveWeights.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		buttonOpenWeights.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		buttonStopLearning.setSize((int)(sizeWindowX/2*0.8),(int)( sizeWindowY/16));
		
		buttonLearning.setLocation((int)(sizeWindowX*0.55),(int)(sizeWindowY/2*1.05));
		buttonOpenFile.setLocation((int)(sizeWindowX*0.55),(int)(sizeWindowY/2*1.20));
		buttonRecognize.setLocation((int)(sizeWindowX*0.55),(int)(sizeWindowY/2*1.35));
		buttonOptions.setLocation((int)(sizeWindowX*0.55),(int)(sizeWindowY/2*1.50));
		buttonClearAll.setLocation((int)(sizeWindowX*0.55),(int)(sizeWindowY/2*1.65));
		
		buttonStartLearning.setLocation((int)(sizeWindowX*0.05),(int)(sizeWindowY/2*1.1));
		buttonSaveWeights.setLocation((int)(sizeWindowX*0.05),(int)(sizeWindowY/2*1.3));
		buttonOpenWeights.setLocation((int)(sizeWindowX*0.05),(int)(sizeWindowY/2*1.5));
		buttonStopLearning.setLocation((int)(sizeWindowX*0.05),(int)(sizeWindowY/2*1.7));
		
		dotSizeX=(int)((sizeWindowX/2-35)/40);
		dotSizeY=(int)((sizeWindowY/2-20)/40);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		mainWindow.repaint();
		
	}
	
	public void mainpanelshow() {
		//repaint();
                buttonLearning.setVisible(true);
		buttonOpenFile.setVisible(true);
		buttonRecognize.setVisible(true);
		buttonOptions.setVisible(true);
		buttonClearAll.setVisible(true);
		buttonStartLearning.setVisible(false);
		buttonSaveWeights.setVisible(false);
		buttonOpenWeights.setVisible(false);
                buttonStopLearning.setVisible(false);
                repaint();
	}
	
	public void learningpanelshow() {
		//repaint();
                buttonLearning.setVisible(false);
		buttonOpenFile.setVisible(false);
		buttonRecognize.setVisible(false);
		buttonOptions.setVisible(false);
		buttonClearAll.setVisible(false);
		buttonStartLearning.setVisible(true);
		buttonSaveWeights.setVisible(true);
		buttonOpenWeights.setVisible(true);
                buttonStopLearning.setVisible(true);
                repaint();
	}
	
    @Override
	public void paint(Graphics g) {
		super.paintComponents(g);
		System.out.println("repaint");
                g.drawImage(fon,0,0,this);
                if(mainWindowBool==true){
                    g.drawLine(sizeWindowX/2, sizeWindowY/2, sizeWindowX, sizeWindowY/2);
                    g.drawLine(sizeWindowX/2, 0, sizeWindowX/2, sizeWindowY);
                    g.drawLine(sizeWindowX/2+10, 10, sizeWindowX-25, 10);
                    g.drawLine(sizeWindowX/2+10, sizeWindowY/2-10, sizeWindowX-25, sizeWindowY/2-10);
                    g.drawLine(sizeWindowX/2+10, 10, sizeWindowX/2+10, sizeWindowY/2-10);
                    g.drawLine(sizeWindowX-25, 10, sizeWindowX-25, sizeWindowY/2-10);
                    buttonLearning.repaint();
                    buttonOpenFile.repaint();
                    buttonRecognize.repaint();
                    buttonOptions.repaint();
                    buttonClearAll.repaint();
                }
                else{
                    
                    buttonStartLearning.repaint();
                    buttonSaveWeights.repaint();
                    buttonOpenWeights.repaint();
                    buttonStopLearning.repaint();
                    NeironWeb.paint(g);
                }
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
