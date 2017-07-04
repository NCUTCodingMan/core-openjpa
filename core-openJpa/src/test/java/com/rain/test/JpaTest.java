package com.rain.test;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rain.entity.Grade;
import com.rain.entity.Teacher;

public class JpaTest {
	private static String persistenceUnitName;
	private static EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		persistenceUnitName = "jpa-demo";
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
	}
	
	@Test
	public void testPersist() throws Exception {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();

		transaction.begin();

		Grade grade = new Grade();
		grade.setGradeName("senior III");

		Teacher teacher = new Teacher();

		teacher.setTeacherName("smi");
		teacher.setTeacherAge(49);
		teacher.setContact("13659851048");
		teacher.setAddress("北京市石景山区晋元庄路5号北方工业大学");
		teacher.setGrade(grade);

		List<Teacher> teachers = new ArrayList<>();
		teachers.add(teacher);

		grade.setTeachers(teachers);

		entityManager.persist(grade);

		transaction.commit();

		entityManager.close();
	}
	
	@Test
	public void testQuery() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		Grade grade = entityManager.find(Grade.class, 14);
		
		List<Teacher> teachers = grade.getTeachers();
		
		for(Teacher teacher : teachers){
			System.out.println(teacher.getTeacherName());
		}
		
		/**
		 * 缓存的触发时机是什么
		 * */
		Cache cache= entityManagerFactory.getCache();
		
		if(cache.contains(Grade.class, 15)){
			System.out.println("该元素已缓存...");
		}else{
			System.out.println("元素未被缓存");
		}
		
		System.out.println(grade);
		
		grade = entityManager.find(Grade.class, 15);
	}
	
	@Test
	public void testRemove() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		Grade grade = entityManager.find(Grade.class, 4);
		
		entityManager.remove(grade);
		
		transaction.commit();

		entityManager.close();
	}
	
	@Test
	public void testGet() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		
		transaction.begin();
		
		Grade grade = entityManager.find(Grade.class, 5);
		
		grade.setGradeName("senior 4");
		
		System.out.println(grade.getGradeName());
		
		transaction.commit();
		
		entityManager.close();
	}
	
	@AfterClass
	public static void destory(){
		entityManagerFactory.close();
	}
	
	/**
	 * 外键约束
	 * 事务的开启与提交,这一点与mybatis不同.手动开启事务,提交事务.切记
	 * 
	 * 下面两个被管理的引用,返回的是同一个实例
	 * */
	@Test
	public void testCache() throws Exception{
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Teacher teacher = new Teacher();
		teacher.setTeacherName("scott");
		teacher.setTeacherAge(20);
		teacher.setContact("13659841011");
		teacher.setAddress("Beijing jingyuanzhuang");
		
		Grade grade = new Grade();
		grade.setGraderNo(13);
		
		teacher.setGrade(grade);
		
		entityManager.persist(teacher);
		
		transaction.commit();
		
		/**
		 *  没有执行查询语句(即并没有访问数据库,直接从内存中读取对象信息),似乎有某种机制,直接获取的对象信息.
		 *  在一个持久化上下文中,对于任何特定的数据库表记录行,都只有一个对象的实例.然而,同一个实体可以有其他
		 *  实体管理器对其操作,此时需要使用乐观锁或者悲观锁来保证相关操作的正确性
		 *  
		 *  下面的代码简单模拟在不同的EntityManager中持有同一个数据库表记录行,从而导致数据的不一致性.
		 */
		Teacher alias = entityManager.find(Teacher.class, teacher.getTeacherId());
		
		System.out.println(teacher == alias);
		
		// 声明另外一个EntityManager
		EntityManager entityManagerAlias = entityManagerFactory.createEntityManager();
		transaction = entityManagerAlias.getTransaction();
		transaction.begin();
		Teacher copy = entityManagerAlias.find(Teacher.class, teacher.getTeacherId());
		copy.setTeacherName("root");
		transaction.commit();
		
		alias = entityManager.find(Teacher.class, teacher.getTeacherId());
		System.out.println(teacher == alias ? teacher : alias);
		
		entityManager.close();
	}
}