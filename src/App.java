import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;

public class App {
    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color NAVY = new Color(32, 45, 70);
    private static final Color BLUE = new Color(53, 105, 190);
    private static final Color GREEN = new Color(42, 137, 103);
    private static final Color TEXT = new Color(45, 55, 72);
    private static final Color MUTED = new Color(103, 116, 137);
    private static final int RUN_TIMEOUT_SECONDS = 8;
    private static final int MAX_OUTPUT_CHARACTERS = 100_000;

    private static final Lesson[] LESSONS = createLessons();

    private static Lesson[] createLessons() {
        return new Lesson[] {
            lesson("Tu primer programa",
                "Java ejecuta instrucciones dentro del método main. La clase organiza el código y println escribe una línea. Las llaves delimitan bloques y cada instrucción suele terminar en punto y coma.",
                """
                public class Hola {
                    public static void main(String[] args) {
                        System.out.println("Hola, mundo");
                    }
                }
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        System.out.println("Hola, mundo");
                    }
                }
                """,
                "¿Qué instrucción imprime una línea?", 2, "println imprime texto y termina la línea.", 0,
                "System.out.println()", "Scanner.next()", "System.in.read()"),
            lesson("Tipos y variables",
                "int, long, double y boolean son tipos primitivos. String y otros objetos son tipos de referencia. Una variable tiene un tipo, un nombre y un valor; el compilador detecta asignaciones incompatibles.",
                """
                int edad = 16;
                double promedio = 9.5;
                boolean aprobado = true;
                String nombre = "Ana";
                System.out.println(nombre + ": " + promedio);
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        int edad = 16;
                        double promedio = 9.5;
                        boolean aprobado = true;
                        String nombre = "Ana";
                        System.out.println(nombre + ", edad " + edad + ", promedio " + promedio
                                + ", aprobado: " + aprobado);
                    }
                }
                """,
                "¿Qué tipo primitivo representa verdadero o falso?", 3, "boolean solo puede valer true o false.", 1,
                "int", "String", "boolean"),
                lesson("Clases envoltorio",
                    "Cada primitivo tiene una clase envoltorio: int corresponde a Integer, double a Double y así sucesivamente. Java puede convertirlos automáticamente (autoboxing/unboxing). Las colecciones usan tipos de referencia y un Integer null no debe convertirse a int.",
                    """
                    List<Integer> puntos = new ArrayList<>();
                    puntos.add(10); // autoboxing: int a Integer
                    int primero = puntos.get(0); // unboxing
                    """,
                    """
                    import java.util.ArrayList;
                    import java.util.List;

                    public class AppStudent {
                        public static void main(String[] args) {
                            List<Integer> puntos = new ArrayList<>();
                            puntos.add(10);
                            puntos.add(20);
                            int suma = puntos.get(0) + puntos.get(1);
                            System.out.println("Suma: " + suma);
                        }
                    }
                    """,
                    "¿Por qué List<int> no es un tipo válido?", 2, "Los genéricos reciben tipos de referencia; para int se usa Integer.", 1,
                    "Las listas solo guardan texto", "Los genéricos requieren clases como Integer", "int es una palabra reservada"),
                lesson("Operadores y conversiones",
                "Los operadores aritméticos hacen cálculos; los relacionales comparan y producen boolean. && y || combinan condiciones con cortocircuito. La división entre enteros trunca decimales; convierte uno a double para conservarlos.",
                """
                int cantidad = 5;
                double precio = 2.50;
                double total = cantidad * precio;
                boolean descuento = cantidad >= 5 && total > 10;
                System.out.println(total + ", descuento: " + descuento);
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        int cantidad = 5;
                        double precio = 2.50;
                        double total = cantidad * precio;
                        double promedio = (double) 7 / 2;
                        boolean descuento = cantidad >= 5 && total > 10;
                        System.out.println("Total: " + total);
                        System.out.println("Promedio: " + promedio);
                        System.out.println("¿Descuento?: " + descuento);
                    }
                }
                """,
                "¿Cómo se conserva el decimal al dividir dos enteros?", 1, "Convierte uno de los operandos a double antes de dividir.", 1,
                "Convierte un operando a double", "Usa == en lugar de /", "Guarda el resultado en int"),
            lesson("Decimales y dinero",
                "double usa coma flotante binaria y puede introducir pequeños errores de redondeo, por lo que no es ideal para dinero. BigDecimal permite aritmética decimal controlada: crea valores desde texto y define explícitamente la escala y el modo de redondeo cuando haga falta.",
                """
                BigDecimal precio = new BigDecimal("19.99");
                BigDecimal cantidad = new BigDecimal("3");
                System.out.println(precio.multiply(cantidad));
                """,
                """
                import java.math.BigDecimal;
                import java.math.RoundingMode;

                public class AppStudent {
                    public static void main(String[] args) {
                        BigDecimal precio = new BigDecimal("19.99");
                        BigDecimal cantidad = new BigDecimal("3");
                        BigDecimal subtotal = precio.multiply(cantidad);
                        BigDecimal impuesto = subtotal.multiply(new BigDecimal("0.16"))
                                .setScale(2, RoundingMode.HALF_UP);
                        System.out.println("Subtotal: " + subtotal);
                        System.out.println("Impuesto: " + impuesto);
                    }
                }
                """,
                "¿Cómo conviene crear un BigDecimal para un importe decimal?", 1, "Usa texto, como new BigDecimal(\"19.99\"), y evita partir de double.", 1,
                "Desde texto decimal", "Desde new BigDecimal(0.1)", "Con un cast a int"),
            lesson("Decisiones: if y switch",
                "if/else expresa condiciones generales. switch resulta claro cuando comparas un mismo valor con varias alternativas. En el switch moderno, -> evita caídas accidentales al siguiente caso.",
                """
                String nivel = switch (puntos / 10) {
                    case 10, 9 -> "Excelente";
                    case 8, 7 -> "Bien";
                    default -> "Sigue practicando";
                };
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        int puntos = 87;
                        String nivel = switch (puntos / 10) {
                            case 10, 9 -> "Excelente";
                            case 8, 7 -> "Bien";
                            default -> "Sigue practicando";
                        };
                        System.out.println(nivel);
                    }
                }
                """,
                "¿Cuándo suele ser más claro usar switch?", 2, "switch ordena varias alternativas de un mismo valor.", 2,
                "Para repetir un bloque", "Para elegir entre varios valores posibles", "Para declarar una clase"),
            lesson("Ciclos",
                "for es práctico si conoces el número de vueltas; while repite mientras una condición sea verdadera. Asegúrate de que la condición pueda cambiar para evitar ciclos infinitos. break y continue alteran el flujo del ciclo.",
                """
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Vuelta " + i);
                }
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        int intentos = 0;
                        while (intentos < 3) {
                            System.out.println("Intento " + (intentos + 1));
                            intentos++;
                        }
                    }
                }
                """,
                "¿Qué evita que un while repita para siempre?", 1, "La condición debe volverse falsa; aquí intentos aumenta en cada vuelta.", 3,
                "Actualizar el valor que usa la condición", "Imprimir dentro del ciclo", "Usar una variable String"),
            lesson("Arreglos",
                "Un arreglo guarda varios valores del mismo tipo y tiene tamaño fijo. Los índices empiezan en cero; el último índice es length - 1. Acceder fuera de esos límites produce un error en ejecución.",
                """
                int[] notas = {8, 9, 10};
                System.out.println(notas[0]);
                System.out.println(notas.length);
                """,
                """
                import java.util.Arrays;

                public class AppStudent {
                    public static void main(String[] args) {
                        int[] notas = {8, 9, 10};
                        int suma = 0;
                        for (int nota : notas) {
                            suma += nota;
                        }
                        System.out.println(Arrays.toString(notas));
                        System.out.println("Promedio: " + (double) suma / notas.length);
                    }
                }
                """,
                "¿Cuál es el índice del primer elemento?", 1, "Los arreglos de Java empiezan en el índice cero.", 6,
                "0", "1", "length"),
            lesson("Texto: String",
                "String es inmutable: sus métodos producen otro texto en vez de cambiar el original. Compara contenido con equals, no con ==. Usa StringBuilder para construir texto repetidamente en un ciclo.",
                """
                String nombre = "Java";
                System.out.println(nombre.equals("Java"));
                System.out.println(nombre.toUpperCase());
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        String lenguaje = "Java";
                        String entrada = new String("Java");
                        System.out.println("Contenido igual: " + lenguaje.equals(entrada));
                        System.out.println("Misma referencia: " + (lenguaje == entrada));
                        StringBuilder mensaje = new StringBuilder();
                        mensaje.append("Aprende ").append(lenguaje);
                        System.out.println(mensaje);
                    }
                }
                """,
                "¿Qué método compara el contenido de dos String?", 2, "equals compara el texto; == compara referencias en objetos.", 7,
                "==", "equals()", "compareTo() siempre devuelve boolean"),
            lesson("Entrada con Scanner",
                "Scanner puede leer datos de la entrada estándar o de otra fuente. next() lee una palabra, nextInt() un entero y nextLine() una línea completa. Valida datos externos y ten cuidado al combinar nextInt() con nextLine(), porque queda pendiente el salto de línea.",
                """
                Scanner entrada = new Scanner(System.in);
                String nombre = entrada.nextLine();
                """,
                """
                import java.util.Scanner;

                public class AppStudent {
                    public static void main(String[] args) {
                        // El editor no solicita entrada interactiva; este texto simula lo que escribiría el usuario.
                        try (Scanner entrada = new Scanner("Ana 16")) {
                            String nombre = entrada.next();
                            int edad = entrada.nextInt();
                            System.out.println(nombre + " tiene " + edad + " años");
                        }
                    }
                }
                """,
                "¿Qué método de Scanner lee una línea completa?", 2, "nextLine() lee el resto de la línea actual.", 4,
                "nextInt()", "nextLine()", "hasNextInt()"),
            lesson("Métodos y sobrecarga",
                "Un método nombra una tarea, recibe parámetros y puede devolver un resultado. void indica que no devuelve valor. La sobrecarga permite varios métodos con el mismo nombre si sus parámetros difieren.",
                """
                static int sumar(int a, int b) {
                    return a + b;
                }
                """,
                """
                public class AppStudent {
                    static int sumar(int a, int b) {
                        return a + b;
                    }

                    static double sumar(double a, double b) {
                        return a + b;
                    }

                    public static void main(String[] args) {
                        System.out.println(sumar(2, 3));
                        System.out.println(sumar(2.5, 3.1));
                    }
                }
                """,
                "¿Qué debe cambiar para sobrecargar un método?", 2, "La lista de parámetros debe ser distinta; cambiar solo el retorno no basta.", 4,
                "El tipo de retorno únicamente", "La cantidad o los tipos de parámetros", "El nombre de la clase"),
            lesson("Clases y objetos",
                "Una clase define el estado y comportamiento de un tipo. Un objeto es una instancia creada normalmente con new. Los campos guardan estado y los métodos definen lo que el objeto puede hacer.",
                """
                class Perro {
                    String nombre;
                    void ladrar() { System.out.println(nombre + " dice guau"); }
                }
                """,
                """
                public class AppStudent {
                    static class Perro {
                        String nombre;
                        void ladrar() {
                            System.out.println(nombre + " dice guau");
                        }
                    }

                    public static void main(String[] args) {
                        Perro mascota = new Perro();
                        mascota.nombre = "Luna";
                        mascota.ladrar();
                    }
                }
                """,
                "¿Qué hace new Perro()?", 1, "new crea una instancia de la clase Perro.", 5,
                "Crea un objeto", "Declara un ciclo", "Importa un paquete"),
            lesson("Constructores y this",
                "El constructor inicializa un objeto y tiene el mismo nombre que la clase. this distingue el campo del parámetro con el mismo nombre. Sobrecargar constructores permite distintas formas válidas de crear objetos.",
                """
                class Persona {
                    private final String nombre;
                    Persona(String nombre) { this.nombre = nombre; }
                }
                """,
                """
                public class AppStudent {
                    static class Persona {
                        private final String nombre;

                        Persona(String nombre) {
                            this.nombre = nombre;
                        }

                        String saludar() {
                            return "Hola, soy " + nombre;
                        }
                    }

                    public static void main(String[] args) {
                        Persona persona = new Persona("Luis");
                        System.out.println(persona.saludar());
                    }
                }
                """,
                "¿Para qué se usa this.nombre = nombre?", 2, "this.nombre es el campo; nombre, el parámetro recibido.", 5,
                "Para llamar al compilador", "Para distinguir el campo del parámetro", "Para crear una subclase"),
            lesson("Encapsulación",
                "La encapsulación protege el estado del objeto con campos privados y métodos públicos. Los métodos pueden validar cambios y mantener invariantes. No agregues getters y setters sin pensar qué operaciones debe permitir el modelo.",
                """
                class Cuenta {
                    private int saldo;
                    void depositar(int importe) {
                        if (importe > 0) saldo += importe;
                    }
                }
                """,
                """
                public class AppStudent {
                    static class Cuenta {
                        private int saldo;

                        void depositar(int importe) {
                            if (importe <= 0) {
                                throw new IllegalArgumentException("El importe debe ser positivo");
                            }
                            saldo += importe;
                        }

                        int getSaldo() {
                            return saldo;
                        }
                    }

                    public static void main(String[] args) {
                        Cuenta cuenta = new Cuenta();
                        cuenta.depositar(50);
                        System.out.println("Saldo: " + cuenta.getSaldo());
                    }
                }
                """,
                "¿Por qué se declara saldo como private?", 1, "Así se protege el dato y se controla cómo se modifica.", 5,
                "Para controlar el acceso al estado", "Para que sea global", "Para evitar crear objetos"),
            lesson("static y final",
                "Un miembro static pertenece a la clase, no a cada instancia. Una constante suele declararse static final. final también impide reasignar una variable local, aunque no vuelve inmutable el objeto referenciado.",
                """
                static final double IVA = 0.16;
                """,
                """
                public class AppStudent {
                    static final double IVA = 0.16;

                    static double precioConImpuesto(double precio) {
                        return precio * (1 + IVA);
                    }

                    public static void main(String[] args) {
                        System.out.println(precioConImpuesto(100));
                    }
                }
                """,
                "¿A quién pertenece un campo static?", 2, "Pertenece a la clase y se comparte entre sus instancias.", 1,
                "A cada objeto por separado", "A la clase", "Al método main únicamente"),
            lesson("Herencia",
                "extends crea una relación de especialización: la subclase hereda miembros accesibles de la clase base y puede ampliar su comportamiento. Java permite heredar de una sola clase; usa herencia solo cuando realmente existe una relación «es un».",
                """
                class Animal {
                    void comer() { System.out.println("Comiendo"); }
                }
                class Gato extends Animal { }
                """,
                """
                public class AppStudent {
                    static class Animal {
                        void comer() {
                            System.out.println("El animal está comiendo");
                        }
                    }

                    static class Gato extends Animal {
                        void maullar() {
                            System.out.println("Miau");
                        }
                    }

                    public static void main(String[] args) {
                        Gato gato = new Gato();
                        gato.comer();
                        gato.maullar();
                    }
                }
                """,
                "¿Qué palabra declara herencia de una clase?", 1, "extends indica que una clase hereda de otra.", 5,
                "extends", "implements", "package"),
            lesson("Polimorfismo",
                "Una variable de tipo base puede referirse a distintas subclases. Al llamar un método sobrescrito, Java selecciona la implementación del objeto real en ejecución. Programa contra el tipo general cuando convenga.",
                """
                Animal animal = new Perro();
                animal.hacerSonido(); // ejecuta la versión de Perro
                """,
                """
                public class AppStudent {
                    static class Animal {
                        void sonido() {
                            System.out.println("Sonido genérico");
                        }
                    }

                    static class Perro extends Animal {
                        @Override
                        void sonido() {
                            System.out.println("Guau");
                        }
                    }

                    public static void main(String[] args) {
                        Animal animal = new Perro();
                        animal.sonido();
                    }
                }
                """,
                "¿Qué versión de sonido() se ejecuta?", 2, "Se ejecuta la sobrescritura de Perro, el tipo real del objeto.", 5,
                "La de Animal siempre", "La sobrescrita de Perro", "Ninguna: la referencia es Animal"),
            lesson("Interfaces y abstracción",
                "Una interfaz define un contrato que una clase implementa con implements. Las clases pueden implementar varias interfaces, lo que facilita sustituir dependencias. Una clase abstracta puede compartir estado y código además de definir métodos abstractos.",
                """
                interface Notificador {
                    void enviar(String mensaje);
                }
                """,
                """
                public class AppStudent {
                    interface Notificador {
                        void enviar(String mensaje);
                    }

                    static class NotificadorConsola implements Notificador {
                        public void enviar(String mensaje) {
                            System.out.println("Aviso: " + mensaje);
                        }
                    }

                    static void avisar(Notificador notificador) {
                        notificador.enviar("Pedido recibido");
                    }

                    public static void main(String[] args) {
                        avisar(new NotificadorConsola());
                    }
                }
                """,
                "¿Qué palabra indica que una clase cumple una interfaz?", 2, "implements declara que la clase implementa el contrato.", 5,
                "extends", "implements", "instanceof"),
            lesson("enum y record",
                "enum representa un conjunto cerrado de opciones y evita valores inválidos escritos como texto libre. record es una forma concisa de crear portadores de datos inmutables con constructor, accesores, equals y hashCode.",
                """
                enum Estado { NUEVO, EN_PROCESO, LISTO }
                record Producto(String nombre, double precio) {}
                """,
                """
                public class AppStudent {
                    enum Estado { NUEVO, EN_PROCESO, LISTO }
                    record Producto(String nombre, double precio) {}

                    public static void main(String[] args) {
                        Producto producto = new Producto("Libro", 12.5);
                        Estado estado = Estado.NUEVO;
                        System.out.println(producto.nombre() + ": " + producto.precio());
                        System.out.println("Estado: " + estado);
                    }
                }
                """,
                "¿Qué representa mejor un enum?", 1, "enum limita un valor a constantes conocidas.", 5,
                "Un conjunto fijo de opciones", "Una lista que cambia de tamaño", "Un archivo de configuración"),
            lesson("Paquetes y visibilidad",
                "package organiza clases con nombres completos y evita colisiones. import permite usar clases de otros paquetes. public, protected, sin modificador y private ofrecen distintos niveles de acceso; el paquete debe coincidir con la estructura compilada.",
                """
                package curso.basico;

                public class AppStudent {
                    public static void main(String[] args) {
                        System.out.println("Paquete curso.basico");
                    }
                }
                """,
                """
                package curso.basico;

                public class AppStudent {
                    public static void main(String[] args) {
                        System.out.println("Clase ejecutada desde el paquete " + AppStudent.class.getPackageName());
                    }
                }
                """,
                "¿Qué declaración debe aparecer primero, antes de imports y clases?", 1, "La declaración package va antes de imports y declaraciones de tipos.", 1,
                "package", "public static", "return"),
            lesson("Excepciones",
                "Las excepciones representan fallos o situaciones excepcionales. try/catch permite manejarlas; finally se ejecuta al terminar. Las excepciones checked deben capturarse o declararse, mientras RuntimeException suele indicar errores de programación o argumentos inválidos.",
                """
                try {
                    int numero = Integer.parseInt("42");
                    System.out.println(numero);
                } catch (NumberFormatException error) {
                    System.out.println("No era un número");
                }
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        String entrada = "42x";
                        try {
                            int numero = Integer.parseInt(entrada);
                            System.out.println(numero);
                        } catch (NumberFormatException error) {
                            System.out.println("Entrada inválida: " + entrada);
                        } finally {
                            System.out.println("Conversión terminada");
                        }
                    }
                }
                """,
                "¿Qué bloque maneja el tipo de error esperado?", 2, "catch recibe y maneja la excepción compatible.", 8,
                "try", "catch", "package"),
            lesson("equals, hashCode y toString",
                "equals define igualdad lógica entre objetos; hashCode debe ser coherente: objetos iguales deben tener el mismo hash. Las colecciones hash dependen de ese contrato. toString ayuda a representar objetos para depuración. Los records generan estos métodos automáticamente.",
                """
                record Usuario(int id, String nombre) {}
                Usuario a = new Usuario(1, "Ana");
                Usuario b = new Usuario(1, "Ana");
                System.out.println(a.equals(b));
                """,
                """
                public class AppStudent {
                    record Usuario(int id, String nombre) {}

                    public static void main(String[] args) {
                        Usuario primero = new Usuario(1, "Ana");
                        Usuario segundo = new Usuario(1, "Ana");
                        System.out.println("¿Iguales?: " + primero.equals(segundo));
                        System.out.println("Hash: " + primero.hashCode());
                        System.out.println(primero);
                    }
                }
                """,
                "Si dos objetos son iguales según equals, ¿qué debe cumplirse con hashCode?", 2, "Deben tener el mismo hashCode para funcionar correctamente en colecciones hash.", 5,
                "Deben tener hashes distintos", "Deben tener el mismo hash", "No hay ninguna relación"),
            lesson("Colecciones: List",
                "List mantiene el orden y admite duplicados; ArrayList es una implementación habitual. Usa la interfaz List en el tipo de la variable y elige una implementación concreta al crearla. Las colecciones crecen dinámicamente, a diferencia de los arreglos.",
                """
                List<String> nombres = new ArrayList<>();
                nombres.add("Ana");
                """,
                """
                import java.util.ArrayList;
                import java.util.List;

                public class AppStudent {
                    public static void main(String[] args) {
                        List<String> nombres = new ArrayList<>();
                        nombres.add("Ana");
                        nombres.add("Luis");
                        nombres.add("Ana");
                        for (String nombre : nombres) {
                            System.out.println(nombre);
                        }
                        System.out.println("Cantidad: " + nombres.size());
                    }
                }
                """,
                "¿Qué característica tiene List?", 2, "List conserva el orden y permite valores repetidos.", 9,
                "No permite duplicados", "Mantiene orden y admite duplicados", "Solo guarda pares clave-valor"),
            lesson("Colecciones: Set y Map",
                "Set representa elementos únicos; HashSet no garantiza orden. Map asocia claves únicas con valores; HashMap es una implementación común. Usa equals/hashCode correctos para las claves y consulta con containsKey/get.",
                """
                Set<String> etiquetas = new HashSet<>();
                Map<String, Integer> edades = new HashMap<>();
                """,
                """
                import java.util.HashMap;
                import java.util.HashSet;
                import java.util.Map;
                import java.util.Set;

                public class AppStudent {
                    public static void main(String[] args) {
                        Set<String> etiquetas = new HashSet<>();
                        etiquetas.add("java");
                        etiquetas.add("java");
                        System.out.println("Etiquetas únicas: " + etiquetas.size());

                        Map<String, Integer> edades = new HashMap<>();
                        edades.put("Ana", 20);
                        edades.put("Luis", 22);
                        System.out.println("Edad de Ana: " + edades.get("Ana"));
                    }
                }
                """,
                "¿Qué estructura almacena pares clave-valor?", 2, "Map asocia cada clave única con un valor.", 9,
                "Set", "Map", "ArrayList"),
            lesson("Genéricos",
                "Los genéricos parametrizan clases y métodos con tipos, por ejemplo List<String>. El compilador comprueba el tipo y evita conversiones inseguras. En tipos genéricos se usan clases envoltorio como Integer, no primitivos como int.",
                """
                static <T> T primero(List<T> valores) {
                    return valores.get(0);
                }
                """,
                """
                import java.util.List;

                public class AppStudent {
                    static <T> T primero(List<T> valores) {
                        return valores.get(0);
                    }

                    public static void main(String[] args) {
                        String nombre = primero(List.of("Ana", "Luis"));
                        Integer numero = primero(List.of(10, 20));
                        System.out.println(nombre + " / " + numero);
                    }
                }
                """,
                "¿Qué ventaja principal ofrecen los genéricos?", 1, "Permiten detectar incompatibilidades de tipos al compilar.", 1,
                "Seguridad de tipos en compilación", "Ejecutar más hilos automáticamente", "Conectar a una base de datos"),
            lesson("Lambdas e interfaces funcionales",
                "Una lambda expresa una implementación breve de una interfaz funcional, que tiene un único método abstracto. Predicate, Function y Consumer son interfaces estándar útiles. Esto permite pasar comportamiento como dato.",
                """
                List<String> nombres = List.of("Ana", "Alejandra", "Luis");
                nombres.forEach(nombre -> System.out.println(nombre));
                """,
                """
                import java.util.List;
                import java.util.function.Predicate;

                public class AppStudent {
                    public static void main(String[] args) {
                        Predicate<String> esLargo = texto -> texto.length() > 4;
                        List<String> nombres = List.of("Ana", "Alejandra", "Luis");
                        nombres.stream()
                                .filter(esLargo)
                                .forEach(nombre -> System.out.println("Hola, " + nombre));
                    }
                }
                """,
                "¿Cuántos métodos abstractos tiene una interfaz funcional?", 1, "Tiene exactamente un método abstracto; puede tener métodos default/static.", 4,
                "Uno", "Dos obligatoriamente", "Ninguno"),
            lesson("Streams",
                "Stream procesa datos en una secuencia de operaciones. filter y map son intermedias y perezosas; collect, count o forEach son terminales. Un stream se consume una vez; no reemplaza automáticamente a un ciclo simple.",
                """
                List<Integer> pares = numeros.stream()
                    .filter(n -> n % 2 == 0)
                    .toList();
                """,
                """
                import java.util.List;

                public class AppStudent {
                    public static void main(String[] args) {
                        List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6);
                        int sumaDePares = numeros.stream()
                                .filter(numero -> numero % 2 == 0)
                                .mapToInt(Integer::intValue)
                                .sum();
                        System.out.println("Suma de pares: " + sumaDePares);
                    }
                }
                """,
                "¿Qué operación terminal suma valores de un IntStream?", 2, "sum() consume el stream y devuelve la suma.", 10,
                "filter()", "sum()", "map()"),
            lesson("null y Optional",
                "null significa ausencia de una referencia y puede causar NullPointerException. Comprueba los valores en los límites del sistema y evita devolver null cuando Optional expresa mejor que un resultado puede faltar. Optional no debe usarse como sustituto universal de cualquier campo.",
                """
                Optional<String> nombre = Optional.of("Ana");
                System.out.println(nombre.orElse("Desconocido"));
                """,
                """
                import java.util.Optional;

                public class AppStudent {
                    static Optional<String> buscarCorreo(String usuario) {
                        if (usuario.equals("ana")) {
                            return Optional.of("ana@ejemplo.com");
                        }
                        return Optional.empty();
                    }

                    public static void main(String[] args) {
                        String correo = buscarCorreo("desconocido")
                                .orElse("Sin correo registrado");
                        System.out.println(correo);
                    }
                }
                """,
                "¿Qué representa Optional.empty()?", 2, "Representa que no hay un valor presente.", 1,
                "Un Optional sin valor", "Una cadena vacía garantizada", "Una excepción"),
            lesson("Fechas y horas",
                "java.time ofrece tipos inmutables y seguros como LocalDate, LocalTime, LocalDateTime e Instant. Para fechas internacionales, usa ZonedDateTime o Instant y conserva la zona horaria cuando sea relevante; evita las antiguas clases Date/Calendar para código nuevo.",
                """
                LocalDate hoy = LocalDate.now();
                LocalDate proximaSemana = hoy.plusWeeks(1);
                """,
                """
                import java.time.LocalDate;
                import java.time.format.DateTimeFormatter;

                public class AppStudent {
                    public static void main(String[] args) {
                        LocalDate inicio = LocalDate.of(2026, 1, 10);
                        LocalDate entrega = inicio.plusDays(14);
                        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        System.out.println("Entrega: " + entrega.format(formato));
                    }
                }
                """,
                "¿Qué clase representa una fecha sin hora ni zona?", 1, "LocalDate representa año, mes y día sin zona horaria.", 14,
                "LocalDate", "Instant", "LocalTime"),
            lesson("Archivos y try-with-resources",
                "java.nio.file.Path y Files ofrecen operaciones modernas para archivos. try-with-resources cierra automáticamente los recursos que implementan AutoCloseable, incluso si ocurre un error. Maneja IOException y valida las rutas externas.",
                """
                Path archivo = Files.createTempFile("nota-", ".txt");
                Files.writeString(archivo, "Hola");
                System.out.println(Files.readString(archivo));
                Files.deleteIfExists(archivo);
                """,
                """
                import java.nio.file.Files;
                import java.nio.file.Path;

                public class AppStudent {
                    public static void main(String[] args) throws Exception {
                        Path archivo = Files.createTempFile("curso-java-", ".txt");
                        try {
                            Files.writeString(archivo, "Estudiar Java");
                            System.out.println(Files.readString(archivo));
                        } finally {
                            Files.deleteIfExists(archivo);
                        }
                    }
                }
                """,
                "¿Qué ventaja ofrece try-with-resources?", 2, "Cierra automáticamente recursos al salir del bloque.", 4,
                "Evita compilar el programa", "Cierra recursos automáticamente", "Convierte archivos en clases"),
            lesson("Anotaciones",
                "Las anotaciones agregan metadatos al código. @Override permite al compilador verificar que sobrescribes un método. Spring usa anotaciones para configurar componentes y rutas; la anotación no reemplaza el contrato ni el comportamiento de Java.",
                """
                @Override
                public String toString() { return "ejemplo"; }
                """,
                """
                public class AppStudent {
                    @interface Descripcion {
                        String value();
                    }

                    @Descripcion("Ejemplo de metadatos")
                    static class Servicio {
                        @Override
                        public String toString() {
                            return "Servicio de ejemplo";
                        }
                    }

                    public static void main(String[] args) {
                        System.out.println(new Servicio());
                        System.out.println("Spring utiliza anotaciones para declarar componentes.");
                    }
                }
                """,
                "¿Qué verifica @Override?", 1, "Comprueba que el método sobrescribe uno heredado o declarado en una interfaz.", 5,
                "Que se está sobrescribiendo un método válido", "Que el método es estático", "Que el método se ejecuta en otro hilo"),
            lesson("Concurrencia básica",
                "Un hilo permite trabajo concurrente, pero compartir datos mutables exige coordinación. ExecutorService administra tareas; Future representa un resultado pendiente. En aplicaciones de servidor, deja que el framework gestione el ciclo de vida de sus hilos.",
                """
                ExecutorService ejecutor = Executors.newSingleThreadExecutor();
                try {
                    Future<Integer> resultado = ejecutor.submit(() -> 2 + 3);
                    System.out.println(resultado.get());
                } finally {
                    ejecutor.shutdown();
                }
                """,
                """
                import java.util.concurrent.ExecutorService;
                import java.util.concurrent.Executors;
                import java.util.concurrent.Future;

                public class AppStudent {
                    public static void main(String[] args) throws Exception {
                        ExecutorService ejecutor = Executors.newSingleThreadExecutor();
                        try {
                            Future<Integer> resultado = ejecutor.submit(() -> 20 + 22);
                            System.out.println("Resultado concurrente: " + resultado.get());
                        } finally {
                            ejecutor.shutdown();
                        }
                    }
                }
                """,
                "¿Qué representa Future<Integer>?", 2, "Future permite obtener más tarde el resultado de una tarea.", 15,
                "Un tipo de excepción", "El resultado pendiente de una tarea", "Una colección ordenada"),
            lesson("Pruebas y aserciones",
                "Una prueba comprueba un comportamiento esperado con entradas conocidas. Las aserciones de Java verifican condiciones cuando se ejecuta con -ea; en producción se prefieren pruebas automatizadas con JUnit. Mantén pruebas independientes y repetibles.",
                """
                static int sumar(int a, int b) { return a + b; }
                // Ejecutar con -ea para activar assert
                assert sumar(2, 3) == 5;
                """,
                """
                public class AppStudent {
                    static int sumar(int a, int b) {
                        return a + b;
                    }

                    static void comprobar(boolean condicion, String mensaje) {
                        if (!condicion) {
                            throw new AssertionError(mensaje);
                        }
                    }

                    public static void main(String[] args) {
                        comprobar(sumar(2, 3) == 5, "2 + 3 debe ser 5");
                        comprobar(sumar(-1, 1) == 0, "La suma debe admitir negativos");
                        System.out.println("Todas las comprobaciones pasaron");
                    }
                }
                """,
                "¿Qué hace una prueba automatizada?", 1, "Compara resultados reales con resultados esperados y señala fallos.", 2,
                "Verifica un comportamiento esperado", "Inicia siempre un servidor web", "Reemplaza el compilador"),
            lesson("Maven y el proyecto Java",
                "Antes de Spring Boot conviene conocer Maven: pom.xml declara coordenadas, dependencias y versión de Java. Maven estandariza compilar, probar y empaquetar; los comandos habituales son mvn test y mvn package. Este editor ejecuta Java sin Maven, pero el ejemplo muestra su flujo.",
                """
                pom.xml define groupId, artifactId, versión, Java y dependencias.
                mvn test
                mvn package
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        String[] fases = {"validate", "compile", "test", "package", "verify"};
                        for (String fase : fases) {
                            System.out.println("Maven puede ejecutar la fase: " + fase);
                        }
                        System.out.println("Las dependencias se declaran en pom.xml.");
                    }
                }
                """,
                "¿Dónde se declaran normalmente las dependencias de Maven?", 2, "El archivo pom.xml describe dependencias y configuración del proyecto.", 13,
                "En main()", "En pom.xml", "En el nombre del archivo .java"),
            lesson("Diseño: depender de interfaces",
                "El principio de inversión de dependencias propone que la lógica use abstracciones y no quede atada a una implementación. Así se puede cambiar el adaptador o probar la lógica con un sustituto. La inyección de dependencias de Spring automatiza parte de este patrón.",
                """
                interface Repositorio {
                    String buscar();
                }
                class Servicio {
                    private final Repositorio repositorio;
                    Servicio(Repositorio repositorio) { this.repositorio = repositorio; }
                }
                """,
                """
                public class AppStudent {
                    interface Repositorio {
                        String buscar();
                    }

                    static class RepositorioEnMemoria implements Repositorio {
                        public String buscar() {
                            return "dato de prueba";
                        }
                    }

                    static class Servicio {
                        private final Repositorio repositorio;
                        Servicio(Repositorio repositorio) {
                            this.repositorio = repositorio;
                        }
                        String obtener() {
                            return repositorio.buscar();
                        }
                    }

                    public static void main(String[] args) {
                        Servicio servicio = new Servicio(new RepositorioEnMemoria());
                        System.out.println(servicio.obtener());
                    }
                }
                """,
                "¿Qué beneficio tiene inyectar una interfaz en un servicio?", 1, "Se puede sustituir la implementación sin acoplar la lógica a una clase concreta.", 5,
                "Facilita sustituir implementaciones y probar", "Hace que todo sea estático", "Evita tener que compilar"),
            lesson("Depuración y errores",
                "Lee un error desde el tipo y el mensaje hasta la primera línea de tu código en el stack trace. La línea indica dónde se detectó el problema, no siempre su causa original. Un depurador permite avanzar paso a paso e inspeccionar variables.",
                """
                try {
                    Integer.parseInt("abc");
                } catch (NumberFormatException error) {
                    System.out.println(error.getMessage());
                }
                """,
                """
                public class AppStudent {
                    static int dividir(int a, int b) {
                        return a / b;
                    }

                    public static void main(String[] args) {
                        try {
                            System.out.println(dividir(10, 0));
                        } catch (ArithmeticException error) {
                            System.out.println("Tipo: " + error.getClass().getSimpleName());
                            System.out.println("Mensaje: " + error.getMessage());
                            System.out.println("Busca en el stack trace la línea de tu código.");
                        }
                    }
                }
                """,
                "¿Qué información suele indicar una línea del stack trace?", 2, "Muestra la ruta de llamadas y la línea donde se observó el error.", 2,
                "El contenido de cada variable del proyecto", "La ruta de llamadas y líneas relacionadas", "La versión del sistema operativo"),
            lesson("HTTP y REST",
                "HTTP define solicitudes y respuestas entre cliente y servidor. GET suele leer, POST crear, PUT reemplazar y DELETE borrar; los códigos 2xx indican éxito, 4xx errores de solicitud y 5xx errores del servidor. REST organiza recursos en rutas y usa métodos HTTP.",
                """
                GET /api/productos/7
                200 OK
                {"id":7,"nombre":"Libro"}
                """,
                """
                public class AppStudent {
                    record Solicitud(String metodo, String ruta) {}

                    public static void main(String[] args) {
                        Solicitud solicitud = new Solicitud("GET", "/api/productos/7");
                        int codigo = solicitud.metodo().equals("GET") ? 200 : 405;
                        System.out.println(solicitud.metodo() + " " + solicitud.ruta());
                        System.out.println("Respuesta HTTP: " + codigo + " OK");
                    }
                }
                """,
                "¿Qué código HTTP suele indicar que una solicitud tuvo éxito?", 1, "Los códigos 2xx, como 200, indican éxito.", 11,
                "200", "404", "500"),
            lesson("JSON y DTO",
                "JSON es un formato común de intercambio de datos. Un DTO (Data Transfer Object) representa los datos que entran o salen de una API; conviene validar la entrada y no exponer directamente entidades internas. Para procesar JSON real en Java se usa una biblioteca, como Jackson.",
                """
                record ProductoRespuesta(long id, String nombre) {}
                ProductoRespuesta dto = new ProductoRespuesta(7, "Libro");
                """,
                """
                public class AppStudent {
                    record ProductoRespuesta(long id, String nombre) {}

                    public static void main(String[] args) {
                        ProductoRespuesta respuesta = new ProductoRespuesta(7, "Libro");
                        String jsonDeEjemplo = "{\\"id\\":" + respuesta.id()
                                + ",\\"nombre\\":\\"" + respuesta.nombre() + "\\"}";
                        System.out.println(jsonDeEjemplo);
                        System.out.println("En una API real, usa una biblioteca JSON.");
                    }
                }
                """,
                "¿Para qué sirve principalmente un DTO?", 2, "Representa los datos transferidos por una frontera, como una API.", 5,
                "Para crear hilos", "Para representar datos que entran o salen", "Para reemplazar una base de datos"),
            lesson("SQL y JDBC",
                "SQL consulta y modifica datos relacionales. JDBC conecta Java con una base de datos; usa PreparedStatement con parámetros para evitar concatenar datos externos en SQL. Las operaciones JDBC pueden fallar y deben manejar excepciones y cerrar recursos.",
                """
                String sql = "SELECT id, nombre FROM productos WHERE id = ?";
                PreparedStatement consulta = conexion.prepareStatement(sql);
                consulta.setLong(1, id);
                """,
                """
                public class AppStudent {
                    public static void main(String[] args) {
                        String sql = "SELECT id, nombre FROM productos WHERE id = ?";
                        long id = 7;
                        System.out.println("Consulta preparada: " + sql);
                        System.out.println("Parámetro número 1: " + id);
                        System.out.println("El ? se enlaza como parámetro, no concatenando texto.");
                    }
                }
                """,
                "¿Por qué usar PreparedStatement con parámetros?", 1, "Separa valores de la consulta y ayuda a evitar inyección SQL.", 12,
                "Para enlazar datos de forma segura", "Para insertar valores directamente en el código SQL", "Para convertir SQL en JSON"),
            lesson("Logs",
                "Los logs registran eventos para entender el comportamiento de una aplicación. Usa niveles como INFO, WARNING y SEVERE, añade contexto útil y evita registrar contraseñas, tokens u otros datos sensibles. En proyectos reales es común una fachada como SLF4J.",
                """
                Logger logger = Logger.getLogger("curso");
                logger.info("Aplicación iniciada");
                """,
                """
                import java.util.logging.Logger;

                public class AppStudent {
                    private static final Logger LOG = Logger.getLogger(AppStudent.class.getName());

                    public static void main(String[] args) {
                        LOG.info("Aplicación iniciada");
                        LOG.warning("Ejemplo de advertencia");
                        System.out.println("No registres contraseñas ni tokens.");
                    }
                }
                """,
                "¿Qué no debes incluir en los logs?", 2, "No registres credenciales ni datos sensibles.", 4,
                "El inicio de la aplicación", "Contraseñas y tokens", "Mensajes de diagnóstico"),
        };
    }

    private static String buildGuide(String title) {
        return switch (title) {
            case "Tu primer programa" -> guide(
                    "Es el esqueleto mínimo que la JVM necesita para iniciar una aplicación.",
                    "class Nombre { public static void main(String[] args) { /* instrucciones */ } }",
                    "public crea una clase accesible; static permite llamar main sin crear un objeto; void indica que no devuelve un resultado. Las llaves abren y cierran bloques y cada instrucción termina con ;.",
                    "El nombre de una clase pública debe coincidir con el archivo. En este comprobador el archivo y la clase principal se llaman AppStudent.");
            case "Tipos y variables" -> guide(
                    "Declaran qué clase de dato puede guardar una variable y permiten al compilador detectar operaciones incorrectas.",
                    "int edad = 16;  double precio = 2.5;  boolean activo = true;  String nombre = \"Ana\";",
                    "El tipo precede al nombre; = asigna el valor. Usa int/long para enteros, double para medidas decimales, boolean para condiciones y String para texto. Usa final si el valor no debe reasignarse.",
                    "Java distingue mayúsculas: String empieza con S. No confundas = (asignación) con == (comparación).");
            case "Clases envoltorio" -> guide(
                    "Integer, Double y las demás clases envoltorio permiten representar primitivos donde se requieren objetos, como en colecciones y genéricos.",
                    "Integer puntos = 10;  int total = puntos;  List<Integer> lista;",
                    "Java convierte automáticamente entre int e Integer (autoboxing/unboxing). Las envolturas también ofrecen constantes y métodos como Integer.parseInt(\"42\").",
                    "Las colecciones no admiten List<int>; usan List<Integer>. Una envoltura puede ser null y al convertirla a primitivo puede provocar NullPointerException.");
            case "Operadores y conversiones" -> guide(
                    "Realizan cálculos, comparaciones y combinaciones lógicas que producen valores para asignar o usar como condiciones.",
                    "+  -  *  /  %     ==  !=  <  <=  >  >=     &&  ||  !",
                    "&& solo evalúa el lado derecho si hace falta; sirve para proteger comprobaciones. Convierte a double antes de una división entera cuando necesitas decimales. Usa paréntesis para dejar claro el orden.",
                    "int / int trunca la parte decimal. == compara referencias si se usa con objetos; para comparar texto usa equals. No compares boolean con true si basta con escribir la condición.");
            case "Decimales y dinero" -> guide(
                    "BigDecimal permite cálculos decimales previsibles, importantes para importes, facturas y redondeos.",
                    "new BigDecimal(\"19.99\").multiply(new BigDecimal(\"3\"));",
                    "Crea el valor desde texto, encadena add/subtract/multiply/divide y define scale y RoundingMode cuando debas redondear. Usa compareTo para comparar magnitudes.",
                    "Evita new BigDecimal(0.1): primero convierte la aproximación binaria de double. equals también compara la escala; para comparar importes normalmente se usa compareTo.");
            case "Decisiones: if y switch" -> guide(
                    "Elige qué instrucciones ejecutar según una condición o el valor de una expresión.",
                    "if (condicion) { ... } else { ... }   |   switch (valor) { case X -> ...; default -> ...; }",
                    "Usa if para rangos y condiciones compuestas. Usa switch cuando un mismo valor tiene varias alternativas claras. El switch de expresiones puede producir un valor y exige cubrir alternativas con default cuando corresponda.",
                    "Las condiciones de if deben ser boolean. En switch clásico, break evita continuar en el siguiente caso; con la sintaxis -> no hay caída accidental.");
            case "Ciclos" -> guide(
                    "Repite una operación para procesar elementos o continuar hasta que se cumpla una condición.",
                    "for (int i = 0; i < limite; i++) { ... }   |   while (condicion) { ... }",
                    "El for reúne inicialización, condición y actualización. while es útil cuando no sabes cuántas vueltas habrá. for (Tipo item : coleccion) recorre cada elemento sin manejar índices.",
                    "Comprueba que el estado cambie hacia el final del ciclo. Un límite <= puede producir una vuelta extra o un índice fuera del arreglo. break sale; continue salta a la próxima vuelta.");
            case "Arreglos" -> guide(
                    "Guardan una cantidad fija de elementos del mismo tipo y permiten acceder a cada uno mediante un índice.",
                    "int[] notas = {8, 9};  notas[0];  notas.length;  new String[3];",
                    "El primer índice es 0 y el último es length - 1. Recorre con for, for-each o Arrays.toString para inspeccionar valores. Usa colecciones cuando necesites que el tamaño cambie.",
                    "Acceder a un índice negativo o >= length produce ArrayIndexOutOfBoundsException. length es un campo, no un método: escribe notas.length.");
            case "Texto: String" -> guide(
                    "String representa texto y ofrece operaciones para buscar, comparar, dividir y transformar cadenas.",
                    "texto.equals(otro);  texto.length();  texto.substring(inicio, fin);  texto.trim();",
                    "String es inmutable: métodos como toUpperCase devuelven un nuevo String. Para comparar contenido usa equals o equalsIgnoreCase. Para construir repetidamente dentro de un ciclo, usa StringBuilder.",
                    "== comprueba si dos referencias apuntan al mismo objeto, no si el texto es igual. substring incluye el inicio y excluye el final.");
            case "Entrada con Scanner" -> guide(
                    "Lee datos escritos por un usuario o provenientes de otra fuente, por ejemplo una línea de consola.",
                    "try (Scanner sc = new Scanner(System.in)) { String linea = sc.nextLine(); }",
                    "next lee una palabra, nextInt un entero, nextDouble un decimal y nextLine el resto de una línea. Después de nextInt, consume el salto pendiente con nextLine antes de leer la siguiente línea completa.",
                    "Entrada externa puede estar vacía o tener un formato inesperado. Comprueba hasNextInt o captura InputMismatchException; cierra el Scanner si también eres responsable del recurso.");
            case "Métodos y sobrecarga" -> guide(
                    "Dividen la lógica en operaciones con nombre, parámetros y, opcionalmente, un valor de retorno. La sobrecarga ofrece distintas firmas para una tarea similar.",
                    "static Tipo nombre(Tipo parametro) { return valor; }   |   void accion() { ... }",
                    "El tipo de retorno debe coincidir con return. void significa que no se devuelve valor. Los parámetros son nombres locales; los argumentos son los valores enviados al llamar. Mantén métodos cortos y con una responsabilidad.",
                    "No puedes sobrecargar un método cambiando únicamente el retorno. Cada ruta de un método no-void debe devolver un valor compatible.");
            case "Clases y objetos" -> guide(
                    "Modelan conceptos del programa: la clase define datos y operaciones; cada objeto mantiene su propio estado.",
                    "class Persona { String nombre; void saludar() { ... } }  |  Persona p = new Persona();",
                    "Declara campos y métodos dentro de la clase. new construye una instancia; el punto accede a sus miembros permitidos. Crea instancias distintas cuando deban tener estados independientes.",
                    "No confundas el nombre de la clase con una variable que contiene un objeto. En diseños reales, protege los campos y valida el estado.");
            case "Constructores y this" -> guide(
                    "Inicializan un objeto al crearlo y aseguran que empiece en un estado válido.",
                    "Persona(String nombre) { this.nombre = nombre; }  |  new Persona(\"Luis\");",
                    "El constructor tiene el nombre de la clase y no declara tipo de retorno. this.nombre refiere al campo del objeto; nombre refiere al parámetro. Puedes definir varios constructores con distintos parámetros.",
                    "Si declaras cualquier constructor, Java deja de proporcionar el constructor vacío automáticamente. Valida argumentos inválidos en el constructor.");
            case "Encapsulación" -> guide(
                    "Protege los datos internos y permite que la clase controle los cambios mediante operaciones bien definidas.",
                    "private int saldo;  public void depositar(int importe) { ... }",
                    "Mantén los campos normalmente private. Expón métodos que expresen acciones del dominio y validen invariantes; proporciona acceso de lectura solo si el consumidor lo necesita.",
                    "Un setter público sin validación puede dejar el objeto en un estado imposible. Encapsular no significa generar getters y setters para todos los campos.");
            case "static y final" -> guide(
                    "static define un miembro de clase compartido; final limita la reasignación de una variable o la sobrescritura/extensión según el miembro.",
                    "static final int MAX_INTENTOS = 3;  final String nombre = \"Ana\";",
                    "Accede a constantes como Clase.CONSTANTE. Un campo de instancia pertenece a cada objeto; uno static pertenece a la clase. Una referencia final no puede apuntar a otro objeto, aunque ese objeto aún pueda ser mutable.",
                    "No uses static para todo: dificulta sustituir dependencias y probar. Convencionalmente las constantes se nombran con MAYÚSCULAS_Y_GUIONES.");
            case "Herencia" -> guide(
                    "Permite definir una especialización que hereda comportamiento de una clase base cuando la relación realmente es «es un».",
                    "class Perro extends Animal { @Override void sonido() { ... } }",
                    "La subclase reutiliza miembros accesibles, puede agregar operaciones y sobrescribir métodos. super llama al constructor o comportamiento de la clase padre. Java permite extender una sola clase.",
                    "No uses herencia solo para reutilizar unas líneas: composición suele ser más flexible. Los constructores no se heredan y los campos private no se acceden directamente desde subclases.");
            case "Polimorfismo" -> guide(
                    "Permite usar objetos distintos a través de un tipo común y ejecutar la implementación concreta elegida en tiempo de ejecución.",
                    "Animal a = new Perro();  a.sonido();  // invoca la versión sobrescrita",
                    "Declara parámetros y colecciones con el tipo común (por ejemplo List<Animal>) y deja que cada clase implemente el comportamiento. @Override permite que el compilador verifique la sobrescritura.",
                    "El tipo de referencia limita qué métodos puedes llamar en compilación; el tipo real determina la implementación sobrescrita. La sobrecarga se decide en compilación, no es lo mismo.");
            case "Interfaces y abstracción" -> guide(
                    "Una interfaz expresa un contrato y desacopla al código que necesita una capacidad de la clase que la proporciona.",
                    "interface Repositorio { String buscar(); }  |  class Memoria implements Repositorio { ... }",
                    "Define operaciones estables en la interfaz, programa consumidores contra ella y crea una implementación concreta. Una clase puede implementar varias interfaces; una interfaz funcional puede usarse con lambda.",
                    "Una clase concreta debe implementar los métodos abstractos. Usa una clase abstracta si necesitas compartir estado o implementación base, no solo declarar un contrato.");
            case "enum y record" -> guide(
                    "enum limita un valor a opciones conocidas; record modela datos como un portador conciso e inmutable.",
                    "enum Estado { NUEVO, LISTO }  |  record Producto(String nombre, double precio) {}",
                    "Accede a una constante como Estado.LISTO. Un record genera constructor, accesores (nombre()), equals, hashCode y toString; valida restricciones en un constructor compacto.",
                    "Un record no genera getNombre(): el accesor usa el nombre del componente. final en sus componentes no vuelve inmutable un objeto mutable guardado dentro.");
            case "Paquetes y visibilidad" -> guide(
                    "Organizan el código, evitan colisiones de nombres y definen qué clases o miembros puede usar cada parte del programa.",
                    "package com.ejemplo.app;  import java.util.List;  public class Servicio { ... }",
                    "La declaración package es la primera instrucción del archivo; import viene después. public permite acceso amplio, private solo desde la clase, protected desde paquete/subclases y sin modificador desde el paquete.",
                    "La estructura de carpetas debe coincidir con el paquete al compilar. Importar una clase no cambia su visibilidad ni convierte el código en público.");
            case "Excepciones" -> guide(
                    "Comunican fallos o situaciones anómalas y permiten manejarlos en el nivel que puede tomar una decisión útil.",
                    "try { ... } catch (TipoError e) { ... } finally { ... }",
                    "Captura excepciones específicas. Las checked deben manejarse o declararse con throws; las unchecked suelen indicar argumentos o estados inválidos. Usa try-with-resources para liberar recursos automáticamente.",
                    "No captures Exception para ocultar cualquier problema ni dejes catch vacío. No uses excepciones como flujo normal para resultados esperados.");
            case "equals, hashCode y toString" -> guide(
                    "Definen igualdad lógica, compatibilidad con colecciones hash y una representación útil para diagnóstico.",
                    "a.equals(b);  a.hashCode();  System.out.println(a);",
                    "Implementa equals y hashCode juntos: si a.equals(b) es true, ambos deben dar el mismo hashCode. Usa campos que definan identidad/valor. Los records generan estos métodos según sus componentes.",
                    "No uses campos mutables que cambian mientras el objeto es clave de HashMap/HashSet. toString debe ayudar a depurar sin exponer secretos.");
            case "Colecciones: List" -> guide(
                    "List representa una secuencia ordenada que permite duplicados y cuyo tamaño puede cambiar.",
                    "List<String> nombres = new ArrayList<>();  nombres.add(\"Ana\");  nombres.get(0);",
                    "Programa contra la interfaz List y elige una implementación. add agrega, get consulta, remove elimina y size indica la cantidad. List.of crea una lista inmutable.",
                    "Los índices de List también empiezan en cero. No modifiques una lista inmutable; y evita eliminar elementos de una colección dentro de un for-each.");
            case "Colecciones: Set y Map" -> guide(
                    "Set guarda elementos únicos; Map asocia claves únicas con valores.",
                    "Set<String> etiquetas = new HashSet<>();  Map<Long, Usuario> usuarios = new HashMap<>();",
                    "Set.add devuelve false si el elemento ya existía. Map.put agrega/reemplaza; get obtiene el valor y containsKey distingue una clave ausente. Usa Set/Map como interfaces y elige implementación según orden y rendimiento.",
                    "HashSet y HashMap no garantizan orden. Las claves deben tener equals/hashCode consistentes; una clave mutable puede dejar una entrada inaccesible.");
            case "Genéricos" -> guide(
                    "Permiten parametrizar clases y métodos con tipos, obteniendo seguridad en compilación y evitando casts.",
                    "List<String> nombres;  static <T> T primero(List<T> datos) { return datos.get(0); }",
                    "Declara parámetros de tipo como <T>, <K, V>. Usa envoltorios como Integer para primitivas. Los límites como <T extends Number> restringen operaciones a una familia de tipos.",
                    "Los genéricos Java son invariantes: List<Integer> no es List<Number>. No uses tipos raw como List sin parámetro, porque pierdes comprobaciones.");
            case "Lambdas e interfaces funcionales" -> guide(
                    "Expresan comportamiento como un valor y permiten pasar una operación a otro método.",
                    "x -> expresion  |  (a, b) -> { return a + b; }  |  Predicate<T>, Function<T,R>, Consumer<T>",
                    "Una lambda implementa la única operación abstracta de una interfaz funcional. Predicate prueba, Function transforma, Consumer consume sin devolver y Supplier produce un valor.",
                    "Las variables locales capturadas deben ser final o efectivamente finales. Elige un bloque con return solo cuando realmente necesitas varias instrucciones.");
            case "Streams" -> guide(
                    "Procesan datos en una canalización declarativa de operaciones intermedias y una operación terminal.",
                    "coleccion.stream().filter(x -> condicion).map(x -> cambio).toList();",
                    "filter conserva elementos, map los transforma, sorted ordena y collect/toList/count/sum termina el procesamiento. Las operaciones intermedias son perezosas y se ejecutan al solicitar el resultado.",
                    "Un stream se consume una sola vez. Evita efectos secundarios dentro de map/filter y no uses parallelStream sin medir y comprender la concurrencia.");
            case "null y Optional" -> guide(
                    "null expresa ausencia de referencia; Optional hace explícito que una operación puede no producir resultado.",
                    "Optional<String> buscar();  resultado.map(String::trim).orElse(\"Sin dato\");",
                    "Usa Optional.of para un valor no nulo, ofNullable para uno posiblemente nulo y empty para ausencia. map transforma si existe; orElse/orElseGet provee alternativa.",
                    "No llames get sin comprobar presencia. Optional no sustituye automáticamente todos los campos, parámetros ni colecciones; evita devolver null desde un método Optional.");
            case "Fechas y horas" -> guide(
                    "java.time representa fechas, horas e instantes con tipos inmutables y operaciones seguras.",
                    "LocalDate.of(2026, 1, 10).plusDays(7);  Instant.now();  ZonedDateTime.now(zona);",
                    "LocalDate es fecha sin hora; LocalTime hora; LocalDateTime fecha/hora sin zona; Instant instante UTC; ZonedDateTime conserva zona. DateTimeFormatter analiza y presenta formatos.",
                    "Las operaciones devuelven un nuevo valor: asigna el resultado. Para eventos entre zonas, no guardes LocalDateTime sin contexto de zona.");
            case "Archivos y try-with-resources" -> guide(
                    "Lee, escribe y administra archivos con Path/Files y libera automáticamente recursos como streams.",
                    "Path p = Path.of(\"datos.txt\");  try (var r = Files.newBufferedReader(p)) { ... }",
                    "Files ofrece readString/writeString para archivos pequeños y streams para grandes. try-with-resources cierra cada recurso al finalizar incluso si se produce una excepción.",
                    "Las operaciones pueden lanzar IOException. Valida rutas y permisos, evita cargar archivos enormes completos y no cierres por accidente recursos que no creaste.");
            case "Anotaciones" -> guide(
                    "Añaden metadatos que herramientas, compiladores o frameworks inspeccionan para validar o configurar comportamiento.",
                    "@Override  |  @Deprecated  |  @interface MiAnotacion { String value(); }",
                    "@Override verifica sobrescritura; @Deprecated advierte de una API antigua. Spring usa anotaciones para declarar componentes, configuración y rutas, interpretadas por el framework.",
                    "Una anotación no implementa por sí sola la lógica anotada. El comportamiento depende de quién la procese y de la configuración de ejecución.");
            case "Concurrencia básica" -> guide(
                    "Permite realizar varias tareas de forma concurrente; ExecutorService administra trabajadores y Future representa un resultado pendiente.",
                    "try (var executor = Executors.newSingleThreadExecutor()) { Future<T> f = executor.submit(tarea); }",
                    "Envía Callable/Runnable al executor, obtiene el resultado con Future.get y apaga el executor cuando termina. Prefiere inmutabilidad y evita compartir estado mutable.",
                    "get puede bloquear y lanzar excepciones; establece límites de espera cuando haga falta. No crees hilos sin límite ni asumas que operaciones compuestas son atómicas.");
            case "Pruebas y aserciones" -> guide(
                    "Las pruebas verifican que el comportamiento real coincida con el esperado y alertan cuando un cambio rompe algo.",
                    "assertEquals(esperado, actual);  assertThrows(Tipo.class, () -> llamada());",
                    "Escribe casos pequeños con Arrange (preparar), Act (ejecutar) y Assert (verificar), incluyendo límites y errores. En el curso el programa usa una comprobación explícita; proyectos Maven suelen usar JUnit.",
                    "Las aserciones Java requieren -ea y no sustituyen un framework de pruebas. Una prueba debe ser determinista y no depender del orden de otras pruebas.");
            case "Maven y el proyecto Java" -> guide(
                    "Maven describe el proyecto, resuelve dependencias y automatiza fases reproducibles de compilación y pruebas.",
                    "pom.xml: <groupId>...</groupId>, <artifactId>...</artifactId>, <version>...</version>; mvn test",
                    "El POM declara coordenadas, Java, plugins y dependencias. El ciclo incluye validate, compile, test y package. Ejecuta mvn test y luego mvn package; los fuentes suelen ir en src/main/java.",
                    "Este comprobador compila Java directamente, no lee pom.xml ni descarga librerías. No agregues dependencias sin revisar versiones y compatibilidad.");
            case "Diseño: depender de interfaces" -> guide(
                    "Reduce el acoplamiento al hacer que la lógica use un contrato en vez de construir directamente un proveedor concreto.",
                    "class Servicio { private final Repositorio repo; Servicio(Repositorio repo) { this.repo = repo; } }",
                    "Define una interfaz pequeña; recibe la implementación por constructor; prueba el servicio con una implementación alternativa. Spring puede crear y conectar estas dependencias como beans.",
                    "No crees una interfaz para cada clase sin necesidad. Mantén el constructor explícito y no hagas que el servicio busque dependencias globalmente.");
            case "Depuración y errores" -> guide(
                    "La depuración ayuda a encontrar dónde y por qué cambia el programa de forma inesperada.",
                    "Breakpoint → Step Over / Step Into → inspeccionar variables → evaluar condición",
                    "Lee el tipo y mensaje de la excepción, revisa el stack trace desde tu código y reproduce el caso mínimo. Un breakpoint pausa la ejecución; avanza línea a línea observando los valores.",
                    "La línea del error señala dónde se detectó, no necesariamente dónde nació el dato inválido. Distingue error de compilación, excepción y resultado lógico incorrecto.");
            case "HTTP y REST" -> guide(
                    "HTTP define mensajes cliente-servidor; REST organiza recursos y usa verbos y códigos de estado coherentes.",
                    "GET /api/productos/7  →  200 OK;  POST /api/productos  →  201 Created",
                    "GET consulta; POST suele crear; PUT reemplaza; PATCH modifica parcialmente; DELETE elimina. 2xx es éxito, 4xx problema de solicitud y 5xx fallo del servidor.",
                    "No uses GET para cambiar estado. Valida entrada, elige estados como 400/404/201 y no confundas una ruta REST con una página HTML.");
            case "JSON y DTO" -> guide(
                    "JSON transporta estructuras de datos; DTO define explícitamente qué campos entran o salen de un límite como una API.",
                    "{\"id\": 7, \"nombre\": \"Libro\"}  |  record ProductoDto(long id, String nombre) {}",
                    "Mantén DTOs separados del modelo interno cuando los contratos deban ser estables. Valida los datos recibidos y usa una biblioteca como Jackson para serializar/deserializar JSON.",
                    "El ejemplo del curso solo construye texto ilustrativo: concatenar JSON no es seguro para datos arbitrarios. En aplicaciones reales, delega escape y conversión a una biblioteca.");
            case "SQL y JDBC" -> guide(
                    "SQL consulta y modifica una base relacional; JDBC conecta Java con ella y devuelve resultados tipados.",
                    "String sql = \"SELECT id FROM producto WHERE id = ?\"; PreparedStatement ps = c.prepareStatement(sql); ps.setLong(1, id);",
                    "Abre Connection, prepara SQL, enlaza cada parámetro con el tipo adecuado y procesa ResultSet en try-with-resources. Ejecuta consultas con executeQuery y cambios con executeUpdate.",
                    "No concatenes entrada en SQL: usa PreparedStatement. El ejemplo es conceptual y no abre una conexión ni ejecuta SQL porque no configura base de datos ni driver.");
            case "Logs" -> guide(
                    "Registran qué ocurrió en una aplicación para diagnosticar fallos y entender su comportamiento en desarrollo y producción.",
                    "logger.info(\"Pedido creado: {}\", id);  logger.warn(...);  logger.error(\"Falló\", error);",
                    "Usa niveles DEBUG/INFO/WARN/ERROR según importancia, añade identificadores de contexto y deja que una biblioteca como SLF4J gestione el destino y formato.",
                    "No registres contraseñas, tokens ni datos personales innecesarios. No uses System.out como sistema de logs de una aplicación de servidor.");
            default -> throw new IllegalArgumentException("No existe una guía detallada para el tema: " + title);
        };
    }

    private static String guide(String purpose, String syntax, String usage, String caution) {
        return "PARA QUÉ SIRVE\n" + purpose
                + "\n\nSINTAXIS CLAVE\n" + syntax
                + "\n\nCÓMO SE UTILIZA\n" + usage
                + "\n\nERRORES Y BUENAS PRÁCTICAS\n" + caution
                + "\n\nPráctica: carga el ejemplo de este tema en el comprobador, ejecútalo y modifica un valor o una condición para observar el cambio.";
    }

    private static Lesson lesson(String title, String explanation, String example,
            String runnableCode, String question, int correctOption, String feedback,
            int diagramIndex, String... options) {
        return new Lesson(title, explanation, example, runnableCode, question,
                options, correctOption, feedback, diagramIndex, buildGuide(title));
    }

    private final JFrame frame = new JFrame("Aprende Java paso a paso");
    private final DefaultListModel<String> lessonListModel = new DefaultListModel<>();
    private final JList<String> lessonList = new JList<>(lessonListModel);
    private final JLabel lessonTitle = new JLabel();
    private final JLabel explanation = new JLabel();
    private final JTextArea guide = new JTextArea();
    private final JTextArea example = new JTextArea();
    private final JTextArea editor = new JTextArea();
    private final JTextArea output = new JTextArea();
    private final DiagramPanel diagram = new DiagramPanel();
    private final JLabel runStatus = new JLabel("Listo para practicar");
    private final JButton runButton = new JButton("▶  Ejecutar código");
    private final boolean[] completed = new boolean[LESSONS.length];
    private int selectedLesson = -1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().show());
    }

    private void show() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1080, 700));
        frame.setSize(1280, 820);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BACKGROUND);
        frame.setLayout(new BorderLayout());
        frame.add(createHeader(), BorderLayout.NORTH);
        frame.add(createWorkspace(), BorderLayout.CENTER);

        for (int i = 0; i < LESSONS.length; i++) {
            lessonListModel.addElement(lessonLabel(i));
        }
        lessonList.setSelectedIndex(0);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                frame.dispose();
            }
        });
        frame.setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 4));
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Aprende Java paso a paso");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        JLabel subtitle = new JLabel("Explora un tema, mira el dibujo y prueba tu propio código.");
        subtitle.setForeground(new Color(213, 223, 239));
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JPanel labels = new JPanel(new BorderLayout(0, 3));
        labels.setOpaque(false);
        labels.add(title, BorderLayout.NORTH);
        labels.add(subtitle, BorderLayout.SOUTH);
        header.add(labels, BorderLayout.WEST);
        return header;
    }

    private JSplitPane createWorkspace() {
        JPanel navigation = createNavigation();
        JPanel study = createStudyPanel();
        JPanel practice = createPracticePanel();

        JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, study, practice);
        rightSplit.setResizeWeight(0.48);
        rightSplit.setDividerLocation(0.48);
        rightSplit.setBorder(null);

        JSplitPane workspace = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigation, rightSplit);
        workspace.setResizeWeight(0);
        workspace.setDividerLocation(225);
        workspace.setBorder(null);
        return workspace;
    }

    private JPanel createNavigation() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 14, 14, 14));

        JLabel heading = new JLabel("TEMAS DEL CURSO");
        heading.setForeground(MUTED);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 11));
        panel.add(heading, BorderLayout.NORTH);

        lessonList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lessonList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lessonList.setFixedCellHeight(48);
        lessonList.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        lessonList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(0, 9, 0, 5));
                label.setForeground(isSelected ? Color.WHITE : TEXT);
                label.setBackground(isSelected ? BLUE : Color.WHITE);
                label.setText("<html><div style='width: 175px;'>" + value + "</div></html>");
                return label;
            }
        });
        lessonList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && lessonList.getSelectedIndex() >= 0) {
                showLesson(lessonList.getSelectedIndex());
            }
        });
        panel.add(new JScrollPane(lessonList), BorderLayout.CENTER);

        JButton quizButton = new JButton("Comprobar lo aprendido");
        quizButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        quizButton.addActionListener(event -> showQuiz());
        panel.add(quizButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createStudyPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 18, 16));

        lessonTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lessonTitle.setForeground(NAVY);
        explanation.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        explanation.setForeground(TEXT);
        explanation.setVerticalAlignment(SwingConstants.TOP);
        explanation.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));

        JPanel intro = new JPanel(new BorderLayout());
        intro.setOpaque(false);
        intro.add(lessonTitle, BorderLayout.NORTH);
        intro.add(explanation, BorderLayout.CENTER);
        panel.add(intro, BorderLayout.NORTH);

        diagram.setPreferredSize(new Dimension(500, 300));
        JPanel diagramCard = cardPanel();
        diagramCard.setLayout(new BorderLayout());
        JLabel diagramHeading = sectionLabel("ASÍ SE VE LA IDEA");
        diagramHeading.setBorder(BorderFactory.createEmptyBorder(14, 16, 0, 0));
        diagramCard.add(diagramHeading, BorderLayout.NORTH);
        diagramCard.add(diagram, BorderLayout.CENTER);

        example.setEditable(false);
        example.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        example.setForeground(new Color(226, 236, 250));
        example.setBackground(new Color(35, 44, 60));
        example.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
        example.setTabSize(4);
        JScrollPane exampleScroll = new JScrollPane(example);
        exampleScroll.setBorder(BorderFactory.createEmptyBorder());
        exampleScroll.setPreferredSize(new Dimension(400, 180));
        JPanel codeCard = cardPanel();
        codeCard.setLayout(new BorderLayout(0, 8));
        JLabel codeHeading = sectionLabel("EJEMPLO DE CÓDIGO");
        codeHeading.setBorder(BorderFactory.createEmptyBorder(12, 14, 0, 0));
        codeCard.add(codeHeading, BorderLayout.NORTH);
        codeCard.add(exampleScroll, BorderLayout.CENTER);

        guide.setEditable(false);
        guide.setLineWrap(true);
        guide.setWrapStyleWord(true);
        guide.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        guide.setForeground(TEXT);
        guide.setBackground(Color.WHITE);
        guide.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JTabbedPane studyTabs = new JTabbedPane();
        studyTabs.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studyTabs.addTab("Guía detallada", new JScrollPane(guide));
        studyTabs.addTab("Ilustración", diagramCard);
        studyTabs.addTab("Ejemplo", codeCard);
        panel.add(studyTabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPracticePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 16, 18, 20));

        JPanel heading = new JPanel(new BorderLayout(0, 4));
        heading.setOpaque(false);
        JLabel title = new JLabel("Prueba tu código");
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        title.setForeground(NAVY);
        JLabel hint = new JLabel("Edita el ejemplo y ejecútalo en tu computadora.");
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(MUTED);
        heading.add(title, BorderLayout.NORTH);
        heading.add(hint, BorderLayout.SOUTH);
        panel.add(heading, BorderLayout.NORTH);

        editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        editor.setTabSize(4);
        editor.setText(LESSONS[0].runnableCode);
        JScrollPane editorScroll = new JScrollPane(editor);
        editorScroll.setBorder(BorderFactory.createLineBorder(new Color(216, 224, 235)));
        JPanel editorCard = cardPanel();
        editorCard.setLayout(new BorderLayout());
        JLabel editorHeading = sectionLabel("EDITOR");
        editorHeading.setBorder(BorderFactory.createEmptyBorder(10, 12, 8, 0));
        editorCard.add(editorHeading, BorderLayout.NORTH);
        editorCard.add(editorScroll, BorderLayout.CENTER);

        JButton loadExample = new JButton("Cargar ejemplo del tema");
        loadExample.addActionListener(event -> editor.setText(LESSONS[selectedLesson].runnableCode));
        runButton.setForeground(Color.WHITE);
        runButton.setBackground(GREEN);
        runButton.setOpaque(true);
        runButton.setBorderPainted(false);
        runButton.setFocusPainted(false);
        runButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        runButton.addActionListener(event -> runStudentCode());

        JPanel buttons = new JPanel(new BorderLayout(8, 0));
        buttons.setOpaque(false);
        buttons.add(loadExample, BorderLayout.CENTER);
        buttons.add(runButton, BorderLayout.EAST);

        JPanel editorSection = new JPanel(new BorderLayout(0, 8));
        editorSection.setOpaque(false);
        editorSection.add(editorCard, BorderLayout.CENTER);
        editorSection.add(buttons, BorderLayout.SOUTH);

        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        output.setBackground(new Color(35, 44, 60));
        output.setForeground(new Color(226, 236, 250));
        output.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        JScrollPane outputScroll = new JScrollPane(output);
        outputScroll.setBorder(BorderFactory.createLineBorder(new Color(216, 224, 235)));

        JPanel outputPanel = new JPanel(new BorderLayout(0, 6));
        outputPanel.setOpaque(false);
        outputPanel.setPreferredSize(new Dimension(400, 180));
        JLabel outputHeading = sectionLabel("RESULTADO");
        outputPanel.add(outputHeading, BorderLayout.NORTH);
        outputPanel.add(outputScroll, BorderLayout.CENTER);

        runStatus.setForeground(MUTED);
        runStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        JPanel bottom = new JPanel(new BorderLayout(0, 7));
        bottom.setOpaque(false);
        bottom.add(runStatus, BorderLayout.NORTH);
        bottom.add(outputPanel, BorderLayout.CENTER);

        JSplitPane vertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT, editorSection, bottom);
        vertical.setResizeWeight(0.63);
        vertical.setDividerLocation(0.63);
        vertical.setBorder(null);
        panel.add(vertical, BorderLayout.CENTER);

        JLabel warning = new JLabel("<html>El código se ejecuta localmente y no está aislado.</html>");
        warning.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        warning.setForeground(MUTED);
        panel.add(warning, BorderLayout.SOUTH);
        return panel;
    }

    private void showLesson(int index) {
        selectedLesson = index;
        Lesson lesson = LESSONS[index];
        diagram.lessonIndex = diagramForLesson(lesson.title);
        lessonTitle.setText((index + 1) + ". " + lesson.title);
        explanation.setText("<html><div style='width: 440px; line-height: 145%;'>"
                + lesson.explanation + "</div></html>");
        guide.setText(lesson.guide);
        guide.setCaretPosition(0);
        example.setText(lesson.example);
        example.setCaretPosition(0);
        editor.setText(lesson.runnableCode);
        output.setText("");
        runStatus.setText("Listo para practicar");
        lessonListModel.set(index, lessonLabel(index));
        frame.repaint();
    }

    private static int diagramForLesson(String title) {
        return switch (title) {
            case "Tu primer programa" -> 0;
            case "Tipos y variables", "Clases envoltorio", "Operadores y conversiones",
                    "Decimales y dinero" -> 1;
            case "Decisiones: if y switch" -> 2;
            case "Ciclos" -> 3;
            case "Métodos y sobrecarga" -> 4;
            case "Arreglos" -> 6;
            case "Texto: String" -> 7;
            case "Excepciones" -> 8;
            case "Colecciones: List", "Colecciones: Set y Map" -> 9;
            case "Streams" -> 10;
            case "HTTP y REST", "JSON y DTO" -> 11;
            case "SQL y JDBC" -> 12;
            case "Maven y el proyecto Java" -> 13;
            case "Fechas y horas" -> 14;
            case "Concurrencia básica" -> 15;
            default -> 5;
        };
    }

    private String lessonLabel(int index) {
        return (completed[index] ? "✓  " : "   ") + (index + 1) + ". " + LESSONS[index].title;
    }

    private void showQuiz() {
        if (selectedLesson < 0) {
            return;
        }
        Lesson lesson = LESSONS[selectedLesson];
        Object selected = JOptionPane.showInputDialog(
                frame,
                lesson.question,
                "Repaso: " + lesson.title,
                JOptionPane.QUESTION_MESSAGE,
                null,
                lesson.options,
                lesson.options[0]);
        if (selected == null) {
            return;
        }
        int answer = -1;
        for (int i = 0; i < lesson.options.length; i++) {
            if (lesson.options[i].equals(selected)) {
                answer = i + 1;
                break;
            }
        }
        boolean correct = answer == lesson.correctOption;
        if (correct) {
            completed[selectedLesson] = true;
            lessonListModel.set(selectedLesson, lessonLabel(selectedLesson));
        }
        JOptionPane.showMessageDialog(
                frame,
                (correct ? "¡Correcto! " : "Aún no. ") + lesson.feedback,
                correct ? "¡Bien hecho!" : "Repasa el tema",
                correct ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private void runStudentCode() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            JOptionPane.showMessageDialog(frame,
                    "No se encontró el compilador de Java. Instala un JDK (Java 17 o posterior).",
                    "Falta el JDK", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String source = editor.getText();
        runButton.setEnabled(false);
        output.setText("");
        runStatus.setText("Compilando...");

        new SwingWorker<RunResult, Void>() {
            @Override
            protected RunResult doInBackground() throws Exception {
                return compileAndRun(compiler, source);
            }

            @Override
            protected void done() {
                runButton.setEnabled(true);
                try {
                    RunResult result = get();
                    output.setText(result.output);
                    output.setCaretPosition(0);
                    runStatus.setText(result.timedOut
                            ? "Se detuvo: superó el límite de " + RUN_TIMEOUT_SECONDS + " segundos."
                            : result.success ? "Ejecución finalizada." : "Revisa los errores de compilación o ejecución.");
                } catch (Exception exception) {
                    runStatus.setText("No se pudo ejecutar el código.");
                    output.setText("Error: " + exception.getMessage());
                }
            }
        }.execute();
    }

    private static RunResult compileAndRun(JavaCompiler compiler, String source) throws Exception {
        Path workDirectory = Files.createTempDirectory("aprende-java-");
        try {
            Path sourceFile = workDirectory.resolve("AppStudent.java");
            Path outputFile = workDirectory.resolve("resultado.txt");
            Files.writeString(sourceFile, source, StandardCharsets.UTF_8);

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            try (var fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
                Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjects(sourceFile.toFile());
                boolean compiled = Boolean.TRUE.equals(compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        java.util.List.of("--release", "17", "-encoding", "UTF-8", "-d", workDirectory.toString()),
                        null,
                        units).call());
                if (!compiled) {
                    StringBuilder errors = new StringBuilder("No se pudo compilar:\n");
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        errors.append("Línea ").append(diagnostic.getLineNumber())
                                .append(": ").append(diagnostic.getMessage(null)).append('\n');
                    }
                    return new RunResult(errors.toString(), false, false);
                }
            }

            Matcher packageMatcher = Pattern.compile(
                    "(?m)^\\s*package\\s+([A-Za-z_$][\\w$]*(?:\\.[A-Za-z_$][\\w$]*)*)\\s*;")
                    .matcher(source);
            String mainClass = packageMatcher.find()
                    ? packageMatcher.group(1) + ".AppStudent"
                    : "AppStudent";

            Process process = new ProcessBuilder(
                    javaExecutable(),
                    "-Dfile.encoding=UTF-8",
                    "-Dstdout.encoding=UTF-8",
                    "-Dstderr.encoding=UTF-8",
                    "-cp",
                    workDirectory.toString(),
                    mainClass)
                    .redirectErrorStream(true)
                    .redirectOutput(outputFile.toFile())
                    .start();
            boolean finished = process.waitFor(Duration.ofSeconds(RUN_TIMEOUT_SECONDS).toMillis(),
                    TimeUnit.MILLISECONDS);
            if (!finished) {
                process.destroyForcibly();
                process.waitFor();
            }

            String result = Files.exists(outputFile)
                    ? new String(Files.readAllBytes(outputFile), StandardCharsets.UTF_8)
                    : "";
            if (result.length() > MAX_OUTPUT_CHARACTERS) {
                result = result.substring(0, MAX_OUTPUT_CHARACTERS) + "\n... (salida recortada)";
            }
            if (!finished) {
                result += "\n\nEl programa se detuvo porque superó el límite de tiempo.";
            } else if (process.exitValue() != 0) {
                result += "\nEl programa terminó con código " + process.exitValue() + ".";
            }
            if (result.isBlank()) {
                result = "(El programa terminó sin mostrar texto.)";
            }
            return new RunResult(result, finished && process.exitValue() == 0, !finished);
        } finally {
            deleteTemporaryDirectory(workDirectory);
        }
    }

    private static String javaExecutable() {
        String executable = System.getProperty("os.name").toLowerCase().contains("win")
                ? "java.exe" : "java";
        return Path.of(System.getProperty("java.home"), "bin", executable).toString();
    }

    private static void deleteTemporaryDirectory(Path directory) throws IOException {
        try (Stream<Path> paths = Files.walk(directory)) {
            for (Path path : paths.sorted(java.util.Comparator.reverseOrder()).toList()) {
                Files.deleteIfExists(path);
            }
        }
    }

    private JPanel cardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(228, 233, 241)));
        return panel;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(MUTED);
        return label;
    }

    private static class DiagramPanel extends JPanel {
        private int lessonIndex;

        private DiagramPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth();
            int height = getHeight();
            g.setColor(new Color(248, 250, 253));
            g.fillRoundRect(12, 12, width - 24, height - 24, 18, 18);

            switch (lessonIndex) {
                case 0 -> drawSequence(g, width, height);
                case 1 -> drawVariables(g, width, height);
                case 2 -> drawDecision(g, width, height);
                case 3 -> drawLoop(g, width, height);
                case 4 -> drawMethod(g, width, height);
                case 6 -> drawArray(g, width, height);
                case 7 -> drawEquality(g, width, height);
                case 8 -> drawException(g, width, height);
                case 9 -> drawCollection(g, width, height);
                case 10 -> drawStream(g, width, height);
                case 11 -> drawHttp(g, width, height);
                case 12 -> drawDatabase(g, width, height);
                case 13 -> drawBuild(g, width, height);
                case 14 -> drawDate(g, width, height);
                case 15 -> drawThread(g, width, height);
                default -> drawClassAndObject(g, width, height);
            }
            g.dispose();
        }

        private void drawSequence(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 100, 34, 200, 46, "Inicio: main", new Color(225, 236, 252), BLUE);
            arrow(g, center, 80, center, 110);
            node(g, center - 120, 111, 240, 52, "System.out.println(...)", new Color(226, 244, 236), GREEN);
            arrow(g, center, 164, center, 194);
            node(g, center - 100, 195, 200, 46, "Fin del programa", new Color(240, 234, 250), new Color(116, 83, 169));
        }

        private void drawVariables(Graphics2D g, int width, int height) {
            int boxWidth = Math.min(150, (width - 72) / 2);
            int left = width / 2 - boxWidth - 10;
            int right = width / 2 + 10;
            node(g, left, 42, boxWidth, 62, "int edad", new Color(225, 236, 252), BLUE);
            node(g, right, 42, boxWidth, 62, "16", new Color(226, 244, 236), GREEN);
            arrow(g, left + boxWidth, 73, right - 4, 73);
            g.setColor(MUTED);
            centered(g, "tipo + nombre", width / 2, 132);
            node(g, width / 2 - 100, 156, 200, 52, "String nombre = \"Ana\"", new Color(255, 242, 219), new Color(178, 119, 31));
        }

        private void drawDecision(Graphics2D g, int width, int height) {
            int center = width / 2;
            diamond(g, center, 78, 105, 58, "nota >= 6?");
            arrow(g, center - 54, 100, center - 112, 156);
            arrow(g, center + 54, 100, center + 112, 156);
            g.setColor(GREEN);
            centered(g, "Sí", center - 104, 139);
            g.setColor(new Color(190, 83, 76));
            centered(g, "No", center + 104, 139);
            node(g, center - 174, 160, 122, 42, "Aprobado", new Color(226, 244, 236), GREEN);
            node(g, center + 52, 160, 144, 42, "Practicar más", new Color(252, 231, 229), new Color(190, 83, 76));
        }

        private void drawLoop(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 95, 29, 190, 44, "i = 1", new Color(225, 236, 252), BLUE);
            arrow(g, center, 74, center, 98);
            diamond(g, center, 126, 118, 52, "¿i <= 5?");
            arrow(g, center - 61, 126, center - 145, 126);
            arrow(g, center - 145, 126, center - 145, 187);
            arrow(g, center - 145, 187, center - 95, 187);
            g.setColor(GREEN);
            centered(g, "Sí", center - 105, 115);
            node(g, center - 92, 166, 184, 42, "Mostrar i; luego i++", new Color(226, 244, 236), GREEN);
            arrow(g, center + 60, 126, center + 139, 126);
            g.setColor(new Color(190, 83, 76));
            centered(g, "No", center + 104, 115);
            node(g, center + 92, 166, 92, 42, "Terminar", new Color(252, 231, 229), new Color(190, 83, 76));
        }

        private void drawMethod(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 120, 42, 240, 50, "sumar(3, 4)", new Color(225, 236, 252), BLUE);
            arrow(g, center, 94, center, 126);
            node(g, center - 130, 127, 260, 55, "sumar(a, b) { return a + b; }", new Color(226, 244, 236), GREEN);
            arrow(g, center, 184, center, 212);
            node(g, center - 87, 213, 174, 44, "Devuelve 7", new Color(255, 242, 219), new Color(178, 119, 31));
        }

        private void drawClassAndObject(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 100, 34, 200, 75, "CLASE: Perro\nmolde / descripción", new Color(225, 236, 252), BLUE);
            arrow(g, center, 112, center, 151);
            node(g, center - 112, 155, 224, 72, "OBJETO: mascota\nnombre = \"Luna\"", new Color(226, 244, 236), GREEN);
            g.setColor(MUTED);
            centered(g, "new Perro()", center, 143);
        }

        private void drawArray(Graphics2D g, int width, int height) {
            int center = width / 2;
            g.setColor(MUTED);
            centered(g, "Índice", center, 56);
            int boxWidth = Math.min(82, (width - 72) / 4);
            int start = center - (boxWidth * 4 + 30) / 2;
            for (int i = 0; i < 4; i++) {
                int x = start + i * (boxWidth + 10);
                g.setColor(MUTED);
                centered(g, Integer.toString(i), x + boxWidth / 2, 95);
                node(g, x, 105, boxWidth, 54, "valor " + (i + 1),
                        new Color(225, 236, 252), BLUE);
            }
            g.setColor(GREEN);
            centered(g, "índices: 0 hasta length - 1", center, 192);
        }

        private void drawEquality(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 178, 62, 150, 54, "String A", new Color(225, 236, 252), BLUE);
            node(g, center + 28, 62, 150, 54, "String B", new Color(225, 236, 252), BLUE);
            arrow(g, center - 25, 82, center + 24, 82);
            g.setColor(GREEN);
            centered(g, "equals(): contenido igual", center, 159);
            g.setColor(MUTED);
            centered(g, "== compara si es la misma referencia", center, 193);
        }

        private void drawException(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 100, 34, 200, 46, "Ejecutar try", new Color(225, 236, 252), BLUE);
            arrow(g, center, 82, center, 107);
            diamond(g, center, 135, 120, 52, "¿Hay error?");
            arrow(g, center - 60, 135, center - 146, 135);
            arrow(g, center + 60, 135, center + 146, 135);
            node(g, center - 202, 166, 112, 42, "catch", new Color(255, 242, 219), new Color(178, 119, 31));
            node(g, center + 90, 166, 112, 42, "Continuar", new Color(226, 244, 236), GREEN);
        }

        private void drawCollection(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 182, 64, 150, 70, "List\norden / repetidos",
                    new Color(225, 236, 252), BLUE);
            node(g, center + 32, 64, 150, 70, "Map\nclave → valor",
                    new Color(226, 244, 236), GREEN);
            g.setColor(MUTED);
            centered(g, "Colecciones genéricas: List<T> · Map<K, V>", center, 183);
        }

        private void drawStream(Graphics2D g, int width, int height) {
            int center = width / 2;
            int boxWidth = 94;
            int gap = 12;
            int start = center - (boxWidth * 3 + gap * 2) / 2;
            String[] stages = {"source", "filter", "map"};
            for (int i = 0; i < stages.length; i++) {
                int x = start + i * (boxWidth + gap);
                node(g, x, 90, boxWidth, 48, stages[i],
                        i == 1 ? new Color(255, 242, 219) : new Color(225, 236, 252),
                        i == 1 ? new Color(178, 119, 31) : BLUE);
                if (i < stages.length - 1) {
                    arrow(g, x + boxWidth, 114, x + boxWidth + gap - 2, 114);
                }
            }
            arrow(g, center + 155, 114, center + 184, 114);
            g.setColor(GREEN);
            centered(g, "operación terminal: collect / sum", center, 177);
        }

        private void drawHttp(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 174, 76, 135, 65, "Cliente\nGET /api/items",
                    new Color(225, 236, 252), BLUE);
            arrow(g, center - 36, 98, center + 34, 98);
            node(g, center + 40, 76, 135, 65, "Servidor\n200 + JSON",
                    new Color(226, 244, 236), GREEN);
            g.setColor(MUTED);
            centered(g, "Solicitud → ruta → respuesta", center, 182);
        }

        private void drawDatabase(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 190, 84, 150, 60, "SQL con ?", new Color(225, 236, 252), BLUE);
            arrow(g, center - 36, 114, center + 10, 114);
            node(g, center + 16, 84, 164, 60, "JDBC\nparámetro enlazado",
                    new Color(226, 244, 236), GREEN);
            g.setColor(MUTED);
            centered(g, "Consulta + datos separados", center, 183);
        }

        private void drawBuild(Graphics2D g, int width, int height) {
            int center = width / 2;
            String[] stages = {"código", "compile", "test", "package"};
            int boxWidth = 88;
            int gap = 10;
            int start = center - (boxWidth * stages.length + gap * (stages.length - 1)) / 2;
            for (int i = 0; i < stages.length; i++) {
                int x = start + i * (boxWidth + gap);
                node(g, x, 91, boxWidth, 48, stages[i],
                        i == stages.length - 1 ? new Color(226, 244, 236) : new Color(225, 236, 252),
                        i == stages.length - 1 ? GREEN : BLUE);
                if (i < stages.length - 1) {
                    arrow(g, x + boxWidth, 115, x + boxWidth + gap - 2, 115);
                }
            }
            g.setColor(MUTED);
            centered(g, "Maven automatiza el ciclo del proyecto", center, 177);
        }

        private void drawDate(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 88, 52, 176, 54, "LocalDate", new Color(225, 236, 252), BLUE);
            arrow(g, center, 108, center, 141);
            node(g, center - 130, 144, 260, 55, "fecha inmutable + operaciones",
                    new Color(226, 244, 236), GREEN);
        }

        private void drawThread(Graphics2D g, int width, int height) {
            int center = width / 2;
            node(g, center - 170, 82, 130, 62, "Tarea", new Color(225, 236, 252), BLUE);
            arrow(g, center - 36, 113, center + 20, 113);
            node(g, center + 26, 82, 150, 62, "Executor", new Color(226, 244, 236), GREEN);
            g.setColor(MUTED);
            centered(g, "Future recibe el resultado después", center, 185);
        }

        private void node(Graphics2D g, int x, int y, int width, int height,
                String text, Color fill, Color outline) {
            g.setColor(fill);
            g.fillRoundRect(x, y, width, height, 16, 16);
            g.setColor(outline);
            g.setStroke(new BasicStroke(1.5f));
            g.drawRoundRect(x, y, width, height, 16, 16);
            g.setColor(TEXT);
            String[] lines = text.split("\n");
            FontMetrics metrics = g.getFontMetrics();
            int lineHeight = metrics.getHeight();
            int totalHeight = lines.length * lineHeight;
            int baseline = y + (height - totalHeight) / 2 + metrics.getAscent();
            for (String line : lines) {
                centered(g, line, x + width / 2, baseline);
                baseline += lineHeight;
            }
        }

        private void diamond(Graphics2D g, int centerX, int centerY, int width, int height, String text) {
            int[] x = {centerX, centerX + width / 2, centerX, centerX - width / 2};
            int[] y = {centerY - height / 2, centerY, centerY + height / 2, centerY};
            g.setColor(new Color(255, 242, 219));
            g.fillPolygon(x, y, 4);
            g.setColor(new Color(178, 119, 31));
            g.setStroke(new BasicStroke(1.5f));
            g.drawPolygon(x, y, 4);
            g.setColor(TEXT);
            centered(g, text, centerX, centerY + g.getFontMetrics().getAscent() / 2 - 2);
        }

        private void arrow(Graphics2D g, int x1, int y1, int x2, int y2) {
            g.setColor(new Color(128, 143, 164));
            g.setStroke(new BasicStroke(1.7f));
            g.drawLine(x1, y1, x2, y2);
            double angle = Math.atan2(y2 - y1, x2 - x1);
            int size = 7;
            int[] x = {
                x2,
                x2 - (int) (size * Math.cos(angle - Math.PI / 6)),
                x2 - (int) (size * Math.cos(angle + Math.PI / 6))
            };
            int[] y = {
                y2,
                y2 - (int) (size * Math.sin(angle - Math.PI / 6)),
                y2 - (int) (size * Math.sin(angle + Math.PI / 6))
            };
            g.fillPolygon(x, y, 3);
        }

        private void centered(Graphics2D g, String text, int centerX, int baseline) {
            g.drawString(text, centerX - g.getFontMetrics().stringWidth(text) / 2, baseline);
        }
    }

    private static class Lesson {
        private final String title;
        private final String explanation;
        private final String guide;
        private final String example;
        private final String runnableCode;
        private final String question;
        private final String[] options;
        private final int correctOption;
        private final String feedback;
        private final int diagramIndex;

        private Lesson(String title, String explanation, String example, String runnableCode,
                String question, String[] options, int correctOption, String feedback,
                int diagramIndex, String guide) {
            this.title = title;
            this.explanation = explanation;
            this.guide = guide;
            this.example = example;
            this.runnableCode = runnableCode;
            this.question = question;
            this.options = options;
            this.correctOption = correctOption;
            this.feedback = feedback;
            this.diagramIndex = diagramIndex;
        }
    }

    private static class RunResult {
        private final String output;
        private final boolean success;
        private final boolean timedOut;

        private RunResult(String output, boolean success, boolean timedOut) {
            this.output = output;
            this.success = success;
            this.timedOut = timedOut;
        }
    }
}
