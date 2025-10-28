package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

public class Conexion {

    // Core
    private Connection connection;
    private Statement statement;

    // Config
    private String host;
    private String database;
    private String user;
    private String password;

    // SQL Server options
    private int port = -1;                // 1433 típico; -1 = sin puerto explícito
    private String instanceName = null;   // p.ej. "SQLEXPRESS"
    private boolean encrypt = true;
    private boolean trustServerCertificate = true;  // DEV: true; PROD: false + cert válido
    private boolean integratedSecurity = false;     // Windows Auth (requiere sqljdbc_auth.dll)

    // Driver / protocolo (fijos para SQL Server)
    private static final String DRIVER  = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String PROTOCOL = "jdbc:sqlserver";

    // --- Constructor (SQL Server) ---
    public Conexion(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    // --- Setters de opciones ---
    public void setPort(int port) { this.port = port; }
    public void setInstanceName(String instanceName) { this.instanceName = instanceName; }
    public void setEncrypt(boolean encrypt) { this.encrypt = encrypt; }
    public void setTrustServerCertificate(boolean trust) { this.trustServerCertificate = trust; }
    public void setIntegratedSecurity(boolean integrated) { this.integratedSecurity = integrated; }

    // --- Getters útiles ---
    public Connection getConnection() { return connection; }
    public Statement getStatement() { return statement; }

    // --- Interno: cargar driver ---
    private boolean loadDriver() {
        try { Class.forName(DRIVER); } catch (ClassNotFoundException ex) { return false; }
        return true;
    }

    // --- Conectar a SQL Server ---
    public void connect() throws SQLException {
        System.out.println("Conectando ...");

        if (!loadDriver()) {
            System.err.println("No se encuentra el Driver: " + DRIVER);
            JOptionPane.showMessageDialog(null, "Error Driver de Base de Datos...", "ADVERTENCIA",
                    JOptionPane.ERROR_MESSAGE);
            throw new SQLException("Driver no encontrado: " + DRIVER);
        }

        String url;
        Properties props = new Properties();

        try {
            // jdbc:sqlserver://host[\instancia][:puerto];databaseName=DB;...
            String hostPart = (instanceName != null && !instanceName.isEmpty())
                    ? host + "\\" + instanceName
                    : host;
            String portPart = (port > 0) ? (":" + port) : "";

            StringBuilder sb = new StringBuilder();
            sb.append(PROTOCOL).append("://").append(hostPart).append(portPart).append(";");
            sb.append("databaseName=").append(database).append(";");
            sb.append("encrypt=").append(encrypt).append(";");

            if (encrypt && trustServerCertificate) {
                sb.append("trustServerCertificate=true;");
            }
            if (integratedSecurity) {
                sb.append("integratedSecurity=true;");
            }

            url = sb.toString();

            if (!integratedSecurity) {
                props.setProperty("user", user);
                props.setProperty("password", password);
            }

            this.connection = DriverManager.getConnection(url, props);
            this.statement = this.connection.createStatement();

        } catch (SQLException ex) {
            System.err.println("No se puede conectar a la Base de Datos.");
            System.err.println(ex.getMessage());
            throw ex;
        }

        System.out.println("Conectado ...");
    }

    // --- Cerrar recursos ---
    public void close() {
        try { if (statement != null && !statement.isClosed()) statement.close(); } catch (SQLException ignored) {}
        try { if (connection != null && !connection.isClosed()) connection.close(); } catch (SQLException ex) {
            System.err.println("No se puede cerrar la conexión a la Base de Datos.");
            System.err.println(ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al cerrar la Base de Datos...",
                    "ADVERTENCIA", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Helpers simples ---
    /** Ejecuta un SELECT y devuelve el ResultSet (recordá cerrarlo en el llamador). */
    public ResultSet executeQuery(String sql) throws SQLException {
        ensureConnected();
        return statement.executeQuery(sql);
    }

    /** Ejecuta INSERT/UPDATE/DELETE y devuelve la cantidad de filas afectadas. */
    public int executeUpdate(String sql) throws SQLException {
        ensureConnected();
        return statement.executeUpdate(sql);
    }

    private void ensureConnected() throws SQLException {
        if (connection == null || connection.isClosed() || statement == null || statement.isClosed()) {
            throw new SQLException("La conexión no está inicializada. Llamá a connect() primero.");
        }
    }
}

