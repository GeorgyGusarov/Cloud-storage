package java_nio_examples;

import java.nio.file.*;

public class PathTest {
    public static void main(String[] args) {
        Path path = Paths.get("1", "2", "1.txt");
        System.out.println(path);
        System.out.println(path.startsWith(Paths.get("1", "2")));
        System.out.println(Files.exists(path));
        System.out.println(path.getFileName());
        System.out.println(path.getFileSystem());
        System.out.println(path.getParent());
        System.out.println(path.getParent().getParent());
        System.out.println(path.getParent().getParent().getParent());



    }
}
