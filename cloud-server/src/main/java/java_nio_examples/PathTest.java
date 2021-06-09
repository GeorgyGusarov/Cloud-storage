package java_nio_examples;

import java.io.IOException;
import java.nio.file.*;

public class PathTest {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("PathTest.txt");

        if (!Files.exists(Paths.get("PathTest_Dir"))) Files.createDirectory(Paths.get("PathTest_Dir"));
        Files.write(path, new byte[]{65, 66, 67}, StandardOpenOption.CREATE);
        Files.copy(path, Paths.get("PathTest_Dir", "PathTest_copy.txt"), StandardCopyOption.REPLACE_EXISTING);

        System.out.println(path);
        System.out.println(path.startsWith(Paths.get("1", "2")));
        System.out.println(Files.exists(path));
        System.out.println(path.getFileName());
        System.out.println(path.getFileSystem());
        System.out.println(path.resolve(Paths.get("PathTest_Dir", "PathTest_copy.txt")));

        Path absPath = path.toAbsolutePath();
        while (absPath.getParent() != null) {
            System.out.println(absPath);
            absPath = absPath.getParent();
        }
    }
}
