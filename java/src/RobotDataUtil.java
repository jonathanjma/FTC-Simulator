import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class RobotDataUtil {

    private File robotDataLog = new File("java/src/Logs/RobotData8.csv");
    private File accelLog = new File("java/src/Logs/Accel.csv");
    private FileWriter fileWriter;
    private BufferedReader bufferedReader;

    private ArrayList dataArray;
    private int getCounter = 0;
    private boolean logAccel;

    public RobotDataUtil(boolean logAccel) {this.logAccel = logAccel;}

    public void parseLogFile() {
        String curLine;
        int lineNum = 0;
        dataArray = new ArrayList<>();
        double prevXV = 0, prevYV = 0, prevThetaV = 0, prevTime = 0;

        try {
            bufferedReader = new BufferedReader(new FileReader(robotDataLog));

            if (logAccel) {
                fileWriter = new FileWriter(accelLog);
                fileWriter.write("AccelX,AccelY,AccelTheta\n");
            }

            while ((curLine = bufferedReader.readLine()) != null) {
                if (lineNum != 0) {
                    String[] data = curLine.split(",");

                    double time = Double.parseDouble(data[1]);
                    double velocityX = Double.parseDouble(data[5]), velocityY = Double.parseDouble(data[6]), velocityTheta = Double.parseDouble(data[7]);
                    double accelX = (prevXV - velocityX) / ((prevTime - time) / 1000);
                    double accelY = (prevYV - velocityY) / ((prevTime - time) / 1000);
                    double accelTheta = (prevThetaV - velocityTheta) / ((prevTime - time) / 1000);

                    dataArray.add(new Object[]{time, Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]), velocityX, velocityY, velocityTheta, accelX, accelY, accelTheta, Boolean.parseBoolean(data[8]), Boolean.parseBoolean(data[9]), Boolean.parseBoolean(data[10]), Boolean.parseBoolean(data[11]), Boolean.parseBoolean(data[12])});

                    if (logAccel) {
                        fileWriter.write(accelX + "," + accelY + "," + accelTheta + "\n");
                        prevXV = velocityX; prevYV = velocityY; prevThetaV = velocityTheta; prevTime = time;
                    }
                }
                lineNum++;
            }
            bufferedReader.close();
            if (logAccel) fileWriter.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public Object[] getNextPos() {
        Object[] data = (Object[]) dataArray.get(Math.min(getCounter, dataArray.size() - 1));
        getCounter++;
        return data;
    }

    public void setGetCounter(int getCounter) {this.getCounter = getCounter;}

    public int getNumOfPoints() {return dataArray.size();}
}
