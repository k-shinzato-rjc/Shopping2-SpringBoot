package com.example.shopping.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.example.shopping.dto.MenuDto;

import lombok.Data;

/**
 * メニュー情報格納用Entity
 * @author koki_shinzato
 */
@Entity
@Data
@Table(name="mst_commodity")
public class MenuEntity {
	
	// 商品ID
	@Id
	private Integer id;
	
	// 商品名
	@Column(name="commodity_name")
	private String commodityName;
	
	// 商品価格
	private Integer price;
	
	/**
	 * Entity → Dto 変換
	 * @author koki_shinzato
	 * 
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