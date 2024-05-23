package com.furrymate.demo.service;

import com.furrymate.demo.model.*;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.Values;
import org.neo4j.driver.Result;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import static org.neo4j.driver.Values.parameters;

import org.springframework.stereotype.Service;

@Service
public class DogService {

    private final Driver driver;

    public DogService() {
        driver = GraphDatabase.driver("bolt://34.201.6.165:7687",
                AuthTokens.basic("neo4j", "architecture-limps-conversion"));
    }

    public Perro getPerroByUsuarioAndPassword(String usuario, String password) {
        try (Session session = driver.session()) {
            String query = """
                        MATCH (p:Perro {usuario: $usuario, password: $password})
                        OPTIONAL MATCH (p)-[:ES_DE_RAZA]->(raza)
                        OPTIONAL MATCH (p)-[:ESTA_EN_UBICACION]->(ubicacion)
                        OPTIONAL MATCH (p)-[:TIENE_EDAD]->(edad)
                        OPTIONAL MATCH (p)-[:TIENE_PESO]->(peso)
                        OPTIONAL MATCH (p)-[:TIENE_SEXO]->(sexo)
                        OPTIONAL MATCH (p)-[:TIENE_TAMAÑO]->(tamaño)
                        OPTIONAL MATCH (p)-[:TIENE_PEDIGREE]->(pedigree)
                        OPTIONAL MATCH (p)-[:ESTA_ENTRENADO]->(entrenado)
                        OPTIONAL MATCH (p)-[:TIENE_ENFERMEDADES]->(enfermedades)
                        OPTIONAL MATCH (p)-[:ES_PARA_CRIA]->(cria)
                        OPTIONAL MATCH (p)-[:ES_DE_COLOR]->(color)
                        OPTIONAL MATCH (p)-[:HA_TENIDO_PAREJA]->(pareja)
                        RETURN p.nombre AS nombre, p.antecedentes AS antecedentes, raza.nombre AS raza, ubicacion.nombre AS ubicacion,
                            edad.valor AS edad, peso.valor AS peso, tamaño.tipo AS tamaño,
                            sexo.tipo AS sexo, pedigree.valor AS tienePedigree, entrenado.valor AS tieneEntrenamiento,
                            enfermedades.estado AS tieneEnfermedades, cria.estado AS paraCria, color.nombre AS color,
                            pareja.estado AS haTenidoPareja LIMIT 1
                    """;
            Result result = session.run(query, parameters("usuario", usuario, "password", password));
            if (result.hasNext()) {
                Record record = result.single();
                Perro perro = new Perro();
                perro.setNombre(record.get("nombre").asString());
                perro.setUsuario(usuario);
                perro.setPassword(password);
                perro.setRaza(record.get("raza", Values.value("Desconocido")).asString());
                perro.setUbicacion(record.get("ubicacion", Values.value("Desconocido")).asString());
                perro.setEdad(record.get("edad", Values.value(0)).asInt());
                perro.setPeso(record.get("peso", Values.value(0)).asInt());
                perro.setTamaño(record.get("tamaño", Values.value("Desconocido")).asString());
                perro.setSexo(record.get("sexo", Values.value("Desconocido")).asString());
                perro.setTienePedigree(record.get("tienePedigree", Values.value(false)).asBoolean());
                perro.setTieneEntrenamiento(record.get("tieneEntrenamiento", Values.value(false)).asBoolean());
                perro.setTieneEnfermedades(record.get("tieneEnfermedades", Values.value(false)).asBoolean());
                perro.setQuedarseCria(record.get("paraCria", Values.value(false)).asBoolean());
                perro.setColor(record.get("color", Values.value("Desconocido")).asString());
                perro.setHaTenidoPareja(record.get("haTenidoPareja", Values.value(false)).asBoolean());
                perro.setAntecedentes(record.get("antecedentes", Values.value("Desconocido")).asString());
                return perro;
            }
        } catch (NoSuchRecordException e) {
            return null;
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public void createPerro(Perro perro, String usuario, String password) {
        perro.setPassword(password);
        perro.setUsuario(usuario);

        System.out.println(
                "================================================ CREANDO PERRO ================================================");

        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                System.out.println(
                        "================================================ LANZADO QUERY PERRO ================================================");

                tx.run("CREATE (p:Perro {nombre: $nombre, usuario: $usuario, antecedentes: $antecedentes, password: $password})",
                        parameters("nombre", perro.getNombre(), "usuario", usuario, "antecedentes",
                                perro.getAntecedentes(), "password", password));

                System.out.println(
                        "================================================ QUERY PERRO CREADO ================================================");

                // Crea y conecta atributos como nodos independientes

                System.out.println(
                        "================================================ LANZADO QUERY MATCHES ================================================");

                String[] queries = {
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Raza {nombre: $raza}) CREATE (p)-[:ES_DE_RAZA]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Ubicacion {nombre: $ubicacion}) CREATE (p)-[:ESTA_EN_UBICACION]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Tamaño {tipo: $tamaño}) CREATE (p)-[:TIENE_TAMAÑO]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Edad {valor: $edad}) CREATE (p)-[:TIENE_EDAD]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Pedigree {valor: $pedigree}) CREATE (p)-[:TIENE_PEDIGREE]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Entrenado {valor: $entrenado}) CREATE (p)-[:ESTA_ENTRENADO]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Peso {valor: $peso}) CREATE (p)-[:TIENE_PESO]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Sexo {tipo: $sexo}) CREATE (p)-[:TIENE_SEXO]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Enfermedades {estado: $enfermedades}) CREATE (p)-[:TIENE_ENFERMEDADES]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Cría {estado: $cria}) CREATE (p)-[:ES_PARA_CRIA]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Color {nombre: $color}) CREATE (p)-[:ES_DE_COLOR]->(a)",
                        "MATCH (p:Perro {usuario: $usuario, password: $password}) MERGE (a:Pareja {estado: $pareja}) CREATE (p)-[:HA_TENIDO_PAREJA]->(a)"
                };

                System.out.println(
                        "================================================ QUERY MATCHES CREADO ================================================");

                System.out.println(
                        "================================================ CICLO DE QUERYS ================================================");

                for (String query : queries) {
                    tx.run(query, parameters(
                            "nombre", perro.getNombre(),
                            "usuario", usuario,
                            "password", password,
                            "raza", perro.getRaza(),
                            "ubicacion", perro.getUbicacion(),
                            "tamaño", perro.getTamaño(),
                            "edad", perro.getEdad(),
                            "pedigree", perro.getTienePedigree(),
                            "entrenado", perro.getTieneEntrenamiento(),
                            "peso", perro.getPeso(),
                            "sexo", perro.getSexo(),
                            "enfermedades", perro.getTieneEnfermedades(),
                            "cria", perro.getQuedarseCria(),
                            "color", perro.getColor(),
                            "pareja", perro.getHaTenidoPareja(),
                            "antecedentes", perro.getAntecedentes()));
                }

                System.out.println(
                        "================================================ PERRO TERMINADO ================================================");

                return null;
            });
        }
    }

    public List<Perro> getPerrosDetails() {
        List<Perro> perros = new ArrayList<>();
        try (Session session = driver.session()) {
            String query = """
                        MATCH (p:Perro)
                        OPTIONAL MATCH (p)-[:ES_DE_RAZA]->(raza)
                        OPTIONAL MATCH (p)-[:ESTA_EN_UBICACION]->(ubicacion)
                        OPTIONAL MATCH (p)-[:TIENE_EDAD]->(edad)
                        OPTIONAL MATCH (p)-[:TIENE_PESO]->(peso)
                        OPTIONAL MATCH (p)-[:TIENE_SEXO]->(sexo)
                        OPTIONAL MATCH (p)-[:TIENE_TAMAÑO]->(tamaño)
                        OPTIONAL MATCH (p)-[:TIENE_PEDIGREE]->(pedigree)
                        OPTIONAL MATCH (p)-[:ESTA_ENTRENADO]->(entrenado)
                        OPTIONAL MATCH (p)-[:TIENE_ENFERMEDADES]->(enfermedades)
                        OPTIONAL MATCH (p)-[:ES_PARA_CRIA]->(cria)
                        OPTIONAL MATCH (p)-[:ES_DE_COLOR]->(color)
                        OPTIONAL MATCH (p)-[:HA_TENIDO_PAREJA]->(pareja)
                        RETURN p.nombre AS nombre, p.antecedentes AS antecedentes, raza.nombre AS raza, ubicacion.nombre AS ubicacion,
                            edad.valor AS edad, peso.valor AS peso, tamaño.tipo AS tamaño,
                            sexo.tipo AS sexo, pedigree.valor AS tienePedigree, entrenado.valor AS tieneEntrenamiento,
                            enfermedades.estado AS tieneEnfermedades, cria.estado AS paraCria, color.nombre AS color,
                            pareja.estado AS haTenidoPareja
                    """;
            Result result = session.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                Perro perro = new Perro();
                perro.setNombre(record.get("nombre").asString());
                perro.setRaza(record.get("raza", Values.value("Desconocido")).asString());
                perro.setUbicacion(record.get("ubicacion", Values.value("Desconocido")).asString());
                perro.setEdad(record.get("edad", Values.value(0)).asInt());
                perro.setPeso(record.get("peso", Values.value(0)).asInt());
                perro.setTamaño(record.get("tamaño", Values.value("Desconocido")).asString());
                perro.setSexo(record.get("sexo", Values.value("Desconocido")).asString());
                perro.setTienePedigree(record.get("tienePedigree", Values.value(false)).asBoolean());
                perro.setTieneEntrenamiento(record.get("tieneEntrenamiento", Values.value(false)).asBoolean());
                perro.setTieneEnfermedades(record.get("tieneEnfermedades", Values.value(false)).asBoolean());
                perro.setQuedarseCria(record.get("paraCria", Values.value(false)).asBoolean());
                perro.setColor(record.get("color", Values.value("Desconocido")).asString());
                perro.setHaTenidoPareja(record.get("haTenidoPareja", Values.value(false)).asBoolean());
                perro.setAntecedentes(record.get("antecedentes", Values.value("Desconocido")).asString());
                perros.add(perro);
            }
        }
        return perros;
    }

    public List<Perro> getPerros() {
        List<Perro> perros = new ArrayList<>();
        try (Session session = driver.session()) {
            String query = """
                        MATCH (p:Perro)-[:ES_DE_RAZA]->(raza),
                              (p)-[:ESTA_EN_UBICACION]->(ubicacion),
                              (p)-[:TIENE_TAMAÑO]->(tamaño),
                              (p)-[:TIENE_EDAD]->(edad),
                              (p)-[:TIENE_PEDIGREE]->(pedigree),
                              (p)-[:ESTA_ENTRENADO]->(entrenado),
                              (p)-[:TIENE_PESO]->(peso),
                              (p)-[:TIENE_SEXO]->(sexo),
                              (p)-[:TIENE_ENFERMEDADES]->(enfermedades),
                              (p)-[:ES_PARA_CRIA]->(cria)
                        RETURN p.nombre AS nombre, raza.nombre AS raza, ubicacion.nombre AS ubicacion,
                               tamaño.tipo AS tamaño, edad.valor AS edad, pedigree.valor AS tienePedigree,
                               entrenado.valor AS tieneEntrenamiento, peso.valor AS peso, sexo.tipo AS sexo,
                               enfermedades.estado AS tieneEnfermedades, cria.estado AS paraCria
                    """;
            Result result = session.run(query);
            while (result.hasNext()) {
                Record record = result.next();
                Perro perro = new Perro(
                        record.get("nombre").asString(),
                        record.get("raza").asString(),
                        record.get("ubicacion").asString(),
                        record.get("tamaño").asString(),
                        record.get("edad").asInt(),
                        record.get("tienePedigree").asBoolean(),
                        record.get("tieneEntrenamiento").asBoolean(),
                        record.get("peso").asInt(),
                        record.get("sexo").asString(),
                        record.get("tieneEnfermedades").asBoolean(),
                        record.get("paraCria").asBoolean());
                perros.add(perro);
            }
        }
        return perros;
    }

    public Perro findBestMatch(Perro myDog, List<Perro> allPerros) {
        Perro bestMatch = null;
        double maxScore = Double.NEGATIVE_INFINITY;
        for (Perro perro : allPerros) {
            if (!perro.getNombre().equals(myDog.getNombre())) { // Asegúrate de no compararlo consigo mismo
                double score = calcularPeso(myDog, perro);
                if (score > maxScore) {
                    maxScore = score;
                    bestMatch = perro;
                }
            }
        }
        return bestMatch;
    }

    private double calcularPeso(Perro p1, Perro p2) {
        double peso = 0;
        peso += (p1.getRaza().equals(p2.getRaza())) ? 10 : 0;
        peso += (p1.getUbicacion().equals(p2.getUbicacion())) ? 15 : 0;

        // Corrección para comparar los tamaños como String
        peso += (p1.getTamaño().equals(p2.getTamaño())) ? 10 : 0;

        peso += calcularDiferenciaEdad(p1.getEdad(), p2.getEdad());
        peso += (p1.getTienePedigree() == p2.getTienePedigree()) ? 10 : 0;
        peso += (p1.getTieneEntrenamiento() == p2.getTieneEntrenamiento()) ? 10 : 0;
        peso += calcularDiferenciaPeso(p1.getPeso(), p2.getPeso());
        peso += (p1.getSexo().equals(p2.getSexo())) ? 0 : 10;
        peso += (p1.getTieneEnfermedades() == p2.getTieneEnfermedades()) ? 0 : 10;
        peso += (p1.getQuedarseCria() == p2.getQuedarseCria()) ? 10 : 0;
        return peso;
    }

    private int calcularDiferenciaEdad(int edad1, int edad2) {
        int diferencia = Math.abs(edad1 - edad2);
        if (diferencia == 0)
            return 10;
        if (diferencia <= 2)
            return 7;
        if (diferencia <= 5)
            return 3;
        return 0;
    }

    private int calcularDiferenciaPeso(int peso1, int peso2) {
        int diferencia = Math.abs(peso1 - peso2);
        if (diferencia == 0)
            return 10;
        if (diferencia <= 5)
            return 7;
        return 0;
    }

    public void close() {
        driver.close();
    }
}
