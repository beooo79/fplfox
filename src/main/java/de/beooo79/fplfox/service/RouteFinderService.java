package de.beooo79.fplfox.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.springframework.stereotype.Service;

@Service
public class RouteFinderService {

    public String getRoute(String ADEP, String ADES) throws IOException {

        // Build parameter string
        String data = "id1=" + ADEP + "&id2=" + ADES
                + "&minalt=FL200&maxalt=FL400&lvl=B&usesid=Y&usestar=Y&rnav=Y&dbid=1206";

        // Send the request
        URL url = new URL("http://rfinder.asalink.net/free/autoroute_rtx.php");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

        // write parameters
        writer.write(data);
        writer.flush();

        // Get the response
        StringBuffer answer = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            answer.append(line);
        }
        writer.close();
        reader.close();

        String route = answer.toString().substring(answer.toString().indexOf("<hr><tt><b>" + ADEP),
                answer.toString().indexOf(ADES + "</b></tt><hr>")).replaceAll("\\<.*?>", "");
        return route;

    }
}
