package examples;

import java.io.*;
import java.net.Socket;

public class FileHandler implements Runnable {

    private Socket socket;
    private EasyServer server;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isRunning = true;
    /**
     * COMMAND - ждем команду. Состояние нулевое.
     * UPLOAD - первая команда.
     * DOWNLOAD - вторая команда.
     */
    private final int COMMAND = 0, UPLOAD = 1, DOWNLOAD = 2;
    private int currentOption;
    private final String serverPath = "C:\\Programming\\Cloud storage\\cloud-server\\src\\main\\resources";

    public void stop() {
        isRunning = false;
    }

    public FileHandler(EasyServer server, Socket socket) throws IOException {
        this.socket = socket;
        this.server = server;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        currentOption = COMMAND;
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                if (currentOption == COMMAND) {
                    String command = in.readUTF(); // прочитали команду
                    if (command.equals("/upload")) {
                        currentOption = UPLOAD;
                    }
                    if (command.equals("/download")) {
                        currentOption = DOWNLOAD;
                    }
                }
                if (currentOption == DOWNLOAD) {
                    String fileName = in.readUTF(); // получили имя файла от клиента
                    File file = new File(serverPath + fileName);
                    if (!file.exists()) {
                        out.writeInt(-1);
                        out.flush();
                        currentOption = COMMAND;
                    } else {
                        long length = file.length(); // шлем на клиент length
                        out.writeLong(length); // записали
                        out.flush();
                        FileInputStream fis = new FileInputStream(file); // затем засылаем байты
                        byte[] buffer = new byte[30_000];
                        while (fis.available() > 0) { // пока есть данные
                            int read = fis.read(buffer);
                            out.write(buffer, 0, read);
                            out.flush();
                        }
                        currentOption = COMMAND;
                    }
                }
                if (currentOption == UPLOAD) {
                    // загружаем, создаем файл на сервере
                    // ждем от клиента имя файла, длинну, кол-во байтов
                    String fileName = in.readUTF(); // получили имя файла
                    long fileLength = in.readLong();
                    File file = new File(serverPath + fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    } else {
                        throw new RuntimeException("File already exist on server!");
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[30_000];
                    for (int i = 0; i < fileLength / 30_000; i++) {
                        int read = in.read(buffer);
                        fos.write(buffer, 0 , read); // записываем данные от нуля до того, сколько прочли
                    }
                    currentOption = COMMAND;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
