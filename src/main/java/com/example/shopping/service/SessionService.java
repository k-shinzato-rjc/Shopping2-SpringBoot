package com.example.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.shopping.dto.CartDto;

/**
 * セッション処理用
 * @author koki_shinzato
 */
@Service
public class SessionService {
	
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
}
