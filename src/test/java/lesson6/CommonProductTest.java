package lesson6;

import lesson6.api.ProductService;
import lesson6.dto.Product;
import lesson6.utils.RetrofitUtils;
import org.junit.jupiter.api.BeforeAll;

public class CommonProductTest extends CommonTest {

    static ProductService productService;
    Product product = null;

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit()
                .create(ProductService.class);
    }

}
