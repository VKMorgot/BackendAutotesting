package lesson7;

import lesson7.api.ProductService;
import lesson7.utils.RetrofitUtils;
import org.junit.jupiter.api.BeforeAll;

public class CommonProductTest extends CommonTest {

    static ProductService productService;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

}
