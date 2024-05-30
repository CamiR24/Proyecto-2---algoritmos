package com.furrymate.demo.service;

import com.furrymate.demo.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.neo4j.driver.Values;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.Value;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.exceptions.NoSuchRecordException;

import static org.neo4j.driver.Values.parameters;

import org.springframework.stereotype.Service;

@Service
public class DogService {

    private final Driver driver;

    public DogService() {
        driver = GraphDatabase.driver("bolt://100.26.251.214:7687",
                AuthTokens.basic("neo4j", "signalman-tool-polarity"));
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
                    OPTIONAL MATCH (p)-[:LIKES]->(liked:Perro)
                    OPTIONAL MATCH (p)-[:DISLIKES]->(disliked:Perro)
                    RETURN p.nombre AS nombre, p.antecedentes AS antecedentes, raza.nombre AS raza, ubicacion.nombre AS ubicacion,
                        edad.valor AS edad, peso.valor AS peso, tamaño.tipo AS tamaño,
                        sexo.tipo AS sexo, pedigree.valor AS tienePedigree, entrenado.valor AS tieneEntrenamiento,
                        enfermedades.estado AS tieneEnfermedades, cria.estado AS paraCria, color.nombre AS color,
                        pareja.estado AS haTenidoPareja, collect(liked { .usuario, .nombre }) AS likes, collect(disliked { .usuario, .nombre }) AS dislikes, p.usuario AS usuario, p.password AS password LIMIT 1
                """;
            Result result = session.run(query, parameters("usuario", usuario, "password", password));
            if (result.hasNext()) {
                Record record = result.single();
                Perro perro = new Perro();
                perro.setNombre(record.get("nombre").asString());
                perro.setUsuario(record.get("usuario").asString());
                perro.setPassword(record.get("password").asString());
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
    
                // Obtener listas de likes y dislikes
                List<Perro> likes = record.get("likes").asList(value -> {
                    Perro likedPerro = new Perro();
                    Map<String, Object> properties = value.asMap();
                    likedPerro.setUsuario(properties.getOrDefault("usuario", "Desconocido").toString());
                    likedPerro.setNombre(properties.getOrDefault("nombre", "Desconocido").toString());
                    // Establecer otros atributos necesarios si se incluyen en la consulta
                    return likedPerro;
                });

                List<Perro> dislikes = record.get("dislikes").asList(value -> {
                    Perro dislikedPerro = new Perro();
                    Map<String, Object> properties = value.asMap();
                    dislikedPerro.setUsuario(properties.getOrDefault("usuario", "Desconocido").toString());
                    dislikedPerro.setNombre(properties.getOrDefault("nombre", "Desconocido").toString());
                    // Establecer otros atributos necesarios si se incluyen en la consulta
                    return dislikedPerro;
                });

                perro.setLikes(likes);
                perro.setDislikes(dislikes);
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
                        RETURN p.nombre AS nombre, p.usuario AS usuario, p.antecedentes AS antecedentes, raza.nombre AS raza, ubicacion.nombre AS ubicacion,
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
                perro.setUsuario(record.get("usuario").asString());
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

    public Perro findBestMatch(Perro myDog, List<Perro> allPerros, List<Perro> likes, List<Perro> dislikes) {
        Perro bestMatch = null;
        double maxScore = Double.NEGATIVE_INFINITY;
    
        for (Perro perro : allPerros) {
            if (perro.getUsuario() != null && !perro.getUsuario().equals(myDog.getUsuario())) { // Asegúrate de no compararlo consigo mismo y que el usuario no sea null
                boolean isLiked = likes.stream().anyMatch(likedPerro -> likedPerro.getUsuario().equals(perro.getUsuario()));
                boolean isDisliked = dislikes.stream().anyMatch(dislikedPerro -> dislikedPerro.getUsuario().equals(perro.getUsuario()));
    
                if (!isLiked && !isDisliked) {
                    double score = calcularPeso(myDog, perro, likes, dislikes);
                    if (score > maxScore && score != -1000) {
                        maxScore = score;
                        bestMatch = perro;
                    }
                }
            }
        }
        return bestMatch;
    }

    public Perro handleLike(String otherPerroUsuario, Perro currentUser) {
        System.out.println("A " + currentUser.getUsuario() + " LE GUSTO EL PERRO DE " + otherPerroUsuario);
        try (Session session = driver.session()) {
            System.out.println("ACTUALIZANDO BASE DE DATOS");
    
            // Agregar la relación de "like" en la base de datos
            session.writeTransaction(tx -> {
                tx.run("MATCH (u:Perro {usuario: $currentUserUsuario, password: $currentUserPassword}), (p:Perro {usuario: $otherPerroUsuario}) MERGE (u)-[:LIKES]->(p)",
                        parameters("currentUserUsuario", currentUser.getUsuario(), "currentUserPassword", currentUser.getPassword(), "otherPerroUsuario", otherPerroUsuario));
                return null;
            });
    
            System.out.println("ACTUALIZADA BASE DE DATOS");
    
            System.out.println("RECOMENDACIONES ACTUALIZANDO...");
    
            // Actualizar las recomendaciones
            Perro bestMatch = updateRecommendations(session, currentUser);
    
            System.out.println("RECOMENDACIONES ACTUALIZADAS");
            return bestMatch;
        }
    }
    
    public Perro handleDislike(String otherPerroUsuario, Perro currentUser) {
        System.out.println("A " + currentUser.getUsuario() + " NO LE GUSTO EL PERRO DE " + otherPerroUsuario);
        try (Session session = driver.session()) {
            System.out.println("ACTUALIZANDO BASE DE DATOS");
    
            // Agregar la relación de "dislike" en la base de datos
            session.writeTransaction(tx -> {
                tx.run("MATCH (u:Perro {usuario: $currentUserUsuario, password: $currentUserPassword}), (p:Perro {usuario: $otherPerroUsuario}) MERGE (u)-[:DISLIKES]->(p)",
                        parameters("currentUserUsuario", currentUser.getUsuario(), "currentUserPassword", currentUser.getPassword(), "otherPerroUsuario", otherPerroUsuario));
                return null;
            });
    
            System.out.println("ACTUALIZADA BASE DE DATOS");
    
            System.out.println("RECOMENDACIONES ACTUALIZANDO...");
    
            // Actualizar las recomendaciones
            Perro bestMatch = updateRecommendations(session, currentUser);
    
            System.out.println("RECOMENDACIONES ACTUALIZADAS");
            return bestMatch;
        }
    }
            
    private Perro updateRecommendations(Session session, Perro currentUser) {
        // Obtener todos los perros excepto los que han sido "liked" o "disliked"
        String query = """
                MATCH (p:Perro)
                WHERE NOT (p)-[:LIKES]-(:Perro {usuario: $usuario, password: $password})
                AND NOT (p)-[:DISLIKES]-(:Perro {usuario: $usuario, password: $password})
                RETURN p
                """;
        Result result = session.run(query, parameters("usuario", currentUser.getUsuario(), "password", currentUser.getPassword()));
        List<Perro> allPerros = new ArrayList<>();
    
        while (result.hasNext()) {
            Record record = result.next();
            Perro perro = new Perro();
            perro.setUsuario(record.get("usuario").asString(null));
            perro.setNombre(record.get("nombre").asString(null));
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
            allPerros.add(perro);
        }
    
        // Obtener los perros que el usuario ha dado "like" y "dislike"
        String likesDislikesQuery = """
                MATCH (u:Perro {usuario: $usuario, password: $password})
                OPTIONAL MATCH (u)-[:LIKES]->(liked:Perro)
                OPTIONAL MATCH (u)-[:DISLIKES]->(disliked:Perro)
                RETURN collect(liked) AS likes, collect(disliked) AS dislikes
                """;
        Result likesDislikesResult = session.run(likesDislikesQuery, parameters("usuario", currentUser.getUsuario(), "password", currentUser.getPassword()));
    
        List<Perro> likes = new ArrayList<>();
        List<Perro> dislikes = new ArrayList<>();
    
        if (likesDislikesResult.hasNext()) {
            Record record = likesDislikesResult.single();
            for (Value value : record.get("likes").asList(Function.identity())) {
                Perro likedPerro = new Perro();
                likedPerro.setUsuario(value.get("usuario").asString(null));
                likedPerro.setNombre(value.get("nombre").asString(null));
                likedPerro.setRaza(value.get("raza", Values.value("Desconocido")).asString());
                likedPerro.setUbicacion(value.get("ubicacion", Values.value("Desconocido")).asString());
                likedPerro.setEdad(value.get("edad", Values.value(0)).asInt());
                likedPerro.setPeso(value.get("peso", Values.value(0)).asInt());
                likedPerro.setTamaño(value.get("tamaño", Values.value("Desconocido")).asString());
                likedPerro.setSexo(value.get("sexo", Values.value("Desconocido")).asString());
                likedPerro.setTienePedigree(value.get("tienePedigree", Values.value(false)).asBoolean());
                likedPerro.setTieneEntrenamiento(value.get("tieneEntrenamiento", Values.value(false)).asBoolean());
                likedPerro.setTieneEnfermedades(value.get("tieneEnfermedades", Values.value(false)).asBoolean());
                likedPerro.setQuedarseCria(value.get("paraCria", Values.value(false)).asBoolean());
                likedPerro.setColor(value.get("color", Values.value("Desconocido")).asString());
                likedPerro.setHaTenidoPareja(value.get("haTenidoPareja", Values.value(false)).asBoolean());
                likedPerro.setAntecedentes(value.get("antecedentes", Values.value("Desconocido")).asString());
                likes.add(likedPerro);
            }
            for (Value value : record.get("dislikes").asList(Function.identity())) {
                Perro dislikedPerro = new Perro();
                dislikedPerro.setUsuario(value.get("usuario").asString(null));
                dislikedPerro.setNombre(value.get("nombre").asString(null));
                dislikedPerro.setRaza(value.get("raza", Values.value("Desconocido")).asString());
                dislikedPerro.setUbicacion(value.get("ubicacion", Values.value("Desconocido")).asString());
                dislikedPerro.setEdad(value.get("edad", Values.value(0)).asInt());
                dislikedPerro.setPeso(value.get("peso", Values.value(0)).asInt());
                dislikedPerro.setTamaño(value.get("tamaño", Values.value("Desconocido")).asString());
                dislikedPerro.setSexo(value.get("sexo", Values.value("Desconocido")).asString());
                dislikedPerro.setTienePedigree(value.get("tienePedigree", Values.value(false)).asBoolean());
                dislikedPerro.setTieneEntrenamiento(value.get("tieneEntrenamiento", Values.value(false)).asBoolean());
                dislikedPerro.setTieneEnfermedades(value.get("tieneEnfermedades", Values.value(false)).asBoolean());
                dislikedPerro.setQuedarseCria(value.get("paraCria", Values.value(false)).asBoolean());
                dislikedPerro.setColor(value.get("color", Values.value("Desconocido")).asString());
                dislikedPerro.setHaTenidoPareja(value.get("haTenidoPareja", Values.value(false)).asBoolean());
                dislikedPerro.setAntecedentes(value.get("antecedentes", Values.value("Desconocido")).asString());
                dislikes.add(dislikedPerro);
            }
        }

        System.out.println("Likes");
        for(Perro like : likes){
            System.err.println(like.getNombre());
        }

        System.out.println("Dislikes");
        for(Perro dislike : dislikes){
            System.err.println(dislike.getNombre());
        }
    
        Perro bestMatch = findBestMatch(currentUser, allPerros, likes, dislikes);
        return bestMatch;
        // Aquí puedes manejar el mejor match, como almacenarlo o mostrarlo
    }
        
    private double calcularPeso(Perro p1, Perro p2, List<Perro> likes, List<Perro> dislikes) {
        double peso = 0;
    
        // Ponderar atributos comunes
        peso += (p1.getRaza().equals(p2.getRaza())) ? 10 : 0;
        peso += (p1.getUbicacion().equals(p2.getUbicacion())) ? 15 : 0;
        peso += (p1.getTamaño().equals(p2.getTamaño())) ? 10 : 0;
        peso += calcularDiferenciaEdad(p1.getEdad(), p2.getEdad());
        peso += (p1.getTienePedigree() == p2.getTienePedigree()) ? 10 : 0;
        peso += (p1.getTieneEntrenamiento() == p2.getTieneEntrenamiento()) ? 10 : 0;
        peso += (p1.getHaTenidoPareja() == p2.getHaTenidoPareja()) ? 10 : 0;
        peso += calcularDiferenciaPeso(p1.getPeso(), p2.getPeso());
        peso += (p1.getTieneEnfermedades() == p2.getTieneEnfermedades()) ? 0 : 10;
        peso += (p1.getQuedarseCria() == p2.getQuedarseCria()) ? 10 : 0;
    
        // Ajustar puntuación basándose en likes
        for (Perro like : likes) {
            if(like != null){
                if (p2.getRaza().equals(like.getRaza())) peso += 5;
                if (p2.getUbicacion().equals(like.getUbicacion())) peso += 5;
                if (p2.getTamaño().equals(like.getTamaño())) peso += 5;
                if (p2.getTienePedigree() == like.getTienePedigree()) peso += 5;
                if (p2.getTieneEntrenamiento() == like.getTieneEntrenamiento()) peso += 5;
                if (p2.getQuedarseCria() == like.getQuedarseCria()) peso += 5;
                if (p2.getColor().equals(like.getColor())) peso += 5;
                if (p2.getHaTenidoPareja() == like.getHaTenidoPareja()) peso += 5;
                if (p2.getTieneEnfermedades() == like.getTieneEnfermedades()) peso += 5;
            }
        }
    
        // Ajustar puntuación basándose en dislikes
        for (Perro dislike : dislikes) {
            if(dislike != null){
                if (p2.getRaza().equals(dislike.getRaza())) peso -= 5;
                if (p2.getUbicacion().equals(dislike.getUbicacion())) peso -= 5;
                if (p2.getTamaño().equals(dislike.getTamaño())) peso -= 5;
                if (p2.getTienePedigree() == dislike.getTienePedigree()) peso -= 5;
                if (p2.getTieneEntrenamiento() == dislike.getTieneEntrenamiento()) peso -= 5;
                if (p2.getQuedarseCria() == dislike.getQuedarseCria()) peso -= 5;
                if (p2.getColor().equals(dislike.getColor())) peso -= 5;
                if (p2.getHaTenidoPareja() == dislike.getHaTenidoPareja()) peso -= 5;
                if (p2.getTieneEnfermedades() == dislike.getTieneEnfermedades()) peso -= 5;
            }
        }

        if(p1.getSexo().equals(p2.getSexo()))
            peso = -1000;
        
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
