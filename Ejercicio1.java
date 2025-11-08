import java.io.*;
import java.util.*;

public class Ejercicio1 {

    public static void main(String[] args) {
        int M = 10; // filas
        int N = 15; // columnas
        double[][] matriz = generarMatrizAleatoria(M, N);
        generarHTML(matriz, "Tabla_matriz_estadisticas.html");
    }

    //Usamos este codigo para generar los valores aleatorios de 3 digitos de 100 a 999
    public static void loadMatrixRandom(double[][] MTX, double AAA, double BBB) {
        int M = MTX.length;
        int N = MTX[0].length;
        for(int i = 0; i < M; i++) {
            for(int j = 0; j < N; j++) {
                double X = AAA + Math.random() * (BBB - AAA + 1);
                MTX[i][j] = Math.floor(X);
            }
        }
    }

    // Generamos una matriz de MxN con valores aleatorios de 3 dígitos (100-999)
    public static double[][] generarMatrizAleatoria(int M, int N) {
        double[][] matriz = new double[M][N];
        loadMatrixRandom(matriz,100, 999); // Valores entre 100 y 999 (3 dígitos)
        return matriz;
    }

    // Calculamos la suma de una fila/columna
    public static double suma(double[] valores) {
        double total = 0;
        for (double v : valores) total += v;
        return Math.round(total * 100.0) / 100.0;
    }

    // Calculamos el promedio
    public static double promedio(double[] valores) {
        double prom = suma(valores) / valores.length;
        return Math.round(prom * 100.0) / 100.0;
    }

    // Calculamos la mediana
    public static double mediana(double[] valores) {
        double[] copia = Arrays.copyOf(valores, valores.length);
        Arrays.sort(copia);
        int n = copia.length;
        double med;
        if (n % 2 == 0) {
            med = (copia[n/2 - 1] + copia[n/2]) / 2.0;
        } else {
            med = copia[n/2];
        }
        return Math.round(med * 100.0) / 100.0;
    }

    // Calculamoa el  mínimo
    public static double minimo(double[] valores) {
        double min = valores[0];
        for (double v : valores) if (v < min) min = v;
        return Math.round(min * 100.0) / 100.0;
    }

    // Calculamos el  máximo
    public static double maximo(double[] valores) {
        double max = valores[0];
        for (double v : valores) if (v > max) max = v;
        return Math.round(max * 100.0) / 100.0;
    }

    // Generamos la tabla HTML
    public static void generarHTML(double[][] matriz, String nombreArchivo) {
        int M = matriz.length;
        int N = matriz[0].length;

        try (PrintWriter pw = new PrintWriter(new FileWriter(nombreArchivo))) {
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Estadísticas de Matriz</title>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println("<style>");
            pw.println("body"); 
            pw.println("table { border-collapse: collapse; width: 100%; font-family: Arial, sans-serif; margin: 20px auto; }");
            pw.println("th, td { border: 1px solid black; padding: 6px; text-align: center; font-size: 12px; }");
            pw.println("th { background-color: #f2f2f2; font-weight: bold; }");
            pw.println(".celda-amarilla-pastel { background-color: #FFFACD; }"); // Amarillo pastel
            pw.println(".celda-verde-claro { background-color: #90EE90; }"); // Verde claro
            pw.println(".container { background-color: white; padding: 20px; margin: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }");
            pw.println("h2 { text-align: center; color: #333; }");
            pw.println("</style>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<div class=\"container\">");
            pw.println("<h2>Estadísticas de Matriz</h2>");
            pw.println("<table>");

            // Generamos los encabezados de las columnas
            pw.print("<tr><th></th>");
            for (int j = 1; j <= N; j++) {
                pw.printf("<th>%d</th>", j);
            }
            pw.println("<th>Suma</th><th>Promedio</th><th>Mediana</th><th>Mínimo</th><th>Máximo</th></tr>");

            // Ponemos las filas de datos con valores aleatorios de 3 dígitos
            for (int i = 0; i < M; i++) {
                pw.printf("<tr><th>%d</th>", i + 1);
                // Ponemos las columnas del 1 al 15 con fondo amarillo pastel y datos aleatorios
                for (int j = 0; j < N; j++) {
                    pw.printf("<td class=\"celda-amarilla-pastel\">%.2f</td>", matriz[i][j]);
                }
                // Para las columnas de estadísticas (suma hasta máximo) con color verde claro
                double sumaFila = suma(matriz[i]);
                double promedioFila = promedio(matriz[i]);
                double medianaFila = mediana(matriz[i]);
                double minFila = minimo(matriz[i]);
                double maxFila = maximo(matriz[i]);
                pw.printf("<td class=\"celda-verde-claro\">%.2f</td>", sumaFila);
                pw.printf("<td class=\"celda-verde-claro\">%.2f</td>", promedioFila);
                pw.printf("<td class=\"celda-verde-claro\">%.2f</td>", medianaFila);
                pw.printf("<td class=\"celda-verde-claro\">%.2f</td>", minFila);
                pw.printf("<td class=\"celda-verde-claro\">%.2f</td></tr>%n", maxFila);
            }

            // Estadísticas por columna (filas suma hasta máximo) con verde claro
            String[] estadisticasColumna = {"Suma", "Promedio", "Mediana", "Mínimo", "Máximo"};
            
            for (String estadistica : estadisticasColumna) {
                pw.printf("<tr><th class=\"celda-verde-claro\">%s</th>", estadistica);
                for (int j = 0; j < N; j++) {
                    double[] columna = new double[M];
                    for (int i = 0; i < M; i++) {
                        columna[i] = matriz[i][j];
                    }
                    double valor = 0;
                    switch (estadistica) {
                        case "Suma": valor = suma(columna); break;
                        case "Promedio": valor = promedio(columna); break;
                        case "Mediana": valor = mediana(columna); break;
                        case "Mínimo": valor = minimo(columna); break;
                        case "Máximo": valor = maximo(columna); break;
                    }
                    pw.printf("<td class=\"celda-verde-claro\"><b>%.2f</b></td>", valor);
                }
                // Celdas vacías para estadísticas de columnas (verde claro)
                pw.println("<td class=\"celda-verde-claro\"></td><td class=\"celda-verde-claro\"></td><td class=\"celda-verde-claro\"></td><td class=\"celda-verde-claro\"></td><td class=\"celda-verde-claro\"></td></tr>");
            }

            pw.println("</table>");
            pw.println("</div>");
            pw.println("</body>");
            pw.println("</html>");

            System.out.println("Archivo HTML generado: " + nombreArchivo);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}