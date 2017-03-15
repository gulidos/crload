package ru.rik.cdrload.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ru.rik.cdrload.domain.Numbers;
import ru.rik.cdrload.domain.Oper;
import ru.rik.cdrload.domain.Result;
import ru.rik.cdrload.domain.Route;

@Component
public class Results {
	private ConcurrentMap<String, Numbers> map;
	@Autowired private RouteRepo routes;
	private final Pattern mob = Pattern.compile("^9\\d{9}$");
	public AtomicInteger inBdpn;
	public AtomicInteger wrongRoute;
	
	public Results() {	}
	
	public ConcurrentMap<String, Numbers> getSrcMap() {
		return map; 
	}

	
	public void init() {
		map = new ConcurrentHashMap<>();
		inBdpn = new AtomicInteger(0);
		wrongRoute = new AtomicInteger(0);
	}

	@Value("${file.a_field:2}")	public int a;
	@Value("${file.b_field:3}")	public int b;
	@Value("${file.day_pos:5}")	public int dayPos;
	@Value("${repo.exclude_bdpn:true}")	private boolean excludeBdpn;
	
	public void add(String[] arr) { 
		String day = arr[0].substring(5, 10);
		
		if (isGoodToAddtoWl(arr[a])) {
			Numbers nums = map.get(day + normalize(arr[a]));
			if (nums == null) {
				nums = new Numbers();
				nums.addDst(arr[b]); // from me
				Numbers oldNums = map.putIfAbsent(day + normalize(arr[a]), nums);
				if (oldNums != null)
					oldNums.addDst(arr[b]);
			} else 
				nums.addDst(arr[b]);
		}
		
		if (isGoodToAddtoWl(arr[b])) {
			Numbers nums = map.get(day + normalize(arr[b]));
			if (nums == null) {
				nums = new Numbers();
				nums.addSrc(arr[a]);
				Numbers oldNums = map.putIfAbsent(day + normalize(arr[b]), nums);
				if (oldNums != null)
					oldNums.addSrc(arr[a]);
			} else 
				nums.addSrc(arr[a]);
		}
	}

	
	public void loadToFile(String filename) throws IOException {
		try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(filename)))) {
			map.entrySet().stream()
			.map(e -> getResult(e))
			.forEach(r -> pw.println(r.toCsv()));
		}
	}
	
	
	private Result getResult(Entry<String, Numbers> e) {
		Result r = new Result(e.getKey().substring(5));
		r.day = e.getKey().substring(0, 5);
		r.dstNum = e.getValue().getDsts().size();
		r.dstDistinct = new HashSet<>(e.getValue().getDsts()).size();
		r.srcNum =  e.getValue().getSrcs().size();
		r.srcDistinct = new HashSet<>(e.getValue().getSrcs()).size();
		return r;
	}
	
	
	public String normalize(String n) {
		if (n.length() == 11 && (n.startsWith("89") || n.startsWith("79"))) 
			return n.substring(1);
		return n;
	}
	
	
	public boolean isMobile(String n) {
		if (mob.matcher(n).find()) return true;		
		return false;		
	}
	
	
	private boolean isGoodToAddtoWl(String num) {
		String n = normalize(num);
		if (!isMobile(n)) return false;
		
		if (excludeBdpn && routes.inBdpn(n)) {
			inBdpn.incrementAndGet();
			return false;
		}
		
		Route r = routes.getRoute(Long.decode(n));
		if (r.getOper() == Oper.UNKNOWN) {
			wrongRoute.incrementAndGet();
			return false;
		}
		
		return true;
	}
}
