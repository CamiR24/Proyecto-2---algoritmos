/**
 * 
 */
package main;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Transaction;
import org.neo4j.driver.TransactionWork;
import org.neo4j.driver.summary.ResultSummary;

import static org.neo4j.driver.Values.parameters;

import java.util.LinkedList;
import java.util.List;
/**
 * @author Administrator
 *
 */
public class EmbeddedNeo4j implements AutoCloseable{

    private final Driver driver;
    

    public EmbeddedNeo4j( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void printGreeting( final String message )
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    Result result = tx.run( "CREATE (a:Greeting) " +
                                                    "SET a.message = $message " +
                                                    "RETURN a.message + ', from node ' + id(a)",
                            parameters( "message", message ) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    
    public LinkedList<String> getDogs()
    {
        try ( Session session = driver.session() )
        {
    
        LinkedList<String> dogs = session.readTransaction( new TransactionWork<LinkedList<String>>()
            {
                @Override
                public LinkedList<String> execute(Transaction tx)
                {
                     //Result result = tx.run( "MATCH (people:Person) RETURN people.name");
                	Result result = tx.run( "MATCH (perro:PERRO) RETURN perro.name");
                    LinkedList<String> mydogs = new LinkedList<String>();
                    List<Record> registros = result.list();
                    for (int i = 0; i < registros.size(); i++) {
                    	 //mydogs.add(registros.get(i).toString());
                    	mydogs.add(registros.get(i).get("perro.name").asString());
                    }
                    return mydogs;
                }
            } );
            return dogs;
        }
    }
    
    public LinkedList<String> getDogsBySex(String dog)
    {
        try (Session session = driver.session() )
        {

            LinkedList<String> perros = session.readTransaction(new TransactionWork<LinkedList<String>>()
            {
                @Override
                public LinkedList<String> execute( Transaction tx )
                {
                    Result result = tx.run( "MATCH (n:PERRO {name: \"" + perros + "\"})-[:Es]->(sexo) RETURN sexo.opc");
                    LinkedList<String> mydogs = new LinkedList<String>();
                    List<Record> registros = result.list();
                    for (int i = 0; i < registros.size(); i++) {
                   	 //mydogs.add(registros.get(i).toString());
                    mydogs.add(registros.get(i).get("sexo.opc").asString());
                    }
                    return mydogs;
                }
            } );
            
            return perros;
        }
    }
    
    public String insertDog(String name, String raza, String owner, String ubicacion, int age, float peso, String size, String color, String pedigree, String antecedentes, String perruno, String cria) {
    	try (Session session = driver.session() )
        {

   	     String result = session.writeTransaction( new TransactionWork<String>()
   		 
            {
                @Override
                public String execute( Transaction tx )
                {
                    tx.run( "CREATE (perro:PERRO {name:'" + name + "', raza:"+ raza +", owner:'"+ owner +"', ubicacion:"+ ubicacion +", age:"+ age +",peso:"+ peso +",size:"+ size +",color:"+ color +",pedigree:"+ pedigree +",antecedentes:"+ antecedentes +", perruno:"+ perruno +",cria:"+ cria +"})");
                    return "OK";
                }
            }
        );
            return result;
        } catch (Exception e) {
        	return e.getMessage();
        }
    }

}
