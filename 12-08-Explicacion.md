Hoy día 05-08 realicé la clase Gui, la cual será la interfaz grafica del escaner consta de una ventana gráfica hecha en Java que simula el escaneo de direcciones IP(simula de momento). Te permite escribir una IP de inicio, una IP final, y un tiempo de espera, y al hacer clic en el botón "Escanear", muestra un mensaje simulando que está revisando ese rango de IPs. También tiene un botón para "Limpiar" todo lo escrito en los campos.
La ventana está organizada en tres partes:
Arriba: los campos de texto donde el usuario escribe las IPs y el tiempo.
En el medio: los botones para iniciar el escaneo o limpiar todo.
Abajo: un cuadro grande donde se muestran los resultados.
Como ya mencioné antes por ahora, el escaneo no es real. Solo muestra un mensaje como si estuviera buscando IPs activas. Pero más adelante agregaré la lógica para que lo haga de verdad.
También tiene controles básicos para avisarte si te olvidaste de escribir algo o si pusiste mal el tiempo de espera.

Hoy día 12-08, el escanear ya no simula más el escaneo sino que realmente lo escanea, además hay una barra de carga que va incrementando en base a las ips escaneadas,la ip se valida antes de ser analizada, en caso de estar mal un mensaje aparecerá e interrumpirá el escaneo, además hay un contador de ips válidas, y todo está organizado en forma de tabla de la siguiente forma.
Dirección ip           nombre         estado          tiempo(ms).
Y por último un botón de guardar en archivo, para guardar lo escaneado en un archivo.                         

Hoy día 18-08 agregué un filtro, el cual sirve para buscar ips, ms, etc, según el mensaje que le pasemos. Además agregué un mensaje en la parte inferior de la pantalla que muestra todos los equipos que respondieron(que están validados) y le agregué un poco de diseño a la interfaz. Además solucioné errroes como ms negativos, o que la ip de inicio sea mayor a la ip final.
Completando y terminando el código 
