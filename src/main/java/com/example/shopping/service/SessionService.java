package com.example.shopping.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopping.dto.CartDto;
import com.example.shopping.dto.MenuDto;

/**
 * セッション処理用
 * @author koki_shinzato
 */
@Service
public class SessionService {
	
	@Autowired
	private MenuService menuService;
	
	// 指定されたセッションデータ（カート情報）の数量を変更
	public List<CartDto> sessionQuantities(List<CartDto> sessionOrders, Integer commodityId, Integer quantity) {
		
		sessionOrders.stream().filter(order -> order.getCommodityId() == commodityId).forEach(order -> order.setQuantity(quantity));
		
		return sessionOrders;
	}
	
	/**
	 * セッションリストから特定のIDの商品のみ削除
	 * （IDに該当する商品を除いて、新しいセッションリストに格納）
	 * @param sessionList
	 * @param commodityId
	 * @return IDの商品を削除したセッション格納用リスト
	 */
	public List<CartDto> sessionDeleteId(List<CartDto> sessionList, Integer commodityId){
		
		List<CartDto> newSessionList = new ArrayList<CartDto>();
		sessionList.stream().filter(order -> order.getCommodityId() != commodityId).forEach(order -> newSessionList.add(order));
		
		return newSessionList;
	}
	
	/**
	 * IDで指定した商品をセッションのカートリストに追加
	 * @param sessionList
	 * @param commodityId
	 * @return 編集後セッションリスト
	 */
	public List<CartDto> sessionAddId(List<CartDto> sessionList, Integer commodityId) {

		sessionList.stream().filter(session -> session.getCommodityId() == commodityId).forEach(session -> {
			
			if(Objects.nonNull(session)) {	
				session.setQuantity(session.getQuantity() + 1);
				
			}else {
				CartDto newOrder = new CartDto();
				MenuDto findMenu = menuService.findById(commodityId);
				
				newOrder.setCommodityId(commodityId);
				newOrder.setQuantity(1);
				newOrder.setMenu(findMenu);
				
				sessionList.add(newOrder);
			}
		});

		return sessionList;
	}
}
