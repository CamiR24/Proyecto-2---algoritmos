/**
 * 
 */
package main;

import java.util.LinkedList;
import java.util.Scanner;


/**
 * @author MAAG
 *
 */
public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String username = "neo4j";
		String password = "duress-bullets-sights";
		String boltURL = "bolt://44.205.14.158:7687";
		
		try (EmbeddedNeo4j db = new EmbeddedNeo4j( boltURL, username, password ) )
        {
			LinkedList<String> mydogs = db.getdogs();
		 	
		 	for (int i = 0; i < mydogs.size(); i++) {
		 		System.out.println(mydogs.get(i));
		 	}
        	
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Ingresa el nombre de tu perro");
		Scanner in = new Scanner(System.in);
		String myDogs = in.nextLine();
		
		try (EmbeddedNeo4j db = new EmbeddedNeo4j(boltURL, username, password))
        {
		 	LinkedList<String> sex = db.getDogsBySex(myDogs);
		 	
			for (int i = 0; i < sex.size(); i++) {
				System.out.println(sex.get(i));
			}
        	
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Ingrese el nombre del perro");
		String name = in.nextLine();
		System.out.println("Ingrese la raza del perro");
		String raza = in.nextLine();
		System.out.println("Ingrese el nombre del dueño");
		String owner = in.nextLine();
		System.out.println("Ingrese ubicación");
		String location = in.nextLine();
		System.out.println("Ingrese edad del perro");
		int age = Integer.parseInt(in.nextLine());
		System.out.println("Ingrese peso del perro");
		String peso = in.nextLine();
		float pesoF = new Float(peso);
		System.out.println("Ingrese tamaño");
		String size = in.nextLine();
		System.out.println("Ingrese color");
		String color = in.nextLine();
		System.out.println("Ingrese si tiene pedigree o no");
		String pedigree = in.nextLine();
		System.out.println("Ingrese antecedentes de enferemedades");
		String antecedentes = in.nextLine();
		System.out.println("Ingrese si ya tuvo pareja anteriormente");
		String perruno = in.nextLine();
		System.out.println("Ingrese si se quiere quedar con la cría");
		String cria = in.nextLine();
		
		try ( EmbeddedNeo4j db = new EmbeddedNeo4j( boltURL, username, password ) )
        {
			String result = db.insertPerro(name, raza, owner, location, age, pesoF, size, color, pedigree, antecedentes, perruno, cria);
		 	
		 	if (result.equalsIgnoreCase("OK")) {
		 		System.out.println("Perro insertado correctamente");
		 	}
        	
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}