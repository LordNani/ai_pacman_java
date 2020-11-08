package main;

import ai.Point;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MapParser {

    static ArrayList<Point> getMapFromFile(String mapName) {
        ArrayList<Point> map = new ArrayList<>();
        try {
            File file = new File("maps/" + mapName);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                String[] coords = data.split(",", 2);
                map.add(new Point(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return map;
    }
}
