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

    public void createPerro(Perro perro) {
        try (Session session = driver.session()) {
            String query = "CREATE (p:Perro {nombre: $nombre, raza: $raza, ubicacion: $ubicacion, edad: $edad}) RETURN p";
            session.run(query, parameters(
                "nombre", perro.getNombre(),
                "raza", perro.getRaza(),
                "ubicacion", perro.getUbicacion(),
                "edad", perro.getEdad()
            ));
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
                RETURN p.nombre AS nombre, raza.nombre AS raza, ubicacion.nombre AS ubicacion, 
                    edad.valor AS edad, peso.valor AS peso, tamaño.valor AS tamaño, 
                    sexo.tipo AS sexo, pedigree.valor AS tienePedigree, entrenado.valor AS tieneEntrenamiento,
                    enfermedades.estado AS tieneEnfermedades, cria.estado AS paraCria
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
                perros.add(perro);
            }
        }
        return perros;
    }
    


    public void close() {
        driver.close();
    }
}

