package T2file;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Ejercicio_2 {

    private static final String FICHERO = "C:\\AD\\Ejercicios\\Tarea_2\\nums.bin";

    public static void main(String[] args) {
        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw");
             Scanner sc = new Scanner(System.in)) {

            raf.setLength(0);

            System.out.println("Introduce 5 numeros enteros:");
            for (int i = 0; i < 5; i++) {
                System.out.print("Numero " + (i + 1) + ": ");
                int numero = sc.nextInt();
                raf.writeInt(numero);
            }

            System.out.println("getFilePointer() = " + raf.getFilePointer());

            raf.seek(0);

            System.out.println("Lectura de los 5 numeros guardados:");
            for (int i = 0; i < 5; i++) {
                System.out.println(raf.readInt());
            }

        } catch (IOException e) {
            System.out.println("Error de fichero: " + e.getMessage());
        }
    }
}