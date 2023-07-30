package lesson5;

import lesson5.api.ProductService;
import lesson5.dto.Product;
import lesson5.utils.RetrofitUtils;
import org.junit.jupiter.api.BeforeAll;

public class CommonProductTest {

    static ProductService productService;
    Product product = null;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }
}
