package com.example.shopping.dto;

import java.io.Serializable;

import com.example.shopping.entity.MenuEntity;
import com.example.shopping.form.MenuForm;

import lombok.Data;

/**
 * メニュー情報格納用Dto
 * @author koki_shinzato
 */
@Data
public class MenuDto implements Serializable{
	
	// 商品ID
	private Integer id;
	
	// 商品名
	private String commodityName;
	
	// 商品価格
	private Integer price;
	
	/**
	 * Dto → Form 変換
	 * @return メニュー情報（Form）
	 */
	public MenuForm toForm() {
		
		MenuForm menuForm = new MenuForm();
		menuForm.setId(id);
		menuForm.setCommodityName(commodityName);
		menuForm.setPrice(price);
		
		return menuForm;
	}
	
	/**
	 * Dto → Entity 変換
	 * @author koki_shinzato
	 * @return メニュー情報（Entity）
	 */
	public MenuEntity toEntity() {
		
		MenuEntity menuEntity = new MenuEntity();
		menuEntity.setId(id);
		menuEntity.setCommodityName(commodityName);
		menuEntity.setPrice(price);
		
		return menuEntity;
	}
}
