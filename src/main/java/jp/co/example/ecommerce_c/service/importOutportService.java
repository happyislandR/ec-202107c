package jp.co.example.ecommerce_c.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_c.common.Batch;
import jp.co.example.ecommerce_c.domain.Item;
import jp.co.example.ecommerce_c.repository.ItemRepository;

@Service
@Transactional
public class importOutportService {

	@Autowired
	private ItemRepository itemRepository;

	public void addItem() {	
		Batch b = new Batch();
		List<Item> itemList = b.importCSV();
		int i = itemRepository.findByMaxId() + 1;
		//int i = 19;
		for(Item item : itemList) {
			item.setId(i);
			itemRepository.insert(item);
			i++;
		}
	}

	public List<Item> showItem() {
		List<Item> itemList = itemRepository.findAll();
		return itemList;
	}
}
