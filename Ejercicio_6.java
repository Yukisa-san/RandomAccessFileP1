package T2file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Ejercicio_6 {

    private static final String FICHERO = "personas.dat";
    private static final int LONGITUD_NOMBRE = 20;
    private static final int TAM_REGISTRO = 48;   // 4 (id) + 40 (nombre) + 4 (edad)
    private static final int OFFSET_EDAD = 44;    // 4 + 40

    public static void main(String[] args) {
        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw");
             Scanner sc = new Scanner(System.in)) {

            // Si el fichero esta vacio, se escriben 3 registros de ejemplo
            if (raf.length() == 0) {
                escribirRegistro(raf, 1, "Alicia", 29);
                escribirRegistro(raf, 2, "Lucia", 36);
                escribirRegistro(raf, 3, "Juan", 23);
            }

            System.out.print("Introduce un id existente: ");
            int idBuscado = sc.nextInt();

            System.out.print("Introduce la nueva edad: ");
            int nuevaEdad = sc.nextInt();

            // Busqueda secuencial: si el id no coincide, se salta al siguiente registro
            raf.seek(0);
            boolean encontrado = false;
            long regInicio = 0;

            while (raf.getFilePointer() < raf.length()) {
                regInicio = raf.getFilePointer();
                int id = raf.readInt();

                if (id == idBuscado) {
                    encontrado = true;
                    break;
                }
                raf.seek(regInicio + TAM_REGISTRO);
            }

            if (!encontrado) {
                System.out.println("No se ha encontrado ningun registro con ese id.");
                return;
            }

            // Se actualiza solo la edad, sin tocar id ni nombre
            raf.seek(regInicio + OFFSET_EDAD);
            raf.writeInt(nuevaEdad);

            // Se vuelve al inicio del registro para mostrarlo completo
            raf.seek(regInicio);
            int id = raf.readInt();
            String nombre = leerCadenaFija(raf, LONGITUD_NOMBRE);
            int edad = raf.readInt();

            System.out.println("Registro actualizado (inicio = " + regInicio + "):");
            System.out.println("ID: " + id + " | Nombre: " + nombre + " | Edad: " + edad);

        } catch (IOException e) {
            System.out.println("Error de fichero: " + e.getMessage());
        }
    }

    private static void escribirRegistro(RandomAccessFile raf, int id, String nombre, int edad)
            throws IOException {
        raf.writeInt(id);

        StringBuilder sb = new StringBuilder(nombre);
        sb.setLength(LONGITUD_NOMBRE);   // recorta o rellena hasta 20 caracteres
        for (int i = 0; i < LONGITUD_NOMBRE; i++) {
            char c = sb.charAt(i);
            raf.writeChar(c == '\0' ? ' ' : c);
        }

        raf.writeInt(edad);
    }

    private static String leerCadenaFija(RandomAccessFile raf, int longitud) throws IOException {
        char[] caracteres = new char[longitud];
        for (int i = 0; i < longitud; i++) {
            caracteres[i] = raf.readChar();
        }
        return new String(caracteres).trim();
    }
}