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

    public String getRoute(String aerodromeOfDeparture, String aerodromeOfDestination) throws IOException {

        // Build parameter string
        var data = "id1=" + aerodromeOfDeparture + "&id2=" + aerodromeOfDestination
                + "&minalt=FL200&maxalt=FL400&lvl=B&usesid=Y&usestar=Y&rnav=Y&dbid=1206";

        // Send the request
        var url = new URL("http://rfinder.asalink.net/free/autoroute_rtx.php");
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        var writer = new OutputStreamWriter(conn.getOutputStream());

        // write parameters
        writer.write(data);
        writer.flush();

        // Get the response
        var answer = new StringBuilder();
        var reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            answer.append(line);
        }
        writer.close();
        reader.close();

        return answer.toString().substring(answer.toString().indexOf("<hr><tt><b>" + aerodromeOfDeparture),
                answer.toString().indexOf(aerodromeOfDestination + "</b></tt><hr>")).replaceAll("[^>]*+", "");

    }
}
