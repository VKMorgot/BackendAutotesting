package lesson5;

import lesson5.dto.Product;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;


public class GetProductTest extends CommonProductTest {

    /**
     * Проверяем, что по заданному id=1 выдается искомый продукт
     */
    @SneakyThrows
    @Test
    void getProductById() {
        int id = 1;

        Response<Product> response = productService.getProductById(id).execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().getId(), CoreMatchers.is(id));
        assertThat(response.body().getCategoryTitle(), CoreMatchers.equalTo("Food"));
        assertThat(response.body().getTitle(), CoreMatchers.equalTo("Milk"));
    }

    /**
     * Проверяем, что по несуществующему id = 1000 вернется 404 ошибка
     */
    @SneakyThrows
    @Test
    void getProductByIdNotExist() {
        int id = 1000;

        Response<Product> response = productService.getProductById(id).execute();

        assertThat(response.code(), CoreMatchers.is(404));
    }

}
