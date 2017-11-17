/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Generacio de la Base de Dades
 * 
 * Versio: v4
 * Comentaris: Falta generar Certificats
 */

package appinicivotacio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.Vector;

public class AppIniciVotacio {
    /* Constants */
    private static final int numElec = 300;
    private static final String NIF_STRING_ASOCIATION = "TRWAGMYFPDXBNJZSQVHLCKE";
    
    private static final String errMsg1 = "Error -1: Unable to connect Data Base";
    private static final String errMsg2 = "Error -2: Unable to reset votation quota"; 
    private static final String errMsg3 = "Error -3: Unable to create new votation quota";
    private static final String errMsg4 = "Error -4: Unable to reset number of votes";
    private static final String errMsg5 = "Error -5: Unable to open votation time";
    private static final String errMsg6 = "Error -6: Unable to disconnect from Data Base"; 
    private static final String errMsg7 = "Error -7: Unable to close copy file"; 
    private static final String succMsg1 = "INFO: Successfully connected to Data Base";
    private static final String succMsg2 = "INFO: Successfully created tax quota for voting";
    private static final String succMsg3 = "INFO: Successfully reseted number of votes";
    private static final String succMsg4 = "INFO: Successfully opened votation time";
    private static final String succMsg5 = "INFO: Successfully disconnected from Data Base";
    private static final String infoMsg1 = "INFO: Opening copy file 'cens.txt'";
    private static final String infoMsg2 = "INFO: Going to created random entries";
    private static final String endMsg = "END: Open votation proces finished";
    
    private static final String certificat = "-----BEGIN PUBLIC KEY-----\n\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAorSAt4R9UNOA97lLrjYw5KfQj3VAMFS4bFYgtuOWA1y+DZv6AEpPbh6rb0xYrBeGkOFi+87New2znAhO4GZCx+BWe5FgP+YtmQnvp2Wq6FCfjW1wh/eTiS9SuYAvthOi1Ome1DgcDZWtC9iYcKeF1bOHC0lRrhQjdTqpq159ck6FnAkm/fEmpdB7ivJ1em4G+EhE1QXJivElVAG6gOiMrLttB50JjZvgceTKbZ1Z6r0pia4htjB7oE2Y3gs0zRfiYQ8ySnctApmGGNlJy6x1HTsW8TccOA3QVFrSzIJdt95QMvxZBbMfzlXYiFl5T4WdK99ihLlYMexgprJpmdDpeQIDAQAB\n-----END PUBLIC KEY-----";
    
    /* Variables */
    private static Connection conn = null;
    private static boolean quotaCreated = false;
    private static Vector<String> noms = new Vector();
    private static Vector<String> cognoms = new Vector();
    private static Vector<String> provincies = new Vector();
    
    public static void main(String[] args) {
        /* Connect to Data Base */
        String url = "jdbc:mysql://mysqlfib.fib.upc.es:3306/";
        String dbName = "BDpxc02";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "pxc02"; 
        String password = "nJoW02Hi";
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, password);
        } catch (Exception e) {
            System.out.println(errMsg1);
        }
        
        /* Delete tax quota for voting */
        boolean reset = true;
        if (conn != null) {
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("DELETE FROM cens");
                psmnt.executeUpdate();
            }
            catch (Exception e) {
                reset = false;
                System.out.println(errMsg2);
            }
        }
        
        /* Create tax quota for voting */
        if (conn != null && reset) {
            System.out.println(succMsg1);
            createQuota();
            if (quotaCreated) System.out.println(succMsg2);
            else System.out.println(errMsg3);
        }
        
        /* Restart number of emited votes */
        boolean nvotes = true;
        if (conn != null && reset && quotaCreated) {
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("UPDATE propietats SET valor=0 WHERE propietat='numvots'");
                psmnt.executeUpdate();
                System.out.println(succMsg3);
            }
            catch (Exception e) {
                nvotes = false;
                System.out.println(errMsg4);
            }
        }
        
        /* Open votation time */
        if (conn != null && reset && quotaCreated && nvotes) {
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("UPDATE propietats SET valor=0 WHERE propietat='estat'");
                psmnt.executeUpdate();
                System.out.println(succMsg4);
            }
            catch (Exception e) {
                System.out.println(errMsg5);
            }
        }
        
        /* Disconnect from Data Base */
        if (conn != null) {
           try {
             conn.close();
             System.out.println(succMsg5);
           } catch (Exception e) {
               System.out.println(errMsg6);
           } 
        }
        
        /* END */
        System.out.println(endMsg);
    }
    
    private static void createQuota() {
        /* Inicialitzem les estructures */
        quotaCreated = true;
        
        //-------------------------PROVINCIES----------------------------------//
        provincies.add("Barcelona");
        provincies.add("Girona");
        provincies.add("Lleida");
        provincies.add("Tarragona");
        
        //----------------------------NOMS-------------------------------------//
        noms.add("Adrian");
        noms.add("Alba");
        noms.add("Alejandro");
        noms.add("Alvaro");
        noms.add("Ana");
        noms.add("Andrea");
        noms.add("Antonia");
        noms.add("Antonio");
        noms.add("Carlos");
        noms.add("Carmen");
        noms.add("Claudia");
        noms.add("Cristina");
        noms.add("Daniel");
        noms.add("David");
        noms.add("Francisco");
        noms.add("Isabel");
        noms.add("Ivan");
        noms.add("Javier");
        noms.add("Joan");
        noms.add("Jordi");
        noms.add("Jose");
        noms.add("Josep");
        noms.add("Josefa");
        noms.add("Juan");
        noms.add("Laura");
        noms.add("Lucia");
        noms.add("Manuel");
        noms.add("Marc");
        noms.add("Maria");
        noms.add("Maria del Carmen");
        noms.add("Maria Teresa");
        noms.add("Marta");
        noms.add("Montserrat");
        noms.add("Nuria");
        noms.add("Oscar");
        noms.add("Pablo");
        noms.add("Paula");
        noms.add("Ramon");
        noms.add("Sara");
        noms.add("Sergio");
        noms.add("Victor");
        
        //--------------------------COGNOMS------------------------------------//
        cognoms.add("Alvarez");
        cognoms.add("Cuadrado");
        cognoms.add("Cortes");
        cognoms.add("Diaz");
        cognoms.add("Dominguez");
        cognoms.add("Fernandez");
        cognoms.add("Ferrer");
        cognoms.add("Font");
        cognoms.add("Hernandez");
        cognoms.add("Garcia");
        cognoms.add("Gil");
        cognoms.add("Gomez");
        cognoms.add("Gonzalez");
        cognoms.add("Gutierrez");
        cognoms.add("Herrero");
        cognoms.add("Jimenez");
        cognoms.add("Lopez");
        cognoms.add("Martin");
        cognoms.add("Martinez");
        cognoms.add("Molina");
        cognoms.add("Moreno");
        cognoms.add("Muñoz");
        cognoms.add("Navarro");
        cognoms.add("Perez");
        cognoms.add("Ramirez");
        cognoms.add("Rodriguez");
        cognoms.add("Romero");
        cognoms.add("Ruiz");
        cognoms.add("Sanchez");
        cognoms.add("Serrano");
        cognoms.add("Torres");
        cognoms.add("Verges");
        
        /* Obrim un fitxer de copia creacio */
        System.out.println(infoMsg1);
        FileWriter fstream = null;
        try {
            fstream = new FileWriter("../appTest/cens.txt"); 
        } catch (IOException ex) {
            quotaCreated = false;
        }
        BufferedWriter out = new BufferedWriter(fstream);
        
        /* Creem el cens */
        if (quotaCreated) {
            System.out.println(infoMsg2);
            for (int i = 0; i < numElec; ++i) {
                /* Calculem les assignacions aleatories */
                int numRandNom = (int) (noms.size()*Math.random());
                int numRandCognom1 = (int) (cognoms.size()*Math.random());
                int numRandCognom2 = (int) (cognoms.size()*Math.random());

                int numRandMes = 1 + (int) (12*Math.random());
                int numRandDia = 0;
                if (numRandMes == 1 || numRandMes == 3 || numRandMes == 5 || numRandMes == 7 || numRandMes == 8 || numRandMes == 10 || numRandMes == 12) {
                    numRandDia = 1 + (int) (31*Math.random());
                }
                else if (numRandMes == 2) {
                    numRandDia = 1 + (int) (28*Math.random());
                }
                else {
                    numRandDia = 1 + (int) (30*Math.random());
                }
                int numRandAny = 1930 + (int) (63*Math.random());
                int numRandProv = (int) (4*Math.random());

                /* Calculem el DNI amb lletra */
                String dni = "";
                int lletra = 0;
                for (int j = 0; j < 8; ++j) {
                    int digit = (int)(10*Math.random());
                    dni = dni + String.valueOf(digit);
                    lletra = lletra*10 + digit;
                }
                dni = dni + NIF_STRING_ASOCIATION.charAt(lletra%23);

                /* Calculem el nom */
                String nom = noms.elementAt(numRandNom);

                /* Calculem el cognom1 */
                String cognom1 = cognoms.elementAt(numRandCognom1);

                /* Calculem el cognom2 */
                String cognom2 = cognoms.elementAt(numRandCognom2);

                /* Calculem la data de naixement */
                String dataNaixement = String.valueOf(numRandAny) + "-" + String.valueOf(numRandMes) + "-" + String.valueOf(numRandDia);

                /* Calculem la provincia */
                String prov = provincies.elementAt(numRandProv);

                /* Mostrem el resultat */
                System.out.println("-->" + dni + " " + nom + " " + cognom1 + " " + cognom2 + " " + dataNaixement + " " + prov);

                /* Afegim a l'arxiu de copia creacio */
                try {
                    out.write(dni);
                    out.newLine();
                }
                catch (Exception e) {
                    quotaCreated = false;
                }

                /* Afegim a la base de dades */
                try {
                    PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("INSERT INTO cens VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                    psmnt.setString(1, dni);
                    psmnt.setString(2, nom);
                    psmnt.setString(3, cognom1);
                    psmnt.setString(4, cognom2);
                    psmnt.setString(5, dataNaixement);
                    psmnt.setString(6, prov);
                    psmnt.setString(7, certificat);
                    psmnt.setString(8, null);
                    psmnt.setString(9, null);
                    psmnt.executeUpdate();
                }
                catch (Exception e) {
                    quotaCreated = false;
                }
            }

            /* Afegim 3 entrades no aleatories */
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("INSERT INTO cens VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psmnt.setString(1, "47917147M");
                psmnt.setString(2, "Oscar");
                psmnt.setString(3, "Dominguez");
                psmnt.setString(4, "Celada");
                psmnt.setString(5, "1988-10-06");
                psmnt.setString(6, "Barcelona");
                psmnt.setString(7, certificat);
                psmnt.setString(8, null);
                psmnt.setString(9, null);
                psmnt.executeUpdate();
            }
            catch (Exception e) {
                quotaCreated = false;
            }
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("INSERT INTO cens VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psmnt.setString(1, "12345678A");
                psmnt.setString(2, "Victor");
                psmnt.setString(3, "Lopez");
                psmnt.setString(4, "Ferrando");
                psmnt.setString(5, "1988-1-1");
                psmnt.setString(6, "Barcelona");
                psmnt.setString(7, certificat);
                psmnt.setString(8, null);
                psmnt.setString(9, null);
                psmnt.executeUpdate();
            }
            catch (Exception e) {
                quotaCreated = false;
            }        
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("INSERT INTO cens VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psmnt.setString(1, "53295906F");
                psmnt.setString(2, "Cristian");
                psmnt.setString(3, "Ramon-Cortes");
                psmnt.setString(4, "Vilarrodona");
                psmnt.setString(5, "1990-10-19");
                psmnt.setString(6, "Barcelona");
                psmnt.setString(7, certificat);
                psmnt.setString(8, null);
                psmnt.setString(9, null);
                psmnt.executeUpdate();
            }
            catch (Exception e) {
                quotaCreated = false;
            }

            /* Tanquem el fitxer de copia creacio */
            try {
                out.close();
            }
            catch (Exception e) {
                System.out.println(errMsg7);
            }
        }
    }
}
