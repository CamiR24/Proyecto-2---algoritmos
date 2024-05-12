import java.util.*;
import java.lang.Math;

class Perro {
    String nombre;
    String raza;
    String ubicacion;
    int tamaño;
    int edad;
    boolean pedigree;
    boolean entrenado;
    int peso;
    String sexo;
    boolean enfermedades;
    boolean cría;

    public Perro(String nombre, String raza, String ubicacion, int tamaño, int edad, boolean pedigree, boolean entrenado, int peso, String sexo, boolean enfermedades, boolean cría) {
        this.nombre = nombre;
        this.raza = raza;
        this.ubicacion = ubicacion;
        this.tamaño = tamaño;
        this.edad = edad;
        this.pedigree = pedigree;
        this.entrenado = entrenado;
        this.peso = peso;
        this.sexo = sexo;
        this.enfermedades = enfermedades;
        this.cría = cría;
    }
}

class Arista {
    Perro destino;
    double peso;

    public Arista(Perro destino, double peso) {
        this.destino = destino;
        this.peso = peso;
    }
}

class Grafo {
    Map<Perro, List<Arista>> adjList;

    public Grafo() {
        adjList = new HashMap<>();
    }

    void agregarPerro(Perro p) {
        adjList.putIfAbsent(p, new ArrayList<>());
    }

    void agregarArista(Perro origen, Perro destino, double peso) {
        adjList.get(origen).add(new Arista(destino, peso));
    }

    List<Arista> getVecinos(Perro p) {
        return adjList.get(p);
    }
}

public class Dijkstra {
    static void calcularDijkstra(Grafo g, Perro origen) {
        Map<Perro, Double> distancias = new HashMap<>();
        Map<Perro, Perro> previo = new HashMap<>();
        PriorityQueue<Perro> cola = new PriorityQueue<>(Comparator.comparing(distancias::get));

        for (Perro p : g.adjList.keySet()) {
            distancias.put(p, Double.POSITIVE_INFINITY);
            previo.put(p, null);
            cola.add(p);
        }
        distancias.put(origen, 0.0);

        while (!cola.isEmpty()) {
            Perro u = cola.poll();

            for (Arista arista : g.getVecinos(u)) {
                Perro v = arista.destino;
                double peso = arista.peso;
                double distanciaAlternativa = distancias.get(u) + peso;

                if (distanciaAlternativa < distancias.get(v)) {
                    distancias.put(v, distanciaAlternativa);
                    previo.put(v, u);
                    cola.add(v);  // Actualizar la prioridad en la cola
                }
            }
        }

        // Mostrar los resultados
        for (Map.Entry<Perro, Double> entrada : distancias.entrySet()) {
            System.out.println("Distancia desde " + origen.nombre + " a " + entrada.getKey().nombre + " es " + entrada.getValue());
        }
    }

    public static void main(String[] args) {
        Perro perro1 = new Perro("Max", "Labrador", "Madrid", 3, 5, true, true, 30, "M", false, true);
        Perro perro2 = new Perro("Bella", "Golden", "Madrid", 3, 3, true, true, 28, "F", false, true);
        Perro perro3 = new Perro("Charlie", "Poodle", "Barcelona", 2, 2, false, false, 10, "M", false, false);
        Perro perro4 = new Perro("Lucy", "Labrador", "Madrid", 3, 4, true, true, 29, "F", false, true);
        Perro perro5 = new Perro("Rocky", "Bulldog", "Sevilla", 2, 6, false, true, 24, "M", true, false);
        Perro perro6 = new Perro("Molly", "Golden", "Valencia", 3, 5, true, true, 27, "F", false, true);
        Perro perro7 = new Perro("Jake", "Beagle", "Madrid", 2, 3, false, false, 20, "M", false, true);
        Perro perro8 = new Perro("Coco", "Poodle", "Barcelona", 1, 2, true, false, 7, "F", false, false);
        Perro perro9 = new Perro("Buddy", "Labrador", "Madrid", 3, 5, true, true, 30, "M", false, true);
        Perro perro10 = new Perro("Daisy", "Dachshund", "Barcelona", 1, 4, false, true, 9, "F", true, false);
        Perro perro11 = new Perro("Maggie", "Golden", "Valencia", 3, 7, true, false, 26, "F", false, true);
        Perro perro12 = new Perro("Bailey", "Labrador", "Madrid", 3, 5, true, true, 30, "F", false, true);
        Perro perro13 = new Perro("Sophie", "Cocker Spaniel", "Madrid", 2, 4, true, true, 14, "F", false, true);


        Grafo g = new Grafo();
        g.agregarPerro(perro1);
        g.agregarPerro(perro2);
        g.agregarPerro(perro3);

        g.agregarPerro(perro4);
        g.agregarPerro(perro5);
        g.agregarPerro(perro6);
        g.agregarPerro(perro7);
        g.agregarPerro(perro8);
        g.agregarPerro(perro9);
        g.agregarPerro(perro10);
        g.agregarPerro(perro11);
        g.agregarPerro(perro12);
        g.agregarPerro(perro13);

        g.agregarArista(perro1, perro2, calcularPeso(perro1, perro2));
        g.agregarArista(perro1, perro3, calcularPeso(perro1, perro3));
        g.agregarArista(perro2, perro3, calcularPeso(perro2, perro3));

        g.agregarArista(perro1, perro4, calcularPeso(perro1, perro4));
        g.agregarArista(perro1, perro5, calcularPeso(perro1, perro5));
        g.agregarArista(perro1, perro6, calcularPeso(perro1, perro6));
        g.agregarArista(perro1, perro7, calcularPeso(perro1, perro7));
        g.agregarArista(perro1, perro8, calcularPeso(perro1, perro8));
        g.agregarArista(perro1, perro9, calcularPeso(perro1, perro9));
        g.agregarArista(perro1, perro10, calcularPeso(perro1, perro10));
        g.agregarArista(perro1, perro11, calcularPeso(perro1, perro11));
        g.agregarArista(perro1, perro12, calcularPeso(perro1, perro12));
        g.agregarArista(perro1, perro13, calcularPeso(perro1, perro13));

        calcularDijkstra(g, perro1);
    }

    static double calcularPeso(Perro p1, Perro p2) {
        double peso = 0;
        peso += (p1.raza.equals(p2.raza)) ? 10 : 0;
        peso += (p1.ubicacion.equals(p2.ubicacion)) ? 15 : 0;
        peso += (p1.tamaño == p2.tamaño) ? 10 : Math.abs(p1.tamaño - p2.tamaño) == 1 ? 5 : 0;
        peso += calcularDiferenciaEdad(p1.edad, p2.edad);
        peso += (p1.pedigree == p2.pedigree) ? 10 : 0;
        peso += (p1.entrenado == p2.entrenado) ? 10 : 0;
        peso += calcularDiferenciaPeso(p1.peso, p2.peso);
        peso += (p1.sexo.equals(p2.sexo)) ? 0 : 10;
        peso += (p1.enfermedades == p2.enfermedades) ? 0 : 10;
        peso += (p1.cría == p2.cría) ? 10 : 0;
        return peso;
    }

    static int calcularDiferenciaEdad(int edad1, int edad2) {
        int diferencia = Math.abs(edad1 - edad2);
        if (diferencia == 0) return 10;
        if (diferencia <= 2) return 7;
        if (diferencia <= 5) return 3;
        return 0;
    }

    static int calcularDiferenciaPeso(int peso1, int peso2) {
        int diferencia = Math.abs(peso1 - peso2);
        if (diferencia == 0) return 10;
        if (diferencia <= 5) return 7;
        return 0;
    }
}
