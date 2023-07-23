package lesson3;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

public abstract class SpoonacularCommonTest {

    private static final Properties scProp = new Properties();
    private static InputStream configFile;
    private static final String PROP_FILE = "src/test/resources/sp.properties";
    private static String apiKey;
    private static String hash;
    private static String username;
    private static String baseUrl;
    private static String complexSearchUrl;
    private static String cuisineUrl;
    private static String mealPlannerUrl;


    @BeforeAll
    static void initTest() throws IOException {
        configFile = new FileInputStream(PROP_FILE);
        scProp.load(configFile);

        apiKey = scProp.getProperty("apiKey");
        hash = scProp.getProperty("hash");
        baseUrl = scProp.getProperty("base_url");
        username = scProp.getProperty("username");
        complexSearchUrl = scProp.getProperty("complexSearch_url");
        cuisineUrl = scProp.getProperty("cuisine_url");
        mealPlannerUrl = scProp.getProperty("mealplanner_url");

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public static String getApiKey() {return apiKey;}

    private static String getBaseUrl() {
        return baseUrl;
    }

    public static String getComplexSearchUrl() {
        return getBaseUrl() + complexSearchUrl;
    }

    public static String getCuisineUrl() {
        return getBaseUrl() + cuisineUrl;
    }

    public static String getMealPlannerUrl() {
        return mealPlannerUrl;
    }

    public static String getHash() {
        return hash;
    }

    public static String getUsername() {
        return username;
    }

    /**
     * Генерируем случайное целое число от 1 до bound
     * @param bound до 100
     * @return число
     */
    public int getNumberValue(int bound) {
        return 1 + new Random().nextInt(bound);
    };

}
