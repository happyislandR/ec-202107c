package jp.co.example.ecommerce_c.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_c.domain.Item;
import jp.co.example.ecommerce_c.repository.ItemRepository;

/**
 * 商品情報を操作するサービス.
 * 
 * @author nakamuratomoya
 *
 */
@Service
public class ShowItemListService {
	
	@Autowired
	private ItemRepository repository;
	
	
	/**
	 * 商品情報を全件検索する.
	 * 
	 * @return 全件の商品
	 */
	public List<Item> showItemList(){
		
		List<Item> itemList = repository.findAll();
		
		return itemList;
		
	}
	
	/**
	 * 商品情報を商品名で曖昧検索する.
	 * 
	 * @param name  商品名
	 * @return　検索名に該当する商品情報
	 */
	public List<Item> showItemNameList(String name){
		
		List<Item> itemList = repository.findByName(name);
		
		return itemList;
	}
	
	/**
	 * 商品情報を値段の昇順で表示する.
	 * @param price_m Mの値段
	 * @return 値段の昇順で並び替えられた商品情報
	 */
	public List<Item> priceSortAsc(String sort){
		List<Item> itemList = repository.findByPriceAsc(sort);
		return itemList;
	}
	
	
	/**
	 * 商品情報を値段の降順で表示する.
	 * @param price_m Mの値段
	 * @return 値段の降順で並び替えられた商品情報
	 */
	public List<Item> priceSortDesc(String sort){
		List<Item> itemList = repository.findByPriceDesc(sort);
		return itemList;
	}
	
	/**
	 * 商品情報を名前の昇順で表示する.
	 * @param name 商品名
	 * @return 商品名の昇順で並び替えられた商品情報
	 */
	public List<Item> nameSortAsc(String sort){
		List<Item> itemList = repository.findByNameAsc(sort);
		return itemList;
	}
	
	/**
	 * 商品情報を名前の降順で表示する.
	 * @param name 商品名
	 * @return 商品名の降順で並び替えられた商品情報
	 */
	public List<Item> nameSortDesc(String sort){
		List<Item> itemList = repository.findByNameDesc(sort);
		return itemList;
	}

	/**
	 * ページング用メソッド.
	 *
	 * @param page 表示させたいページ数
	 * @param size １ページに表示させる従業員数
	 * @param itemList 絞り込み対象リスト
	 * @return ページに表示されるサイズ分の商品一覧情報
	 */
	public Page<Item> showListPaging(int page, int size, List<Item> itemList) {
	    // 表示させたいページ数を-1しなければうまく動かない
	    page--;
	    // どの商品から表示させるかと言うカウント値
	    int startItemCount = page * size;
	    // 絞り込んだ後の商品リストが入る変数
	    List<Item> list;

	    if (itemList.size() < startItemCount) {
	    	// もし表示させたい商品カウントがサイズよりも大きい場合は空のリストを返す
	    	list = Collections.emptyList();
	    } else {
	    	// 該当ページに表示させる商品一覧を作成
	    	int toIndex = Math.min(startItemCount + size, itemList.size());
	    	list = itemList.subList(startItemCount, toIndex);
	    }

	    // 上記で作成した該当ページに表示させる商品一覧をページングできる形に変換して返す
	    Page<Item> itemPage = new PageImpl<Item>(list, PageRequest.of(page, size), itemList.size());

	    return itemPage;
	}
}
