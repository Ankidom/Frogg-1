package nl.saxion.ptbc.frog_pilot;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObstaclesCords {
    static ArrayList<ObstaclesCords> list = new ArrayList<>();
    private int breadthX;
    private int heightY;
    private int depthZ;

    public ObstaclesCords(int breadthX, int heightY, int depthZ) {
        this.breadthX = breadthX;
        this.heightY = heightY;
        this.depthZ = depthZ;
    }

    public int getBreadthX() {
        return breadthX;
    }

    public void setBreadthX(int breadthX) {
        this.breadthX = breadthX;
    }

    public int getHeightY() {
        return heightY;
    }

    public void setHeightY(int heightY) {
        this.heightY = heightY;
    }

    public int getDepthZ() {
        return depthZ;
    }

    public void setDepthZ(int depthZ) {
        this.depthZ = depthZ;
    }
    public static ArrayList<ObstaclesCords> getCords(String message){
        Pattern pattern = Pattern.compile("-?\\d+,\\d+");
        Matcher matcher = pattern.matcher(message);
        int breadthX = 0, heightY = 0, depthZ = 0;
        int i = 0;
        while (matcher.find()) {
            String s = matcher.group().replace(',', '.');
            float value = Float.parseFloat(s);
            int intValue = (int) value;

            if (i % 3 == 0) {
                breadthX = intValue;
            } else if (i % 3 == 1) {
                heightY = intValue;
            } else {
                depthZ = intValue;
                list.add(new ObstaclesCords(breadthX, heightY, depthZ));
            }
            i++;
        }
        return list;
    }
}
