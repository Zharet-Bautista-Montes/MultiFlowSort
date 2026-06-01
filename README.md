# MultiFlowSort 🚀

**MultiFlowSort** es un algoritmo de ordenamiento híbrido, recursivo y altamente adaptativo diseñado para operar de manera *in-place* (con un uso de memoria adicional estrictamente acotado). A través de un enfoque bidireccional, el algoritmo identifica, aísla y procesa dinámicamente flujos ordenados (tanto ascendentes como descendentes), minimizando la fricción de comparaciones e intercambios redundantes en la jerarquía de memoria del hardware moderno. 

---

## 📌 Características Clave

* **Adaptabilidad Sensible a la Entropía:** Detecta de forma nativa bloques de datos pre-ordenados o completamente invertidos en cualquier nivel de la recursión, aplicando cortocircuitos de ejecución para optimizar el rendimiento.
* **Fusión de Flujos Bidireccional:** Utiliza fases alternas *upstream* (hacia la derecha) y *downstream* (hacia la izquierda) para colapsar la ventana de trabajo de manera agresiva sobre el núcleo desordenado del arreglo.
* **Alta Localidad de Referencia:** Diseñado con escaneos lineales contiguos que aprovechan al máximo el *prefetching* de hardware y la jerarquía de caché del procesador (L1/L2).
* **Eficiencia Espacial Resiliente:** Opera directamente sobre el arreglo original (*in-place*), requiriendo únicamente el espacio de la pila de llamadas recursivas.

---

## ⚙️ Arquitectura y Funcionamiento

El algoritmo transforma la filosofía tradicional de "Divide y Vencerás" en un proceso de **ordenamiento y combinación por flujos**. Su ejecución se divide en tres fases fundamentales:

1.  **Escaneo de Adaptabilidad Local:** Al inicio de cada subproblema, se verifica si el segmento está ordenado o invertido. Si localiza una racha estrictamente descendente, se ejecuta una inversión directa de bloque con RCF (Reverse Contrary Flows, *O(n)*) y, en caso de que al final el segmento esté ordenado, se aborta la recursión innecesaria. Si se elimina esta fase, se obtiene un algoritmo plenamente estable, aunque entonces su mejor caso se degrada a *O(nlogn)*
2.  **División Jerárquica:** Si el bloque presenta un desorden complejo, se divide binariamente mediante una estructura fija de mitades (*halving*), delegando el ordenamiento local a las capas inferiores para garantizar bloques estructurados en el retorno.
3.  **Combinación Bidireccional Avanzada (The Core Loop):** Mediante un bucle dinámico, se alternan en las dos direcciones operaciones especializadas OPF (Overlap Parallel Flows, *O(nlogn)*), que funcionan como un motor de arrastre log-lineal que identifica segmentos de elementos mayores seguidos de segmentos de elementos menores y los intercambia en bloque. 

---

## 📊 Análisis de Complejidad y Rendimiento

* Se llevó a cabo un análisis minucioso con base en el Teorema Maestro (donde *T(n) = O(n) + 2T(n/2) + O(nlogn)*)
* Se ejecutaron exhaustivas pruebas empíricas en vectores con diferentes características (Random, Reversed, Almost Sorted, Few Unique, Full ZigZag) y en diferentes tamaños (desde 128 hasta 16384 elementos). 
* Como resultado, las métricas de crecimiento, basadas en el método Log-Log, han demostrado un comportamiento sub-cuadrático de alta eficiencia:

| Métrica | Complejidad Teórica | Exponente Práctico ($k$) | Notas / Comportamiento |
| :--- | :--- | :--- | :--- |
| **Mejor Caso** | $O(n)$ | $\approx 1.0$ | Datos completamente ordenados o invertidos de fábrica, o bien con un segmento desordenado muy pequeño. |
| **Caso Promedio** | $O(n\log^{2}n)$ | $\approx 1.28$ (Comparaciones)<br>$\approx 1.28$ (Swaps) | Justificado estadísticamente por la reducción de la entropía local en la combinación. |
| **Peor Caso** | $O(n\log^{2}n)$ | — | Patrones de desorden extremo (ej. zigzag) confinados por la recursión. |
| **Tiempo Real** | — | **$1.10 - 1.20$** | El rendimiento de tiempo real simula curvas $O(n \log n)$ gracias a la óptima afinidad a la caché. |
| **Memoria** | $O(\log n)$ | — | Memoria adicional limitada estrictamente a la pila recursiva en el peor escenario ($O(1)$ en el mejor). |

---

## 🛠️ Estructura del Código

El núcleo de la lógica se distribuye en las siguientes funciones principales:

* `multiFlowSort(int[] arrayed, int top, int btm)`: Método recursivo principal que controla los límites de la ventana de operación y la subdivisión de tareas.
* `reverseContraryFlows(int limit, int dir, int aux)`: Escáner lineal encargado de detectar e invertir masivamente los flujos que contradicen el orden objetivo.
* `overlapParallelFlows(int limit, int dir, int aux)`: Optimizador de arrastre que alinea flujos paralelos reduciendo las inversiones a un ritmo acelerado.

---

## 💬 Contribuciones y Discusión

Este es un proyecto de investigación algorítmica abierto a debate. Nos interesa especialmente discutir con la comunidad de ciencias de la computación:

1.  Estrategias para mitigar o estabilizar el orden relativo de elementos idénticos en las fases RCF.
2.  Análisis formal de la convergencia de la ventana de trabajo bajo distribuciones patológicas específicas.
3.  Posibles optimizaciones en la hibridación de los casos base para subarreglos diminutos ($n \le 16$).

**Post-Data:** Se incluyen en el repositorio desarrollos paralelos de otros algoritmos como HalvingSort, SmartInsertionSort o FlowSort, aunque corresponden a versiones ineficientes del tipo $O(n^{2})$ y sólo con el propósito de estudiar opciones para el desarrollo del algoritmo final. 

¡Siente libre de abrir un *Issue* o enviar un *Pull Request* para aportar al desarrollo de **MultiFlowSort**!