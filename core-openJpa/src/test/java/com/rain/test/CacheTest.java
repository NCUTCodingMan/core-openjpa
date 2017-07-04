package com.rain.test;

import java.lang.reflect.Field;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.openjpa.jdbc.meta.MappingRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rain.entity.Teacher;

public class CacheTest {
	private static String persistenceUnitName;
	private static EntityManagerFactory entityManagerFactory;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		persistenceUnitName = "jpa-demo";
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
	}
	
	@Test
	public void cacheOrNot() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		EntityManager entityManagerAlias = entityManagerFactory.createEntityManager();
		
		EntityTransaction transaction = entityManager.getTransaction();
		
		/*
		transaction.begin();
		
		Teacher teacher = new Teacher();
		teacher.setTeacherName("scott");
		teacher.setTeacherAge(20);
		teacher.setContact("13659841011");
		teacher.setAddress("Wuhna jiangtan");
		
		Grade grade = new Grade();
		grade.setGraderNo(13);
		
		teacher.setGrade(grade);
		
		entityManager.persist(teacher);
		
		transaction.commit();
		*/
		
		Teacher teacher = entityManager.find(Teacher.class, 24);
		
		// 此处查询到的teacher并没有缓存
		Teacher teacherAlias = entityManagerAlias.find(Teacher.class, teacher.getTeacherId() - 1);
		
		if(teacher == teacherAlias){
			System.out.println(":) EntityManager会共享Data Cache中缓存的对象");
		}else{
			System.out.println("EntityManager不会共享Data Cache中缓存的对象  :(");
		}
		
		/**
		 * EntityManager
		 * 	该接口类似于jdbc中的Connection,非线程安全.
		 * 	
		 * Cache基于Data Cache层面提供的API
		 * 	提供
		 * 		(1)判断某对象是否在二级缓存中
		 * 		(2)将某对象以及某个类型的所有对象移除缓存
		 * 	在开启了二级缓存Data Cache的基础上.
		 * 		(1)对象在commit后,会加入到二级缓存中
		 * 		(2)对象在query后,会加入到二级缓存中
		 * 	是否所有的EntityManager会共享Data Cache中缓存的对象
		 * 		(1)分析上面的代码,在查询主键分别为24,23的Teacher时,都会将Teacher对象设置到缓存中(可以查看Cache中的ConcurrentHashMap查看).
		 * 			问题,不同的EntityManager访问主键相同的Teacher时,获取的Teacher对象内存地址并不一样.个人觉得此处是因为事务的原因.
		 * 			EntityManager代表的是从事务隔离这块入手,若A事务可以查看到B事务中某些对象的状态,并共用该状态,这个容易出现问题
		 * 		(2)再分析,当持久化数据发生了更新或者删除时,Data Cache部分的操作流程.默认情况下会移除相关对象
		 * */
		Cache cache = entityManagerFactory.getCache();
		
		boolean flag = cache.contains(Teacher.class, teacher.getTeacherId());
		
		if(flag){
			System.out.println(":) 该对象已加入Data Cache");
		}else{
			System.out.println("该对象未加入Data Cache :( ");
		}
		
		entityManager.close();
		entityManagerAlias.close();
	}
	
	@Test
	public void testCacheStatus() throws Exception{
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		
		Teacher teacher = entityManager.find(Teacher.class, 24);
		
		teacher.setTeacherName("are you delete");
		
		Cache cache = entityManagerFactory.getCache();
		System.out.println(cache.contains(Teacher.class, teacher.getTeacherId()));
		
		entityManager.persist(teacher);
		
		EntityManager entityManagerAlias = entityManagerFactory.createEntityManager();
		
		Teacher teacherAlias = entityManagerAlias.find(Teacher.class, teacher.getTeacherId());
		System.out.println(teacherAlias.toString());
		
		transaction.commit();
		entityManager.close();
		
		System.out.println(cache.contains(Teacher.class, teacher.getTeacherId()));
		
		cache = entityManagerFactory.getCache();
		
		/**
		 * 如何通过反射机制获取父类中申明的属性与方法
		 * */
		Field[] fields = cache.getClass().getDeclaredFields();
		for(Field field : fields){
			System.out.println(field.getName());
			field.setAccessible(true);
			Object obj = field.get(cache);
			if(obj instanceof MappingRepository){
				@SuppressWarnings("rawtypes")
				Class clazz = ((MappingRepository)obj).getClass();			
				Field deleget = clazz.getField("_aliases");
				Object map = deleget.get(obj);
				System.out.println(map);
			}
		}
	}
	
	@AfterClass
	public static void destory(){
		entityManagerFactory.close();
	}
}
