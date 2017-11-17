/*
 * Autors: Oscar Dominguez
 *         Victor Lopez
 *         Cristian Ramon-Cortes
 * 
 * Assignatura: PXC 2012-2013 Q1
 * Projecte: DNI-eLection
 * Modul: Applet per votar
 * 
 * Versio: v5
 * Comentaris: ---
 */

package appletvotacio;

import java.applet.Applet;

import java.io.*;
import java.io.InputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.security.*;
import java.security.spec.*;

import java.awt.*;
import java.awt.event.*;

import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

import dnieframework.DNIeFramework;

import javax.crypto.Cipher;
import java.math.BigInteger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

public class AppletVotar extends Applet implements ActionListener {
    /* Constants */
    private static final String errMsg1 = "El seu DNIe no s'ha pogut carregar o la contrasenya es incorrecte.";
    private static final String errMsg2 = "Ha de seleccionar un partit a votar.";
    private static final String errMsg3 = "Error de connexio.";
    
    private static final String urlEmitVote = "https://localhost:8443/DNIe-LectionWeb/servlet/emitVote";
    private static final String urlGetCandidatures = "https://localhost:8443/DNIe-LectionWeb/servlet/getCandidatures";
   
    private static final String loggerMsg1 = "DNIe no carregat";
    private static final String loggerMsg2 = "Vot no seleccionat";
    private static final String loggerMsg3 = "Vot xifrat";
    private static final String loggerMsg4 = "Firma invalida";
    private static final String loggerMsg5 = "Error de connexio a l'enviar el vot";
    private static final String loggerMsg6 = "Error de connexio al rebre les candidatures";
    
    /* Logger */
    private static final Logger logger = Logger.getLogger(DNIeFramework.class.getName());
    
    /* GUI widgets */
    private Button votaButton;
    private Button backButton;
    private Panel candidaturesPanel;
    private CheckboxGroup candidaturesGroup;
    
    /* Variables */
    private DNIeFramework dnie;
    
    @Override
    public void init() {
        makeGuiVotar();
    }

    @Override
    public void update(Graphics g) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == votaButton) {
            enviarVot();
            repaint();
        }
        else if (e.getSource() == backButton) {
            makeGuiVotar();
            repaint();
        }
    }

    private void makeGuiError(String missatge) {
        this.removeAll();
        this.setLayout(new GridLayout(2, 1));
        Label lmissatge = new Label(missatge);
        Label lactualitza = new Label("Actualitza la pàgina");
        this.add("Center", lmissatge);
        this.add("Center", lactualitza);
        this.validate();
    }
    
    private void makeGuiVotar() {
        /* Remove previous displayed pannels */
        this.removeAll();

        /* Load DNIe information */
        boolean loadDNIe = true;

        try {
            // Carregar dades del DNIe
            dnie = new DNIeFramework();
            dnie.cargarDatosDNIe();
        } catch (Exception ex) {
            loadDNIe = false;
            logger.info(loggerMsg1);
        }

        /* Show results to user */
        if (!loadDNIe) {
            /* Set error message and return button */
            this.setLayout(new GridLayout(2,1));
            Label message = new Label(errMsg1, Label.CENTER);
            this.add("Center", message);
            
            backButton = new Button("Tornar");
            backButton.addActionListener(this);
            this.add("Center", backButton);
        }
        else {
            /* Set votation parameters */
            this.setLayout(new GridLayout(3, 1));

            /* Header with user name */
            Label usuari = new Label("Nom: " + dnie.obtenerNombre() +
                                     " " + dnie.obtenerApellidos() +
                                     ",\t NIF: " + dnie.obtenerNIF(), Label.CENTER);
            this.add("Center", usuari);

            /* Table with Political Parties */
            candidaturesPanel = new Panel();
            candidaturesPanel.setLayout(new GridLayout(3, 0));
            candidaturesGroup = new CheckboxGroup();
            String[] candidatures = getCandidatures();
            if (candidatures.length == 1) {
                Label errorLabel = new Label(candidatures[0], Label.CENTER);
                this.add(errorLabel);
                
                /* Button */
                votaButton = new Button("Vota");
                votaButton.addActionListener(this);
                votaButton.setEnabled(false);
                this.add("Center", votaButton);
            }
            else {
                candidaturesPanel = new Panel();
                candidaturesPanel.setLayout(new GridLayout(3, 0));
                candidaturesGroup = new CheckboxGroup();
                for (int i = 1; i < candidatures.length; ++i) {
                    candidaturesPanel.add(new Checkbox(candidatures[i], candidaturesGroup, false));
                }
                this.add(candidaturesPanel);
                
                /* Button */
                votaButton = new Button("Vota");
                votaButton.addActionListener(this);
                votaButton.setEnabled(true);
                this.add("Center", votaButton);
            }
        }

        /* Validate all the new pannels added on the applet */
        this.validate();
    }
    
    private void makeGuiRebut (String resposta) {
        /* Remove previous displayed pannels */
        this.removeAll();

        /* Show results to user (message + button)*/
        this.setLayout(new GridLayout(2, 1));

        Label message = new Label(resposta, Label.CENTER);
        this.add("Center", message);
        
        backButton = new Button("Torna a l'inici");
        backButton.addActionListener(this);
        this.add("Center", backButton);

        /* Validate all the new pannels added on the applet */
        this.validate();
    }
    
    private String[] getCandidatures() {
        /* Obtain candidatures from server */
        boolean error = false;
        String resMessage = "";
        try {
            String params = "nif=" + URLEncoder.encode(dnie.obtenerNIF(), "utf-8");
            resMessage = enviaPOST(urlGetCandidatures, params);
        } catch (Exception ex) {
            error = true;
            resMessage = errMsg3;
            logger.info(loggerMsg6);
        }
        
        /* Convert result to Array */
        String[] candidatures = new String[0];
        if (!error) {
            candidatures = resMessage.split("<c>");
        }
        return candidatures;
    }
    
    private void enviarVot() {
        String resMessage = "";
        
        /* Catch the vote emited by user */
        boolean selectedVote = true;
        String vot = "";
        try {
           vot = candidaturesGroup.getSelectedCheckbox().getLabel();
        }
        catch (Exception e) {
            selectedVote = false;
            resMessage = errMsg2;
            logger.info(loggerMsg2);
        }
        if (selectedVote){
            /* Cipher vote */
            SecureRandom random = new SecureRandom();
            vot = vot + ";" + new BigInteger(100, random).toString(32);
            byte[] input = vot.getBytes();
            logger.info("Xifro el vot: " + vot);
            byte[] xvot = xifra(input);
            logger.info(loggerMsg3);

            byte[] signatura = null;
            boolean err = false;
            try {
                signatura = dnie.firmar(xvot);
            } catch (Exception ex) {
                err = true;
                logger.info(loggerMsg4);
            }

            if (err) {
                resMessage = errMsg1;
            }
            else {
                try {
                    String params = "nif=" + URLEncoder.encode(dnie.obtenerNIF(), "utf-8")
                                    + "&xvot=" + Base64.encodeBase64URLSafeString(xvot)
                                    + "&signatura=" + Base64.encodeBase64URLSafeString(signatura);
                    resMessage = enviaPOST(urlEmitVote, params);
                } catch (Exception ex) {
                    resMessage = errMsg3;
                    logger.info(loggerMsg5);
                }
            }
        }
        
        /* Show result to user */
        makeGuiRebut(resMessage);
    }

    private String enviaPOST(String url, String params) throws IOException {
        /* Prepare servlet connection */
        URL servletURL = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) servletURL.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));

        /* Write POST parameters */
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();

        /* Read servlet answer */
        DataInputStream urlin = new DataInputStream(conn.getInputStream());
        String resposta = "";
        String str;
        while ((str = urlin.readLine()) != null) {
            resposta = resposta + str;
        }
        urlin.close();
        
        return resposta;
    }

    private byte[] xifra(byte[] input) {
        try {
            PublicKey pubKey = getPublicKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(input);
        } catch (Exception ex) {
            Logger.getLogger(AppletVotar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private PublicKey getPublicKey() throws Exception {
        InputStream in = AppletVotar.class.getResourceAsStream("server_public.der");
        byte[] keyBytes = IOUtils.toByteArray(in);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

}
