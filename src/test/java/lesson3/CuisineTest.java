package lesson3;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.MatcherAssert.assertThat;

public class CuisineTest extends SpoonacularCommonTest {

    /**
     * Проверка ответа, когда тело запроса пустое
     */
    @Test
    void emptyBody() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post(getCuisineUrl())
                .then()
                .statusCode(200)
                .time(lessThan(3000L))
                .body("cuisine", equalTo("Italian"))
                .body("confidence", equalTo(0.0F));
    }


    /**
     * Проверка кухни по названию блюда
     */
    @Test
    void cuisine() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .statusCode(200)
                .body("cuisine", equalTo("Indian"));
    }

    /**
     * Проверка реакции на ввод неизвестного параметра query
     */
    @Test
    void unknownParam() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("nonlang", "foreign")
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .statusCode(200)
                .body("cuisine", equalTo("Indian"));
    }

    /**
     * Проверка реакции на ввод неизвестного параметра body
     */
    @Test
    void unknownBody() {
        given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("unknown", "something")
                .when()
                .post(getCuisineUrl())
                .then()
                .statusCode(200)
                .body("cuisine", equalTo("Italian"))
                .body("confidence", equalTo(0.0F));
    }

    /**
     * Проверка принадлежности к кухне по названию блюда
     */
    @Test
    void cuisines() {
        JsonPath jsonData = given()
                .queryParam("apiKey", getApiKey())
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .statusCode(200)
                .extract().jsonPath();

        assertThat(jsonData.getList("cuisines").size(), equalTo(2));
        assertThat(jsonData.getList("cuisines").get(0), equalTo("Indian"));
        assertThat(jsonData.getList("cuisines").get(1), equalTo("Asian"));

    }

}
