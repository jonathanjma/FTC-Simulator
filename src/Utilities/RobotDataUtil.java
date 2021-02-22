package Utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("FieldCanBeLocal")
public class RobotDataUtil {

    private String basePath = "robotLogs/";
    //private String basePath = "C:/Users/jonat/Downloads/rev_robotics-control_hub_v1_0-192.168.43.1_5555/sdcard/FIRST/robotLogs/";
    //private String basePath = "C:/Users/jonat/Downloads/rev_robotics-control_hub_v1_0-ftc.robot_5555/sdcard/FIRST/robotLogs/";

    private ArrayList<DataPoint> dataArray;

    public RobotDataUtil(String logName) {
        basePath += logName + ".csv";
    }

    public void parseLogFile() {
        dataArray = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(basePath));
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());

            for (int i = 0; i < lines.size(); i++) {
                if (i > 1) {
                    String[] data = lines.get(i).split(",");
                    dataArray.add(new DataPoint(Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]),
                            Double.parseDouble(data[4]), Double.parseDouble(data[5]), Double.parseDouble(data[6]),
                            Double.parseDouble(data[7]), Double.parseDouble(data[8]), Double.parseDouble(data[9]),
                            Double.parseDouble(data[10]), Integer.parseInt(data[11]), Boolean.parseBoolean(data[12]),
                            Boolean.parseBoolean(data[13]), Integer.parseInt(data[14])));
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

    public int getNumOfPoints() {return dataArray.size();}
}
