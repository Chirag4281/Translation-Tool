/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Chirag Badani
 */
public class Translate extends HttpServlet {
    private static final long serialVersionUID = 1L;


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
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
        processRequest(request, response);
        
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
 String text = request.getParameter("text");
        String destLanguage = request.getParameter("dest_language");

        if (text == null || destLanguage == null) {
            request.setAttribute("error", "Text and destination language are required");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        try {
            String encodedText = URLEncoder.encode(text, "UTF-8");
            String encodedDestLanguage = URLEncoder.encode(destLanguage, "UTF-8");

            String apiUrl = "http://127.0.0.1:5000/translate?text=" + encodedText + "&dest_language=" + encodedDestLanguage;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder responseBody = new StringBuilder();
                while (scanner.hasNext()) {
                    responseBody.append(scanner.nextLine());
                }
                scanner.close();
                
                // Decode Unicode escape sequences
                String decodedText = decodeUnicode(responseBody.toString());
                request.setAttribute("translatedText", decodedText);
            } else {
                request.setAttribute("error", "Failed to get translation from Flask API");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
        }

        request.getRequestDispatcher("/Home.jsp").forward(request, response);
    }

        private String decodeUnicode(String unicode) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < unicode.length()) {
            char c = unicode.charAt(i++);
            if (c == '\\' && i < unicode.length() && unicode.charAt(i) == 'u') {
                i++;
                int value = 0;
                for (int j = 0; j < 4; j++) {
                    value = (value << 4) + Character.digit(unicode.charAt(i++), 16);
                }
                sb.append((char) value);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
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

}
