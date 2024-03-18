package com.freshii.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freshii.dao.repo.ItemRepo;
import com.freshii.model.Item;

@Service
public class ItemService {

	@Autowired
	private ItemRepo itemRepo;

	public Item getItemById(String itemId) {
		return itemRepo.findById(itemId).orElse(null);
	}

	public Item getItemByCategory(String category) {
		return itemRepo.findItemByCategory(category);
	}

	public Item getItemByName(String name) {
		return itemRepo.findItemByName(name);
	}

	public List<Item> getItems() {
		return itemRepo.findAll();
	}

	public Item createItem(Item item) {
		return itemRepo.save(item);
	}

	public Item deleteItem(String itemId) {
		Item item = itemRepo.findById(itemId).orElse(null);
		if (item == null) {
			return null;
		}
		itemRepo.deleteById(itemId);
		item = itemRepo.findById(itemId).orElse(null);
		if (item != null) {
			return null;
		}
		return item;
	}

	public Item updateItem(Item item) {
		return itemRepo.save(item);
	}
}
