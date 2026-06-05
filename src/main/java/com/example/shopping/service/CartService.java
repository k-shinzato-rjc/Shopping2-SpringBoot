package com.example.shopping.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dto.CartDto;
import com.example.shopping.dto.MenuDto;
import com.example.shopping.entity.CartEntity;
import com.example.shopping.form.CartForm;
import com.example.shopping.repository.CartRepository;

/**
 * カートサービス
 * @author koki_shinzato
 */
@Service
public class CartService {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private MenuService menuService;
	
	/**
	 * 全カート情報取得、Dto型で返す
	 * @return カート情報リスト（Dto）
	 */
	public List<CartDto> findAll(){
		
		List<CartEntity> entityList = cartRepository.findAll();
		
		return this.convertFromEntityToDto(entityList);
	}
	
	/**
	 * IDに該当するカート情報取得、Dto型で返す
	 * @param commodityId 検索ID
	 * @return カート情報（Dto）
	 */
	public CartDto findById(Integer commodityId) {
		
		return cartRepository.findById(commodityId).map(entity -> entity.toDto()).orElse(null);
	}
	
	/**
	 * カートに商品(ID指定)を追加する
	 * 
	 * カートからIDに該当した商品を取り出し、個数＋1して再登録
	 * 商品が存在しない場合は、メニューテーブルからIDで商品を取得して、カートに新規登録
	 * 
	 * @param cartDto カート情報
	 * @return 登録済み情報
	 */
	public void add(Integer id) {
		Optional<CartEntity> op = cartRepository.findById(id);
		
		// 既にカートに該当商品が入っていた場合、個数を+1して再登録
		op.ifPresentOrElse(
			(entity) -> {
				entity.setQuantity(entity.getQuantity() + 1);
				cartRepository.save(entity);
			},
			
			// 初めて登録する商品の場合（カートに該当商品が無かった場合）
			() -> {
				// 該当する商品をメニューテーブルから取得
				MenuDto menuDto = menuService.findById(id);
				
				// 新しくカートへ登録するデータを作成し、商品ID・数量(1）・メニュー情報をセット
				CartDto cartDto = new CartDto();
				cartDto.setCommodityId(menuDto.getId());
				cartDto.setQuantity(1);
				cartDto.setMenu(menuDto);
				
				// カートテーブルへ再登録
				cartRepository.save(cartDto.toEntity());
			}
		);
	}
	
	/**
	 * カート情報（セッション）をDBへ反映
	 * 反映前にDB情報（古い情報）は全削除する
	 */
	public void update(List<CartDto> sessionCartList) {
		
		cartRepository.deleteAll();
		
		List<CartEntity> entityList = convertFromDtoToEntity(sessionCartList);
		entityList.stream().forEach(entity -> {
			cartRepository.save(entity);
		});
	}
	
	/**
	 * カート情報1件削除
	 * @param commodityId 検索ID
	 */
	public void deleteById(Integer commodityId) {
		
		cartRepository.deleteById(commodityId);
	}
	
	/**
	 * カート情報全削除
	 */
	public void deleteAll() {
		cartRepository.deleteAll();
	}

	public void changeQuantity(Integer commodityId,Integer quantity) {
		
		Optional<CartEntity> op = cartRepository.findById(commodityId);
		
		op.ifPresent(entity -> {
				entity.setQuantity(quantity);
				cartRepository.save(entity);
			}
		);
	}
	
	/**
	 * Entityリスト → Dtoリスト 変換
	 * @param entityList カート情報リスト（Entity）
	 * @return dtoList カート情報リスト（Dto）
	 */
	public List<CartDto> convertFromEntityToDto(List<CartEntity> entityList){
		
		List<CartDto> dtoList = new ArrayList<CartDto>();
		
		entityList.stream().forEach(entity -> {
			dtoList.add(entity.toDto());
		});
		
		return dtoList;
	}
	
	/**
	 * Dtoリスト → Formリスト 変換
	 * @param dtoList カート内商品リスト（Dto）
	 * @return formList カート内商品リスト（Form)
	 */
	public List<CartForm> convertFromDtoToForm(List<CartDto> dtoList){
		
		List<CartForm> formList = new ArrayList<CartForm>();
		
		dtoList.stream().forEach(dto -> {
			formList.add(dto.toForm());
		});
		
		return formList;
	}
	
	/**
	 * Formリスト → Dtoリスト 変換
	 * @param formList カート内商品リスト（form）
	 * @return dtoList カート内商品リスト（Dto)
	 */
	public List<CartDto> convertFromFormToDto(List<CartForm> formList){
		
		List<CartDto> dtoList = new ArrayList<CartDto>();
		
		formList.stream().forEach(form -> {
			dtoList.add(form.toDto());
		});
		
		return dtoList;
	}
	
	/**
	 * Dtoリスト → Entityリスト 変換
	 * @param dtoList dtoリスト
	 * @return Entityリスト
	 */
	public List<CartEntity> convertFromDtoToEntity(List<CartDto> dtoList){
		
		List<CartEntity> entityList = new ArrayList<CartEntity>();
		
		dtoList.stream().forEach(dto -> {
			entityList.add(dto.toEntity());
		});
		
		return entityList;
	}
}
