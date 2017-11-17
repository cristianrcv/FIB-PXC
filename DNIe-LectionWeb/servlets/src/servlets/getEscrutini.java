/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Servlet per consultar l'escrutini
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

public class getEscrutini extends HttpServlet {  
  /* Constants */
  private static final String errMsg1 = "Error -1: Unable to connect with database";
  private static final String errMsg2 = "Error: Parametres incorrectes (estat)";
  private static final String errMsg3 = "Error: Parametres incorrectes (numvots)";
  private static final String errMsg4 = "Error: Parametres incorrectes (vots)";
  private static final String errMsg5 = "Error: Parametres incorrectes (escrutatvots)";
  private static final String errMsg6 = "Error: Parametres incorrectes (escrutini)";
  private static final String errMsg7 = "Error -2: Unable to disconnect from database";
  
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
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
 
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
           Statement inst1 = conn.createStatement();
           ResultSet r1 = inst1.executeQuery("SELECT valor FROM propietats WHERE propietat='estat'");
           /* Treat obtained result */
           r1.next();
           voteActive = (r1.getInt(1) == 0);
        }
        catch (Exception e) {
            out.println(errMsg2);
        }
    }
    
    /* Display result depending on the votation state */
    if (conn != null) {
        if (voteActive) {
            /* Only display current participation */
            int numVots = 0;
            try {
               Statement inst2 = conn.createStatement();
               ResultSet r2 = inst2.executeQuery("SELECT valor FROM propietats WHERE propietat='numvots'");
               /* Treat obtained result */
               out.println("Nombre de vots emesos: ");
               r2.next();
               numVots = r2.getInt(1);
               out.println(String.valueOf(numVots));
               out.println("<br>");
            }
            catch (Exception e) {
                out.println(errMsg3);
            }
            
            if (numVots != 0) {
                out.println("<table style='border-collapse:collapse;>'");
                out.println("<tr>");
                out.println("<th style='border-bottom:1px dotted black; border-right:1px dotted black;' align=left>Nom</th>");
                out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Primer Cognom</th>");
                out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Segon Cognom</th>");
                out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>DNI</th>");
                out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Vot</th>");
                out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black;' align=left>Signatura</th>");
                out.println("</tr>");
                
                try {
                    Statement instruction = conn.createStatement();
                    ResultSet result = instruction.executeQuery("SELECT nom, cognom1, cognom2, NIF, vot, signatura FROM cens WHERE vot is NOT null");

                    /* Treat obtained result */
                    while (result.next()) {
                        String nom = result.getString(1);
                        String cognom1 = result.getString(2);
                        String cognom2 = result.getString(3);
                        String dni = result.getString(4);
                        String vot = result.getString(5);
                        String signatura = result.getString(6);
                        
                        out.println("<tr>");
                        out.println("<td style='border-right:1px dotted black;' align=left>" + nom + "</td>");
                        out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + cognom1 + "</td>");
                        out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + cognom2 + "</td>");
                        out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + dni + "</td>");
                        out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + vot + "</td>");
                        out.println("<td style=<'border-left:1px dotted black;' align=left>" + signatura + "</td>");
                        out.println("</tr>");
                    }
                }
                catch (Exception e) {
                    out.println(errMsg4);
                }
                out.println("</table>");
            }
        }
        else {
            /* Display %processed votes */
            try {
               Statement inst2 = conn.createStatement();
               ResultSet r2 = inst2.executeQuery("SELECT valor FROM propietats WHERE propietat='numvots'");
               Statement inst3 = conn.createStatement();
               ResultSet r3 = inst3.executeQuery("SELECT valor FROM propietats WHERE propietat='escrutatvots'");
               /* Treat obtained result */
               out.println("Percentatge escrutat: ");
               r2.next();
               int numVots = r2.getInt(1);
               r3.next();
               int escrutatVots = r3.getInt(1);
               double escr = 0.0;
               if (numVots != 0) escr = (((double)(escrutatVots))/((double)(numVots)))*100.0;
               out.println(String.valueOf(escr) + "%<br>");
            } catch (Exception e) {
                out.println(errMsg5);
            }
            /* Display table results */
            try {
               Statement inst4 = conn.createStatement();
               ResultSet r4 = inst4.executeQuery("SELECT candidatura, vot_Barcelona, vot_Girona, vot_Lleida, vot_Tarragona FROM escrutini");
               /* Treat obtained result */
               out.println("<table style='border-collapse:collapse;>'");
               out.println("<tr>");
               out.println("<th style='border-bottom:1px dotted black; border-right:1px dotted black;' align=left>Candidatura</th>");
               out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Vot Barcelona</th>");
               out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Vot Girona</th>");
               out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Vot Lleida</th>");
               out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black;' align=left>Vot Tarragona</th>");
               out.println("</tr>");
               
               while (r4.next()) {
                   out.println("<tr>");
                   out.println("<td style='border-right:1px dotted black;' align=left>" + r4.getString(1) + "</td>");
                   out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + r4.getString(2) + "</td>");
                   out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + r4.getString(3) + "</td>");
                   out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + r4.getString(4) + "</td>");
                   out.println("<td style=<'border-left:1px dotted black;' align=left>" + r4.getString(5) + "</td>");
                   out.println("</tr>");
               }
               out.println("</table>");
            }
            catch (Exception e){
                out.println(errMsg6);
            }
        }
    }
    
    /* Close connection */
    if (conn != null) {
       try {
           conn.close();
       } catch (Exception e) {
           out.println(errMsg7);
       } 
    }

    /* Close answer */
    out.close();
  }
}