# Aprende Java paso a paso

Aplicación educativa de escritorio en español para Windows. La ruta está ordenada para aprender primero Java y luego entender mejor los conceptos que aparecen en una aplicación Spring Boot. Cada tema incluye una explicación, una guía con propósito, sintaxis, uso y errores habituales, además de un diagrama, un ejemplo ejecutable y una pregunta de repaso.

## Ruta de aprendizaje

1. **Fundamentos:** primer programa, tipos y variables, clases envoltorio, operadores, `BigDecimal`, decisiones (`if` y `switch`), ciclos, arreglos, texto (`String`), entrada con `Scanner` y métodos.
2. **Programación orientada a objetos:** clases y objetos, constructores, encapsulación, `static` y `final`, herencia, polimorfismo, interfaces y abstracción, `enum` y `record`, paquetes y visibilidad.
3. **Java práctico:** excepciones, `equals`/`hashCode`/`toString`, colecciones (`List`, `Set`, `Map`), genéricos, lambdas, streams, `null` y `Optional`, fechas, archivos, anotaciones, concurrencia, pruebas, logs y depuración.
4. **Preparación para backend y Spring Boot:** Maven, diseño basado en interfaces e inyección de dependencias, HTTP/REST, JSON/DTO y SQL/JDBC.

El menú muestra cuáles temas aprobaste mientras la aplicación está abierta. El editor permite modificar, compilar y ejecutar ejemplos Java, y muestra tanto el resultado como los errores.

## Requisitos

- Windows
- JDK de Java instalado (Java 17 o posterior)

Para comprobar que Java está disponible, abre una terminal y ejecuta `java -version` y `javac -version`.

## Iniciar en Windows

Haz doble clic en `iniciar.bat`. El archivo compila el programa y abre la interfaz gráfica. También puedes abrir la carpeta en Visual Studio Code y ejecutar `App.java`.

En cada tema, consulta la pestaña **Guía detallada** y revisa **Ilustración** y **Ejemplo**. Usa **Cargar ejemplo del tema** para copiar un programa completo al editor y **Ejecutar código** para comprobarlo. Luego cambia un valor o condición y vuelve a ejecutarlo; usa la pregunta del tema para repasar. El editor compila con Java 17 y admite ejemplos con declaración de paquete.

El ejemplo de Maven explica las fases y el archivo `pom.xml`; este curso ejecuta ejemplos Java directamente y no requiere tener Maven instalado. Las lecciones de JSON y JDBC enseñan sus conceptos, pero no instalan bibliotecas ni conectan a servicios externos.

Los programas de práctica se detienen si tardan más de 8 segundos. El código se ejecuta en tu computadora y no está aislado; puede acceder a los recursos disponibles para tu usuario. Ejecuta solamente código que entiendas o en el que confíes.

## Iniciar desde una terminal

Desde la carpeta del proyecto, ejecuta:

```powershell
javac --release 17 -encoding UTF-8 -d bin src\App.java
java '-Dfile.encoding=UTF-8' -cp bin App
```
