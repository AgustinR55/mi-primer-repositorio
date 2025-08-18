# Guía de uso del programa "Escáner de IPs"

El programa cuenta con una interfaz gráfica amigable que facilita el proceso de escanear un rango de direcciones IP en una red. A continuación se describen los pasos a seguir:

1. Ingresar los datos
   
En la pantalla principal encontrarás tres campos de texto:

IP de inicio: la primera dirección IP del rango que se quiere analizar(Ejemplo: 192.168.0.1).
  
    IP de fin: la última dirección IP del rango(Ejemplo: 192.168.0.10).El programa analizará todas las direcciones comprendidas entre la IP    de inicio y la IP de fin.
    Tiempo de espera (ms): la cantidad de milisegundos que el programa esperará por la respuesta de cada IP(Ejemplo: 1000 (un segundo de       espera)). Si se ingresa un número negativo, el programa mostrará un mensaje de error.





3. Iniciar el escaneo

Presionar el botón Escanear.

La barra de progreso mostrará el avance del análisis.

El programa irá probando cada dirección del rango y evaluará si está activa.

3. Ver los resultados

Los resultados se mostrarán en una tabla, donde cada fila corresponde a una IP escaneada.

La tabla incluye:

Dirección IP

Nombre del equipo (si se logra resolver, de lo contrario aparece como “Nombre desconocido”)

Estado (ACTIVO o Inactivo)

Tiempo de respuesta en milisegundos

Debajo de la tabla se muestra la cantidad total de equipos que respondieron.

4. Opciones adicionales

Limpiar: borra todos los resultados y deja la pantalla lista para un nuevo análisis.

Guardar resultados: permite exportar la información obtenida a un archivo de texto para futuras consultas.

5. Ejemplo de uso

Supongamos que queremos escanear el rango de direcciones entre 192.168.1.1 y 192.168.1.5 con un tiempo de espera de 1000 ms.

Escribir en "IP de inicio": 192.168.1.1

Escribir en "IP de fin": 192.168.1.5

Escribir en "Tiempo de espera": 1000

Presionar Escanear

El programa mostrará qué dispositivos respondieron y su tiempo de respuesta.
