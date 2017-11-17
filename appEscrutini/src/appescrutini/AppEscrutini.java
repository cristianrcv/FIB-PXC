/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Realitzacio de l'Escrutini
 * 
 * Versio: v2
 * Comentaris: ---
 */

package appescrutini;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;

public class AppEscrutini {
    /* Constants */  
    private static final String errMsg1 = "Error -1: Unable to connect with database";
    private static final String errMsg2 = "Error -2: Unable to close votation";
    private static final String errMsg3 = "Error -3: Unable to set to zero all votes";
    private static final String errMsg4 = "Error -4: Unable to set to zero the number of treated votes";
    private static final String errMsg5 = "Error -5: Can't count votes";
    private static final String errMsg6 = "Error -6: Unable to disconnect from database";
    
    private static final String succMsg1 = "INFO: Successfully connected with Data Base";
    private static final String succMsg2 = "INFO: Successfully closed votation time";
    private static final String succMsg3 = "INFO: Successfully reset escrutini table";
    private static final String succMsg4 = "INFO: Successfully reset number of treated votes";
    private static final String succMsg5 = "INFO: Votes processed";
    private static final String succMsg6 = "INFO: Successfully disconnected from Data Base";
    
    private static final String infoMsg = "INFO: Processing votes";
    
    private static final String endMsg = "END: Votes counted";
    
    public static void main(String[] args) {
        /* Set connection values */
        Connection conn = null;
        String url = "jdbc:mysql://mysqlfib.fib.upc.es:3306/";
        String dbName = "BDpxc02";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "pxc02"; 
        String password = "nJoW02Hi";

        /* Connect to Data Base */
        try {
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url+dbName, userName, password);
        } catch (Exception e) {
            System.out.println(errMsg1);
        }
        
        /* Disable votation mark */
        boolean votationClosed = false;
        if (conn != null) {
            System.out.println(succMsg1);
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("UPDATE propietats SET valor=1 WHERE propietat = ?");
                psmnt.setString(1, "estat");
                psmnt.executeUpdate();
                votationClosed = true;
            } catch (Exception e) {
                votationClosed = false;
                System.out.println(errMsg2);
            }
        }
        
        /* Set to 0 all votes */
        boolean setToZero = true;
        if (conn != null && votationClosed) {
            System.out.println(succMsg2);
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("UPDATE escrutini SET vot_Barcelona=0, vot_Girona=0, vot_Lleida=0, vot_Tarragona=0");
                psmnt.executeUpdate();
            } catch (Exception e) {
                setToZero = false;
                System.out.println(errMsg3);
            }
        }
        
        /* Set to 0 number of treated votes */
        if (conn != null && votationClosed) {
            System.out.println(succMsg3);
            try {
                PreparedStatement psmnt = (PreparedStatement) conn.prepareStatement("UPDATE propietats SET valor=0 WHERE propietat='escrutatvots'");
                psmnt.executeUpdate();
            } catch (Exception e) {
                setToZero = false;
                System.out.println(errMsg4);
            }
        }
        
        /* Count votes */
        boolean error = false;
        if (conn != null && votationClosed && setToZero) {
            System.out.println(succMsg4);
            try {
                Statement instruction = conn.createStatement();
                ResultSet result = instruction.executeQuery("SELECT provincia, vot FROM cens WHERE vot is NOT null");
                System.out.println(infoMsg);
                while (result.next()) {
                    String prov = result.getString(1);
                    String xvot = result.getString(2);

                    /* Decrypt vote */
                    byte[] bxvot = Base64.decodeBase64(xvot);
                    byte[] bvot = desxifra(bxvot);
                    String partit = new String(bvot);
                    String vot = partit.split(";")[0];
                    System.out.println("-->" + vot);
                    
                    /* Add information of vote to Data Base */
                    PreparedStatement psmnt1 = (PreparedStatement) conn.prepareStatement("UPDATE escrutini SET vot_" + prov + "=vot_" + prov + "+1 WHERE candidatura = ?");
                    psmnt1.setString(1, vot);
                    psmnt1.executeUpdate();
                    
                    /* Add processed votes to Data Base*/
                    PreparedStatement psmnt2 = (PreparedStatement) conn.prepareStatement("UPDATE propietats SET valor=valor+1 WHERE propietat='escrutatvots'");
                    psmnt2.executeUpdate();
                }
            }
            catch (Exception e) {
                error = true;
                System.out.println(errMsg5);
            }
            if (!error) System.out.println(succMsg5);
        }
        
        /* Disconnect from Data Base */
        if (conn != null) {
           try {
             conn.close();
             System.out.println(succMsg6);
           } catch (Exception e) {
               System.out.println(errMsg6);
           } 
        }
        
        /* END */
        System.out.println(endMsg);
    }
    
    private static byte[] desxifra(byte[] input) throws Exception {
        PrivateKey privKey = getPrivateKey("server_private.der");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return cipher.doFinal(input);
    }
    
    private static PrivateKey getPrivateKey(String filename) throws Exception {
        File f = new File(filename);
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
}
