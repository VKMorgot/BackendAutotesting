package lesson3;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class ComplexSearchTest extends SpoonacularCommonTest {

    /**
     * Проверка запроса без параметров
     */
    @Test
    void noParams() {
        given()
                .queryParam("apiKey", getApiKey())
                .expect()
                .body("totalResults", equalTo(5222))
                .when()
                .get(getComplexSearchUrl())
                .then()
                .statusCode(200)
                .statusLine("HTTP/1.1 200 OK")
                .header("Connection", "keep-alive")
                .header("Content-Length", Integer::parseInt, lessThan(3000))
                .contentType(ContentType.JSON)
                .time(lessThan(5000L));
    }

    /**
     * Проверка параметра number
     */
    @Test
    void number() {
        int number = getNumberValue(100);
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", number)
                .expect()
                .body("number", equalTo(number))
                .when()
                .get(getComplexSearchUrl())
                .then()
                .statusCode(200);
    }

    /**
     * Проверка параметра maxProtein. У полученных запросов значение протеина не должно превышать заданное значение.
     */
    @Test
    void maxProtein() {
        int mProtein = getNumberValue(50);
        JsonPath jsonData = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("maxProtein", mProtein)
                .when()
                .get(getComplexSearchUrl())
                .body()
                .jsonPath();

        for (int i = 0; i < ((ArrayList<?>) jsonData.get("results")).size(); i++) {
            float protein = (float) ((HashMap<?, ?>) ((ArrayList<?>) (( (HashMap<?, ?>) ( ((HashMap<?, ?>) jsonData.getList("results").get(i)).get("nutrition"))).get("nutrients"))).get(0)).get("amount");
            assertThat("Текущее значение протеина должно быть меньше максимального значения", protein, lessThan((float) mProtein));
        }
    }

    /**
     * Проверка вегетарианской диеты
     */
    @Test
    void vegetarianDiet() {
        JsonPath jsonData = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("addRecipeInformation", true)
                .queryParam("diet", "vegetarian")
                .when()
                .get(getComplexSearchUrl())
                .body()
                .jsonPath();

        for (int i = 0; i < ((ArrayList<?>) jsonData.get("results")).size(); i++) {
            boolean isVegetarian = (boolean) ( ((HashMap<?, ?>) jsonData.getList("results").get(i)).get("vegetarian"));
            assertThat("Блюдо должно быть вегетарианским", isVegetarian, is(true));
        }
    }

    /**
     * Проверка заголовка
     */
    @Test
    void title() {
        String mTitle = "Crock";

        JsonPath jsonData = given()
                .queryParam("apiKey", getApiKey())
                .queryParam("titleMatch", mTitle)
                .when()
                .get(getComplexSearchUrl())
                .body()
                .jsonPath();

        for (int i = 0; i < ((ArrayList<?>) jsonData.get("results")).size(); i++) {
            String title =  ( ((HashMap<?, ?>) jsonData.getList("results").get(i)).get("title")).toString();
            assertThat(mTitle + " должен быть в заголовке", title, containsString(mTitle));
        }
    }


}
