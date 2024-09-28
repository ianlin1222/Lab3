package org.translation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private final List<String> countryCodes;
    private final Map<String, JSONObject> countryDataMap;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     *
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        countryCodes = new ArrayList<>();
        countryDataMap = new HashMap<>();

        try {
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryObject = jsonArray.getJSONObject(i);
                String countryCode = countryObject.getString("alpha3");

                countryCodes.add(countryCode);

                countryDataMap.put(countryCode, countryObject);
            }
        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        JSONObject countryObject = countryDataMap.get(country);

        if (countryObject == null) {
            return new ArrayList<>();
        }

        List<String> languages = new ArrayList<>();
        for (String key : countryObject.keySet()) {
            if (!key.equals("id") && !key.equals("alpha2") && !key.equals("alpha3")) {
                languages.add(key);
            }
        }

        return new ArrayList<>(languages);
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryCodes);
    }

    @Override
    public String translate(String country, String language) {
        JSONObject countryObject = countryDataMap.get(country);

        if (countryObject == null) {
            return "Country not found";
        }

        return countryObject.optString(language, "Translation not available");
    }
}
