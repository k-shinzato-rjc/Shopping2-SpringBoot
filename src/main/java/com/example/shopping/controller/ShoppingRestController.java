package com.example.shopping.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopping.dto.CartDto;
import com.example.shopping.dto.MenuDto;
import com.example.shopping.form.CartForm;
import com.example.shopping.form.MenuForm;
import com.example.shopping.service.CartService;
import com.example.shopping.service.MenuService;
import com.example.shopping.service.SessionService;

/**
 * レストコントローラー
 * DB操作 → next.jsへJsonデータを送信
 * @author koki_shinzato
 */
@RestController
@CrossOrigin(origins="http://localhost:3000")
public class ShoppingRestController {
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private SessionService sessionService;
	
	/**
	 * 全Menu情報を取得し、レスポンス
	 * @return メニュー情報リスト（Json）
	 */
	@GetMapping("/menu/list")
	public List<MenuForm> menuList() {
		
		List<MenuDto> dtoList = menuService.findAll();
		
		return menuService.convertFromDtoToForm(dtoList);
	}
	
	/**
	 * カート情報を取得し、レスポンス
	 * @return カート内 商品リスト（Json）
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/cart/list")
	public List<CartForm> cartList(HttpSession httpSession) {
		
		// DBからカート情報を取得し、セッションに保存
		List<CartDto> cartList = cartService.findAll();
		httpSession.setAttribute("orders", cartService.convertFromDtoToForm(cartList));
		
		return (List<CartForm>)httpSession.getAttribute("orders");
	}
	
	/**
	 * メニュー画面からクリアボタンを押下 → カート内全削除
	 */
	@ResponseBody
	@GetMapping("/cart/all/clear")
	public void cartClear() {
		cartService.deleteAll();
	}
	
	/**
	 * IDに該当する商品をカートへ1つ追加
	 * （カートテーブルからIDに該当した商品データを取り出し、個数を1加算して再登録する）
	 * @param id
	 */
	@ResponseBody
	@PostMapping("/cart/order/add")
	public void cartAdd(@RequestParam(name="id") Integer id) {
		
		cartService.add(id);
	}
	
	/**
	 * カート一覧画面から削除ボタン押下 → IDに該当するカート情報を削除
	 * @param commodityId
	 * @return
	 */
	@ResponseBody
	@PostMapping("/cart/order/delete")
	public List<CartForm> cartDelete(HttpSession httpSession,@RequestParam(name="commodityId") Integer commodityId) {
		
		// セッション情報（カート内商品リスト）を取り出す
		@SuppressWarnings("unchecked")
		List<CartDto> sessionList = (List<CartDto>)httpSession.getAttribute("orders");
		
		// IDに該当する商品を取り除き、再びセッションに格納
		List<CartForm> newSessionList = cartService.convertFromDtoToForm(sessionService.sessionDeleteId(sessionList, commodityId));
		httpSession.setAttribute("orders", newSessionList);
		
		// Jsonデータとして返す
		return newSessionList;
	}
	
	/**
	 * カート一覧画面からセレクトボックスで数量変更 → セッションに反映して返す
	 * @param commodityId
	 * @param quantity
	 * @param model
	 * @param sessionList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/cart/quantity/change")
	public List<CartForm> cartUpdate(@RequestParam("commodityId") Integer commodityId, @RequestParam("quantity") Integer quantity,
			HttpSession httpSession) {
		
		@SuppressWarnings("unchecked")
		List<CartForm> sessionList = (List<CartForm>)httpSession.getAttribute("orders");
		List<CartDto> changeSession = sessionService.sessionQuantities(cartService.convertFromFormToDto(sessionList), commodityId, quantity);
		httpSession.setAttribute("orders", cartService.convertFromDtoToForm(changeSession));
		
		return (List<CartForm>)httpSession.getAttribute("orders");
	}
	
	/**
	 * カート内 保存ボタン押下 → カートテーブルのデータを全削除し、セッションデータをDBに登録
	 * @param httpSession
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/cart/regist")
	public List<CartForm> cartUpdate(HttpSession httpSession) {
		
		List<CartForm> sessionList = (List<CartForm>)httpSession.getAttribute("orders");
		cartService.update(cartService.convertFromFormToDto(sessionList));
		
		return sessionList;
	}
	
	/**
	 * 購入ボタン押下 → カートテーブル内の全データを削除
	 * @return カート一覧画面
	 */
	@GetMapping("/cart/purchase")
	public String cartPurChase() {
		
		cartService.deleteAll();
		
		return "redirect:/cart/list";
	}
}
