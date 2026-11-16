package T2file;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Ejercicio_3 {

    private static final String FICHERO = "slots.bin";
    private static final int TAM_SLOT = 4; // un int ocupa 4 bytes

    public static void main(String[] args) {
        int[] numeros = {11, 22, 33, 44, 55};

        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {
            raf.setLength(0);

            // Cada numero va en su propio slot: slot i empieza en i * 4
            for (int i = 0; i < numeros.length; i++) {
                long offset = i * (long) TAM_SLOT;
                raf.seek(offset);
                raf.writeInt(numeros[i]);
            }

            // Lectura directa del segundo valor (slot 2 -> posicion (2 - 1) * 4 = 4)
            int slot = 2;
            raf.seek((slot - 1) * (long) TAM_SLOT);
            int segundoValor = raf.readInt();

            System.out.println("Segundo valor leido directamente: " + segundoValor);
            System.out.println("Posicion = (slot - 1) * 4 = (" + slot + " - 1) * 4 = " + (slot - 1) * TAM_SLOT);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}