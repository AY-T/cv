package api;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.zip.GZIPInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;


public class GetInformartionFromApi {

    public JSONArray getTrainInfoFromApi(String station1, String station2) {
        String localDateNow = LocalDate.now().toString();
        // localDateNow = "2024-04-12";

        String uriForApi = "https://rata.digitraffic.fi/api/v1/live-trains/station/" + station1 + "/" + station2 + "?departure_date=" + localDateNow + "&include_nonstopping=false";

        HttpRequest request = HttpRequest.newBuilder()
	    .GET()
        .uri(URI.create(uriForApi))
        .header("Accept-Encoding", "gzip")
	    .build();

        JSONArray json = null;

        try {        
            HttpResponse<InputStream> response = HttpClient.newHttpClient()
	        .send(request, HttpResponse.BodyHandlers.ofInputStream());

            GZIPInputStream gzipstream = new GZIPInputStream(response.body());
            InputStreamReader isReader = new InputStreamReader(gzipstream);
            BufferedReader in = new BufferedReader(isReader);

            JSONParser parser = new JSONParser();
            json = (JSONArray) parser.parse(in);

        } catch (Exception e) {
            System.out.print("Error: ");
            System.out.println(e);
            System.exit(10);
        }

        return json;
    }
   
}


