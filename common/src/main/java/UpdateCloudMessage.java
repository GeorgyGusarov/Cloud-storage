import lombok.Getter;

import java.util.ArrayList;

@Getter
public class UpdateCloudMessage extends AbstractMessage {
    private ArrayList<String> cloudFileList;

    UpdateCloudMessage() {

    }

    public UpdateCloudMessage(ArrayList<String> serverList) {
        this.cloudFileList = serverList;
    }
}
