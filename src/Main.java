import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Main extends Application {

    private Stage window;
    private Scene startScene, mainScene;
    private TextField inDirT, outDir1T, outDir2T, outDir3T, outDir4T;
    private Button buttonOut1, buttonOut2, buttonOut3, buttonOut4;

    private ImageView img;

    private File lastFile;

    private ArrayList<File> images;
    private Iterator<File> it;

    private static final String[] extensions = {".jpg", ".jpeg", ".png"};
    private File inputDir, Output1, Output2, Output3, Output4, completed;
    private int numOutDirs = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        primaryStage.setTitle("ImageSorter");

        inDirT = new TextField();
        Label inDirL = new Label("Input Directory");
        HBox inDirHBox = new HBox();
        inDirHBox.getChildren().addAll(inDirL, inDirT);
        inDirL.setPadding(new Insets(0, 35, 0, 0));

        outDir1T = new TextField();
        Label outDir1L = new Label("Output Directory 1");
        HBox outDir1HBox = new HBox();
        outDir1HBox.getChildren().addAll(outDir1L, outDir1T);
        outDir1L.setPadding(new Insets(0, 10, 0, 0));

        outDir2T = new TextField();
        Label outDir2L = new Label("Output Directory 2");
        HBox outDir2HBox = new HBox();
        outDir2HBox.getChildren().addAll(outDir2L, outDir2T);
        outDir2L.setPadding(new Insets(0, 10, 0, 0));

        outDir3T = new TextField();
        Label outDir3L = new Label("Output Directory 3");
        HBox outDir3HBox = new HBox();
        outDir3HBox.getChildren().addAll(outDir3L, outDir3T);
        outDir3L.setPadding(new Insets(0, 10, 0, 0));

        outDir4T = new TextField();
        Label outDir4L = new Label("Output Directory 4");
        HBox outDir4HBox = new HBox();
        outDir4HBox.getChildren().addAll(outDir4L, outDir4T);
        outDir4L.setPadding(new Insets(0, 10, 0, 0));

        Button submit = new Button("Begin Sorting");
        submit.setAlignment(Pos.CENTER);
        submit.setOnAction(e -> beginSorting());

        VBox root = new VBox();
        root.getChildren().addAll(inDirHBox, outDir1HBox, outDir2HBox, outDir3HBox, outDir4HBox, submit);
        root.setSpacing(10);
        root.setPadding(new Insets(20, 20, 20, 20));
        startScene = new Scene(root, 400, 300);
        startScene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.ENTER) {
                submit.fire();
            }
        });


        img = new ImageView();
        img.setPreserveRatio(true);
        img.setFitHeight(1080);
        img.setFitWidth(1920);

        buttonOut1 = new Button();
        buttonOut1.setPrefSize(200, 20);
        buttonOut1.setOnAction(event -> sendToOutput(1));

        buttonOut2 = new Button();
        buttonOut2.setPrefSize(200, 20);
        buttonOut2.setOnAction(event -> sendToOutput(2));

        buttonOut3 = new Button();
        buttonOut3.setPrefSize(200, 20);
        buttonOut3.setOnAction(event -> sendToOutput(3));

        buttonOut4 = new Button();
        buttonOut4.setPrefSize(200, 20);
        buttonOut4.setOnAction(event -> sendToOutput(4));

        Button view = new Button("View image in explorer");
        view.setOnAction(event -> {
            try {
                Runtime.getRuntime().exec("explorer.exe /select, " + lastFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        HBox bHBox = new HBox();
        bHBox.getChildren().addAll(view);
        bHBox.setAlignment(Pos.CENTER);

        HBox outButtonsHBox = new HBox();
        outButtonsHBox.getChildren().addAll(buttonOut1, buttonOut2, buttonOut3, buttonOut4);
        outButtonsHBox.setAlignment(Pos.CENTER);

        VBox mainRoot = new VBox();
        mainRoot.getChildren().addAll(img, outButtonsHBox, bHBox);

        mainScene = new Scene(mainRoot, 1920, 1200);
        mainScene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.DIGIT1) {
                buttonOut1.fire();
            }
            if (key.getCode() == KeyCode.DIGIT2) {
                buttonOut2.fire();
            }
            if (key.getCode() == KeyCode.DIGIT3) {
                buttonOut3.fire();
            }
            if (key.getCode() == KeyCode.DIGIT4) {
                buttonOut4.fire();
            }
            if (key.getCode() == KeyCode.ENTER) {
                view.fire();
            }
        });


        window.setScene(startScene);
        window.show();
    }

    private void sendToOutput(int outputDir) {
        try {
            switch (outputDir) {
                case 1: {
                    Files.copy(lastFile.toPath(), new File(Output1.getPath() + File.separator + lastFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                }
                case 2: {
                    Files.copy(lastFile.toPath(), new File(Output2.getPath() + File.separator + lastFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                }
                case 3: {
                    Files.copy(lastFile.toPath(), new File(Output3.getPath() + File.separator + lastFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                }
                case 4: {
                    Files.copy(lastFile.toPath(), new File(Output4.getPath() + File.separator + lastFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    break;
                }
            }
            Files.move(lastFile.toPath(), new File(completed.getPath() + File.separator + lastFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);

            if (it.hasNext()) {
                lastFile = it.next();
                img.setImage(new Image(lastFile.toURI().toString()));
            } else {
                System.out.println("Done");
                System.exit(0);
            }
        } catch (IOException ex) {
            System.out.println("Exception " + ex.getMessage());
        }
    }

    private void beginSorting() {
        submitTextFields();

        if (numOutDirs >= 2) {
            buttonOut1.setText(Output1.getName());
            buttonOut2.setText(Output2.getName());

            buttonOut3.setDisable(numOutDirs < 3);
            if (numOutDirs >= 3)
                buttonOut3.setText(Output3.getName());

            buttonOut4.setDisable(numOutDirs < 4);
            if (numOutDirs >= 4)
                buttonOut4.setText(Output4.getName());

            images = new ArrayList<>();
            getImages(images, inputDir);
            it = images.iterator();
            if (it.hasNext())
                lastFile = it.next();
            else {
                System.out.println("Input dir is empty");
                System.exit(0);
            }

            img.setImage(new Image(lastFile.toURI().toString()));

            window.setScene(mainScene);
        }
    }

    private void submitTextFields() {
        numOutDirs = 0;
        String inText = inDirT.getText();
        inText = inText.replace('\\', '/');
        File in = new File(inText);
        if (in.isDirectory()) {
            inputDir = in;
            System.out.println("Input directory is " + in.getAbsolutePath());

            completed = new File(in.getPath() + "_completed");
            completed.mkdir();
            System.out.println("Completed directory is " + completed.getAbsolutePath());

        } else return;

        inText = outDir1T.getText();
        inText = inText.replace('\\', '/');
        File o1 = new File(inText);
        if (o1.isDirectory()) {
            Output1 = o1;
            System.out.println("Output1 directory is " + o1.getAbsolutePath());
            numOutDirs++;
        } else return;

        inText = outDir2T.getText();
        inText = inText.replace('\\', '/');
        File o2 = new File(inText);
        if (o2.isDirectory()) {
            Output2 = o2;
            System.out.println("Output2 directory is " + o2.getAbsolutePath());
            numOutDirs++;
        } else return;

        inText = outDir3T.getText();
        inText = inText.replace('\\', '/');
        File o3 = new File(inText);
        if (o3.isDirectory()) {
            Output3 = o3;
            System.out.println("Output3 directory is " + o3.getAbsolutePath());
            numOutDirs++;
        } else return;

        inText = outDir4T.getText();
        inText = inText.replace('\\', '/');
        File o4 = new File(inText);
        if (o4.isDirectory()) {
            Output4 = o4;
            System.out.println("Output4 directory is " + o4.getAbsolutePath());
            numOutDirs++;
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void getImages(ArrayList<File> out, File dir) {
        if (!dir.isDirectory()) {
            System.out.println("The path " + dir.getAbsolutePath() + " is not a valid directory!");
        } else {
            out.addAll(Arrays.asList(Objects.requireNonNull(dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(final File fi, final String name) {
                    if (fi != null) {
                        boolean result = false;
                        for (String ext : extensions)
                            result = result || name.toLowerCase().endsWith(ext);
                        return result;
                    } else {
                        return false;
                    }
                }
            }))));
        }
    }
}
