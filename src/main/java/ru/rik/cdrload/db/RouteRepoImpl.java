package ru.rik.cdrload.db;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import ru.rik.cdrload.domain.Oper;
import ru.rik.cdrload.domain.Route;


@Repository
public class RouteRepoImpl implements RouteRepo, ApplicationContextAware {
	static final Logger logger = LoggerFactory.getLogger(RouteRepoImpl.class);
	static final String query = "select * from numberplan";
	static final String bdpn = "select * from bdpn";  
	
	private volatile TreeMap<Long, Route> map = new TreeMap<>();
	private Map<String, Oper> bdpnMap = new HashMap<>();
	
	@Autowired DataSource dataSource;
	
	public RouteRepoImpl() {}
	
	

	@Override
	public int loadRoutes() {
		try (Connection con = dataSource.getConnection(); 
				PreparedStatement stmtRoutes = con.prepareStatement(query);
				PreparedStatement stmtBdpn = con.prepareStatement(bdpn)) {
			
			TreeMap<Long, Route> newmap = new TreeMap<>();
			ResultSet rs = stmtRoutes.executeQuery();
			while (rs.next()) {
				Route r = Route.builder()
						.fromd(rs.getLong("fromd"))
						.tod(rs.getLong("tod"))
						.oper(Oper.findByMnc(rs.getInt("mnc")))
						.regcode(rs.getInt("regcode"))
						.build();
				newmap.put(r.getFromd(), r);
				map = newmap;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return map.size();
	}	
	
	
	@Override
	public int loadBdpn() {
		try (Connection con = dataSource.getConnection(); 
				PreparedStatement stmtBdpn = con.prepareStatement(bdpn)) {
			Map<String, Oper> newBdpnMap = new HashMap<>();
			long start = System.currentTimeMillis();
			ResultSet rs = stmtBdpn.executeQuery();
			while (rs.next()) {
				newBdpnMap.put(rs.getString("number"), Oper.findByMnc(rs.getInt("mnc")));
			}
			
			long duration = System.currentTimeMillis() - start;
			bdpnMap = newBdpnMap;
			System.out.println("Load duration of bdpn:" + duration + "ms, cache size:" + bdpnMap.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return bdpnMap.size();
	}	
	
	
	@Override
    public Route getRoute(Long num) {
        Entry<Long, Route> closestEntry = map.floorEntry(num);
        if (closestEntry != null) {
        	Route r = closestEntry.getValue();
            if (r != null && r.isIn(num)) 
                return r;
        }
        return Route.NULL_ROUTE;
    }
	
	@Override
	public Map<Long, Route> getAll() {
		return map;
	}
	@Override
	public Map<String, Oper> getBdpnMap() {
		return bdpnMap;
	}



	@Override
	public Oper getRealOper(String num) {
		return bdpnMap.get(num);		
	}
	
	@Override
	public boolean inBdpn(String n) {
		Oper o = getRealOper(n);
		return o != null;
	}

	@Override
	public void dumpBdpn() {
		try (FileOutputStream fos = new FileOutputStream("/tmp/bdpnMap.ser");
				ObjectOutputStream oos = new ObjectOutputStream(fos);) {
			oos.writeObject(bdpnMap);
			System.out.printf("Serialized HashMap data is saved in hashmap.ser");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public void loadBdpnSer() {
		try (FileInputStream fis = new FileInputStream("/tmp/bdpnMap.ser");
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));) {
			Map<String, Oper> newBdpnMap = new HashMap<>();
			newBdpnMap = (HashMap<String, Oper>) ois.readObject();
			bdpnMap = newBdpnMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	} 
}
