package ch.pitaya.pitaya.service;

import java.io.InputStream;
import java.sql.Blob;

import javax.persistence.EntityManagerFactory;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {
	
	private SessionFactory sessionFactory;
	
	@Autowired
	public FileService(EntityManagerFactory factory) {
		if(factory.unwrap(SessionFactory.class) == null){
			throw new NullPointerException("factory is not a hibernate factory");
		}
		this.sessionFactory = factory.unwrap(SessionFactory.class);
	}
	
	public Blob createBlob(InputStream content, long size) {
		try {
		    sessionFactory.getCurrentSession();
		    return (Blob) sessionFactory.getCurrentSession().getLobHelper().createBlob(content, size);
		} catch (HibernateException e) {
		    sessionFactory.openSession();
		    return (Blob) sessionFactory.openSession().getLobHelper().createBlob(content, size);
		}
	}

}
