package com.example.shopping.dto;

import java.io.Serializable;

import com.example.shopping.entity.CartEntity;
import com.example.shopping.form.CartForm;

import lombok.Data;

/**
 * カート情報格納用Dto
 * @author koki_shinzato
 */
@Data
public class CartDto implements Serializable{
	
	// 商品ID
	private Integer commodityId;
	
	// 商品数
	private Integer quantity;
	
	// 結合先テーブル
	private MenuDto menu;
	
	/**
	 * Dto → Form 変換
	 * @return カート情報（Form）
	 */
	public CartForm toForm() {
		
		CartForm cartForm = new CartForm();
		cartForm.setCommodityId(commodityId);
		cartForm.setQuantity(quantity);
		cartForm.setMenu(menu.toForm());
		
		return cartForm;
	}
	
	/**
	 * Dto → Entity 変換
	 * @return カート情報（Entity）
	 */
	public CartEntity toEntity() {
		
		CartEntity cartEntity = new CartEntity();
		cartEntity.setCommodityId(commodityId);
		cartEntity.setQuantity(quantity);
		
		return cartEntity;
	}
}
