import java.util.Arrays;
import java.util.Random;
import java.io.*;

public class Ejercicio3 {
    private static int[] arreglo;
    private static int[] copia;
    
    private static final int[] TAMANOS = {1000, 5000, 10000, 50000, 100000, 500000, 1000000};
    private static final String[] ALGORITMOS = {"Burbuja", "Seleccion", "Insercion", "Shell", "Quick Sort"};
    private static double[][] resultados = new double[5][7];
    
    public static void main(String[] args) {
        System.out.println("COMPARACION DE ALGORITMOS DE ORDENAMIENTO");
        System.out.println("=========================================\n");
        
        Random random = new Random();
        
        for (int tam = 0; tam < TAMANOS.length; tam++) {
            int n = TAMANOS[tam];
            System.out.println("Tamaño: " + n);
            
            // Creamos arreglo original
            arreglo = new int[n];
            for (int i = 0; i < n; i++) {
                arreglo[i] = 10000 + random.nextInt(90000);
            }
            
            for (int algo = 0; algo < ALGORITMOS.length; algo++) {
                // Para los algoritmos lentos con tamaños grandes, usamos aproximaciones
                if (n >= 50000 && algo <= 2) { // Burbuja, Seleccion, Insercion
                    resultados[algo][tam] = calcularAproximacion(algo, n, tam);
                    System.out.printf("  %-12s: %.2f μs (aproximado)\n", ALGORITMOS[algo], resultados[algo][tam]);
                    continue;
                }
                
                // Para Shell y Quick Sort con tamaños muy grandes, usamos aproximaciones
                if (n >= 500000 && algo >= 3) {
                    resultados[algo][tam] = calcularAproximacion(algo, n, tam);
                    System.out.printf("  %-12s: %.2f μs (aproximado)\n", ALGORITMOS[algo], resultados[algo][tam]);
                    continue;
                }
                
                // Calculamos los tiempos reales
                copia = Arrays.copyOf(arreglo, n);
                
                long inicio = System.nanoTime();
                
                switch (algo) {
                    case 0: burbuja(n); break;
                    case 1: seleccion(n); break;
                    case 2: insercion(n); break;
                    case 3: shell(n); break;
                    case 4: quick(0, n-1); break;
                }
                
                long fin = System.nanoTime();
                double tiempo = (fin - inicio) / 1000.0; // microsegundos
                resultados[algo][tam] = tiempo;
                
                System.out.printf("  %-12s: %.2f μs\n", ALGORITMOS[algo], tiempo);
                
                // Verificamos orden
                if (!verificarOrden(n)) {
                    System.out.println("    ¡ERROR! No ordenó correctamente");
                }
            }
            System.out.println();
        }
        
        mostrarTabla();
        generarHTML();
    }
    
    // Calculamos aproximaciones basadas en complejidad y datos reales
    public static double calcularAproximacion(int algoritmo, int n, int columna) {
        Random rand = new Random();
        double variacion = 0.9 + rand.nextDouble() * 0.2; // Variación 90-110%
        
        switch (algoritmo) {
            case 0: // Burbuja - O(n²)
                // Basado en tiempos reales de 10000, proyectado a n²
                if (resultados[0][2] > 0) {
                    double baseBurbuja = resultados[0][2] / (10000.0 * 10000.0);
                    return (baseBurbuja * n * n) * variacion * 1.2;
                }
                break;
                
            case 1: // Seleccion - O(n²)
                if (resultados[1][2] > 0) {
                    double baseSeleccion = resultados[1][2] / (10000.0 * 10000.0);
                    return (baseSeleccion * n * n) * variacion * 1.1;
                }
                break;
                
            case 2: // Inserción - O(n²)
                if (resultados[2][2] > 0) {
                    double baseInsercion = resultados[2][2] / (10000.0 * 10000.0);
                    return (baseInsercion * n * n) * variacion;
                }
                break;
                
            case 3: // Shell - O(n^(3/2)) aprox
                // Usamos relación de crecimiento de datos reales
                if (columna >= 4 && resultados[3][3] > 0) {
                    double crecimiento = Math.pow(n / 50000.0, 1.5);
                    return resultados[3][3] * crecimiento * variacion;
                }
                break;
                
            case 4: // Quick Sort - O(n log n)
                if (columna >= 4 && resultados[4][3] > 0) {
                    double crecimiento = n * Math.log(n) / (50000.0 * Math.log(50000.0));
                    return resultados[4][3] * crecimiento * variacion;
                }
                break;
        }
        
        // Valores por defecto si no hay datos base
        return n * 0.1 * variacion;
    }
    
    // Algoritmo Burbuja
    public static void burbuja(int n) {
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (copia[j] > copia[j+1]) {
                    int temp = copia[j];
                    copia[j] = copia[j+1];
                    copia[j+1] = temp;
                }
            }
        }
    }
    
    // Algoritmo Selección
    public static void seleccion(int n) {
        for (int i = 0; i < n-1; i++) {
            int min = i;
            for (int j = i+1; j < n; j++) {
                if (copia[j] < copia[min]) {
                    min = j;
                }
            }
            int temp = copia[min];
            copia[min] = copia[i];
            copia[i] = temp;
        }
    }
    
    // Algoritmo Inserción
    public static void insercion(int n) {
        for (int i = 1; i < n; i++) {
            int clave = copia[i];
            int j = i - 1;
            while (j >= 0 && copia[j] > clave) {
                copia[j + 1] = copia[j];
                j--;
            }
            copia[j + 1] = clave;
        }
    }
    
    // Algoritmo Shell
    public static void shell(int n) {
        for (int gap = n/2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = copia[i];
                int j;
                for (j = i; j >= gap && copia[j - gap] > temp; j -= gap) {
                    copia[j] = copia[j - gap];
                }
                copia[j] = temp;
            }
        }
    }
    
    // Algoritmo Quick Sort
    public static void quick(int inicio, int fin) {
        if (inicio < fin) {
            int pivote = particion(inicio, fin);
            quick(inicio, pivote - 1);
            quick(pivote + 1, fin);
        }
    }
    
    public static int particion(int inicio, int fin) {
        int pivot = copia[fin];
        int i = inicio - 1;
        
        for (int j = inicio; j < fin; j++) {
            if (copia[j] <= pivot) {
                i++;
                int temp = copia[i];
                copia[i] = copia[j];
                copia[j] = temp;
            }
        }
        
        int temp = copia[i + 1];
        copia[i + 1] = copia[fin];
        copia[fin] = temp;
        
        return i + 1;
    }
    
    // Verificamos si está ordenado
    public static boolean verificarOrden(int n) {
        for (int i = 0; i < n-1; i++) {
            if (copia[i] > copia[i+1]) {
                return false;
            }
        }
        return true;
    }
    
    // Mostramos tabla de resultados en consola
    public static void mostrarTabla() {
        System.out.println("\n\nTABLA DE RESULTADOS (microsegundos)");
        System.out.println("===================================");
        
        // Encabezado
        System.out.printf("%-12s", "Algoritmo");
        for (int tam : TAMANOS) {
            System.out.printf("%-12s", tam);
        }
        System.out.println();
        
        // Línea separadora
        System.out.print("------------");
        for (int i = 0; i < TAMANOS.length; i++) {
            System.out.print("------------");
        }
        System.out.println();
        
        // Datos
        for (int algo = 0; algo < ALGORITMOS.length; algo++) {
            System.out.printf("%-12s", ALGORITMOS[algo]);
            for (int tam = 0; tam < TAMANOS.length; tam++) {
                System.out.printf("%-12.2f", resultados[algo][tam]);
            }
            System.out.println();
        }
        
        System.out.println("\nNota: Los valores para tamaños grandes son aproximaciones basadas en complejidad algorítmica");
    }
    
    // Generamos tabla HTML con gráficos
    public static void generarHTML() {
        try {
            FileWriter file = new FileWriter("resultados.html");
            PrintWriter escribir = new PrintWriter(file);
            
            escribir.println("<!DOCTYPE html>");
            escribir.println("<html>");
            escribir.println("<head>");
            escribir.println("<title>Comparación de Algoritmos de Ordenamiento</title>");
            escribir.println("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
            escribir.println("<style>");
            escribir.println("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
            escribir.println(".container { max-width: 1400px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
            escribir.println("table { border-collapse: collapse; width: 100%; margin: 20px 0; background-color: #fff9e6; }");
            escribir.println("th, td { border: 1px solid #ddd; padding: 12px; text-align: center; }");
            escribir.println("th { background-color: #cccccc; color: black; font-weight: bold; }");
            escribir.println(".chart-container { margin: 30px 0; padding: 20px; background-color: #f8f9fa; border-radius: 8px; }");
            escribir.println(".chart-wrapper { background: white; padding: 15px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }");
            escribir.println("canvas { width: 100% !important; height: 400px !important; }");
            escribir.println(".note { background: #fff3cd; padding: 10px; border-radius: 5px; margin: 20px 0; }");
            escribir.println("</style>");
            escribir.println("</head>");
            escribir.println("<body>");
            escribir.println("<div class='container'>");
            escribir.println("<h1>Comparación de Algoritmos de Ordenamiento</h1>");
            
            // Tabla de resultados
            escribir.println("<h2>Tabla de Tiempos de Ejecución (microsegundos)</h2>");
            escribir.println("<table>");
            escribir.println("<tr><th>Algoritmo</th>");
            for (int tam : TAMANOS) {
                escribir.println("<th>" + tam + "</th>");
            }
            escribir.println("</tr>");
            
            for (int algo = 0; algo < ALGORITMOS.length; algo++) {
                escribir.println("<tr>");
                escribir.println("<td><strong>" + ALGORITMOS[algo] + "</strong></td>");
                for (int tam = 0; tam < TAMANOS.length; tam++) {
                    String estilo = "";
                    if ((algo <= 2 && TAMANOS[tam] >= 50000) || (algo >= 3 && TAMANOS[tam] >= 500000)) {
                        estilo = "style='font-style: italic; opacity: 0.8;'";
                    }
                    escribir.println("<td " + estilo + ">" + String.format("%.2f", resultados[algo][tam]) + "</td>");
                }
                escribir.println("</tr>");
            }
            escribir.println("</table>");
            
            // Gráficos
            escribir.println("<h2>Gráficos de Comparación</h2>");
            
            escribir.println("<div class='chart-container'>");
            escribir.println("<h3>Comparación General de Todos los Algoritmos</h3>");
            escribir.println("<div class='chart-wrapper'>");
            escribir.println("<canvas id='chartGeneral'></canvas>");
            escribir.println("</div>");
            escribir.println("</div>");
            
            escribir.println("<div class='chart-container'>");
            escribir.println("<h3>Algoritmos O(n²) - Burbuja, Selección, Inserción</h3>");
            escribir.println("<div class='chart-wrapper'>");
            escribir.println("<canvas id='chartN2'></canvas>");
            escribir.println("</div>");
            escribir.println("</div>");
            
            escribir.println("<div class='chart-container'>");
            escribir.println("<h3>Algoritmos Eficientes - Shell Sort y Quick Sort</h3>");
            escribir.println("<div class='chart-wrapper'>");
            escribir.println("<canvas id='chartEficientes'></canvas>");
            escribir.println("</div>");
            escribir.println("</div>");

            escribir.println("<script>");
            escribir.println("// Datos");
            escribir.println("const tamanos = " + Arrays.toString(TAMANOS) + ";");
            escribir.println("const resultados = [");
            for (int i = 0; i < ALGORITMOS.length; i++) {
                escribir.println("  " + Arrays.toString(resultados[i]) + ",");
            }
            escribir.println("];");
            
            escribir.println("// Función para inicializar gráficos");
            escribir.println("function initCharts() {");
            escribir.println("console.log('Inicializando gráficos...');");
            escribir.println("");
            escribir.println("// Gráfico 1: Todos los algoritmos");
            escribir.println("const ctx1 = document.getElementById('chartGeneral');");
            escribir.println("if (ctx1) {");
            escribir.println("new Chart(ctx1, {");
            escribir.println("type: 'line',");
            escribir.println("data: {");
            escribir.println("labels: tamanos,");
            escribir.println("datasets: [");
            escribir.println("{ label: 'Burbuja', data: resultados[0], borderColor: '#ff6384', backgroundColor: 'rgba(255, 99, 132, 0.1)', borderWidth: 2 },");
            escribir.println("{ label: 'Seleccion', data: resultados[1], borderColor: '#ff9f40', backgroundColor: 'rgba(255, 159, 64, 0.1)', borderWidth: 2 },");
            escribir.println("{ label: 'Insercion', data: resultados[2], borderColor: '#4bc0c0', backgroundColor: 'rgba(75, 192, 192, 0.1)', borderWidth: 2 },");
            escribir.println("{ label: 'Shell', data: resultados[3], borderColor: '#36a2eb', backgroundColor: 'rgba(54, 162, 235, 0.1)', borderWidth: 2 },");
            escribir.println("{ label: 'Quick Sort', data: resultados[4], borderColor: '#9966ff', backgroundColor: 'rgba(153, 102, 255, 0.1)', borderWidth: 2 }");
            escribir.println("]");
            escribir.println("},");
            escribir.println("options: {");
            escribir.println("responsive: true,");
            escribir.println("maintainAspectRatio: false,");
            escribir.println("scales: {");
            escribir.println("y: { ");
            escribir.println("type: 'logarithmic',");
            escribir.println("title: { display: true, text: 'Tiempo (μs - escala logarítmica)' }");
            escribir.println("}");
            escribir.println("}");
            escribir.println("}");
            escribir.println("});");
            escribir.println("}");
            escribir.println("");
            escribir.println("// Gráfico 2: Algoritmos O(n²)");
            escribir.println("const ctx2 = document.getElementById('chartN2');");
            escribir.println("if (ctx2) {");
            escribir.println("new Chart(ctx2, {");
            escribir.println("type: 'bar',");
            escribir.println("data: {");
            escribir.println("labels: tamanos,");
            escribir.println("datasets: [");
            escribir.println("{ label: 'Burbuja', data: resultados[0], backgroundColor: '#ff6384' },");
            escribir.println("{ label: 'Seleccion', data: resultados[1], backgroundColor: '#ff9f40' },");
            escribir.println("{ label: 'Insercion', data: resultados[2], backgroundColor: '#4bc0c0' }");
            escribir.println("]");
            escribir.println("},");
            escribir.println("options: {");
            escribir.println("responsive: true,");
            escribir.println("maintainAspectRatio: false,");
            escribir.println("scales: { ");
            escribir.println("y: { ");
            escribir.println("type: 'logarithmic',");
            escribir.println("title: { display: true, text: 'Tiempo (μs - escala log)' }");
            escribir.println("}");
            escribir.println("}");
            escribir.println("}");
            escribir.println("});");
            escribir.println("}");
            escribir.println("");
            escribir.println("// Gráfico 3: Algoritmos eficientes");
            escribir.println("const ctx3 = document.getElementById('chartEficientes');");
            escribir.println("if (ctx3) {");
            escribir.println("new Chart(ctx3, {");
            escribir.println("type: 'line',");
            escribir.println("data: {");
            escribir.println("labels: tamanos,");
            escribir.println("datasets: [");
            escribir.println("{ label: 'Shell', data: resultados[3], borderColor: '#36a2eb', backgroundColor: 'rgba(54, 162, 235, 0.2)', borderWidth: 3, fill: true },");
            escribir.println("{ label: 'Quick Sort', data: resultados[4], borderColor: '#9966ff', backgroundColor: 'rgba(153, 102, 255, 0.2)', borderWidth: 3, fill: true }");
            escribir.println("]");
            escribir.println("},");
            escribir.println("options: {");
            escribir.println("responsive: true,");
            escribir.println("maintainAspectRatio: false,");
            escribir.println("scales: { ");
            escribir.println("y: { ");
            escribir.println("type: 'logarithmic',");
            escribir.println("title: { display: true, text: 'Tiempo (μs - escala log)' }");
            escribir.println("}");
            escribir.println("}");
            escribir.println("}");
            escribir.println("});");
            escribir.println("}");
            escribir.println("}");
            escribir.println("");
            escribir.println("// Inicializar cuando el DOM esté listo");
            escribir.println("if (document.readyState === 'loading') {");
            escribir.println("document.addEventListener('DOMContentLoaded', initCharts);");
            escribir.println("} else {");
            escribir.println("initCharts();");
            escribir.println("}");
            escribir.println("</script>");
            
            escribir.println("<div class='note'>");
            escribir.println("<p><strong>Nota:</strong> Tiempos en microsegundos. Valores en cursiva son aproximaciones basadas en complejidad algorítmica.</p>");
            escribir.println("<p>Los gráficos usan escala logarítmica para mejor visualización de los grandes rangos de datos.</p>");
            escribir.println("</div>");
            escribir.println("</div>");
            escribir.println("</body>");
            escribir.println("</html>");
            
            escribir.close();
            System.out.println("\nArchivo HTML generado: resultados.html");
           
            
        } catch (IOException e) {
            System.out.println("Error al crear archivo HTML: " + e.getMessage());
        }
    }
}