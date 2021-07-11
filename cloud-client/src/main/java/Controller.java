import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private String localStoragePath = "C:\\Programming\\Cloud storage\\cloud-client\\local_storage";

    @FXML
    ListView<String> localFilesList;

    @FXML
    ListView<String> cloudFilesList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Network.start();
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    AbstractMessage absMsg = Network.readObject();
                    if (absMsg instanceof FileMessage) {
                        FileMessage fileMsg = (FileMessage) absMsg;
                        Files.write(Paths.get(localStoragePath + fileMsg.getFileName()), fileMsg.getData(), StandardOpenOption.CREATE);
                        updateLocalFilesList();
                    }
                    if (absMsg instanceof UpdateCloudMessage) {
                        UpdateCloudMessage updateCloudMessage = (UpdateCloudMessage) absMsg;
                        updateCloudFilesList(updateCloudMessage.getCloudFileList());
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.close();
            }
        });
        t.setDaemon(true);
        t.start();
        updateLocalFilesList();
        Network.sendMsg(new UpdateCloudMessage());
    }

    private void updateLocalFilesList() {
        updateUI(() -> {
            try {
                localFilesList.getItems().clear();
                Files.list(Paths.get(localStoragePath))
                        .map(path -> path.getFileName()
                        .toString())
                        .forEach(o -> localFilesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateCloudFilesList(ArrayList<String> filesList) {
        updateUI(() -> {
            cloudFilesList.getItems().clear();
            cloudFilesList.getItems().addAll(filesList);
        });
    }

    private static void updateUI(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    public void buttonUpload(ActionEvent actionEvent) {
        try {
            Network.sendMsg(new FileMessage(Paths.get(localStoragePath + localFilesList.getSelectionModel().getSelectedItems())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void buttonDownload(ActionEvent actionEvent) {
        Network.sendMsg(new DownloadRequest(cloudFilesList.getSelectionModel().getSelectedItem()));
    }

    public void buttonDeleteFromCloud(ActionEvent actionEvent) {
        Network.sendMsg(new DeleteRequest(cloudFilesList.getSelectionModel().getSelectedItem()));
    }

    public void buttonDeleteFromLocalStorage(ActionEvent actionEvent) {
        try {
            Files.delete(Paths.get(localStoragePath + localFilesList.getSelectionModel().getSelectedItems()));
            updateLocalFilesList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
