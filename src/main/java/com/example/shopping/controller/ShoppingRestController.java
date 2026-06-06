package com.example.shopping.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import com.example.shopping.service.CartService;
import com.example.shopping.service.MenuService;
import com.example.shopping.service.SessionService;

/**
 * レストコントローラー
 * DB操作 → next.jsへJsonデータを送信
 * @author koki_shinzato
 */
@RestController
@CrossOrigin(origins="http://localhost:3000",allowCredentials = "true")
public class ShoppingRestController {
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private SessionService sessionService;
	
	/**
	 * 全メニュー情報を取得し、セッションに格納
	 * @return セッション内 商品リスト（Json）
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@GetMapping("/menu/list")
	public List<MenuDto> menuList(HttpSession httpSession) {
		
		httpSession.setAttribute("orders", cartService.findAll());
		
		// もしカート情報セッションが存在しない場合、空のセッションを作成
		if(Objects.isNull(httpSession.getAttribute("orders"))) {
			List<CartDto> newSession = new ArrayList<CartDto>();
			httpSession.setAttribute("orders", newSession);
		}
		
		List<MenuDto> dtoList = menuService.findAll();
		httpSession.setAttribute("menus", dtoList);
		
		return dtoList;
	}
	
	/**
	 * カート情報を取得し、セッションに格納
	 * @return セッション内 カート内商品リスト（Json）
	 */
	@ResponseBody
	@SuppressWarnings("unchecked")
	@GetMapping("/cart/list")
	public List<CartDto> cartList(HttpSession httpSession) {

		return (List<CartDto>)httpSession.getAttribute("orders");
	}
	
	/**
	 * メニュー画面からクリアボタンを押下 → セッション内 カート情報削除
	 */
	@GetMapping("/cart/all/clear")
	public void cartClear(HttpSession httpSession) {
		httpSession.removeAttribute("orders");
		
		List<CartDto> newSession = new ArrayList<CartDto>();
		httpSession.setAttribute("orders", newSession);
	}
	
	/**
	 * メニュー一覧からIDに該当する商品を1つ追加（データを編集してセッションへ再格納）
	 * @param id
	 */
	@PostMapping("/cart/order/add")
	public void cartAdd(HttpSession httpSession, @RequestParam(name="id") Integer id) {
		
		@SuppressWarnings("unchecked")
		List<CartDto> sessionOrders = (List<CartDto>) httpSession.getAttribute("orders");
		
		if (sessionOrders == null) {
			sessionOrders = new ArrayList<CartDto>();
		}
		
		@SuppressWarnings("unchecked")
		List<CartDto> editcartList = sessionService.sessionAddId(sessionOrders, id);
		
		// テスト
		editcartList.stream().forEach(cart -> {
			System.out.println(cart.getCommodityId());
		});
		
		httpSession.setAttribute("orders", editcartList);
	}
	
	/**
	 * カート一覧画面から削除ボタン押下 → IDに該当するカート情報を削除
	 * @param commodityId
	 * @return 該当商品削除後のカート商品リスト
	 */
	@ResponseBody
	@PostMapping("/cart/order/delete")
	public List<CartDto> cartDelete(HttpSession httpSession,@RequestParam(name="commodityId") Integer commodityId) {
		
		// セッション情報（カート内商品リスト）を取り出す
		@SuppressWarnings("unchecked")
		List<CartDto> sessionList = (List<CartDto>)httpSession.getAttribute("orders");
		
		// IDに該当する商品を取り除き、再びセッションに格納
		List<CartDto> newSessionList = sessionService.sessionDeleteId(sessionList, commodityId);
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
	 * @return ID該当商品の数量を変更した後のカート内商品リスト（セッションデータ）
	 */
	@ResponseBody
	@SuppressWarnings("unchecked")
	@PostMapping("/cart/quantity/change")
	public List<CartDto> changeQuantity(@RequestParam("commodityId") Integer commodityId, @RequestParam("quantity") Integer quantity,
			HttpSession httpSession) {
		
		@SuppressWarnings("unchecked")
		List<CartDto> sessionList = (List<CartDto>)httpSession.getAttribute("orders");
		List<CartDto> changeSession = sessionService.sessionQuantities(sessionList, commodityId, quantity);
		httpSession.setAttribute("orders", changeSession);
		
		return changeSession;
	}
	
	/**
	 * カート内 一時保存ボタン押下 → カートテーブルのデータを全削除し、セッションデータをDBに登録
	 * @param httpSession
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/cart/regist")
	public void registCart(HttpSession httpSession) {
		
		cartService.deleteAll();
		
		List<CartDto> sessionList = (List<CartDto>)httpSession.getAttribute("orders");
		cartService.update(sessionList);
	}
	
	/**
	 * 購入ボタン押下 → セッションデータを削除し、DBにも反映
	 * @return カート一覧画面
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@GetMapping("/cart/purchase")
	public List<CartDto> purchaseCart(HttpSession httpSession) {
		
		List<CartDto> emptySession = new ArrayList<CartDto>();
		httpSession.setAttribute("orders", emptySession);
		
		cartService.deleteAll();
		
		return emptySession;
	}
}
