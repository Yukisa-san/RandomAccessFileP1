package T2file;
import java.io. IOException;
import java.io.RandomAccessFile;

public class Ejercicio_4 {

    private static final String FICHERO = "nombres.bin";

    public static void main(String[] args) {
        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {

            raf.setLength(0);

            raf.writeUTF("Elisa");
            raf.writeUTF("Luna");
            raf.writeUTF("Pamela");

            raf.seek(0);

            System.out.println(raf.readUTF());
            System.out.println(raf.readUTF());
            System.out.println(raf.readUTF());

        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());

        }
    }
}