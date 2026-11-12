# Aprende Java paso a paso

Aplicación educativa de escritorio en español para Windows. La ruta está ordenada para aprender primero Java y luego entender mejor los conceptos que aparecen en una aplicación Spring Boot. Cada tema incluye una explicación, una guía con propósito, sintaxis, uso y errores habituales, además de un diagrama, un ejemplo ejecutable y una pregunta de repaso.

## Ruta de aprendizaje

1. **Fundamentos:** primer programa, tipos y variables, clases envoltorio, operadores, `BigDecimal`, decisiones (`if` y `switch`), ciclos, arreglos, texto (`String`), entrada con `Scanner` y métodos.
2. **Programación orientada a objetos:** clases y objetos, constructores, encapsulación, `static` y `final`, herencia, polimorfismo, interfaces y abstracción, `enum` y `record`, paquetes y visibilidad.
3. **Java práctico:** excepciones, `equals`/`hashCode`/`toString`, colecciones (`List`, `Set`, `Map`), genéricos, lambdas, streams, `null` y `Optional`, fechas, archivos, anotaciones, concurrencia, pruebas, logs y depuración.
4. **Preparación para backend y Spring Boot:** Maven, diseño basado en interfaces e inyección de dependencias, HTTP/REST, JSON/DTO y SQL/JDBC.

UML aparece como el segundo tema. Después de aprender clases y objetos, la ruta introduce interfaces, herencia y polimorfismo antes de continuar con temas avanzados de orientación a objetos.

El menú muestra cuáles temas aprobaste mientras la aplicación está abierta. El editor permite modificar, compilar y ejecutar ejemplos Java, y muestra tanto el resultado como los errores.

## Proyectos Java

La pestaña **Mi proyecto** permite crear un proyecto o abrir una carpeta existente. La pestaña **Archivos** muestra las clases Java; al seleccionar un archivo se abre en una pestaña independiente del editor. Usa **Nueva clase** y **Nueva interfaz** para crear tipos, **Guardar todo** para guardar cambios y **Compilar y ejecutar** para compilar juntos todos los archivos Java del proyecto y ejecutar una clase con `public static void main(String[] args)`.

Los proyectos nuevos usan `src/main/java` y empiezan con `Main.java`. Para ejecutar el proyecto, guarda todos los archivos. El entorno es educativo: compila con las bibliotecas estándar del JDK, pero todavía no resuelve dependencias Maven/Gradle ni incluye depurador integrado.

## Requisitos

- Windows
- JDK de Java instalado (Java 17 o posterior)

Para comprobar que Java está disponible, abre una terminal y ejecuta `java -version` y `javac -version`.

## Iniciar en Windows

Haz doble clic en `iniciar.bat`. El archivo compila el programa y abre la interfaz gráfica. También puedes abrir la carpeta en Visual Studio Code y ejecutar `App.java`.

### Instalador autónomo de Windows

El instalador `dist\AprendeJava-Setup-1.0.1.exe` instala la aplicación con su propio runtime y compilador de Java; los estudiantes no necesitan instalar Java ni tener internet para usarla. Para generarlo, ejecuta `construir-instalador.bat`. El proceso requiere un JDK 17 o posterior con `jpackage` e Inno Setup 6. Si el JDK no se detecta automáticamente, configura `JAVA_HOME` antes de iniciar la compilación. El instalador es por usuario y crea accesos directos en el menú Inicio y, opcionalmente, en el escritorio.

Esta versión está preparada para Windows x64; el instalador bloquea Windows de 32 bits y ARM64 porque el runtime incluido es x64. La compatibilidad garantizada depende de la versión de Windows admitida por el JDK con el que se generó el paquete. El instalador no está firmado digitalmente, por lo que Windows puede mostrar un aviso de editor desconocido.

La ventana organiza los **temas a la izquierda**, el **editor y su resultado en el centro** y la **explicación a la derecha**. En cada tema, consulta la guía paso a paso y revisa las pestañas **Ilustración** y **Ejemplo**. El editor muestra números de línea. Usa **Restaurar ejemplo** para volver al programa inicial, **Limpiar salida** para borrar el resultado y **Ejecutar código** o **Ctrl+Enter** para compilar y ejecutar. Luego cambia un valor o condición y compara la salida; usa la pregunta del tema para repasar. El editor compila con Java 17 y admite ejemplos con declaración de paquete.

El ejemplo de Maven explica las fases y el archivo `pom.xml`; este curso ejecuta ejemplos Java directamente y no requiere tener Maven instalado. Las lecciones de JSON y JDBC enseñan sus conceptos, pero no instalan bibliotecas ni conectan a servicios externos.

Los programas de práctica se detienen si tardan más de 8 segundos. El código se ejecuta en tu computadora y no está aislado; puede acceder a los recursos disponibles para tu usuario. Ejecuta solamente código que entiendas o en el que confíes.

## Iniciar desde una terminal

Desde la carpeta del proyecto, ejecuta:

```powershell
javac --release 17 -encoding UTF-8 -d bin src\App.java
java '-Dfile.encoding=UTF-8' -cp bin App
```
