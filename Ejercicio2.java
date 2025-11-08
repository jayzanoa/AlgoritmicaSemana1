import java.io.*;
import java.util.*;

public class Ejercicio2 {
    public static void main(String[] args) {

        //====================CREAMOS EL VECTOR ====================

        int N = 100;
        double[] V = new double[N];
        Random random = new Random();

        double min = 1500.00;
        double max = 9500.00;
        double intervalo = 500.00;
        int cantidadValores = (int) ((max - min) / intervalo) + 1;

        
        for (int k = 0; k < N; k++) {
            int indice = random.nextInt(cantidadValores);
            double valorAleatorio = min + (indice * intervalo);
            V[k] = valorAleatorio;
        }

       //========== CREAMOS EL archivo.dat "Salarios" con los datos del vector ==========


        try (DataOutputStream archivo = new DataOutputStream(new FileOutputStream("Salarios.dat"))) {
            for (int k = 0; k < N; k++) {
                archivo.writeDouble(V[k]); 
            }
            System.out.println("Archivo 'Salarios.dat' creado correctamente con " + N + " valores.");
        } catch (IOException e) {
            System.out.println("Error al crear el archivo: " + e.getMessage());
        }
    

       //=============Lectura del archivo binario .dat====================

        String archivoDatos = "Salarios.dat";
        String archivoHTML = "salarios.html";

        try (RandomAccessFile file = new RandomAccessFile(archivoDatos, "r")) {
            long numRegistros = file.length() / 8; 

            double[] salarios = new double[(int) numRegistros];
            int[] frecuencias = new int[(int) numRegistros];
            int numSalarios = 0; 

           
            for (int i = 0; i < numRegistros; i++) {
                double salario = file.readDouble();

              
                int indice = -1;
                for (int j = 0; j < numSalarios; j++) {
                    if (salarios[j] == salario) {
                        indice = j;
                        break;
                    }
                }

                if (indice != -1) {
                  
                    frecuencias[indice]++;
                } else {
                   
                    salarios[numSalarios] = salario;
                    frecuencias[numSalarios] = 1;
                    numSalarios++;
                }
            }


            for (int i = 0; i < numSalarios - 1; i++) {
                for (int j = i + 1; j < numSalarios; j++) {
                    if (salarios[i] > salarios[j]) {
                        double tempS = salarios[i];
                        salarios[i] = salarios[j];
                        salarios[j] = tempS;

                        int tempF = frecuencias[i];
                        frecuencias[i] = frecuencias[j];
                        frecuencias[j] = tempF;
                    }
                }
            }

            int total = 0;
            for (int i = 0; i < numSalarios; i++) {
                total += frecuencias[i];
            }
            
            //====ARMAMOS EL ARCHIVO .html====

            try (PrintWriter writer = new PrintWriter(new FileWriter(archivoHTML))) {
                writer.println("<html>");
                writer.println("<head>");
                writer.println("<title>Distribución de Salarios</title>");
                writer.println("<style>");
                writer.println("body { font-family: Arial; text-align: center; }");
                writer.println("table { margin: 0 auto; border-collapse: collapse; width: 300px; }");
                writer.println("th, td { border: 1px solid #333; padding: 8px; text-align: center; }");
                writer.println("th { background-color: #d0e0ff; }");
                writer.println("td { background-color: #e8f0ff; }");
                writer.println("tfoot td { background-color: #ccc; font-weight: bold; }");
                writer.println("</style>");
                writer.println("</head>");
                writer.println("<body>");
                writer.println("<h2>Distribución de Salarios</h2>");
                writer.println("<table>");
                writer.println("<tr><th>SALARIO</th><th>FRECUENCIA</th></tr>");

                for (int i = 0; i < numSalarios; i++) {
                    writer.printf("<tr><td>%.2f</td><td>%d</td></tr>%n", salarios[i], frecuencias[i]);
                }

                writer.printf("<tr><td><b>TOTAL</b></td><td><b>%d</b></td></tr>", total);
                writer.println("</table>");
                writer.println("</body>");
                writer.println("</html>");
            }

            System.out.println("Archivo '" + archivoHTML + "' generado correctamente.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}