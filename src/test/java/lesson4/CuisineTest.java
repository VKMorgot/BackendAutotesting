package lesson4;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        given()
                .when()
                .post(getCuisineUrl())
                .then()
                .body("cuisine", equalTo("Mediterranean"))
                .body("confidence", equalTo(0.0F));
    }

    /**
     * Проверка кухни по названию блюда
     */
    @Test
    void cuisine() {
        given()
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .body("cuisine", equalTo("Indian"));
    }

    /**
     * Проверка реакции на ввод неизвестного параметра query
     */
    @Test
    void unknownParam() {
        given()
                .queryParam("nonlang", "foreign")
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .body("cuisine", equalTo("Indian"));
    }

    /**
     * Проверка реакции на ввод неизвестного параметра body
     */
    @Test
    void unknownBody() {
        given()
                .formParam("unknown", "something")
                .when()
                .post(getCuisineUrl())
                .then()
                .body("cuisine", equalTo("Italian"))
                .body("confidence", equalTo(0.0F));
    }

    /**
     * Проверка принадлежности к кухне по названию блюда
     */
    @Test
    void cuisines() {
        JsonPath jsonData = given()
                .formParam("title", "Slow Cooker Lamb Curry")
                .when()
                .post(getCuisineUrl())
                .then()
                .extract().jsonPath();

        assertThat(jsonData.getList("cuisines").size(), equalTo(2));
        assertThat(jsonData.getList("cuisines").get(0), equalTo("Indian"));
        assertThat(jsonData.getList("cuisines").get(1), equalTo("Asian"));

    }

}
