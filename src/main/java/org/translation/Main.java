package org.translation;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    public static void main(String[] args) {
        // Use the JSONTranslator for the full program
        Translator translator = new JSONTranslator();
        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        // Create converters to translate between codes and names
        CountryCodeConverter countryConverter = new CountryCodeConverter();
        LanguageCodeConverter languageConverter = new LanguageCodeConverter();

        while (true) {
            String countryName = promptForCountry(translator, countryConverter);
            if ("quit".equalsIgnoreCase(countryName)) {
                break;
            }

            // Convert the country name back to the 3-letter code using the converter
            String countryCode = countryConverter.fromCountry(countryName);

            if (countryCode.equals("Unknown Country Code")) {
                System.out.println("Error: Invalid country name selected.");
                continue;
            }

            String languageName = promptForLanguage(translator, countryCode, languageConverter);
            if ("quit".equalsIgnoreCase(languageName)) {
                break;
            }

            // Convert the language name back to the 2-letter code
            String languageCode = languageConverter.fromLanguage(languageName);

            if (languageCode.equals("Unknown Language Code")) {
                System.out.println("Error: Invalid language name selected.");
                continue;
            }

            String translation = translator.translate(countryCode, languageCode);

            System.out.println(countryName + " in " + languageName + " is " + translation);
            System.out.println("Press enter to continue or type 'quit' to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if ("quit".equalsIgnoreCase(textTyped)) {
                break;
            }
        }
    }

    private static String promptForCountry(Translator translator, CountryCodeConverter countryConverter) {
        List<String> countryCodes = translator.getCountries();

        // Convert country codes to actual country names
        List<String> countryNames = countryCodes.stream()
                .map(countryConverter::fromCountryCode)
                .filter(name -> !name.equals("Unknown Country Code")) // Filter out any invalid mappings
                .collect(Collectors.toList());

        if (countryNames.isEmpty()) {
            System.out.println("Error: No countries found. Please check your data files.");
            return "quit";
        }

        // Sort the country names alphabetically
        Collections.sort(countryNames);

        // Print the sorted country names
        System.out.println("Available countries:");
        countryNames.forEach(System.out::println);

        System.out.println("Select a country from the list (or type 'quit' to exit):");
        Scanner s = new Scanner(System.in);
        String selectedCountry = s.nextLine().trim();

        return countryNames.contains(selectedCountry) ? selectedCountry : "quit";
    }

    private static String promptForLanguage(Translator translator, String countryCode, LanguageCodeConverter languageConverter) {
        List<String> languageCodes = translator.getCountryLanguages(countryCode);

        // Convert language codes to actual language names
        List<String> languageNames = languageCodes.stream()
                .map(languageConverter::fromLanguageCode)
                .filter(name -> !name.equals("Unknown Language Code")) // Filter out any invalid mappings
                .collect(Collectors.toList());

        if (languageNames.isEmpty()) {
            System.out.println("Error: No languages found for this country. Please check your data files.");
            return "quit";
        }

        // Sort the language names alphabetically
        Collections.sort(languageNames);

        // Print the sorted language names
        System.out.println("Available languages:");
        languageNames.forEach(System.out::println);

        System.out.println("Select a language from the list (or type 'quit' to exit):");
        Scanner s = new Scanner(System.in);
        String selectedLanguage = s.nextLine().trim();

        return languageNames.contains(selectedLanguage) ? selectedLanguage : "quit";
    }
}
