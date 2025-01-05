package src.utils;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.Scanner;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

/**
 * Classe che gestisce le query e le varie operazioni da effettuare al database.
 * Contiene delle costanti identificanti il database e l'utente, modificando queste si possono modificare le impostazioni di
 * accesso al database.
 */
public class Database {

    private static Database instance = null;

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_NAME = "src";
    private static final String USER = "root";
    private static final String PASS = "";
    private static final boolean debug = false;
    private static final DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();

    private Connection conn = null;

    private Database() {
        try {
            this.startMariaDB();

            if (!this.checkDBExists(Database.DB_NAME)) {
                JOptionPane.showMessageDialog(null,
                        Utils.getText("database_creation"),
                        Utils.getText("database_creation_title"), JOptionPane.INFORMATION_MESSAGE);
                this.createDatabase(Database.DB_NAME);
                importSQL("oop.sql");
                JOptionPane.showMessageDialog(null,
                        Utils.getText("success_db_creation"),
                        Utils.getText("success"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    Utils.getText("error_db_creation"),
                    Utils.getText("error"), JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Starts an embedded instance of MariaDB
     */
    private void startMariaDB() throws Exception {
        configBuilder.setPort(0);
        configBuilder.setDataDir("data");
        DB db = DB.newEmbeddedDB(configBuilder.build());
        db.start();
    }

    /**
     * Esegue un file .sql, quali un dump o query generiche
     *
     * @param fileName il file sql
     * @throws SQLException
     * @throws FileNotFoundException se il file non esiste allora lancia questa eccezione
     */
    public void importSQL(String fileName) throws SQLException, FileNotFoundException {
        conn = DriverManager.getConnection(configBuilder.getURL(DB_NAME), USER, PASS);
        InputStream in = new FileInputStream(new File(fileName));
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = null;
        try {
            st = conn.createStatement();
            while (s.hasNext()) {
                String line = s.next();
                if (line.startsWith("--"))
                    continue;
                if (line.startsWith("/*!") && line.endsWith("*/")) {
                    int i = line.indexOf(' ');
                    line = line.substring(i + 1, line.length() - " */".length());
                }

                if (line.trim().length() > 0) {
                    st.execute(line);
                    System.out.println(line);
                }
            }
        } finally {
            if (st != null) st.close();
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Esegue una query definita dall'attributo sql e restituisce un oggetto ResultSQL contenente i valori del risultato della query.
     *
     * @param sql    query da effettuare
     * @param update true se la query modifica la struttura delle tabelle, false altrimenti
     * @return Un oggetto ResultSQL contenente tutti i risultati della query
     */
    public ResultSQL query(String sql, boolean update) {
        if (debug)
            System.out.println(sql);

        Statement stmt = null;
        ResultSet rs;
        ResultSQL resultList = new ResultSQL();
        try {
            //registra il driver JDBC
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(configBuilder.getURL(DB_NAME), USER, PASS);

            //esegui la query
            stmt = conn.createStatement();
            if (update)
                stmt.executeUpdate(sql);
            else {
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    resultList.addRow();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        if (resultList.getFromLast(rs.getMetaData().getColumnName(i)) == null)
                            resultList.addToLast(rs.getMetaData().getColumnName(i), rs.getObject(i));
                        else
                            resultList.addToLast("_" + rs.getMetaData().getColumnName(i), rs.getObject(i));
                    }
                }
            }
            //chiudi tutto
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //chiudi tutto se non l'hai già fatto
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return resultList;
    }

    /**
     * Ottiene il nome di tutte le colonne relative ad una determinata tabella
     *
     * @param table nome della tabella di cui ottenere le colonne
     * @return array di stringhe contenente i nomi delle colonne
     */
    public String[] getColumnNames(String table) {
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(configBuilder.getURL(DB_NAME), USER, PASS);
            stmt = conn.createStatement();

            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + table);
            ResultSetMetaData meta = resultSet.getMetaData();

            Integer columncount = meta.getColumnCount();
            int count = 1;
            String[] columnNames = new String[columncount];
            while (count <= columncount) {
                columnNames[count - 1] = meta.getColumnName(count);
                count++;
            }
            return columnNames;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Crea un nuovo database
     *
     * @param name il nome del database da creare
     */
    private void createDatabase(String name) {
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(configBuilder.getURL(""), USER, PASS);

            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE DATABASE " + name);
            System.out.println("Database created successfully...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException ignored) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /**
     * Controlla se il database esiste già
     *
     * @param dbName il nome del database da controllare
     * @return true se esiste, false altrimenti
     */
    private boolean checkDBExists(String dbName) {

        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(configBuilder.getURL(""), USER, PASS);

            ResultSet resultSet = conn.getMetaData().getCatalogs();

            while (resultSet.next()) {

                String databaseName = resultSet.getString(1);
                if (databaseName.equals(dbName)) {
                    if (debug)
                        System.out.println("Database '" + databaseName + "' exists!");
                    return true;
                }
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    Utils.getText("no_dmbs_detected"),
                    Utils.getText("error"), JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        return false;
    }

    /**
     * Resetta il database alle impostazioni di partenza
     *
     * @throws FileNotFoundException
     * @throws SQLException
     */
    public void resetDatabase() throws FileNotFoundException, SQLException {
        query("DROP DATABASE " + Database.DB_NAME, true);
        createDatabase(DB_NAME);
        importSQL("oop.sql");
    }

    public static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }
}