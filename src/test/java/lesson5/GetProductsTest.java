package lesson5;

import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;

public class GetProductsTest extends CommonProductTest {

    /**
     * Проверяем вывод всех продуктов
     */
    @SneakyThrows
    @Test
    void getProductsTest() {
        Response<ResponseBody> response = productService.getProducts().execute();

        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        assertThat(response.body().string(), CoreMatchers.notNullValue());
    }
}
