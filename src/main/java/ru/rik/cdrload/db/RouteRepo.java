package ru.rik.cdrload.db;

import java.util.Map;

import ru.rik.cdrload.domain.Oper;
import ru.rik.cdrload.domain.Route;

public interface RouteRepo {
	public int loadRoutes( );
	public Route getRoute(Long num);
	public Map<Long, Route> getAll();
	Oper getRealOper(String num);
	void dumpBdpn();
	int loadBdpn();
	void loadBdpnSer();
	Map<String, Oper> getBdpnMap();
	boolean inBdpn(String n);
}