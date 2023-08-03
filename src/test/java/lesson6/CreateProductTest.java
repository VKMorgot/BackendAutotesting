package lesson6;

import com.github.javafaker.Faker;
import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.Products;
import lesson6.dto.Product;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.apache.ibatis.session.SqlSession;
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

        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            Products productFromDB = productsMapper.selectByPrimaryKey((long) id);

            assertThat(productFromDB.getTitle(), CoreMatchers.equalTo(product.getTitle()));
            assertThat(productFromDB.getPrice(), CoreMatchers.equalTo(product.getPrice()));
            assertThat(productFromDB.getCategory_id(), CoreMatchers.equalTo(1L)); // id = 1 - категория Food
        }
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

        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            Products productFromDB = productsMapper.selectByPrimaryKey((long) id);
            Products productFromDBDouble = productsMapper.selectByPrimaryKey((long) id_double);

            assertThat(productFromDB, CoreMatchers.not(productFromDBDouble));
            assertThat(productFromDB.getId(), CoreMatchers.not(productFromDBDouble.getId()));
            assertThat(productFromDB.getTitle(), CoreMatchers.equalTo(productFromDBDouble.getTitle()));
            assertThat(productFromDB.getPrice(), CoreMatchers.equalTo(productFromDBDouble.getPrice()));
            assertThat(productFromDB.getCategory_id(), CoreMatchers.equalTo(productFromDBDouble.getCategory_id())); // id = 1 - категория Food
        }

        deleteProductFromDB(id_double);
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

    /**
     * Удаление продукта из базы
     * @param productId id продукта
     */
    @SneakyThrows
    void deleteProductFromDB(long productId) {
        try(SqlSession session = getSqlSessionFactory().openSession()) {
            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            productsMapper.deleteByPrimaryKey(productId);
            session.commit();
        }
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        deleteProductFromDB(id);
    }

}
