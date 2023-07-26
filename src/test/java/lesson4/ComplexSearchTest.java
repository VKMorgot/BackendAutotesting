package lesson4;

import io.restassured.path.json.JsonPath;
import lesson4.dto.response.ComplexSearchDietResponse;
import lesson4.dto.response.ComplexSearchProteinResponse;
import lesson4.dto.response.ComplexSearchResponse;
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
                .expect()
                .body("totalResults", equalTo(5222))
                .when()
                .get(getComplexSearchUrl())
                .then()
                .header("Content-Length", Integer::parseInt, lessThan(3000));
    }

    /**
     * Проверка параметра number
     */
    @Test
    void number() {
        int number = getNumberValue(100);
        given()
                .queryParam("number", number)
                .expect()
                .body("number", equalTo(number))
                .when()
                .get(getComplexSearchUrl());
    }

    /**
     * Проверка параметра maxProtein. У полученных запросов значение протеина не должно превышать заданное значение.
     */
    @Test
    void maxProtein() {
        int mProtein = getNumberValue(50);

        ComplexSearchProteinResponse proteinResponse = given()
                .queryParam("maxProtein", mProtein)
                .when()
                .get(getComplexSearchUrl())
                .then()
                .extract()
                .body()
                .as(ComplexSearchProteinResponse.class);

        for (int i = 0; i < proteinResponse.getResults().size(); i++) {
            float protein = proteinResponse.getResults().get(i).getNutrition().getNutrients().get(0).getAmount();
            assertThat("Текущее значение протеина должно быть меньше максимального значения", protein, lessThan((float) mProtein));
        }
    }

    /**
     * Проверка вегетарианской диеты
     */
    @Test
    void vegetarianDiet() {
        ComplexSearchDietResponse dietResponse = given()
                .queryParam("addRecipeInformation", true)
                .queryParam("diet", "vegetarian")
                .when()
                .get(getComplexSearchUrl())
                .then()
                .extract()
                .body()
                .as(ComplexSearchDietResponse.class);

        for (int i = 0; i < dietResponse.getResults().size(); i++) {
            boolean isVegetarian = dietResponse.getResults().get(i).getVegetarian();
            assertThat("Блюдо должно быть вегетарианским", isVegetarian, is(true));
        }
    }

    /**
     * Проверка заголовка
     */
    @Test
    void title() {
        String mTitle = "Crock";

        ComplexSearchResponse complexSearchResponse = given()
                .queryParam("titleMatch", mTitle)
                .when()
                .get(getComplexSearchUrl())
                .then()
                .extract()
                .body()
                .as(ComplexSearchResponse.class);

        for (int i = 0; i < complexSearchResponse.getResults().size(); i++) {
            String title = complexSearchResponse.getResults().get(i).getTitle();
            assertThat(mTitle + " должен быть в заголовке", title, containsString(mTitle));
        }
    }


}
