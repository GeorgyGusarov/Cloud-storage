package examples;

import java.io.IOException;
import java.nio.file.*;

public class NIOPathTest {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("PathTest.txt");

        if (!Files.exists(Paths.get("PathTest_Dir"))) Files.createDirectory(Paths.get("PathTest_Dir"));
        Files.write(path, new byte[]{65, 66, 67}, StandardOpenOption.CREATE);
        Files.copy(path, Paths.get("PathTest_Dir", "PathTest_copy.txt"), StandardCopyOption.REPLACE_EXISTING);

        System.out.println(path + "\n");
        System.out.println(path.startsWith(Paths.get("1", "2")) + "\n");
        System.out.println(Files.exists(path) + "\n");
        System.out.println(path.getFileName() + "\n");
        System.out.println(path.getFileSystem() + "\n");
        System.out.println(path.resolve(Paths.get("PathTest_Dir", "PathTest_copy.txt")) + "\n");

        Path absPath = path.toAbsolutePath();
        while (absPath.getParent() != null) {
            System.out.println(absPath);
            absPath = absPath.getParent();
        }
        System.out.println();

        // Path итерируемый, поэтому можно через foreach
        Path p = Paths.get("PathTest_Dir");
        for (Path tmp : p) {
            System.out.println(tmp);
        }
        System.out.println();
    }
}
