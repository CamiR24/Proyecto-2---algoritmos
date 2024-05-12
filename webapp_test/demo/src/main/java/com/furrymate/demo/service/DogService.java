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
import static org.neo4j.driver.Values.parameters;

import static org.neo4j.driver.Values.parameters;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class DogService {

    private final Driver driver;

    public DogService() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456789"));
    }

    @SuppressWarnings("deprecation")
    public void createPerro(Perro perro) {
    try (Session session = driver.session()) {
        session.writeTransaction(tx -> {
            tx.run("CREATE (p:Perro {nombre: $nombre})",
                parameters("nombre", perro.getNombre()));

            // Crea y conecta atributos como nodos independientes
            String[] queries = {
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Raza {nombre: $raza}) CREATE (p)-[:ES_DE_RAZA]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Ubicacion {nombre: $ubicacion}) CREATE (p)-[:ESTA_EN_UBICACION]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Tamaño {tipo: $tamaño}) CREATE (p)-[:TIENE_TAMAÑO]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Edad {valor: $edad}) CREATE (p)-[:TIENE_EDAD]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Pedigree {valor: $pedigree}) CREATE (p)-[:TIENE_PEDIGREE]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Entrenado {valor: $entrenado}) CREATE (p)-[:ESTA_ENTRENADO]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Peso {valor: $peso}) CREATE (p)-[:TIENE_PESO]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Sexo {tipo: $sexo}) CREATE (p)-[:TIENE_SEXO]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Enfermedades {estado: $enfermedades}) CREATE (p)-[:TIENE_ENFERMEDADES]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Cría {estado: $cria}) CREATE (p)-[:ES_PARA_CRIA]->(a)",
                "MATCH (p:Perro {nombre: $nombre}) MERGE (a:Color {nombre: $color}) CREATE (p)-[:ES_DE_COLOR]->(a)"
            };

            for (String query : queries) {
                tx.run(query, parameters(
                    "nombre", perro.getNombre(),
                    "raza", perro.getRaza(),
                    "ubicacion", perro.getUbicacion(),
                    "tamaño", perro.getTamaño(),
                    "edad", perro.getEdad(),
                    "pedigree", perro.getTienePedigree(),
                    "entrenado", perro.getTieneEntrenamiento(),
                    "peso", perro.getPeso(),
                    "sexo", perro.getSexo(),
                    "enfermedades", perro.getTieneEnfermedades(),
                    "cria", perro.getCria(),
                    "color", perro.getColor()
                ));
            }
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
                RETURN p.nombre AS nombre, raza.nombre AS raza, ubicacion.nombre AS ubicacion, 
                    edad.valor AS edad, peso.valor AS peso, tamaño.tipo AS tamaño, 
                    sexo.tipo AS sexo, pedigree.valor AS tienePedigree, entrenado.valor AS tieneEntrenamiento,
                    enfermedades.estado AS tieneEnfermedades, cria.estado AS paraCria, color.nombre AS color 
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
                perro.setParaCria(record.get("paraCria", Values.value(false)).asBoolean());
                perro.setColor(record.get("color", Values.value("Desconocido")).asString());
                perros.add(perro);
            }
        }
        return perros;
    }
    


    public void close() {
        driver.close();
    }
}

