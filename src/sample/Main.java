package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.io.File;
import java.util.Scanner;


public class Main extends Application {
    String path1;
    String path2;
    double finalPlagiarism;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Label heading1 = new Label("Upload 1st file: ");
        Button btn1 = new Button("File1!");
        Label heading2 = new Label("Upload 2nd file:");
        Button btn2 = new Button("File2!");
        Button b = new Button("Submit");

        GridPane root = new GridPane();

        root.setMinSize(400, 200);

        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(5);
        root.setHgap(5);
        root.setAlignment(Pos.CENTER);

        root.add(heading1, 0, 0);
        root.add(btn1, 1, 0);
        root.add(heading2, 0, 1);
        root.add(btn2, 1, 1);
        root.add(b, 0, 2);

        btn1.setOnAction(e->
        {
            FileChooser file = new FileChooser();
            file.setTitle("Upload File 1");
            //System.out.println(pic.getId());
            File file1 = file.showSaveDialog(primaryStage);

            this.path1 = file1.getAbsolutePath();
        });

        btn2.setOnAction(e->
        {
            FileChooser file = new FileChooser();
            file.setTitle("Upload File 2");
            //System.out.println(pic.getId());
            File file2 = file.showSaveDialog(primaryStage);

            this.path2 = file2.getAbsolutePath();
        });


        b.setOnAction(e->
        {
            this.finalPlagiarism = checkPlagiarism (path1, path2);

            Label result = new Label("Percentage of plagiarism = " + this.finalPlagiarism + " %");
            root.add(result, 0, 4);
        });



        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Plagiarism Checker by Madhav (16BCE2311)");
        primaryStage.show();
    }

    public static double checkPlagiarism (String path1, String path2) {
        try {

            // Read file 1
            File file1 = new File (path1);
            Scanner readFile1 = new Scanner (file1);

            String content1 = "";
            int count1 = 0;

            while (readFile1.hasNext()) {
                count1++;
                content1 += readFile1.nextLine() + "\n";
            }
            readFile1.close();

            // Read file 2
            File file2 = new File (path2);
            Scanner readFile2 = new Scanner (file2);

            String content2 = "";
            int count2 = 0;

            while (readFile2.hasNext()) {
                count2++;
                content2 += readFile2.nextLine() + "\n";
            }

            readFile2.close();

            // Convert content1 string into array of lines
            String[] firstFileLines = content1.split("\n");
            int numLines1 = firstFileLines.length;

            // Convert content2 string into array of lines
            String[] secondFileLines = content2.split("\n");
            int numLines2 = secondFileLines.length;

            // Find the percentage plagiarism
            int minFactor;
            int plagiarisedLines = 0;

            if (numLines1<numLines2) {
                minFactor = numLines1;

                for (int i=0; i<minFactor; i++) {
                    // Split each line into words
                    String[] lineOneWords = firstFileLines[i].split("[\\\\s@&.?$+-]+");

                    // Check this line with each of the lines in other content
                    for (int j=0; j<numLines2; j++) {
                        int currentPlagiarised = 0;
                        for (int k=0; k<lineOneWords.length; k++) {
                            if (secondFileLines[j].indexOf(lineOneWords[k]) > 0)
                                currentPlagiarised++;
                        }
                        if (currentPlagiarised > (lineOneWords.length)/3) {
                            plagiarisedLines++;
                            break;
                        }
                    }
                }
            } else {
                minFactor = numLines2;

                for (int i=0; i<minFactor; i++) {
                    // Split each line into words
                    String[] lineOneWords = secondFileLines[i].split("[\\\\s@&.?$+-]+");

                    // Check this line with each of the lines in other content
                    for (int j=0; j<numLines1; j++) {
                        int currentPlagiarised = 0;
                        for (int k=0; k<lineOneWords.length; k++) {
                            if (firstFileLines[j].indexOf(lineOneWords[k]) > 0)
                                currentPlagiarised++;
                        }
                        if (currentPlagiarised > (lineOneWords.length)/3) {
                            plagiarisedLines++;
                            break;
                        }
                    }
                }
            }

            System.out.println("minfactor: " + minFactor);
            System.out.println("num lines plagiarised = " + plagiarisedLines);

            double percentagePlagiarism = ((double)plagiarisedLines/minFactor) * 100;
            System.out.println("% plagiarism = " + percentagePlagiarism);
            return percentagePlagiarism;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public static void main(String[] args) {

        launch(args);

    }
}
