package examples;

import java.io.IOException;
import java.nio.file.*;

public class NIOFilesTest {
    public static void main(String[] args) throws IOException {
        if (!Files.exists(Paths.get("FileTest.txt"))) Files.createFile(Paths.get("FileTest.txt"));

        //Files.list(Paths.get("./")).forEach(element -> System.out.println(element));
        Files.list(Paths.get("./")).forEach(System.out::println);


    }
}
