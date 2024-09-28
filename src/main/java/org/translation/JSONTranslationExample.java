package org.translation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A minimal example of reading and using the JSON data from resources/sample.json.
 */
public class JSONTranslationExample {

    public static final int CANADA_INDEX = 30;
    private final JSONArray jsonArray;

    // Note: CheckStyle is configured so that we are allowed to omit javadoc for constructors
    public JSONTranslationExample() {
        try {
            // Split the line to ensure it's under 120 characters as per CheckStyle requirements
            String jsonString = Files.readString(
                    Paths.get(getClass().getClassLoader().getResource("sample.json").toURI()));
            this.jsonArray = new JSONArray(jsonString);
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the Spanish translation of Canada.
     *
     * @return the Spanish translation of Canada
     */
    public String getCanadaCountryNameSpanishTranslation() {
        // Use the defined constant instead of the magic number
        JSONObject canada = jsonArray.getJSONObject(CANADA_INDEX);
        return canada.getString("es");
    }

    /**
     * Returns the name of the country based on the provided country and language codes.
     *
     * @param countryCode  the country, as its three-letter code.
     * @param languageCode the language to translate to, as its two-letter code.
     * @return the translation of country to the given language or "Country not found" if there is no translation.
     */
    public String getCountryNameTranslation(String countryCode, String languageCode) {
        // Iterate over the JSON array to find the object with the matching country code
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject country = jsonArray.getJSONObject(i);
            // Check if the "alpha3" key matches the provided country code
            if (country.getString("alpha3").equalsIgnoreCase(countryCode)) {
                // Return the translation in the specified language
                return country.optString(languageCode, "Translation not available");
            }
        }
        return "Country not found";
    }

    /**
     * Prints the Spanish translation of Canada.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        JSONTranslationExample jsonTranslationExample = new JSONTranslationExample();

        System.out.println(jsonTranslationExample.getCanadaCountryNameSpanishTranslation());
        String translation = jsonTranslationExample.getCountryNameTranslation("can", "es");
        System.out.println(translation);
    }
}
