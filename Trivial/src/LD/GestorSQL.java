package LD;
import java.sql.*;
import java.util.ArrayList;


import LN.FichaDePartida;
import LN.Jugador;
import LN.Partida;
import LN.Pregunta;
public class GestorSQL 
{
	public Connection ConectarA (String ruta)
	{
		// Cargamos el driver
		try 
		{
			Class.forName("org.sqlite.JDBC");
		} 
		catch (ClassNotFoundException e) 
		{
			System.out.println("No se ha encontrado la libreria");
		}
		
		
		
		 Connection conec = null; // Declaramos la conexion
		 try 
		 {
			 conec = DriverManager.getConnection("jdbc:sqlite:"+ ruta );
			 System.out.println("Conexi�n realizada correctamente - Ruta de base de datos: " + ruta);
		 }
		 
		 catch (SQLException e)
		 {
			 System.out.println("No se ha encontrado la ruta de la BD");
		 }
		 
		 return conec;
	
	}
	public ArrayList<Pregunta> listaPreguntas(String categoria){
		Connection conn = ConectarA("data/trivial");
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String sentencia = "SELECT * FROM preguntas WHERE categoria = \""+categoria+"\"";
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sentencia);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int id_pregunta = 0;
		String pregunta = null;
		
		int correcta = -1;
		
		ArrayList<Pregunta> resultado = new ArrayList<Pregunta>();
		Pregunta aux;
		try {
			while(rs.next()){
				ArrayList <String> respuestas = new ArrayList<String>();
				id_pregunta = rs.getInt("id_pregunta");
				for(int i = 0; i<4; i++){
					String res = rs.getString("respuesta"+(i+1));
					respuestas.add(res);
				}
				pregunta = rs.getString("pregunta");
				correcta = rs.getInt("correcta");
				aux = new Pregunta(categoria, pregunta, respuestas, correcta);
				aux.setId_pregunta(id_pregunta);
				resultado.add(aux);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return resultado;
	}
	
	
	public ArrayList<Jugador> MostrarJugadores()  
	{
		Connection conn = ConectarA("data/trivial.db"); //Nos conectamos a la BD
		Statement stmt = null;
	
		try {
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		
		String sentencia= "SELECT * FROM JUGADOR";
		
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery(sentencia);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String nombre = " ";
		String id= " ";
		
		ArrayList<Jugador> datos_J_aux = new ArrayList<Jugador>();
		int id_int;
		
		Jugador j1;
		
		try {
			
				while(rs.next())
				{
					j1 = new Jugador();
					id =rs.getString(1);
					System.out.println(id);
			
					id_int=Integer.parseInt(id);
					System.out.println(id_int);
					j1.setId(id_int);
			
					nombre= rs.getString(2);
					System.out.println(nombre);
					j1.setNombre_usuario(nombre);
			
					datos_J_aux.add(j1);

				}
				
				
		}
		catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return datos_J_aux;
		
		
		
	}
	
	
	
	public static ArrayList<Partida> MostrarPartidas() 

{
	GestorSQL G = new GestorSQL(); 
	Connection conn = G.ConectarA("data/Trivial.db"); //Nos conectamos a la BD
	Statement stmt = null;

	try {
		stmt = conn.createStatement();
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

		
		String sentencia= "SELECT * FROM PARTIDA";
		
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery(sentencia);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String ID1 = " ";
		String ID2= " ";
		String IDP= " ";
		int turno= 0;
		boolean terminada=false;
		String fecha = " ";
		
		ArrayList<Partida> datos_P_aux = new ArrayList<Partida>();
		
		
		Partida P1;
		
		try {
			
				while(rs.next())
				{
					P1 = new Partida();
					IDP =rs.getString(1);
					P1.setId_P(IDP);
					
					ID1= rs.getString(2);
					P1.setId_j1(ID1);
					
					ID2= rs.getString(3);
					P1.setId_j2(ID2);
					
					turno=rs.getInt(4);
					
					
					
					terminada=rs.getBoolean("TERMINADA");
					P1.setTerminada(terminada);
					
					fecha=rs.getString(6);
					P1.setFecha_inic(fecha);
					
				
					datos_P_aux.add(P1);

				}
				
				
		}
		catch ( SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for(Partida p : datos_P_aux)
		{
			System.out.println(p.getId_P());
			System.out.println(p.getFecha_inic());
			
		}
		
		return datos_P_aux;
		
		
		
	}

public void guardarJugador(Jugador jug)
	{
		Connection conn = ConectarA("data/Trivial.db"); //Nos conectamos a la BD
		Statement stmt = null;
	
		try 
		{
			stmt = conn.createStatement();
		}
		catch (SQLException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String sentencia = "INSERT INTO JUGADOR (ID_J, NOMBRE_J ) VALUES ( " +jug.getId()+ ", '"+jug.getNombre_usuario()+"'"+" );";
		
		
		try 
		{
			stmt.executeUpdate(sentencia);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		sentencia = "INSERT INTO ESTADISTICA VALUES ("+ jug.getId()+" ,0 ,0 ,0 ,0"+ ");" ; 
		
		try {
			stmt.executeUpdate(sentencia);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			conn.close();
		} 
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Falta comprobar si escribe en la BD.
		
		
	}

public void GuardarPartida (Partida par, FichaDePartida p1, FichaDePartida p2)
{
	
	
	Connection conn = ConectarA("data/Trivial.db"); //Nos conectamos a la BD
	Statement stmt = null;
	
	
//Comprobado que la partida y las fichas llegan OK
	

	try 
	{
		stmt = conn.createStatement();
	}
	catch (SQLException e1) 
	{
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	String sentencia = " INSERT INTO PARTIDA " + "VALUES( '" +par.getId_P()+"' , '"+par.getId_j1()+"' , '"+par.getId_j2()+"' ," +par.isTurno()+ ",'" +par.isTerminada()+"', '"+par.getFecha_inic()+"' );";
	System.out.println(sentencia);
	try {
		stmt.executeUpdate(sentencia);
		System.out.println("Hecho, partida guardada!");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	sentencia = "INSERT INTO FICHA_PARTIDA VALUES (  '"+ p1.getId_jugador()+"', '"+p1.getId_partida()+"', "+p1.getNum_quesitos()+", "+p1.getPreguntas_realizadas()+", "+p1.getPreguntas_acertadas()+" );";
	try {
		stmt.executeUpdate(sentencia);
		System.out.println("Ficha1 guardada!");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	sentencia = "INSERT INTO FICHA_PARTIDA VALUES (  "+ p2.getId_jugador()+", "+p2.getId_partida()+", "+p2.getNum_quesitos()+", "+p2.getPreguntas_realizadas()+", "+p2.getPreguntas_acertadas()+" );";
	try {
		stmt.executeUpdate(sentencia);
		System.out.println("Ficha2 guardada!");
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	try {
		conn.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
