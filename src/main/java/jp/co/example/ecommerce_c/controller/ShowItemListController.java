package jp.co.example.ecommerce_c.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_c.domain.Item;
import jp.co.example.ecommerce_c.form.ShowItemListForm;
import jp.co.example.ecommerce_c.service.ShowItemListService;
import jp.co.example.ecommerce_c.service.importOutportService;

/**
 * 商品情報を操作するコントローラー.
 * 
 * @author nakamuratomoya
 *
 */
@Controller
@RequestMapping("/")
public class ShowItemListController {

	@Autowired
	private ShowItemListService service;

	@Autowired
	private importOutportService importOutportService;

	private static final int VIEW_SIZE = 9;

	@ModelAttribute
	public ShowItemListForm setUpForm() {
		return new ShowItemListForm();
	}

	/**
	 * 商品一覧画面を出力する.
	 * 
	 * @param model requestスコープ
	 * @return 商品一覧画面
	 */
	@RequestMapping("")
	public String showItemList(Model model, Integer page) {
		
		// ページング機能追加
		if (page == null) {
			// ページ数の指定が無い場合は1ページ目を表示させる
			page = 1;
		}

		List<Item> itemList = service.showItemList();

		// 表示させたいページ数、ページサイズ、商品リストを渡し１ページに表示させる商品リストを絞り込み
		Page<Item> itemPage = service.showListPaging(page, VIEW_SIZE, itemList);

		model.addAttribute("itemPage", itemPage);

		List<List<Item>> itemList3 = new ArrayList<>();
		List<Item> itemList2 = new ArrayList<>();
		int roopCount = 0;

		for (Item item : itemPage) {

			itemList2.add(item);
			roopCount++;

			if (roopCount % 3 == 0) {
				itemList3.add(itemList2);
				itemList2 = new ArrayList<>();
			}
		}
		if (roopCount % 3 != 0) {
			itemList3.add(itemList2);
		}

		model.addAttribute("itemList3", itemList3);
		//オートコンプリート実装のため追加しました。（金子）
		model.addAttribute("itemList", itemList);

		// ページングのリンクに使うページ数をスコープに格納
		List<Integer> pageNumbers = calcPageNumbers(model, itemPage);
		model.addAttribute("pageNumbers", pageNumbers);

		return "/item_list_toy";
	}

	private List<Integer> calcPageNumbers(Model model, Page<Item> itemPage) {
		int totalPages = itemPage.getTotalPages();
		List<Integer> pageNumbers = null;
		if (totalPages > 0) {
			pageNumbers = new ArrayList<Integer>();
			for (int i = 1; i <= totalPages; i++) {
				pageNumbers.add(i);
			}
		}
		return pageNumbers;
	}

	/**
	 * 商品名で曖昧検索をする.
	 * 
	 * @param showItemListForm フォーム
	 * @param model            requestスコープ
	 * @return 検索名に該当する商品情報
	 */
	@RequestMapping("/showItemNameList")
	public String showItemNameList(ShowItemListForm showItemListForm, Model model, Integer page) {

		if (page == null) {
			page = 1;
		}

		if (showItemListForm.getName().equalsIgnoreCase("")) {
			model.addAttribute("errorMessage", "検索結果が1件もありませんでした");
			showItemList(model,page);
			return "/item_list_toy";
		}

		List<Item> itemList = service.showItemNameList(showItemListForm.getName());

		if (itemList == null) {
			model.addAttribute("errorMessage", "検索結果が1件もありませんでした");
			showItemList(model,page);
			return "/item_list_toy";
		}

		Page<Item> itemPage = service.showListPaging(page, VIEW_SIZE, itemList);
		
		model.addAttribute("itemPage", itemPage);

		List<List<Item>> itemList3 = new ArrayList<>();
		List<Item> itemList2 = new ArrayList<>();
		int roopCount = 0;

		for (Item item : itemPage) {

			itemList2.add(item);
			roopCount++;

			if (roopCount % 3 == 0) {
				itemList3.add(itemList2);
				itemList2 = new ArrayList<>();

			}

		}

		if (roopCount < 3) {
			itemList3.add(itemList2);
		}

		model.addAttribute("itemList3", itemList3);

		List<Integer> pageNumbers = calcPageNumbers(model, itemPage);
		model.addAttribute("pageNumbers", pageNumbers);

		return "/item_list_toy";

	}

	/**
	 * ソート機能で表示を変える.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/sort")
	public String priceSortList(String sort, Model model) {
		List<Item> itemList = null;
		if (sort.equals("1")) {
			itemList = service.nameSortAsc(sort);
		}else if(sort.equals("2")) {
			itemList = service.nameSortDesc(sort);
		}else if(sort.equals("3")) {
			itemList = service.priceSortAsc(sort);
		}else if(sort.equals("4")) {
			itemList = service.priceSortDesc(sort);	
		}

			List<List<Item>> itemList3 = new ArrayList<>();
			List<Item> itemList2 = new ArrayList<>();

			int roopCount = 0;

			for (Item item : itemList) {

				itemList2.add(item);
				roopCount++;

				if (roopCount % 3 == 0) {
					itemList3.add(itemList2);
					itemList2 = new ArrayList<>();
				}
			}

			model.addAttribute("itemList3", itemList3);
			return "/item_list_toy";

		}

		@RequestMapping("/import")
		public String csvImport(Model model) {
			importOutportService.addItem();
			return "redirect:/";
		}

}
