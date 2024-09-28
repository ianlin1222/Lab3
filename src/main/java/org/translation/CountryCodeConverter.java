package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting country codes to their names.
 */
public class CountryCodeConverter {

    private Map<String, String> codeToCountry; // Map for storing country code to name
    private Map<String, String> countryToCode; // Map for storing country name to code

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */
    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            codeToCountry = new HashMap<>();
            countryToCode = new HashMap<>();

            // Iterate over lines starting from the second line to skip the header row
            for (int i = 1; i < lines.size(); i++) {
                String[] parts = lines.get(i).split("\t"); // Tab-separated values

                if (parts.length >= 2) {
                    // Extracting name and code based on expected column order
                    String name = parts[0].trim();   // Country name in the first column
                    String code = parts[1].trim();   // Country code in the second column

                    // Preserve exact name including parentheses
                    codeToCountry.put(code, name);
                    countryToCode.put(name, code);
                }
            }

        } catch (IOException | URISyntaxException ex) {
            throw new RuntimeException("Error reading file: " + filename, ex);
        }
    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        return codeToCountry.getOrDefault(code, "Unknown Country Code");
    }

    /**
     * Returns the code of the country for the given country name.
     * @param country the name of the country
     * @return the 3-letter code of the country
     */
    public String fromCountry(String country) {
        return countryToCode.getOrDefault(country, "Unknown Country Name");
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return codeToCountry.size();
    }
}
