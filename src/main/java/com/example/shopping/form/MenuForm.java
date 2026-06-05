package com.example.shopping.form;

import com.example.shopping.dto.MenuDto;

import lombok.Data;

/**
 * メニュー情報格納用Form
 * @author koki_shinzato
 */
@Data
public class MenuForm {
	
	// 商品ID
	private Integer id;
	
	// 商品名
	private String commodityName;
	
	// 商品価格
	private Integer price;
	
	/**
	 * Form → Dto 変換
	 * @return メニュー情報（Dto）
	 */
	public MenuDto toDto() {
		
		MenuDto menuDto = new MenuDto();
		menuDto.setId(id);
		menuDto.setCommodityName(commodityName);
		menuDto.setPrice(price);
		
		return menuDto;
	}
}
