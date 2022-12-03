package model;

import view.ClassBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Class to deal with file handling - saving and loading
 * Save - saves the class source data in encoded format
 *
 */
public class FileHandler {

    /**
     * Load - loads the encoded class source data and decodes in blackboard
     * @param loadFile file to be loaded
     */
    public void loadFile(File loadFile) {
        ClassSource classSource = ClassSource.getInstance();
        classSource.clearSource();

        try (BufferedReader br = new BufferedReader(new FileReader(loadFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens[0].equals("0")){
                    ClassBox currBox = new ClassBox(tokens[1], Integer.parseInt(tokens[2]),
                            Integer.parseInt(tokens[3]));
                    classSource.addClassBox(currBox);
                } else if (tokens[0].equals("1")){
                    ClassBox fromClass = null;
                    ClassBox toClass = null;
                    for (ClassBox classbox : classSource.getClassBoxes()) {
                        if (classbox.getClassName().equals(tokens[2])) {
                            fromClass = classbox;
                        }
                        if (classbox.getClassName().equals(tokens[3])) {
                            toClass = classbox;
                        }
                    }

                    if (fromClass == null || toClass == null){
                        System.out.println("from class or to class is " +
                                "null");
                        return;
                    }

                    LoadHandlerArrow arrow = new LoadHandlerArrow();
                    LoadHandlerDiamond diamond = new LoadHandlerDiamond();
                    LoadHandlerTriangle triangle =
                            new LoadHandlerTriangle();

                    arrow.setSuccessor(triangle);
                    triangle.setSuccessor(diamond);
                    arrow.loadConnection(tokens, fromClass, toClass);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
