package T2file;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Ejercicio_7 {

    private static final String FICHERO_DATOS = "personas.dat";
    private static final String FICHERO_INDICE = "personas.idx";
    private static final int LONGITUD_NOMBRE = 20;
    private static final int TAM_REGISTRO = 48;   // personas.dat: 4 + 40 + 4
    private static final int TAM_ENTRADA = 12;    // personas.idx: id (4) + offset (8)

    public static void main(String[] args) {
        try (RandomAccessFile datos = new RandomAccessFile(FICHERO_DATOS, "rw");
             RandomAccessFile indice = new RandomAccessFile(FICHERO_INDICE, "rw");
             Scanner sc = new Scanner(System.in)) {

            if (datos.length() == 0) {
                escribirRegistro(datos, 1, "ANA", 23);
                escribirRegistro(datos, 2, "LUIS", 31);
                escribirRegistro(datos, 3, "MARTA", 27);
            }

            construirIndice(datos, indice);

            System.out.print("Introduce el id a buscar: ");
            int idBuscado = sc.nextInt();

            Long offsetEncontrado = buscarOffsetPorId(indice, idBuscado);

            if (offsetEncontrado == null) {
                System.out.println("No existe ese id en el indice.");
                return;
            }

            // Salto directo al registro usando el offset del indice
            datos.seek(offsetEncontrado);

            int id = datos.readInt();
            String nombre = leerCadenaFija(datos, LONGITUD_NOMBRE);
            int edad = datos.readInt();

            System.out.println("Registro encontrado usando el indice:");
            System.out.println("ID: " + id + " | Nombre: " + nombre + " | Edad: " + edad);
            System.out.println("Offset utilizado: " + offsetEncontrado);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void construirIndice(RandomAccessFile datos, RandomAccessFile indice)
            throws IOException {
        indice.setLength(0);
        long totalRegistros = datos.length() / TAM_REGISTRO;

        for (int i = 0; i < totalRegistros; i++) {
            datos.seek(i * (long) TAM_REGISTRO);   // el registro i empieza en i * 48
            int id = datos.readInt();

            indice.writeInt(id);
            indice.writeLong(i * 48L);
        }
        System.out.println("Indice creado: " + totalRegistros + " entradas, "
                + indice.length() + " bytes (" + TAM_ENTRADA + " por entrada)");
    }

    // Busqueda lineal en el indice; devuelve null si el id no existe
    private static Long buscarOffsetPorId(RandomAccessFile indice, int idBuscado)
            throws IOException {
        indice.seek(0);
        while (indice.getFilePointer() < indice.length()) {
            int id = indice.readInt();
            long offset = indice.readLong();
            if (id == idBuscado) {
                return offset;
            }
        }
        return null;
    }

    private static void escribirRegistro(RandomAccessFile raf, int id, String nombre, int edad)
            throws IOException {
        raf.writeInt(id);

        StringBuilder sb = new StringBuilder(nombre);
        sb.setLength(LONGITUD_NOMBRE);
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