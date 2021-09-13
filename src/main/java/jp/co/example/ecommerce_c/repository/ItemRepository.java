package jp.co.example.ecommerce_c.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_c.domain.Item;

/**
 * itemsテーブルを操作するリポジトリ.
 *
 * @author fukushima, nakamuratomoya
 *
 */
@Repository
public class ItemRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * itemsオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		item.setImagePath(rs.getString("image_path"));
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};

	/**
	 * 商品IDから商品情報を取得します.
	 *
	 * @param id 商品ID
	 * @return 商品情報
	 */
	public Item load(Integer id) {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items WHERE id=:id;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		return item;
	}

	/**
	 * 商品情報を全件取得するメソッド.
	 * 
	 * @return 全件の商品情報
	 */
	public List<Item> findAll() {

		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY price_m ;";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);

		return itemList;

	}

	/**
	 * 商品名で曖昧検索するメソッド.
	 * 
	 * @param name 商品名
	 * @return 検索名に該当する商品情報
	 */
	public List<Item> findByName(String name) {

		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items WHERE name ILIKE :name ORDER BY price_m ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);

		if (itemList.size() == 0) {
			return null;
		}
		return itemList;
	}

	/**
	 * 値段を昇順でソート.
	 * 
	 * @param price_m Mの金額
	 * @return 金額が昇順でソートされた商品リスト
	 */
	public List<Item> findByPriceAsc(String sort) {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY price_m;";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 値段を降順でソート.
	 * 
	 * @param price_m Mの金額
	 * @return 金額が降順でソートされた商品リスト
	 */
	public List<Item> findByPriceDesc(String sort) {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY price_m DESC;";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 名前を昇順でソート.
	 * 
	 * @param name 商品名
	 * @return 商品名が昇順でソートされた商品リスト
	 */
	public List<Item> findByNameAsc(String sort) {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY name;";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * 名前を降順でソート.
	 * 
	 * @param name 商品名
	 * @return 商品名が降順でソートされた商品リスト
	 */
	public List<Item> findByNameDesc(String sort) {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY name DESC;";

		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	public void insert(Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);

		String sql = "INSERT INTO items(id, name, description, price_m, price_l, image_path, deleted)"
				+ " VALUES(:id, :name, :description, :priceM, :priceL, :imagePath, :deleted)";

		template.update(sql, param);
	}

	public int findByMaxId() {
		String sql = "SELECT max(id) FROM items;";
		
		SqlParameterSource param = new MapSqlParameterSource();
		Integer result = template.queryForObject(sql, param, Integer.class);
		return result;
	}

}
