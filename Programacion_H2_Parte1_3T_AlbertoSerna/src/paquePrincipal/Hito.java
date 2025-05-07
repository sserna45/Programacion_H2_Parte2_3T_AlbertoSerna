package paquePrincipal;

import java.sql.*;
import java.util.Scanner;

public class Hito {

    // Primero pondré los datos para conectarme a la base de datos MySQL.
    static final String URL = "jdbc:mysql://localhost:3306/cine_albertosernaa";
    static final String USER = "root";
    static final String PASSWORD = "curso";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int opcion = 0;

        // Aquí creo el menú
        do {
            System.out.println("1 - Ver películas");
            System.out.println("2 - Añadir película");
            System.out.println("3 - Eliminar película");
            System.out.println("4 - Modificar película"); // Opción añadida para modificar
            System.out.println("5 - Salir");
            System.out.print("Selecciona una opción: ");
            try {
                opcion = sc.nextInt(); 
            } catch (Exception e) {
                System.out.println("Opción no válida.");
                sc.next(); 
                continue;
            }
            
            //Ahora voy a crear un switch para que se ejecute un método u otro.
            switch (opcion) {
                case 1:
                    mostrarPeliculas(); 
                    break;
                case 2:
                    añadirPelicula();
                    break;
                case 3:
                    eliminarPelicula();
                    break;
                case 4:
                    modificarPelicula();  // Llamada al método para modificar película
                    break;
                case 5:
                    System.out.println("Saliendo del sistema");
                    break;
                default:
                    System.out.println("Opción no válida."); 
            }
        } while (opcion != 5); 
        sc.close(); 
    }

    // Aquí mostramos las  películas desde la base de datos
    public static void mostrarPeliculas() {
        String sql = "SELECT p.id_pelicula, p.titulo, p.duracion, p.año_lanzamiento, " +
                     "p.edad_recomendada, g.nombre AS genero " +
                     "FROM peliculas p JOIN generos g ON p.id_genero = g.id_genero";

        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println("\nCódigo: " + rs.getString("id_pelicula"));
                System.out.println("Título: " + rs.getString("titulo"));
                System.out.println("Duración: " + rs.getInt("duracion") + " minutos");
                System.out.println("Año de lanzamiento: " + rs.getInt("año_lanzamiento"));
                System.out.println("Edad recomendada: " + rs.getString("edad_recomendada"));
                System.out.println("Género: " + rs.getString("genero"));
                System.out.println("--------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error al acceder a la base de datos: " + e.getMessage());
        }
    }

    //Ahora voy a crear el método para añadir peliculas a la base de datos
    public static void añadirPelicula() {
        Scanner sc = new Scanner(System.in);
        
        // Aquí solicitaremos al usuario que vaya introduciendo los datos necesarios para añadir la película (id, título, duración...)
        System.out.print("Introduce el ID de la película: ");
        String id = sc.nextLine();
        
        System.out.print("Introduce el título: ");
        String titulo = sc.nextLine();
        
        System.out.print("Introduce la duración en minutos: ");
        int duracion = sc.nextInt();
        sc.nextLine(); 
        
        System.out.print("Introduce el año de lanzamiento: ");
        int anio = sc.nextInt();
        sc.nextLine(); 
        
        System.out.print("Introduce la edad recomendada : ");
        String edad = sc.nextLine();

        System.out.print("Introduce el ID del género: ");
        String genero = sc.nextLine();
        
        // Ahora para que se conecte a la base de datos
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Aquí se consulta si esa pelicula ya existe en la base de datos
            String consulta = "SELECT * FROM peliculas WHERE id_pelicula = ?";
            PreparedStatement psBuscar = conexion.prepareStatement(consulta);
            psBuscar.setString(1, id);
            ResultSet rs = psBuscar.executeQuery();
            
            // Ahora se verificar si ya existe una película con ese ID y si es así se mostrará un mensaje.
            if (rs.next()) {
                System.out.println("Ya existe una película con ese ID.");
            } else {
                // Aquí se insertará la película a la base de datos.
                String insert = "INSERT INTO peliculas (id_pelicula, titulo, duracion, año_lanzamiento, edad_recomendada, id_genero) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement psInsert = conexion.prepareStatement(insert);
                psInsert.setString(1, id);
                psInsert.setString(2, titulo);
                psInsert.setInt(3, duracion);
                psInsert.setInt(4, anio);
                psInsert.setString(5, edad);
                psInsert.setString(6, genero);
                
               int filas = psInsert.executeUpdate();
                
                // Ahora se verificara si la pelicula se a añadido y se mostrara un mensaje de exito o de que no se a podido añadir.
                if (filas > 0) {
                    System.out.println("Película añadida correctamente.");
                } else {
                    System.out.println("No se pudo añadir la película.");
                }
                
                // Cerrar el PreparedStatement de inserción
                psInsert.close();
            }
            
            // Cierra todos los recursos
            rs.close();
            psBuscar.close();
        } catch (SQLException e) {
            // Ahora se pone el manejo de errores y excepciones
            System.out.println("Error al añadir la película: " + e.getMessage());
        }
    }

    // Ahora creare el metodo para eliminar una película
    public static void eliminarPelicula() {
        Scanner sc = new Scanner(System.in);
        
        // Primero se solicita al usuario el ID de la película que quiere eliminar
        System.out.print("Introduce el ID de la película que quieres eliminar: ");
        String id = sc.nextLine();
        
        // Se hace la conexion a la base de datos
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Y se consulta si la película ya existe en la base de datos
            String consulta = "SELECT * FROM peliculas WHERE id_pelicula = ?";
            PreparedStatement psBuscar = conexion.prepareStatement(consulta);
            psBuscar.setString(1, id);
            ResultSet rs = psBuscar.executeQuery();
            
            // Se verifica si la película existe
            if (rs.next()) {
                // Y si existe se elimina de la base de datos
                String delete = "DELETE FROM peliculas WHERE id_pelicula = ?";
                PreparedStatement psDelete = conexion.prepareStatement(delete);
                psDelete.setString(1, id);
                
                // Esto sirve para ejecutar la eliminación de la película
                int filas = psDelete.executeUpdate();
                
                // Ahora se verifica que se haya elminado y se muestra un mensaje
                if (filas > 0) {
                    System.out.println("Película eliminada correctamente.");
                }
                
                psDelete.close();
                //En el  caso de que no exista una pelicula con ese ID, se muestra un mensaje.
            } else {
                System.out.println("No existe ninguna película con ese ID.");
            }
            
            // Cerramos todos los recursos
            rs.close();
            psBuscar.close();
        } catch (SQLException e) {
            // Y de nuevo los errores y excepciones
            System.out.println("Error al eliminar la película: " + e.getMessage());
        }
    }

    // Por último creo el método para modificar uuna película
    public static void modificarPelicula() {
        Scanner sc = new Scanner(System.in);
        
        // Primero se solicita el ID de la película que se desea modificar
        System.out.print("Introduce el ID de la película que deseas modificar: ");
        String id = sc.nextLine();
        
        // Se hace la conexión a la base de datos
        try (Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Se consulta si la película existe en la base de datos
            String consulta = "SELECT * FROM peliculas WHERE id_pelicula = ?";
            PreparedStatement psBuscar = conexion.prepareStatement(consulta);
            psBuscar.setString(1, id);
            ResultSet rs = psBuscar.executeQuery();
            
            // Si existe la película se podrá modificar
            if (rs.next()) {
                System.out.println("Película encontrada: " + rs.getString("titulo"));
                
                // Se solicitarán los nuevos datos a modificar que serán el titulo y la duración
                System.out.print("Introduce el nuevo título: ");
                String nuevoTitulo = sc.nextLine();
                if (nuevoTitulo.isEmpty()) {
                    nuevoTitulo = rs.getString("titulo"); 
                }
                
                System.out.print("Introduce la nueva duración: ");
                String nuevaDuracion = sc.nextLine();
                int duracion = rs.getInt("duracion");
                if (!nuevaDuracion.isEmpty()) {
                    duracion = Integer.parseInt(nuevaDuracion); 
                }
                
                // Aquí se realiza la consulta para actualizar los datos de la película.
                String update = "UPDATE peliculas SET titulo = ?, duracion = ? WHERE id_pelicula = ?";
                PreparedStatement psUpdate = conexion.prepareStatement(update);
                psUpdate.setString(1, nuevoTitulo);
                psUpdate.setInt(2, duracion);
                psUpdate.setString(3, id);
                
                //Se mostrará un mensaje si se a modificado o si no se a podio modificar
                int filas = psUpdate.executeUpdate();
                if (filas > 0) {
                    System.out.println("Película modificada correctamente.");
                } else {
                    System.out.println("No se pudo modificar la película.");
                }
                
                psUpdate.close();
            } else {
                System.out.println("No existe una película con ese ID.");
            }
            
            // Se cierran los recursos
            rs.close();
            psBuscar.close();
        } catch (SQLException e) {
            // Se manejan los errores y excepciones
            System.out.println("Error al modificar la película: " + e.getMessage());
        }
    }
}




