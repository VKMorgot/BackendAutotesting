package lesson6;

import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.ProductsExample;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.session.SqlSession;
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

        System.out.println("Hi");
        System.out.println(response.body().source());

        //проверяем, что в БД так же есть записи с продуктами (по-умолчанию их 5)
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            ProductsExample example = new ProductsExample();

            long productsNumber = productsMapper.countByExample(example);
            assertThat(productsNumber, CoreMatchers.equalTo(5L));
        }
    }
}
