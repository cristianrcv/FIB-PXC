/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Servlet per emetre vots
 * 
 * Versio: v3
 * Comentaris: ---
 */

package servlets;

import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

public class emitVote extends HttpServlet {  
  /* Constants */
  private static final String errMsg1 = "Error -1: Unable to connect with database";
  private static final String errMsg2 = "Error: El periode de votacio esta tancat.";
  private static final String errMsg3 = "Error: No li esta permes votar.";
  private static final String errMsg4 = "Error: Ja ha votat.";
  private static final String errMsg5 = "Error: El vot enviat es invalid";
  private static final String errMsg6 = "Error -2: Unable to disconnect from database";
  private static final String succMsg1 = "Votacio realitzada amb exit";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {   
    processRequest(req, res);
  }
  
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processRequest(req, res);
  }
  
  protected void processRequest (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    /* Initialize answer */
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    /* Get the parameters from POST */
    String vote = req.getParameter("xvot");
    String dni = req.getParameter("nif");
    String signatura = req.getParameter("signatura");
 
    /* Set connection values */
    Connection conn = null;
    String url = "jdbc:mysql://mysqlfib.fib.upc.es:3306/";
    String dbName = "BDpxc02";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "pxc02"; 
    String password = "nJoW02Hi";

    /* Begin connection with database */
    try {
        Class.forName(driver).newInstance();
        conn = DriverManager.getConnection(url+dbName, userName, password);
    } catch (Exception e) {
        out.println(errMsg1);
    }
    
    /* Check if votation is enabled */
    boolean voteActive = false;
    if (conn != null) {
        try {
           Statement instruction = conn.createStatement();
           ResultSet result = instruction.executeQuery("SELECT valor FROM propietats WHERE propietat='estat'");
           /* Treat obtained result */
           result.next();
           voteActive = (result.getInt(1) == 0);
           if (!voteActive) {
               out.println(errMsg2);
           }
        }
        catch (Exception e) {
            out.println(errMsg2);
        }
    }
    
    /* Check out if its a valid DNI and it hasn't voted yet */
    boolean allowed = true;
    String certificat = null;
    if (conn != null && voteActive) {
        try {
            Statement instruction = conn.createStatement();
            ResultSet result = instruction.executeQuery("SELECT vot, certificat FROM cens WHERE NIF='" + dni + "'");
            if (!result.next()) {
                out.println(errMsg3);
                allowed = false;
            }
            else {
                result.getString(1);
                if (!result.wasNull()) {
                    out.println(errMsg4);
                    allowed = false;
                }
                else {
                    certificat = result.getString(2);
                }
            }
        }
        catch (Exception e) {
            out.println(errMsg3);
            allowed = false;
        }
    }
    
    /* Check if the signature is correct  */
    boolean correctSignature = false;
    if (conn != null && voteActive && allowed) {
        try {
            /* Calculate SHA1 of vote */
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] bxvot = Base64.decodeBase64(vote);
            md.update(bxvot);
            byte[] hashVote = md.digest();

            /* Get Public key */
            Security.addProvider(new BouncyCastleProvider());
            StringReader fileReader = new StringReader(certificat);
            PEMReader r = new PEMReader(fileReader);
            PublicKey pubKey = (PublicKey) r.readObject();

            /* Prepare verification */
            Signature signatureVer = Signature.getInstance("SHA1withRSA");
            signatureVer.initVerify(pubKey);
            signatureVer.update(hashVote);

            /* Verify signature */
            byte[] bsignatura = Base64.decodeBase64(signatura);
            correctSignature = signatureVer.verify(bsignatura);

            if (!correctSignature) {
                out.println(errMsg5);
                allowed = false;
            }
        }
        catch (Exception ex) {
            out.println(errMsg5);
            allowed = false;
        }
    }

    /* Emit vote */
    if (conn != null && voteActive && allowed && correctSignature) {
        try {
            /* Vote */
            PreparedStatement psmnt1 = (PreparedStatement) conn.prepareStatement("UPDATE cens SET vot = ? , signatura = ? WHERE NIF = ?");
            psmnt1.setString(1, vote);
            psmnt1.setString(2, signatura);
            psmnt1.setString(3, dni);
            psmnt1.executeUpdate();
            
            /* Inc number of votes -- CANNOT USE TRIGGERS */
            PreparedStatement psmnt2 = (PreparedStatement) conn.prepareStatement("UPDATE propietats SET valor=valor+1 WHERE propietat = ?");
            psmnt2.setString(1, "numvots");
            psmnt2.executeUpdate();

            /* Store result */
            out.println(succMsg1);
        } catch (Exception e) {
            out.println(errMsg5);
        }
    }
    
    /* Close connection */
    if (conn != null) {
       try {
         conn.close();
       } catch (Exception e) {
           out.println(errMsg6);
       } 
    }

    /* Close answer */
    out.close();
  }
}