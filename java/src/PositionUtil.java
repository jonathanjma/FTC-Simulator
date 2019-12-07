import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class PositionUtil {

    private File robotPositionLog = new File("java/src/PositionLogs/log.txt");
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;

    private ArrayList<double[]> posArray;
    private int getCounter = 0;

    public void startLogging() {
        try {fileWriter = new FileWriter(robotPositionLog);} catch (Exception e) {e.printStackTrace();}
    }

    public void stopLogging() {
        try {fileWriter.close();} catch (Exception e) {e.printStackTrace();}
    }

    public void writePos(double x, double y, double theta) {
        try {
            fileWriter.write(x + "\n");
            fileWriter.write(y + "\n");
            fileWriter.write(theta + "\n");
        } catch (Exception e) {e.printStackTrace();}
    }

    public void parsePosFile() {
        String curLine;
        double[] robotPos = new double[3];
        posArray = new ArrayList<>();

        int counter = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader(robotPositionLog));
            while ((curLine = bufferedReader.readLine()) != null) {
                robotPos[counter] = Double.parseDouble(curLine);
                if (counter == 2) {
                    posArray.add(new double[] {robotPos[0], robotPos[1], robotPos[2]});
                }
                counter = (counter + 1) % 3;
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

    public void setGetCounter(int getCounter) {
        this.getCounter = getCounter;
    }

    public int getNumOfPoints() {return posArray.size();}
}
