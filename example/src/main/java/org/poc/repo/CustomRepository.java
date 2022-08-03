package org.poc.repo;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.naming.spi.DirStateFactory.Result;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.From;
import javax.transaction.Transactional;
import javax.xml.crypto.KeySelectorResult;

import org.hibernate.sql.Select;
import org.jvnet.hk2.annotations.Service;
import org.poc.exception.ItemAlreadyExists;
import org.poc.exception.UserAlreadyExists;
import org.poc.model.Item;
import org.poc.model.User;
import org.springframework.stereotype.Repository;

import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;

@Service
public class CustomRepository {  
	
	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("test");
	EntityManager entityManager = entityManagerFactory.createEntityManager();
	
	
	private static CustomRepository single_instance = null;
	
	
	@Transactional
	public User saveUser(User user) throws UserAlreadyExists{
		
		List<User> userExist = getbyUserId(user.getUserId());
		
		if(Objects.nonNull(userExist)) {
			throw new UserAlreadyExists(Response.status(400).entity("UserID already Exist").build() );
		}
		if(entityManager.getTransaction().isActive()) {
			entityManager.persist(user);
			entityManager.getTransaction().commit();
		}else {
			entityManager.getTransaction().begin();	
			entityManager.persist(user);
			entityManager.getTransaction().commit();
		}
		return user;
	}
	
	@Transactional
	public User saveAdminUser(User user) {
		entityManager.getTransaction().begin();
		entityManager.persist(user);
		entityManager.getTransaction().commit();
		
		return user;
	}
	
	public List<User> getbyName(String name)
	{
		 Query q = entityManager.createQuery("select u from User u where u.name=:name", User.class);
		 q.setParameter("name", name);
		 
		 List<User> result =  q.getResultList();
		 System.out.println("RESULT "+result);
		 if(result.isEmpty()) {
			 return null;
		 }
		 return result;
	}
	
	public List<User> getbyUserId(Integer userId)
	{
		
		 Query q = entityManager.createQuery("select u from User u where u.userId=:userId", User.class);
		 q.setParameter("userId", userId);
		 
		 List<User> result =  q.getResultList();
		 
		 if(result.isEmpty()) {
			 return null;
		 }
		 return result;
	}
	
	public List<User> getAllUsers(){
		List<User> userList = new ArrayList<User>();
		Query q = entityManager.createQuery("select u from User u", User.class);
		return  q.getResultList();
	}
	
    public static CustomRepository getInstance()
    {
        if (single_instance == null)
            single_instance = new CustomRepository();  
        return single_instance;
    }
    
	@Transactional
	public Item saveItem(Item item) {
		
		System.out.println("REached Transaction");
		List<Item> result = getItemById(item.getItemId());
		
		if(Objects.nonNull(result)){
			throw new ItemAlreadyExists(Response.status(400).entity("Item ID Already Exists").build());
		}
		
		if(entityManager.getTransaction().isActive()) {
			entityManager.persist(item);
			entityManager.getTransaction().commit();
		}else {
			entityManager.getTransaction().begin();	
			entityManager.persist(item);
			entityManager.getTransaction().commit();
		}
		return item;
	}
	
	public List<Item> getAllItems(){
		List<Item> itemList = new ArrayList<Item>();
		Query q = entityManager.createQuery("select i from Item i", Item.class);
		return  q.getResultList();
	}

	public List<Item> getItemById(String itemId) {
		// TODO Auto-generated method stub
		 Query q = entityManager.createQuery("select i from Item i where i.itemId=:itemId", Item.class);
		 q.setParameter("itemId", itemId);
		 
		 List<Item> result =   q.getResultList();
		 
		if(result.isEmpty()) {
			return null;
		}
		 return result;
	}
	
	@Transactional
	public Item updateItem(Item item) {
		
		if(entityManager.getTransaction().isActive()) {
			Query query = entityManager.createQuery("update Item i set i.name =:name, i.description=:description,i.price=:price where id=:itemId");
			query.setParameter("name", item.getName());
			query.setParameter("description", item.getDescription());
			query.setParameter("price", item.getPrice());
			query.setParameter("itemId", item.getItemId());
			int rowUpdate = query.executeUpdate();
			entityManager.getTransaction().commit();
		}else {
			entityManager.getTransaction().begin();	
			Query query = entityManager.createQuery("update Item i set i.name =:name, i.description=:description,i.price=:price where id=:itemId");
			query.setParameter("name", item.getName());
			query.setParameter("description", item.getDescription());
			query.setParameter("price", item.getPrice());
			query.setParameter("itemId", item.getItemId());
			int rowUpdate = query.executeUpdate();
			entityManager.getTransaction().commit();
		}
		return item;
	}

	public Item deleteItem(String id) {
		// TODO Auto-generated method stub
		Item item = new Item();
		if(entityManager.getTransaction().isActive()) {
			item = entityManager.find(Item.class, id);
			entityManager.remove(item);
			entityManager.getTransaction().commit();
		}else {
			entityManager.getTransaction().begin();
			item = entityManager.find(Item.class, id);
			entityManager.remove(item);
			entityManager.getTransaction().commit();
		}
		return item;
	}

}
