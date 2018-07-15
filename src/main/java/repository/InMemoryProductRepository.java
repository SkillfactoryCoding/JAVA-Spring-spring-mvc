package repository;

import domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product repository which allow to CRUD operation with product.
 */
@Repository
public class InMemoryProductRepository implements ProductRepository {


    /**
     * JDBC object.
     */
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    /**
     * Return all product from database.
     * @return list of all products.
     */
    @Override
    public List<Product> getAllProducts() {
        Map<String, Object> params = new HashMap<>();
        List<Product> products = jdbcTemplate.query("SELECT * FROM products", params, new ProductMapper());
        return products;
    }

    /**
     * Update stock at the app.
     * @param productId of product which will be updated.
     * @param count how many we add products.
     */
    @Override
    public void updateStock(String productId, long count) {
        Map<String, Object> params = new HashMap<>();
        params.put("count", count);
        params.put("productId", productId);
        jdbcTemplate.update("UPDATE products SET units_in_stock = :count where id = :productId", params);
    }

    /**
     * Class which allow to map result set to the domain object.
     */
    private static final class ProductMapper implements RowMapper<Product> {
        /**
         * Create a new domain object with data extract from the database.
         * @param rs sequence of the result.
         * @param i iteration integer.
         * @return instance of domain object.
         * @throws SQLException throw if something was wrong.
         */
        @Override
        public Product mapRow(ResultSet rs, int i) throws SQLException {
            Product product = new Product();
            product.setProductId(rs.getString("id"));
            product.setName(rs.getString("name"));
            product.setDescription(rs.getString("description"));
            product.setUnitPrice(rs.getBigDecimal("unit_price"));
            product.setCategory(rs.getString("category"));
            product.setManufacturer(rs.getString("manufacturer"));
            product.setCondition(rs.getString("condition"));
            product.setUnitsInStock(rs.getLong("units_in_stock"));
            product.setUnitsInOrder(rs.getLong("units_in_order"));
            product.setDiscounted(rs.getBoolean("discounted"));
            return product;
        }
    }
}
