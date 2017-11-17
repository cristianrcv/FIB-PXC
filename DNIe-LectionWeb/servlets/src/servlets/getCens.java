/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Servlet per consultar el cens
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

public class getCens extends HttpServlet {
  /* Constants */
  private static final String errMsg1 = "Error -1: Unable to connect with database";
  private static final String errMsg2 = "Error: Parametres incorrectes";
  private static final String errMsg3 = "Error -2: Unable to disconnect from database";
    
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
    
    /* Obtain and treat result*/
    if (conn != null) {
        try {
            Statement instruction = conn.createStatement();
            ResultSet result = instruction.executeQuery("SELECT nom, cognom1, cognom2, NIF FROM cens");   
            
            out.println("<table style='border-collapse:collapse;>'");
            out.println("<tr>");
            out.println("<th style='border-bottom:1px dotted black; border-right:1px dotted black;' align=left>Nom</th>");
            out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Primer Cognom</th>");
            out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black; border-right:1px dotted black;' align=left>Segon Cognom</th>");
            out.println("<th style='border-bottom:1px dotted black; border-left:1px dotted black;' align=left>DNI</th>");
            out.println("</tr>");
            
            while (result.next()) {
                String nom = result.getString(1);
                String cognom1 = result.getString(2);
                String cognom2 = result.getString(3);
                String dni = result.getString(4);
                out.println("<tr>");
                out.println("<td style='border-right:1px dotted black;' align=left>" + nom + "</td>");
                out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + cognom1 + "</td>");
                out.println("<td style='border-left:1px dotted black; border-right:1px dotted black;' align=left>" + cognom2 + "</td>");
                out.println("<td style=<'border-left:1px dotted black;' align=left>" + dni + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
        catch (Exception ex){
            out.println(errMsg2);
        }
    }
    
    /* Close connection */
    if (conn != null) {
       try {
         conn.close();
       } catch (Exception e) {
           out.println(errMsg3);
       } 
    }
  }
}