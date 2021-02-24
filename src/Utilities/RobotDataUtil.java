package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("FieldCanBeLocal")
public class RobotDataUtil {

    private String logName;
    private String filePath;
    private final String[] possiblePaths = {"robotLogs/",
            System.getProperty("user.home") + "/Downloads/rev_robotics-control_hub_v1_0-192.168.43.1_5555/sdcard/FIRST/robotLogs/",
            System.getProperty("user.home") + "/Downloads/rev_robotics-control_hub_v1_0-ftc.robot_5555/sdcard/FIRST/robotLogs/",
            System.getProperty("user.home") + "/Documents/AndroidStudio/DeviceExplorer/rev_robotics-control_hub_v1_0-192.168.43.1_5555/sdcard/FIRST/robotLogs/"};

    private ArrayList<DataPoint> dataArray;

    public RobotDataUtil(boolean getFromFile) {
        if (getFromFile) {
            try {
                logName = new BufferedReader(new FileReader("src/replay.txt")).readLine();
                filePath = getBasePath(logName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RobotDataUtil(String logName) {
        this.logName = logName;
        filePath = getBasePath(logName);
    }

    public String getBasePath(String logName) {
        for (String path : possiblePaths) {
            String fileName = path + logName + ".csv";
            if (new File(fileName).exists()) {
                return fileName;
            }
        }
        return "";
    }

    public void parseLogFile() {
        dataArray = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());

            for (int i = 0; i < lines.size(); i++) {
                if (i > 1) {
                    String[] data = lines.get(i).split(",");
                    dataArray.add(new DataPoint(Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]),
                            Double.parseDouble(data[4]), Double.parseDouble(data[5]), Double.parseDouble(data[6]),
                            Double.parseDouble(data[7]), Double.parseDouble(data[8]), Double.parseDouble(data[9]),
                            Double.parseDouble(data[10]), Integer.parseInt(data[11]), Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]), Integer.parseInt(data[14]), 15, 6.03));
                }
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataPoint getData(int index) {
        return dataArray.get(index);
    }

    public double getTimeDiff(int index) {
        if (index != 0) {
            DataPoint prev = dataArray.get(index - 1);
            DataPoint cur = dataArray.get(index);
            return cur.sinceStart - prev.sinceStart;
        } else {
            return 0;
        }
    }

    public int getNumOfPoints() {
        return dataArray.size();
    }

    public String getLogName() {
        return logName;
    }
}
