package com.example.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shopping.entity.MenuEntity;

/**
 * メニュー情報取得用レポジトリ
 * @author koki_shinzato
 */
@Repository
public interface MenuRepository extends JpaRepository<MenuEntity, Integer> {
	
	/**
	 * 全メニュー情報を取得
	 */
	public List<MenuEntity> findAll();
	
	/**
	 * IDに該当したメニュー情報を取得
	 */
	public Optional<MenuEntity> findById(Integer id);
}
