package com.ecommerce.bikes.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.bikes.entity.ProductDAO;
import com.ecommerce.bikes.repository.ProductRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service("ProductService")
public class ProductService {

	@Autowired
	ProductRepository productRepository;

	@PersistenceContext
	private EntityManager entityManager;


	public ProductDAO findById(Long id) throws NoSuchElementException {
		return productRepository.findById(id).get();
	}



	public List<ProductDAO> findAllProductsByName(String name) throws NoSuchElementException {
		return productRepository.findByNameContainingIgnoreCase(name);
	}

	public List<ProductDAO> findAll() {
		return productRepository.findAll();
	}

	@Transactional
	public int insertLike(int productId, int userId) {
		int result = entityManager.createNativeQuery("INSERT INTO likes (product_id, user_id) VALUES (?, ?)")
				.setParameter(1, productId).setParameter(2, userId).executeUpdate();

		return result;
	}

	@Transactional
	public Object getLike(int productId, int userId) throws NoResultException {
		Object result = entityManager.createNativeQuery("SELECT * FROM likes WHERE product_id=? AND user_id=?")
				.setParameter(1, productId).setParameter(2, userId).getSingleResult();

		return result;
	}

	@Transactional
	public int deleteLike(int productId, int userId) {
		int result = entityManager.createNativeQuery("DELETE FROM likes WHERE product_id=? AND user_id=?")
				.setParameter(1, productId).setParameter(2, userId).executeUpdate();

		return result;
	}
}
