import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("FieldCanBeLocal")
public class PositionUtil {

    private File robotPositionLog = new File("java/src/Logs/RobotPosition.csv");
    private BufferedReader bufferedReader;

    private ArrayList<double[]> posArray;
    private int getCounter = 0;

    public PositionUtil(){}

    public void parseLogFile() {
        String curLine;
        int lineNum = 0;
        posArray = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new FileReader(robotPositionLog));
            while ((curLine = bufferedReader.readLine()) != null) {
                if (lineNum != 0) {
                    String[] data = curLine.split(","); //System.out.println(Arrays.toString(data));
                    posArray.add(new double[]{Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4])});
                    //System.out.println(Arrays.toString(new double[]{Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4])}));
                }
                lineNum++;
            }
            bufferedReader.close();
            //for (double[] data : posArray) {for (double d: data) {System.out.println(d);}}
        } catch (IOException e) {e.printStackTrace();}
    }

    public double[] getNextPos() {
        double[] posData = posArray.get(Math.min(getCounter, posArray.size() - 1));
        getCounter++;
        return posData;
    }

    public void setGetCounter(int getCounter) {this.getCounter = getCounter;}

    public int getNumOfPoints() {return posArray.size();}
}
