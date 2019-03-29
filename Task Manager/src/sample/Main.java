package sample;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Lockheed Martin");
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Queue<String> queue = allProcesses();

        Stage     accountCreation = buildAccountCreationStage(primaryStage, queue);
        Hyperlink createAccount   = buildCreateAccountLink(primaryStage, accountCreation);


        TextFlow flow = new TextFlow(
                new Text("All Processes"), createAccount
        );
        flow.setPadding(new Insets(10));


        Scene scene = new Scene(flow, 200, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);

    }

    public static Queue<String> allProcesses() {
        Queue<String> q = new LinkedList<>();

        try {
            String line;
            Process p = Runtime.getRuntime().exec
                    (System.getenv("windir") +"\\system32\\"+"tasklist.exe");
            BufferedReader input =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line); //<-- Parse data here.
                q.add(line);
            }
            input.close();
        } catch (Exception err) {
            err.printStackTrace();
        }

        return q;
    }

    private Hyperlink buildCreateAccountLink(Stage primaryStage, Stage accountCreation) {
        Hyperlink createAccount = new Hyperlink("Click here");

        createAccount.setOnAction(event -> {
            accountCreation.setX(primaryStage.getX());
            accountCreation.setY(primaryStage.getY() + primaryStage.getHeight());
            accountCreation.show();
        });

        return createAccount;
    }

    private Stage buildAccountCreationStage(Stage primaryStage, Queue<String> queue) {
        Stage accountCreation = new Stage(StageStyle.UTILITY);

        StackPane root = new StackPane();

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(5));
        gridpane.setHgap(10);
        gridpane.setVgap(10);

        final TextArea cssEditorFld = new TextArea();
        cssEditorFld.setPrefRowCount(10);
        cssEditorFld.setPrefColumnCount(100);
        cssEditorFld.setWrapText(true);
        cssEditorFld.setPrefWidth(150);
        GridPane.setHalignment(cssEditorFld, HPos.CENTER);
        gridpane.add(cssEditorFld, 0, 1);

        String string = "";
        while (queue.isEmpty() == false){
            string += queue.remove() + "\n";
        }

        cssEditorFld.setText(string);
        root.getChildren().add(cssEditorFld);

        accountCreation.initModality(Modality.WINDOW_MODAL);
        accountCreation.initOwner(primaryStage);
        accountCreation.setTitle("Process");
        accountCreation.setScene(new Scene(root, 500, 500));

        return accountCreation;
    }
}
