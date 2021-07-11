import lombok.Getter;

@Getter
public class DownloadRequest extends AbstractMessage {
    private String fileName;

    public DownloadRequest(String fileName) {
        this.fileName = fileName;
    }
}
