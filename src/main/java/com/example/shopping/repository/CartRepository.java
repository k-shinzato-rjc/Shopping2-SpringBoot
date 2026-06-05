package com.example.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shopping.entity.CartEntity;

/**
 * カート情報操作用レポジトリ
 * @author koki_shinzato
 */
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
	
	/**
	 * 全カート情報取得用
	 */
	public List<CartEntity> findAll();
	
	/**
	 * iDに該当したカート情報取得
	 */
	public Optional<CartEntity> findById(Integer commodityId);
	
	/**
	 * カート情報登録用
	 * @param commodityId 検索ID
	 * @return 登録済みデータ
	 */
	public CartEntity save(CartEntity cartEntity);
	
	/**
	 * カート情報削除用
	 * @param commodityId 検索ID
	 * @return 削除済みデータ
	 */
	public void deleteById(Integer commodityId);
	
	/**
	 * カート情報全削除
	 */
	public void deleteAll();
}
