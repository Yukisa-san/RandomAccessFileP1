package T2file;
import java.io. IOException;
import java.io. RandomAccessFile;

public class Ejercicio_5 {

    private static final String FICHERO = "registros_fijos.dat";
    private static final int LONGITUD_NOMBRE = 10;
    private static final int TAM_REGISTRO = 4 + (LONGITUD_NOMBRE * 2);

    public static void main(String[] args) {
        try (RandomAccessFile raf = new RandomAccessFile(FICHERO, "rw")) {

            raf.setLength(0);

            escribirRegistro(raf, 1, "ANA");
            escribirRegistro(raf, 2, "LUIS");
            escribirRegistro(raf, 3, "MARTA");

            long offsetTercerRegistro = 2L * TAM_REGISTRO;
            raf.seek(offsetTercerRegistro);

            int id = raf.readInt();
            String nombre = leerCadenaFija(raf, LONGITUD_NOMBRE);

            System.out.println("ID: " + id + " | Nombre: " + nombre);

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());

        }
    }

private static void escribirRegistro(RandomAccessFile raf, int id, String nombre) throws IOException {
            raf.writeInt(id);
            StringBuilder sb = new StringBuilder(nombre);
            if (sb.length() > LONGITUD_NOMBRE) {
                sb.setLength(LONGITUD_NOMBRE);
            } else {
                while (sb.length() < LONGITUD_NOMBRE) {
                    sb.append(' ');
                }
            }

                    for (int i = 0; i < LONGITUD_NOMBRE; i++) {
                        raf.writeChar(sb.charAt(i));

                    }
                }

                    private static String leerCadenaFija(RandomAccessFile raf, int longitud) throws
                    IOException {
                        char[] caracteres = new char[longitud];
                        for (int i = 0; i < longitud; i++) {
                            caracteres[i] = raf.readChar();
                        }
                            return new String(caracteres).trim();
                        }
                    }