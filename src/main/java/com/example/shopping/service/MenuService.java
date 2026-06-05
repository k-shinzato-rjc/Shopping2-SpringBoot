package com.example.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dto.MenuDto;
import com.example.shopping.entity.MenuEntity;
import com.example.shopping.form.MenuForm;
import com.example.shopping.repository.MenuRepository;

/**
 * メニューサービス
 * @author koki_shinzato
 */
@Service
public class MenuService {
	
	@Autowired
	private MenuRepository menuRepository;
	
	/**
	 * 全メニュー情報を取得、Dto型リストで返す
	 * @return メニュー情報リスト（Dto）
	 */
	public List<MenuDto> findAll(){
		List<MenuEntity> entityList = menuRepository.findAll();
		
		return this.convertFromEntityToDto(entityList);
	}
	
	/**
	 * IDに該当するメニュー情報を取得、Dto型で返す
	 * @param id 検索ID
	 * @return メニュー情報（Dto）
	 */
	public MenuDto findById(Integer id) {
		
		return menuRepository.findById(id).map(entity -> entity.toDto()).orElse(null);
	}
	
	/**
	 * Entityリスト → Dtoリスト 変換
	 * @param entityList メニュー情報リスト（Entity)
	 * @return dtoList メニュー情報リスト（Dto）
	 */
	public List<MenuDto> convertFromEntityToDto(List<MenuEntity> entityList){
		
		List<MenuDto> dtoList = new ArrayList<MenuDto>();
		
		entityList.stream().forEach(entity -> {
			dtoList.add(entity.toDto());
		});
		
		return dtoList;
	}
	
	/**
	 * Dtoリスト → Formリスト 変換
	 * @param dtoList メニュー情報（Dto）
	 * @return formList メニュー情報（Form）
	 */
	public List<MenuForm> convertFromDtoToForm(List<MenuDto> dtoList){
		
		List<MenuForm> formList = new ArrayList<MenuForm>();
		dtoList.stream().forEach(dto -> {
			formList.add(dto.toForm());
		});
		
		return formList;
		
	}
}
