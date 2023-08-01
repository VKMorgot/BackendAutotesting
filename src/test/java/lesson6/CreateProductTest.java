package lesson6;

import com.github.javafaker.Faker;
import lesson6.dto.Product;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;

public class CreateProductTest extends CommonProductTest {

    Product product = null;
    Faker faker = new Faker();
    int id = 0;

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }

    /**
     * Проверка создания продукта
     */
    @SneakyThrows
    @Test
    void createProductInFoodCategoryTest() {

        Response<Product> response = productService.createProduct(product)
                .execute();
        id = response.body().getId();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(201));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.equalTo(product.getCategoryTitle()));
        assertThat(response.body().getTitle(), CoreMatchers.equalTo(product.getTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.equalTo(product.getPrice()));
    }

    /**
     * Проверяем, что два одинаковых продукта добавляются в базу
     */
    @SneakyThrows
    @Test
    void createDoubleProduct() {

        Response<Product> response = productService.createProduct(product)
                .execute();
        id = response.body().getId();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));

        Response<Product> response_double = productService.createProduct(product)
                .execute();
        int id_double = response_double.body().getId();
        assertThat(response_double.isSuccessful(), CoreMatchers.is(true));

        assertThat(response.body().getId(), CoreMatchers.not(response_double.body().getId()));
        assertThat(response.body().getTitle(), CoreMatchers.equalTo(response_double.body().getTitle()));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.equalTo(response_double.body().getCategoryTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.equalTo(response_double.body().getPrice()));

        Response<ResponseBody> response_double_delete = productService.deleteProduct(id_double).execute();
        assertThat(response_double_delete.isSuccessful(), CoreMatchers.is(true));
    }

    /**
     * Проверяем, что нельзя добавить продукт в несуществующую категорию
     */
    @SneakyThrows
    @Test
    void createProductNoExistCategory() {

        product.setCategoryTitle("NoFood");

        Response<Product> response = productService.createProduct(product)
                .execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(500));

    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        if (id != 0) {
            Response<ResponseBody> response = productService.deleteProduct(id).execute();
            assertThat(response.isSuccessful(), CoreMatchers.is(true));
        }
    }

}
