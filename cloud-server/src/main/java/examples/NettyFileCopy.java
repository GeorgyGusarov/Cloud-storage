package examples;

import java.io.*;

public class NettyFileCopy {
    public static void main(String[] args) {
        File file = new File("C:\\Programming\\Cloud storage\\cloud-server\\src\\main\\java\\examples\\dir1\\pepe.jpg");
        String name = file.getName();

        File fileCopy = new File("C:\\Programming\\Cloud storage\\cloud-server\\src\\main\\java\\examples\\dir2\\pepe_copy.jpg");

        try (InputStream in = new BufferedInputStream(new FileInputStream(file));
             OutputStream out = new BufferedOutputStream(new FileOutputStream(fileCopy))) {
            while (in.available() > 0) {
                out.write(in.read());
            }
            /**
             * более длинный, но быстрый пример. Скорость передачи выше.
             */
//            byte[] buffer = new byte[8192];
//            while (in.available() > 0) {
//                int read = in.read(buffer); // прочитано
//                if (read == 8192) {
//                    out.write(buffer);
//                } else {
//                    byte[] tail = new byte[read];
//                    if (read >= 0) System.arraycopy(buffer, 0, tail, 0, read);
//                    out.write(tail);
//                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("origin: " + file.length() + " bytes");
        System.out.println("copy: " + fileCopy.length() + " bytes");
    }
}
