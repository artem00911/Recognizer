package recognizer1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;




public class ClassNeironWeb  {
    
    List <ClassNeironLayer> Layers=new ArrayList<>();
    List <Integer> countListNeirons=new ArrayList<>();
    int countLayers;
    int hideenLayerSizeX=10,hideenLayerSizeY=15,layerOutput=11;
    public boolean learningMode=false,learningProcess=false;
    double [][]result;
    int[][]image;
    double[][]imagepack;
    int sizeList;
    int symbolTable[][];
    double sumError=0;
    int age=0;
    List<Double> list_error= new ArrayList<>();
    double graphMaxY,graphMinY=0;
    double graphPositionX=25,graphPositionY=50,graphPositionEdgeX=740,graphPositionEdgeY=430;
    double maxSizeList=1000;
    String[] names;
    File file;
    Image fon;
    BufferedImage[] learnPack;
    BufferedImage recognirTarget;
    BufferedImage[]test=new BufferedImage[1];
    double[]tes1;
    double[]recognizerInput;
    JFrame frame;
    String pathToFolderForLearning="E:/Eclipse/Recognizer/LearnPackDigitals";
    String pathToFileForTest="E:/Eclipse/Recognizer/test.png";
    String pathToFileWithWeigths="";
    
    public ClassNeironWeb(List <Integer> count){
    	countListNeirons=count;
    	countLayers=count.size()-1;
        for(int i=0;i<countLayers;i++) {
        	Layers.add(new ClassNeironLayer(count.get(i), count.get(i+1)));
        }
        importImage();
    }
    
    public void getImage(int[][]newimage,int x,int y){	image=newimage;}
    
    public void setRecognirTarget(BufferedImage image) {
    	recognirTarget=image;
    	 for (int a=0;a<hideenLayerSizeY;a++){
         	for (int j=0;j<hideenLayerSizeX;j++){
         		Color c = new Color(recognirTarget.getRGB(j,a));
         		recognizerInput[a*hideenLayerSizeX+j]=getColorDot(c);
         		//if(imagepack[a*Hideenlayersize+j][i]>50){imagepack[a*Hideenlayersize+j][i]=255;}else{imagepack[a*Hideenlayersize+j][i]=0;}
         	}
         }
    }
    
    public void recognizeMethod() {
    	double input[]=recognizerInput;
    	for (int i=0;i<countLayers;i++) {
    		Layers.get(i).setLayerresult(input);
    		Layers.get(i).summing();
    		double []result=Layers.get(i).getResult();
    		input=null;
    		input=result;
    	}
    	
        System.out.println("Result recognize:");
        double max=0,maxj=0;
        for(int j=0;j<layerOutput;j++){
        	if (max<input[j]) {max=input[j];maxj=j;}
        }
        System.out.println(maxj+"("+max+")  ");
    }
    
    public void setImagePack() throws IOException{
        file= new File(pathToFolderForLearning);
        String[] name = file.list();
        String extension = "";
        sizeList=0;
        for(int i=0;i<name.length;i++){
            int ext = name[i].lastIndexOf('.');
            if (ext >= 0) { extension = name[i].substring(ext+1); }
            if(extension.equals("png")|extension.equals("gif")|extension.equals("tiff")|extension.equals("png")|extension.equals("jpeg")){
            	sizeList++;
            }
            else{ name[i]="exception"; }
        }
        names=new String[sizeList];
        symbolTable=new int[layerOutput][sizeList];
        int count=0;
        for(int i=0;i<name.length;i++){
            if(!name[i].equals("exception")){
                names[count]=name[i];
                char[]chars=name[i].toCharArray();
                
                for(int j=0;j<layerOutput;j++){symbolTable[j][count]=0;}
                for(int j=0;j<chars.length;j++){
                    if(chars[j]=='.'|chars[j]=='-'){break;}
                    else{
                        if(chars[j]!='_'){
                            symbolTable[returnNumberSymbol(chars[j])][count]=1; 
                        }
                    }
                }
                count++;
            }
        }
        for(int i=0;i<sizeList;i++){
            System.out.print("Name file "+names[i]+"  ");
            for(int j=0;j<layerOutput;j++){
                if(symbolTable[j][i]==1)System.out.print(returnNSymbol(j)+"("+symbolTable[j][i]+")  ");
            }
            System.out.println();
        }
        //System.out.print(returnNSymbol(5));
        Layers.get(countLayers-1).setSymbolTable(symbolTable);
        learnPack=new BufferedImage[sizeList];
        
        for(int i=0;i<sizeList;i++){
            try {
                learnPack [i]= ImageIO.read(new File(pathToFolderForLearning+"/"+names[i]));
            } catch (IOException e) {System.out.println(pathToFolderForLearning+"/"+names[i]+"   "+e);}
            
        }
        
    }
    
    public void handlerImage(){
        imagepack=new double [hideenLayerSizeX*hideenLayerSizeY][learnPack.length];
        for (int i=0;i<learnPack.length;i++){
            for (int a=0;a<hideenLayerSizeY;a++){
                for (int j=0;j<hideenLayerSizeX;j++){
                	Color c = new Color(learnPack[i].getRGB(j,a));
                    imagepack[a*hideenLayerSizeX+j][i]=getColorDot(c);
                    //if(imagepack[a*Hideenlayersize+j][i]>50){imagepack[a*Hideenlayersize+j][i]=255;}else{imagepack[a*Hideenlayersize+j][i]=0;}
                }
            }
        }
    }
    
    public static int getColorDot(Color c) {
    	int red = c.getRed();
        int green = c.getGreen();
        int blue = c.getBlue();
        return (int)(Math.sqrt(red*red+green*green+blue*blue)/441*255);
    }
    
    public void learningMethod(){
        while (learningMode){
            learningProcess=true;
            sumError=0;
            for (int i=0;i<sizeList;i++){
            	for(int j=0;j<countLayers;j++) {
            		if(j==0) {	Layers.get(j).setLayer(imagepack,  hideenLayerSizeX*hideenLayerSizeY,i);}
            		Layers.get(j).summing();
            		if(j!=countLayers-1) {	Layers.get(j+1).setLayerresult(Layers.get(j).returnListResult());}
            	}
            	for(int j=countLayers-1;j>0;j--) {
            		if(j==countLayers-1) {
            			sumError+=Math.abs(Layers.get(j).calculationOutputError(i));
            		}
            		//Layers.get(j).calculationDifWeight();
            		Layers.get(j-1).setError(Layers.get(j).calculationSigma());
            		Layers.get(j-1).calculationDifWeight();
            		
            	}
            	for(int j=0;j<countLayers;j++) {
            		Layers.get(j).calculatingWeight();
            	}
            }
          /*  list_error.add(sumError);
            if(graphMaxY<sumError) {
            	graphMaxY=sumError;
            }
            if (list_error.size()>maxSizeList) {
            	list_error.remove(0);
            }*/
            System.out.println("SumError["+age+"]="+sumError+"  "+list_error.size());
            if(sumError<=Math.pow(10,-5)){
            	learningMode=false;
            	controlLearningPack();
            	testingOfOtherImage();
            }	
            age++;
            learningProcess=false;
        }
    }
    
    public void importImage() {
    	tes1=new double[hideenLayerSizeX*hideenLayerSizeY];
        try {
        	test[0] = ImageIO.read(new File(pathToFileForTest));
        } catch (IOException e) {System.out.println(pathToFileForTest+"   "+e);}
                
        for (int a=0;a<hideenLayerSizeY;a++){
        	for (int j=0;j<hideenLayerSizeX;j++){
        		Color c = new Color(test[0].getRGB(j,a));
        		tes1[a*hideenLayerSizeX+j]=getColorDot(c);
        		//if(imagepack[a*Hideenlayersize+j][i]>50){imagepack[a*Hideenlayersize+j][i]=255;}else{imagepack[a*Hideenlayersize+j][i]=0;}
        	}
        }
    }
    
    public void testingOfOtherImage() {
		for(int j=0;j<countLayers;j++) {
        	if(j==0) {	Layers.get(j).setLayerresult(tes1);}
        	Layers.get(j).summing();
        	if(j!=countLayers-1) {
        		Layers.get(j+1).setLayerresult(Layers.get(j).returnListResult());
        	}
        }
        double []res=Layers.get(Layers.size()-1).getResult();
        System.out.print("Result test:		");
        double max=0,maxj=0;
        for(int j=0;j<layerOutput;j++){
        	if (max<res[j]) {max=res[j];maxj=j;}
        }
        System.out.println(maxj+"("+max+")  ");
		
	}

	public void controlLearningPack() {
		sumError=0;
		int countTest=0;int truth=0;
		for(int i=0;i<sizeList;i++){
			
        	for(int j=0;j<countLayers;j++) {
        		if(j==0) {	Layers.get(j).setLayer(imagepack, hideenLayerSizeX*hideenLayerSizeY,i);}
        		
        		Layers.get(j).summing();
        		if(j!=countLayers-1) {
        			Layers.get(j+1).setLayerresult(Layers.get(j).returnListResult());
        		}
        	}
        	double res[]=Layers.get(Layers.size()-1).getResult();
        	double max=0;int maxj=0;
			for(int j=0;j<layerOutput;j++){
				if (max<res[j]) {max=res[j];maxj=j;}
			}
			for(int j=0;j<layerOutput;j++) {if(symbolTable[j][i]==1) {truth=j;}}
			if(truth==maxj) {countTest++;}else {System.out.print("("+maxj+"/"+truth+")");}
        }
		System.out.println("Test of learn pack ended.Image is recognize: "+countTest+"/"+sizeList);
		
		if(countTest==sizeList) {System.out.println("All right");}else {learningMode=true;}
	}

	public void paint(Graphics g) {
    	//super.paintComponents(g);
    	g.drawImage(fon,0,0, (ImageObserver) this);
    	g.drawLine((int)graphPositionX,(int)graphPositionY ,(int)graphPositionEdgeX ,(int) graphPositionY);
    	g.drawLine((int)graphPositionX,(int)graphPositionY ,(int)graphPositionX ,(int) graphPositionEdgeY);
    	g.drawLine((int)graphPositionEdgeX,(int)graphPositionY ,(int)graphPositionEdgeX ,(int) graphPositionEdgeY);
    	g.drawLine((int)graphPositionX,(int)graphPositionEdgeY ,(int)graphPositionEdgeX ,(int) graphPositionEdgeY);
    }
    
    public static int returnNumberSymbol(char symbol){
        switch(symbol){
            case '1':return 1;
            case '2':return 2;
            case '3':return 3;
            case '4':return 4;
            case '5':return 5;
            case '6':return 6;
            case '7':return 7;
            case '8':return 8;
            case '9':return 9;
            case '0':return 0;
        }
        return 10;
    }
    
    public static char returnNSymbol(int number){
        switch(number){
            case 1:return '1';
            case 2:return '2';
            case 3:return '3';
            case 4:return '4';
            case 5:return '5';
            case 6:return '6';
            case 7:return '7';
            case 8:return '8';
            case 9:return '9';
            case 0:return '0';
           
        }
        return '?';
    }
}
