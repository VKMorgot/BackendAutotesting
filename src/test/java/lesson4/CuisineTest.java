package lesson4;

import lesson4.dto.response.CuisineResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CuisineTest extends SpoonacularCommonTest {

    @BeforeAll
    static void addRequest() {
        requestSpecification = requestSpecification
                .contentType("application/x-www-form-urlencoded");
    }

    /**
     * Проверка ответа, когда тело запроса пустое
     */
    @Test
    void emptyBody() {
        CuisineResponse cuisineResponse = given()
                .when()
                .post(getCuisineUrl())
                .then()
                .extract()
                .body()
                .as(CuisineResponse.class);

        List<String> cuisinesExpectedList = Arrays.asList("Mediterranean", "European", "Italian");

        assertThat(cuisineResponse.getCuisine(), equalTo("Mediterranean"));
        assertThat(cuisineResponse.getConfidence(), equalTo(0.0));
        assertThat(cuisineResponse.getCuisines(), equalTo(cuisinesExpectedList));
    }

    /**
     * Проверка кухни по названию блюда
     */
    @Test
    void cuisine() {
        CuisineResponse cuisineResponse = given()
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(cuisineResponse.getCuisine(), equalTo("Indian"));
    }

    /**
     * Проверка реакции на ввод неизвестного параметра query
     */
    @Test
    void unknownParam() {
        CuisineResponse cuisineResponse = given()
                .queryParam("nonlang", "foreign")
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(cuisineResponse.getCuisine(), equalTo("Indian"));
    }

    /**
     * Проверка реакции на ввод неизвестного параметра body
     */
    @Test
    void unknownBody() {
        CuisineResponse cuisineResponse = given()
                .formParam("unknown", "something")
                .when()
                .post(getCuisineUrl())
                .then()
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(cuisineResponse.getCuisine(), equalTo("Mediterranean"));
        assertThat(cuisineResponse.getConfidence(), equalTo(0.0));
    }

    /**
     * Проверка принадлежности к кухне по названию блюда
     */
    @Test
    void cuisines() {
        CuisineResponse cuisineResponse = given()
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .extract()
                .body()
                .as(CuisineResponse.class);

        assertThat(cuisineResponse.getCuisines().size(), equalTo(2));
        assertThat(cuisineResponse.getCuisines().get(0), equalTo("Indian"));
        assertThat(cuisineResponse.getCuisines().get(1), equalTo("Asian"));

    }

}
