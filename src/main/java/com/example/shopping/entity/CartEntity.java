package com.example.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.example.shopping.dto.CartDto;

import lombok.Data;

/**
 * カート情報格納用Entity
 * @author koki_shinzato
 */
@Entity
@Data
@Table(name="tbl_cart")
public class CartEntity {
	
	// 商品ID
	@Id
	@Column(name="commodity_id")
	private Integer commodityId;
	
	// 商品数
	private Integer quantity;
	
	@OneToOne
	@JoinColumn(name="commodity_Id", referencedColumnName="id", insertable=false, updatable=false)
	private MenuEntity menu;
	
	/**
	 * Entity → Dto 変換
	 * @return カート情報（Entity）
	 */
	public CartDto toDto() {
		
		CartDto cartDto = new CartDto();
		cartDto.setCommodityId(commodityId);
		cartDto.setQuantity(quantity);
		cartDto.setMenu(menu.toDto());
		
		return cartDto;
	}
}
