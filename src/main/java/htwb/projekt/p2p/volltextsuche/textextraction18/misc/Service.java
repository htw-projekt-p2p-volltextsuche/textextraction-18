package htwb.projekt.p2p.volltextsuche.textextraction18.misc;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import htwb.projekt.p2p.volltextsuche.textextraction18.model.Speach;
import htwb.projekt.p2p.volltextsuche.textextraction18.util.HibernateUtil;

public class Service {

	Session session;
	private ObjectMapper mapper;

	public Service() {
		session = HibernateUtil.getSessionFactory().openSession();

		mapper = new ObjectMapper();
		mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	}

	public UUID save(Speach speach) {
		Transaction transaction = session.beginTransaction();
		session.persist(speach);
		transaction.commit();
		return speach.getId();
	}

	//TODO change return as JSONArray
	public String getAll() {
		Transaction transaction = session.beginTransaction();
		List<Speach> list = session.createQuery("from Speach", Speach.class).list();
		StringBuilder sb = new StringBuilder();
		sb.append("[\n");
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				sb.append(list.get(i)).append("\n]");
			} else {
				sb.append(list.get(i)).append(",").append(System.lineSeparator());
			}
		}
		transaction.commit();
		return sb.toString();
	}

}
