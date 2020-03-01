package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("FieldCanBeLocal")
public class RobotDataUtil {

    private String basePath = "src/Logs/";
    //private String basePath ="C:/Users/jonat/Downloads/motorola-moto_e__4_-192.168.49.1_7303/sdcard/FIRST/robotLogs/";
    private BufferedReader bufferedReader;

    private ArrayList<Object[]> dataArray;

    public RobotDataUtil(String logName) {
        basePath += logName + ".csv";
    }

    public void parseLogFile() {
        String curLine;
        int lineNum = 0;
        dataArray = new ArrayList<>();

        try {
            File robotDataLog = new File(basePath);
            bufferedReader = new BufferedReader(new FileReader(robotDataLog));

            while ((curLine = bufferedReader.readLine()) != null) {
                if (lineNum != 0) {
                    String[] data = curLine.split(",");
                    dataArray.add(new Object[]{Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Double.parseDouble(data[4]), Double.parseDouble(data[5]), Double.parseDouble(data[6]), Double.parseDouble(data[7]), Double.parseDouble(data[8]), Double.parseDouble(data[9]), Double.parseDouble(data[10]), Boolean.parseBoolean(data[11]), Boolean.parseBoolean(data[12]), Boolean.parseBoolean(data[13]), Boolean.parseBoolean(data[14]), Boolean.parseBoolean(data[15]), Boolean.parseBoolean(data[16])});
                }
                lineNum++;
            }
            bufferedReader.close();
        } catch (IOException e) {e.printStackTrace();}
    }

    public Object[] getData(int index) {
        return dataArray.get(index);
    }

    public double getTimeDiff(int index) {
        if (index != 0) {
            Object[] prev = dataArray.get(index - 1);
            Object[] cur = dataArray.get(index);
            return (double) cur[0] - (double) prev[0];
        } else {
            return 0;
        }
    }

    public int getNumOfPoints() {return dataArray.size();}
}
