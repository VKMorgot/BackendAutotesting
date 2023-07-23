package lesson3;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class MealPlanner extends SpoonacularCommonTest {

    /**
     * Очищаем Meal Planner на выбранную дату
     * @param date дата очистки
     */
    void clearMeal(String date) {
        given()
                .pathParam("date", date)
                .pathParam("username", getUsername())
                .queryParam("hash", getHash())
                .queryParam("apiKey", getApiKey())
                .delete(getMealPlannerUrl() + "{username}/day/{date}")
                .then()
                .statusCode(200);
    }

    /**
     * Добавляем к Meal Planner нужный ингредиент на нужную дату
     * @param date дата для добавления
     * @return id добавленного ингредиента
     */
    String addMeal(String date) {

        return given()
                    .pathParam("username", getUsername())
                    .queryParam("apiKey", getApiKey())
                    .queryParam("hash", getHash())
                    .body("{\n"
                            + " \"date\": "+ date + ",\n"
                            + " \"slot\": 1,\n"
                            + " \"position\": 0,\n"
                            + " \"type\": \"INGREDIENTS\",\n"
                            + " \"value\": {\n"
                            + " \"ingredients\": [\n"
                            + " {\n"
                            + " \"name\": \"1 banana\"\n"
                            + " }\n"
                            + " ]\n"
                            + " }\n"
                            + "}")
                    .when()
                    .post(getMealPlannerUrl() + "{username}/items")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .get("id")
                    .toString();
    }

    /**
     * Удаляем выбранный ингредиент из Meal Plan
     * @param id ингредиент
     */
    void deleteMeal(String id) {
        given()
                .pathParam("username", getUsername())
                .pathParam("id", id)
                .queryParam("hash", getHash())
                .queryParam("apiKey", getApiKey())
                .delete(getMealPlannerUrl() + "{username}/items/{id}")
                .then()
                .statusCode(200);
    }

    /**
     * Тестирование цепочки для создания и удаления блюда в MealPlan
     */
    @Test
    void mealPlanTest() {

        String date = "1589500800";

        clearMeal(date);
        String idMeal = addMeal(date);
        deleteMeal(idMeal);
    }

}
