package com.example.shopping.form;

import com.example.shopping.dto.CartDto;

import lombok.Data;

/**
 * カート情報格納用Form
 * @author koki_shinzato
 */
@Data
public class CartForm {
	
	// 商品ID
	private Integer commodityId;
	
	// 商品数
	private Integer quantity;
	
	// 結合先テーブル
	private MenuForm menu;
	
	/**
	 * Form → Dto 変換
	 * @return カート情報（Dto）
	 */
	public CartDto toDto() {
		
		CartDto cartDto = new CartDto();
		cartDto.setCommodityId(commodityId);
		cartDto.setQuantity(quantity);
		cartDto.setMenu(menu.toDto());
		
		return cartDto;
	}
}
