package lesson6;

import com.github.javafaker.Faker;
import lesson6.db.dao.ProductsMapper;
import lesson6.db.model.Products;
import lesson6.db.model.ProductsExample;
import lesson6.dto.Product;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ModifyProductTest extends CommonProductTest {

    Product product = null;
    Faker faker = new Faker();
    int id = 0;

    /**
     * Добавляем новый продукт.
     * Делаем через api, так как при добавлении через db было бы проблемно получать id добавленного элемента
     */
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

        //проверяем, что продукт в базе изменился
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            Products productFromDB = productsMapper.selectByPrimaryKey((long) id);

            assertThat(productFromDB.getTitle(), equalTo(productToModify.getTitle()));
            assertThat(productFromDB.getCategory_id(), equalTo(1L)); // id = 1 - это Food
            assertThat(productFromDB.getPrice(), equalTo(productToModify.getPrice()));
        }
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
        try(SqlSession session = getSqlSessionFactory().openSession()) {
            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            productsMapper.deleteByPrimaryKey((long) id);
            session.commit();
        }
    }

}
