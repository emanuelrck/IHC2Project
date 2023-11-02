package pt.app.ihc2;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DirectionsJSONParser {
    public List<List<LatLng>> parse(JSONObject jObject) {
        List<List<LatLng>> routes = new ArrayList<>();
        JSONArray routesArray, legsArray, stepsArray;

        try {
            routesArray = jObject.getJSONArray("routes");

            for (int i = 0; i < routesArray.length(); i++) {
                legsArray = ((JSONObject) routesArray.get(i)).getJSONArray("legs");
                List<LatLng> path = new ArrayList<>();

                for (int j = 0; j < legsArray.length(); j++) {
                    stepsArray = ((JSONObject) legsArray.get(j)).getJSONArray("steps");

                    for (int k = 0; k < stepsArray.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) stepsArray.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        path.addAll(list);
                    }
                }
                routes.add(path);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return routes;
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng(lat / 1E5, lng / 1E5);
            poly.add(p);
        }

        return poly;
    }
}
