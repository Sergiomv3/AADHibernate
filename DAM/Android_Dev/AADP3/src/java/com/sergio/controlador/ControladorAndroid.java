/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sergio.controlador;
import com.sergio.hibernate.Inmueble;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import com.sergio.modelo.ModeloInmueble;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author Sergio
 */
@WebServlet(name = "ControladorAndroid", urlPatterns = {"/controladorandroid"})
@MultipartConfig
public class ControladorAndroid extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException {

        String action = request.getParameter("action");
        response.setContentType("application/json;charset=UTF-8");
        JSONObject objetoJson = new JSONObject();
        if (action.equals("fichero")) {
            boolean fotoError = getFoto(request);
            if (fotoError) {
                objetoJson.put("subido", 0);
            } else {
                objetoJson.put("subido", 1);
            }
        } else if (action.equals("inmueble")) {
            String json = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = in.readLine();
            while (line != null) {
                json += line;
                line = in.readLine();
            }
            long id = getInmueble(json);
            objetoJson.put("idinmueble", id);
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(objetoJson.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("PRUEBA");
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ControladorAndroid.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("PRUEBA");
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(ControladorAndroid.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private long getInmueble(String json) {
        long id;
        JSONObject jsonObject = new JSONObject();
        id = ModeloInmueble.insert(new Inmueble(jsonObject));
        return id;
    }

    private boolean getFoto(HttpServletRequest request) {
        boolean error = false;
        InputStream fileContent = null;
        try {
            
            Part stringPart = request.getPart("idinmueble");
            Scanner scanner = new Scanner(stringPart.getInputStream());
            String id = scanner.nextLine();
            
            stringPart = request.getPart("nombre");
            scanner = new Scanner(stringPart.getInputStream());
            String nombreRecibido = scanner.nextLine();

            nombreRecibido = nombreRecibido.substring(nombreRecibido.indexOf("inmueble"));
            int segundo = nombreRecibido.indexOf("_", nombreRecibido.indexOf("_")+1);
            nombreRecibido = nombreRecibido.substring(segundo);
            
            String nombre = "inmueble_" + id + nombreRecibido;

            Part filePart = request.getPart("archivo");
            fileContent = filePart.getInputStream();
            if (filePart.getContentType().contains("png")) {
                nombre = nombre + ".png";
            } else if (filePart.getContentType().contains("jpeg")) {
                nombre = nombre + ".jpg";
            } else if (filePart.getContentType().contains("gif")) {
                nombre = nombre + ".gif";
            }
            String destino = getServletContext().getRealPath("") + File.separator + "subido" + File.separator + id + File.separator;
            File carpeta = new File(destino);
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }
            byte[] array = new byte[1000];
            FileOutputStream fos = new FileOutputStream(destino + nombre);
            try {
                int leido = fileContent.read(array);
                while (leido > 0) {
                    fos.write(array, 0, leido);
                    leido = fileContent.read(array);
                }
            } catch (Exception e) {
                error = true;
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            error = true;
        } finally {
            try {
                fileContent.close();
            } catch (Exception e) {
            }
        }
        return error;
    }
    
}
