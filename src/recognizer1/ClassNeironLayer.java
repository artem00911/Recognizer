package recognizer1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ClassNeironLayer {
    double xInput[], weight[][],xOutput[],yResult[],neironOffset[];
    int sizeX,sizeY,sizeXoutput,sizeYoutput,symbolTable[][];
    double error[],sigma[], dWeight[][];
    
    public ClassNeironLayer(int sizex1,int sizexoutput1){
        this.sizeX=sizex1;
        this.sizeXoutput=sizexoutput1;
        xInput=new double[sizeX];
        weight=new double[sizeX][sizeXoutput];
        neironOffset=new double[sizeXoutput];
        dWeight=new double[sizeX][sizeXoutput];
        Random rand=new Random();
        for(int jout=0;jout<sizeXoutput;jout++){
            for(int j=0;j<sizeX;j++){
                weight[j][jout]=rand.nextDouble()+0.01;
                //System.out.println(weight[j][jout]+" "+j+"  "+jout);
                dWeight[j][jout]=0;
            }
            neironOffset[jout]=rand.nextDouble();
        }
        xOutput=new double[sizeXoutput];
        yResult=new double[sizeXoutput];
        error=new double[sizeXoutput];
        sigma=new double[sizeX];
    }
    
    public void setLayer(double [][]Layer,int x,int n){for(int j=0;j<x;j++){xInput[j]=Layer[j][n];}}
    
    public void setLayerresult(double []Layer){xInput=Layer;    }
    
    public void setLayerresult(List<Double>Layer){
    	for(int i=0;i<Layer.size();i++){
    		xInput[i]=Layer.get(i);
    	}
    }
    
    public void setSymbolTable(int [][]x){symbolTable=x;}
           
    public void summing(){
    	for(int jout=0;jout<sizeXoutput;jout++){
        	xOutput[jout]=0;
            for(int j=0;j<sizeX;j++){
                xOutput[jout]+=xInput[j]*weight[j][jout];
            }
            xOutput[jout]+=neironOffset[jout];
            yResult[jout]=functionActivation(xOutput[jout]);
        }
    }
    
    public double[]getResult(){return yResult;}
    
    public List<Double>returnListResult(){
    	List<Double> listresult=new ArrayList<>();
    	for(int i=0;i<sizeXoutput;i++) {
    		listresult.add(yResult[i]);
    	}
    	return listresult;
    }
    
    public static double  functionActivation(double x){  return (double)1/(1+Math.exp(-x));}
    
    public static double difFunction(double x){ return functionActivation(x)*(1-functionActivation(x));}
    
    public double calculationOutputError(int num){
        double sum=0;
        for(int i=0;i<sizeXoutput;i++){
            error[i]=(symbolTable[i][num]-yResult[i])*difFunction(xOutput[i]);
            sum+=error[i];
        }
        for(int b=0;b<sizeX;b++){
            for(int j=0;j<sizeXoutput;j++){
                dWeight[b][j]+=error[j]*xInput[b]*speed();
            }
        }
        return sum;
    }
    
    public double[] calculationSigma() {
        for(int b=0;b<sizeX;b++){
            sigma[b]=0;
            for(int j=0;j<sizeXoutput;j++){
                sigma[b]+=error[j]*weight[b][j];
            }
            sigma[b]*=difFunction(xInput[b]);
        }
        return sigma;
    }
    
    public void calculationDifWeight(){
        for(int b=0;b<sizeX;b++){
            for(int j=0;j<sizeXoutput;j++){
                dWeight[b][j]+=error[j]*xInput[b]*speed();
            }
        }
    }
    
    public void calculatingWeight(){
        for(int j=0;j<sizeXoutput;j++){
            for(int b=0;b<sizeX;b++){        
                weight[b][j]+=dWeight[b][j];
                dWeight[b][j]=0;
            }
        }
        for(int j=0;j<sizeXoutput;j++){
            neironOffset[j]+=error[j]*speed();
        }
    }
    
    public double[] getSigma(){return sigma;}
    
    public void setError(double sigma1[]){ error=sigma1;}
    
    public static double speed(){   return 25;}
}
