package lab4;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Maker {

    public static void makeSource(String fileName, String data) {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("D:\\Studying\\Методы трансляции\\Mt4_lab2\\Mt3_lab4-2\\Mt3\\src\\main\\java\\gen\\" +fileName + ".java"))) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
