
import lombok.Getter;

@Getter
public class DeleteRequest extends AbstractMessage {
    private String fileName;

    public DeleteRequest(String fileName) {
        this.fileName = fileName;
    }
}
