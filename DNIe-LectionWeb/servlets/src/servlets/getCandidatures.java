/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Servlet per recollir candidatures
 * 
 * Versio: v3
 * Comentaris: ---
 */

package servlets;

import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.*;
import javax.servlet.http.*;

public class getCandidatures extends HttpServlet {
  /* Constants */
  private static final String errMsg1 = "Error -1: Unable to connect with database";
  private static final String errMsg2 = "Error: El periode de votacio esta tancat.";
  private static final String errMsg3 = "Error: DNI invalid";
  private static final String errMsg4 = "Error -2: Unable to disconnect from database";
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {   
    processRequest(req, res);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    processRequest(req, res);
  }
  
  private void processRequest (HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    /* Initialize answer */
    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();

    /* Get the parameters from POST */
    String dni = req.getParameter("nif");
    
    /* Set connection values */
    Connection conn = null;
    String url = "jdbc:mysql://mysqlfib.fib.upc.es:3306/";
    String dbName = "BDpxc02";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "pxc02"; 
    String password = "nJoW02Hi";

    /* Begin connection with database */
    try {
       /* Begin connection */
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
    
    /* Check out if its a valid DNI and get its province */
    boolean allowed = false;
    String prov = "";
    if (conn != null && voteActive) {
        try {
            Statement instruction = conn.createStatement();
            ResultSet result = instruction.executeQuery("SELECT provincia FROM cens WHERE NIF='" + dni + "'");
            if (!result.next()) {
                out.println(errMsg3);
                allowed = false;
            }
            else {
                prov = result.getString(1);
                allowed = true;
            }
        }
        catch (Exception e) {
            out.println(errMsg3);
        }
    }
    
    /* Obtain candidatures from selected province and store result */
    if (conn != null && voteActive && allowed) {
        try {
            Statement instruction = conn.createStatement();
            ResultSet result = instruction.executeQuery("SELECT sigles FROM candidatures WHERE " + prov + "=1");
            while (result.next()) {
                String val = result.getString(1);
                out.println("<c>");
                out.println(val);
            }
        }
        catch (Exception e) {
            out.println(errMsg3);
        }
    }
    
    /* Close connection */
    if (conn != null) {
       try {
         conn.close();
       } catch (Exception e) {
           out.println(errMsg4);
       } 
    }

    /* Close answer */
    out.close();
  }
}
