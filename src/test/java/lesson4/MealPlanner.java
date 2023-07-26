package lesson4;

import lesson4.dto.request.AddMealRequest;
import lesson4.dto.response.AddMealResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class MealPlanner extends SpoonacularCommonTest {

    AddMealRequest mealRequest = new AddMealRequest();
    private static final String MP_FILE = "src/test/resources/mp.properties";
    private static final Properties mpProp = new Properties();
    private static InputStream mpConfigFile;

    @BeforeAll
    static void addRequest() {
        requestSpecification = requestSpecification
                .contentType("application/x-www-form-urlencoded")
                .queryParam("hash", getHash())
                .pathParam("username", getUsername());
    }

    /**
     * Создаем объект для добавления в Meal Planner
     * @return объект для добавления
     * @throws IOException исключение
     */
    AddMealRequest createMealRequest() throws IOException {

        mpConfigFile = new FileInputStream(MP_FILE);
        mpProp.load(mpConfigFile);

        AddMealRequest.Ingredient ingredient = new AddMealRequest.Ingredient();
        ingredient.setName(mpProp.getProperty("ingredient"));

        ArrayList<AddMealRequest.Ingredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);

        AddMealRequest.Value value = new AddMealRequest.Value();
        value.setIngredients(ingredients);

        mealRequest.setDate(mpProp.getProperty("date"));
        mealRequest.setPosition(mpProp.getProperty("position"));
        mealRequest.setSlot(mpProp.getProperty("slot"));
        mealRequest.setType(mpProp.getProperty("type"));
        mealRequest.setValue(value);
        return mealRequest;
    }

    /**
     * Очищаем Meal Planner на выбранную дату
     * @param date дата очистки
     */
    void clearMeal(String date) {
        given()
                .pathParam("date", date)
                .delete(getMealPlannerUrl() + "{username}/day/{date}");
    }

    /**
     * Добавляем к Meal Planner нужный ингредиент на нужную дату
     * @param mealRequest объект для добавления
     * @return id добавленного ингредиента
     */
    AddMealResponse addMeal(AddMealRequest mealRequest) {

        AddMealResponse mealResponse = new AddMealResponse();
        mealResponse = given()
                    .body("{\n"
                            + " \"date\": "+ mealRequest.getDate() + ",\n"
                            + " \"slot\": " + mealRequest.getSlot() + ",\n"
                            + " \"position\": " + mealRequest.getPosition() + ",\n"
                            + " \"type\": \"" + mealRequest.getType() + "\",\n"
                            + " \"value\": {\n"
                            + " \"ingredients\": [\n"
                            + " {\n"
                            + " \"name\": \"" + mealRequest.getValue().getIngredients().get(0).getName() + "\"\n"
                            + " }\n"
                            + " ]\n"
                            + " }\n"
                            + "}")
                    .when()
                    .post(getMealPlannerUrl() + "{username}/items")
                    .then()
                    .extract()
                    .body()
                    .as(AddMealResponse.class);
        return mealResponse;
    }

    /**
     * Удаляем выбранный ингредиент из Meal Plan
     * @param id ингредиент
     */
    void deleteMeal(String id) {
        given()
                .pathParam("id", id)
                .delete(getMealPlannerUrl() + "{username}/items/{id}");
    }

    /**
     * Тестирование цепочки для создания и удаления блюда в MealPlan
     */
    @Test
    void mealPlanTest() throws IOException {

        AddMealRequest mealRequest = createMealRequest();

        clearMeal(mealRequest.getDate());
        AddMealResponse mealResponse = addMeal(mealRequest);
        deleteMeal(mealResponse.getId());
    }

}
