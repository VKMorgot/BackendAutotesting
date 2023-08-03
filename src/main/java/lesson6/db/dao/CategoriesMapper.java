package lesson6.db.dao;

import lesson6.db.model.Categories;
import lesson6.db.model.CategoriesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoriesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    long countByExample(CategoriesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int deleteByExample(CategoriesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int insert(Categories record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int insertSelective(Categories record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    List<Categories> selectByExample(CategoriesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    Categories selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int updateByExampleSelective(@Param("record") Categories record, @Param("example") CategoriesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int updateByExample(@Param("record") Categories record, @Param("example") CategoriesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int updateByPrimaryKeySelective(Categories record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table categories
     *
     * @mbg.generated Tue Aug 01 12:15:18 MSK 2023
     */
    int updateByPrimaryKey(Categories record);
}