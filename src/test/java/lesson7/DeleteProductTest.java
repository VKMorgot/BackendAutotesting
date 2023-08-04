package lesson7;

import com.github.javafaker.Faker;
import lesson7.db.dao.ProductsMapper;
import lesson7.db.model.Products;
import lesson7.db.model.ProductsExample;
import lesson7.dto.Product;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

        //проверяем, что продукт добавился в базу
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            ProductsExample example = new ProductsExample();

            example.createCriteria().andIdEqualTo((long) id);
            long counter  = productsMapper.countByExample(example);
            assertThat(counter, equalTo(1L));
        }
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

        //проверяем, что продукт в базе удалился
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            Products productFromDB = productsMapper.selectByPrimaryKey((long) id);

            assertThat(productFromDB, Matchers.nullValue());
        }
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
        try(SqlSession session = getSqlSessionFactory().openSession()) {
            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            productsMapper.deleteByPrimaryKey((long) id);
            session.commit();
        }
    }
}
