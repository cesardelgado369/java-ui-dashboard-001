
package com.raven.model;

import java.sql.Connection;
import javax.swing.JOptionPane;
import java.sql.DriverManager;


public class Conexion {
    Connection conectar = null;
    
    String usuario="usuarioTFI";
    String clave="123456abcd";
    String bd="TFIConcesionariaDB";
    String ip="localhost";
    String puerto="1433";
    
    String cadena = "jdbc:sqlserver//"+ip+" : "+puerto+"/"+bd;
    
    public Connection establecerConexion() {
    try {
        String cadena =
            "jdbc:sqlserver://localhost:" + puerto + ";" +
            "databaseName=" + bd + ";" +
            "encrypt=true;" +
            "trustServerCertificate=true;";

        conectar = DriverManager.getConnection(cadena, usuario, clave);
        JOptionPane.showMessageDialog(null, "Se conectó exitosamente");
        return conectar;

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error conectando a la BD: " + e.toString());
    }
    return null;
    }
}

