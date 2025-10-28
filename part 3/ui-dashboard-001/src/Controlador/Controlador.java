/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import Modelo.Conexion;
import java.sql.ResultSet;

public class Controlador {

    private Conexion db; // guardamos la referencia para cerrar luego

    
    
    public static void iniciarConexion() {
        try {
            // === SQL Server en localhost, BD "ventas" ===
            // Windows Auth → user/pass vacíos
            db = new Conexion("localhost", "ventas", "", "");

            // Elegí solo una de estas opciones:
            db.setPort(1433);                 // Opción A: por puerto (típico 1433)
            // db.setInstanceName("SQLEXPRESS"); // Opción B: instancia con nombre (comentá setPort)

            // Seguridad (DEV). En prod usá certificado válido y dejá false.
            db.setEncrypt(true);
            db.setTrustServerCertificate(true);

            // Autenticación integrada de Windows (requiere sqljdbc_auth.dll x64 en PATH)
            db.setIntegratedSecurity(true);

            // Conectar
            db.connect();
            System.out.println("✅ Conexión a 'ventas' inicializada.");

            // Smoke test opcional: ver usuario y versión
            String sqlTest = "SELECT @@VERSION AS version, DB_NAME() AS db, SUSER_SNAME() AS login";
            try (ResultSet rs = db.executeQuery(sqlTest)) {
                if (rs.next()) {
                    System.out.println("Base actual : " + rs.getString("db"));
                    System.out.println("Usuario     : " + rs.getString("login"));
                    System.out.println("Versión     : " + rs.getString("version"));
                }
            }

            // TODO: acá podés instanciar DAOs/Models y seguir tu flujo MVC

        } catch (Exception ex) {
            System.err.println("❌ Error al iniciar la conexión: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void cerrarConexion() {
        try {
            if (db != null) {
                db.close();
                System.out.println("🔒 Conexión cerrada.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al cerrar la conexión: " + e.getMessage());
        } finally {
            db = null;
        }
    }

    // Si querés probar rápido desde acá:
    public static void main(String[] args) {
        Controlador c = new Controlador();
        c.iniciarConexion();
        c.cerrarConexion();
    }
}

