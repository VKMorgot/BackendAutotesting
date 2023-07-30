package lesson5;

import com.github.javafaker.Faker;
import lesson5.dto.Product;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;

public class ModifyProductTest extends CommonProductTest{

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

    /**
     * Проверка изменения существующего продукта
     */
    @SneakyThrows
    @Test
    void modifyProductTest() {

        Product productToModify = new Product()
                .withId(id)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000) + product.getPrice());

        Response<Product> response = productService.modifyProduct(productToModify)
                .execute();

        product.setId(id);
        assertThat(productToModify, CoreMatchers.not(CoreMatchers.equalTo(product)));
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.code(), CoreMatchers.is(200));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.equalTo(productToModify.getCategoryTitle()));
        assertThat(response.body().getTitle(), CoreMatchers.equalTo(productToModify.getTitle()));
        assertThat(response.body().getPrice(), CoreMatchers.equalTo(productToModify.getPrice()));
    }

    /**
     * Проверка изменения существующего продукта с добавлением в несуществующую категорию
     */
    @SneakyThrows
    @Test
    void modifyProductNoExistCategoryTest() {

        Product productToModify = new Product()
                .withId(id)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("NoFood")
                .withPrice((int) (Math.random() * 10000));

        Response<Product> response = productService.modifyProduct(productToModify)
                .execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(500));
    }

    /**
     * Проверка изменения несуществующего продукта
     */
    @SneakyThrows
    @Test
    void modifyProductNoExistProductTest() {

        Product productToModify = new Product()
                .withId(id + 1)
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));

        Response<Product> response = productService.modifyProduct(productToModify)
                .execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(false));
        assertThat(response.code(), CoreMatchers.is(400));
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
