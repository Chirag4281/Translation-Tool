<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Translation and TTS Service</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .container {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            padding: 30px;
            width: 100%;
            max-width: 700px;
            transform: rotateX(5deg) rotateY(5deg);
            transition: transform 0.3s ease-in-out;
        }

        .container:hover {
            transform: rotateX(0) rotateY(0) scale(1.05);
            box-shadow: 0 12px 24px rgba(0, 0, 0, 0.3);
        }

        h1 {
            text-align: center;
            color: #333;
            margin-bottom: 20px;
            font-size: 2em;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #444;
        }

        textarea {
            width: 100%;
            height: 120px;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            resize: vertical;
            transition: border-color 0.3s;
        }

        textarea:focus {
            border-color: #007bff;
            outline: none;
        }

        select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s;
        }

        select:focus {
            border-color: #007bff;
            outline: none;
        }

        button {
            display: block;
            width: 100%;
            padding: 12px;
            background-color: #007bff;
            border: none;
            border-radius: 8px;
            color: #fff;
            font-size: 18px;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.3s;
        }

        button:hover {
            background-color: #0056b3;
            transform: scale(1.05);
        }

        .result {
            margin-top: 20px;
        }

        .result p {
            font-size: 18px;
            color: #333;
        }

        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body bgcolor="black">
    <div class="container">
        <h1>Text Translator and TTS</h1>
         
         <%
    String DB_URL = "jdbc:mysql://localhost:3306/Translator";
String DB_USER = "root";
String DB_PASSWORD = "root";
HttpSession session1 = request.getSession(false);

if (session1 != null) {
    String username = (String) session1.getAttribute("username");
    Long loginTime = (Long) session1.getAttribute("loginTime");

    
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to insert or update based on username
             String sql = "INSERT INTO user_sessions (username, login_time) VALUES (?, ?)" ;
                         


            PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setTimestamp(2, new java.sql.Timestamp(loginTime));
                stmt.executeUpdate();
            }
         catch (Exception e) {
            System.out.println(e);
        }
             }
      


        
    %>

        <form id="translationForm" action="Translate" method="post">
            <div class="form-group">
                <label for="text">Text to Translate:</label>
                <textarea id="text" name="text" required></textarea>
            </div>
            <div class="form-group">
                <label for="dest_language">Select Language:</label>
                <select id="dest_language" name="dest_language" required>
                    <option value="hi">Hindi</option>
                    <option value="bn">Bengali</option>
                    <option value="te">Telugu</option>
                    <option value="mr">Marathi</option>
                    <option value="ta">Tamil</option>
                    <option value="ur">Urdu</option>
                    <option value="gu">Gujarati</option>
                    <option value="ml">Malayalam</option>
                    <option value="kn">Kannada</option>
                    <option value="pa">Punjabi</option>
                    <option value="as">Assamese</option>
                    <option value="or">Oriya</option>
                </select>
            </div>
            <button type="submit">Translate</button><br><!-- comment -->
            
            
        </form>
        
        <div class="result">
            <% 
                String jsonResponse = (String) request.getAttribute("translatedText");
                String error = (String) request.getAttribute("error");

                if (error != null) {
                    out.println("<p class='error'>" + error + "</p>");
                }

                if (jsonResponse != null) {
                    // Parse the JSON response to extract translated text
                    org.json.JSONObject jsonObject = new org.json.JSONObject(jsonResponse);
                    String translatedText = jsonObject.optString("translated_text", "No translation available");
                    out.println("<p>Translation: " + translatedText + "</p>");
                }
            %>
        </div>
    </div>
</body>
</html>
