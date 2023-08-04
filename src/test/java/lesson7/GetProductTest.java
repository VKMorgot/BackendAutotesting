package lesson7;

import lesson7.db.dao.ProductsMapper;
import lesson7.db.model.Products;
import lesson7.db.model.ProductsExample;
import lesson7.dto.Product;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

        //проверяем, что в БД с таким же id находится этот же продукт с такими же параметрами
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            Products product = productsMapper.selectByPrimaryKey((long) 1);

            assertThat(product.getCategory_id(), equalTo(1L)); // id=1 категории = Food
            assertThat(product.getTitle(), equalTo("Milk"));
        }
    }

    /**
     * Проверяем, что по несуществующему id = 1000 вернется 404 ошибка
     */
    @SneakyThrows
    @Test
    void getProductByIdNotExist() {
        int id = 1000;

        //проверяем, что такого элемента в БД нет
        try (SqlSession session = getSqlSessionFactory().openSession()) {

            ProductsMapper productsMapper = session.getMapper(ProductsMapper.class);
            ProductsExample example = new ProductsExample();

            example.createCriteria().andIdEqualTo((long) id);
            assertThat(productsMapper.countByExample(example), equalTo(0L));
        }

        Response<Product> response = productService.getProductById(id).execute();
        assertThat(response.code(), CoreMatchers.is(404));
    }

}
