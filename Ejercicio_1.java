package T2file;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Ejercicio_1 {

    private static final String DIRECTORIO = "C:\\AD\\Ejercicios\\Tarea_2";
    private static final String FICHERO = DIRECTORIO + File.separator + "empleados_ap_1.txt";

    private static final int LONGITUD_APELLIDO = 10;
    private static final int TAM_ID = 4;
    private static final int TAM_APELLIDO = LONGITUD_APELLIDO * 2;
    private static final int TAM_DEPARTAMENTO = 4;
    private static final int TAM_SALARIO = 8;
    private static final int TAM_REGISTRO = TAM_ID + TAM_APELLIDO + TAM_DEPARTAMENTO + TAM_SALARIO; // 36

    public static void main(String[] args) {
        // Parametro opcional solo para probar en otra ruta (p. ej. en Linux)
        String ruta = args.length > 0 ? args[0] : FICHERO;

        String[] apellidos = {"GOMEZ", "RUIZ", "LOPEZ", "MARTIN"};
        int[] departamentos = {10, 20, 10, 30};
        double[] salarios = {1500.50, 1725.80, 2100.00, 1899.95};

        File fichero = new File(ruta);
        File carpeta = fichero.getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            System.out.println("No se pudo crear la carpeta: " + carpeta);
            return;
        }

        System.out.println("Fichero: " + fichero.getAbsolutePath());
        System.out.println("Estructura: id(4) + apellido(" + TAM_APELLIDO + ") + departamento(4) + salario(8) = "
                + TAM_REGISTRO + " bytes por registro\n");

        try (RandomAccessFile raf = new RandomAccessFile(fichero, "rw")) {

            raf.setLength(0);

            // ---------- ESCRITURA ----------
            System.out.println("--- Escritura ---");
            boolean escrituraOk = true;
            for (int i = 0; i < apellidos.length; i++) {
                int id = i + 1; // el id coincide con indice + 1 y es > 0
                if (id <= 0) {
                    throw new IllegalArgumentException("El identificador debe ser mayor que cero");
                }

                long inicio = raf.getFilePointer();

                raf.writeInt(id);
                escribirCadenaFija(raf, apellidos[i], LONGITUD_APELLIDO);
                raf.writeInt(departamentos[i]);
                raf.writeDouble(salarios[i]);

                long escritos = raf.getFilePointer() - inicio;
                boolean ok = escritos == TAM_REGISTRO && inicio == (long) i * TAM_REGISTRO;
                escrituraOk &= ok;
                System.out.println("Registro " + id + ": posicion " + inicio + ", " + escritos + " bytes "
                        + (ok ? "[OK]" : "[ERROR]"));
            }

            long esperado = (long) apellidos.length * TAM_REGISTRO;
            boolean tamanoOk = raf.length() == esperado;
            System.out.println("Tamano del fichero: " + raf.length() + " bytes (esperado " + apellidos.length
                    + " x " + TAM_REGISTRO + " = " + esperado + ") " + (tamanoOk ? "[OK]" : "[ERROR]"));

            // ---------- LECTURA Y VERIFICACION ----------
            System.out.println("\n--- Lectura secuencial y verificacion ---");
            raf.seek(0);
            int n = 0;
            boolean lecturaOk = true;
            while (raf.getFilePointer() < raf.length()) {
                long inicio = raf.getFilePointer();

                int id = raf.readInt();
                String apellido = leerCadenaFija(raf, LONGITUD_APELLIDO);
                int departamento = raf.readInt();
                double salario = raf.readDouble();

                long leidos = raf.getFilePointer() - inicio;

                boolean ok = id > 0
                        && id == n + 1
                        && leidos == TAM_REGISTRO
                        && apellido.equals(apellidos[n])
                        && departamento == departamentos[n]
                        && salario == salarios[n];
                lecturaOk &= ok;

                System.out.println("ID: " + id
                        + " | Apellido: " + apellido
                        + " | Departamento: " + departamento
                        + " | Salario: " + String.format("%.2f", salario)
                        + " | " + leidos + " bytes " + (ok ? "[OK]" : "[ERROR]"));
                n++;
            }

            System.out.println("\n--- Resultado de las comprobaciones ---");
            System.out.println("Registros leidos = registros escritos (" + n + "): " + (n == apellidos.length ? "OK" : "ERROR"));
            System.out.println("Todos los registros ocupan " + TAM_REGISTRO + " bytes: " + (escrituraOk ? "OK" : "ERROR"));
            System.out.println("Tamano total correcto: " + (tamanoOk ? "OK" : "ERROR"));
            System.out.println("Id > 0 e igual a indice + 1, y datos leidos = datos escritos: " + (lecturaOk ? "OK" : "ERROR"));

        } catch (IOException e) {
            System.out.println("Error de E/S: " + e.getMessage());
        }
    }

    /** Escribe exactamente 'longitud' caracteres: rellena con espacios o recorta. */
    private static void escribirCadenaFija(RandomAccessFile raf, String texto, int longitud) throws IOException {
        String fijo = String.format("%-" + longitud + "." + longitud + "s", texto);
        raf.writeChars(fijo); // longitud * 2 bytes
    }

    /** Lee 'longitud' caracteres y elimina el relleno. */
    private static String leerCadenaFija(RandomAccessFile raf, int longitud) throws IOException {
        char[] c = new char[longitud];
        for (int i = 0; i < longitud; i++) {
            c[i] = raf.readChar();
        }
        return new String(c).trim();
    }
}