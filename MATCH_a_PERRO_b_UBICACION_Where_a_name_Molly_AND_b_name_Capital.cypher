MATCH (a:PERRO), (b:UBICACION) Where a.name = "Molly" AND b.name = "Capital" 
CREATE (a)-[r:Se_encuentra_en]-> (b)
RETURN a,b