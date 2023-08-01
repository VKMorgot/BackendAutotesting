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

public class DeleteProductTest extends CommonProductTest {

    Product product = null;
    Faker faker = new Faker();
    int id = 0;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

        Response<Product> response = productService.createProduct(product)
                .execute();
        id = response.body().getId();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
    }

    /*
    Проверка удаления существующего продукта
     */
    @SneakyThrows
    @Test
    void deleteProductTest() {
        Response<ResponseBody> response = productService.deleteProduct(id).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));
    }

    /**
     * Проверка удаления несуществующего продукта
     */
    @SneakyThrows
    @Test
    void deleteNoExistProductTest() {
        Response<ResponseBody> response = productService.deleteProduct(id + 1).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(500));
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        if (id != 0) {
            Response<ResponseBody> response = productService.deleteProduct(id).execute();
        }
    }
}
