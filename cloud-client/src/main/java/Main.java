import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("C:\\Programming\\Cloud storage\\cloud-client\\src\\main\\resources\\client.fxml"));
        stage.setTitle("Cloud storage");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }
}
