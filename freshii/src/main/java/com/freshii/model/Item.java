package com.freshii.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.freshii.model.Customer.Builder;

@Document("item")
public class Item {

    @Id
    private String id;
    private String name;
    private int stock;
    private String ingredients;
    private int length;
    private int width;
    private int height;
    private String nutrition;
    private String size="medium";
    private String category;
    private int quantity=1;
    private double smallPrice;
    private double largePrice;
    private double mediumPrice;
    private String image;
    
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public double getSmallPrice() {
		return smallPrice;
	}

	public void setSmallPrice(double smallPrice) {
		this.smallPrice = smallPrice;
	}
	public double getLargePrice() {
		return largePrice;
	}
	public void setLargePrice(double largePrice) {
		this.largePrice = largePrice;
	}
	public double getMediumPrice() {
		return mediumPrice;
	}
	public void setMediumPrice(double mediumPrice) {
		this.mediumPrice = mediumPrice;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getIngredients() {
		return ingredients;
	}
	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public String getNutrition() {
		return nutrition;
	}
	public void setNutrition(String nutrition) {
		this.nutrition = nutrition;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public static class Builder
	{

	    private String id;
	    private String name;
	    private int stock;
	    private String ingredients;
	    private int length;
	    private int width;
	    private int height;
	    private String nutrition;
	    private String size="medium";
	    private String category;
	    private int quantity=1;
	    private double smallPrice;
	    private double largePrice;
	    private double mediumPrice;
	    private String image;
		
		public Builder() {
			this.size="medium";
		}
		public Builder id(String id) {
			this.id = id;
			return this;
		}
		public Builder image(String image) {
			this.name = image;
			return this;
		}
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder stock(int stock) {
			this.stock = stock;
			return this;
		}
		public Builder smallPrice(int smallPrice) {
			this.smallPrice = smallPrice;
			return this;
		}
		public Builder quantity(int quantity) {
			this.quantity = quantity;
			return this;
		}
		public Builder largePrice(int largePrice) {
			this.largePrice = largePrice;
			return this;
		}
		public Builder mediumPrice(int mediumPrice) {
			this.mediumPrice = mediumPrice;
			return this;
		}
		
		public Builder ingredients(String ingredients) {
			this.ingredients = ingredients;
			return this;
		}
		public Builder length(int length) {
			this.length = length;
			return this;
		}
		public Builder width(int width) {
			this.width = width;
			return this;
		}
		public Builder height(int height) {
			this.height = height;
			return this;
		}

		public Builder nutrition(String nutrition) {
			this.nutrition = nutrition;
			return this;
		}
		
		public Builder size(String size) {
			this.size = size;
			return this;
		}
		public Builder category(String category) {
			this.category = category;
			return this;
		}
		

		public Item build() {
			Item item =  new Item();
			item.id=this.id;
			item.name = this.name;
			item.stock = this.stock;
			item.smallPrice = this.smallPrice;
			item.mediumPrice = this.mediumPrice;
			item.largePrice = this.largePrice;
			item.quantity = this.quantity;
			item.ingredients = this.ingredients;
			item.length = this.length;
			item.width = this.width;
			item.height=this.height;
			item.nutrition=this.nutrition;
			item.size= this.size;
			item.category=this.category;
			item.image=this.image;
			return item;
		}

	}

}
