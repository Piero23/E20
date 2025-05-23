package org.unical.enterprise.authentication;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpResponse;

@Service
public class AuthenticationService {
/*
    private HttpResponse<?> request(String link, String RequestType){
        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(RequestType);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Costruisci la query string con parametri urlencoded
            if(RequestType.equals("POST")) {
                StringBuilder params = new StringBuilder();
                params.append("grant_type=").append(URLEncoder.encode("password", "UTF-8"));
                params.append("&client_id=").append(URLEncoder.encode("esse20-client", "UTF-8"));
                params.append("&client_secret=").append(URLEncoder.encode("xYFpNBla8svc7yZna4eArJQRhAh4jgMh", "UTF-8"));
                params.append("&username=").append(URLEncoder.encode(loginDTO.mail(), "UTF-8"));
                params.append("&password=").append(URLEncoder.encode(loginDTO.password(), "UTF-8"));
            }
            // Scrivi i dati nel body della richiesta
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(params.toString());
                dos.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()
            ));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

 */
}
