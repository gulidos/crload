
import java.text.SimpleDateFormat;

import org.crsh.cli.Argument;
import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Required;
import org.crsh.cli.Usage;

import ru.rik.cdrload.db.RouteRepo;
import ru.rik.cdrload.db.RouteRepoImpl;

class repo {

	@Usage("show the current time")
	@Command
	public Object main(@Usage("the time format") @Required @Argument(name = "arg1") String arg,
			@Option(names = [ "f", "format" ]) String format) {
		if (format == null)
			format = "EEE MMM d HH:mm:ss z yyyy";
		out.println("format: " + format, org.crsh.text.Color.red);
		out.println("arg: " + arg, org.crsh.text.Color.red);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		out.println("hello 2", org.crsh.text.Color.red);
		return formatter.format(date);
	}

//	@Command
//	public void sub1() {
//		out.println("sub1", org.crsh.text.Color.red);
//		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
//		if (r == null)
//			out.println("routes are null", org.crsh.text.Color.red);
//		Map<Long, Route> map = r.getAll();
//		for (Long k : map.keySet()) {
//			out.println(k + " " + map.get(k), org.crsh.text.Color.yellow);
//		}
//	}
//
//	@Command
//	public void sub2() {
//		out.println("sub2", org.crsh.text.Color.red);
//		context.getAttributes().get("routeRepoImpl");
//		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
//		if (r == null)
//			out.println("routes are null", org.crsh.text.Color.red);
//	}

	@Command
	public void loadRoutes() {
		long start = System.currentTimeMillis();
		out.println("Loading routes...", org.crsh.text.Color.red);
		out.flush();

		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
		if (r == null) {
			out.println("routes are null", org.crsh.text.Color.red);
			return;
		}

		int count = r.loadRoutes();
		long duration = System.currentTimeMillis() - start;
		out.println("Loaded " + count + " in " + duration + " milli seconds", org.crsh.text.Color.red);
		out.flush();
	}
	
	@Command
	public void loadBdpn() {
		long start = System.currentTimeMillis();
		out.println("Loading bdpn...", org.crsh.text.Color.red);
		out.flush();

		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
		if (r == null) {
			out.println("routes are null", org.crsh.text.Color.red);
			return;
		}

		int count = r.loadBdpn();
		long duration = System.currentTimeMillis() - start;
		out.println("Loaded " + count + " in " + duration + " milli seconds", org.crsh.text.Color.red);
		out.flush();
	}
	
	@Command
	public void dumpBdpn() {
		long start = System.currentTimeMillis();
		out.println("Dump bdpn...", org.crsh.text.Color.red);
		out.flush();

		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
		if (r == null) {
			out.println("routes are null", org.crsh.text.Color.red);
			return;
		}

		r.dumpBdpn();
		long duration = System.currentTimeMillis() - start;
		out.println("Dumped " + duration + " milli seconds", org.crsh.text.Color.red);
		out.flush();
	}
	
	@Command
	public void loadAll() {
		loadRoutes();
		loadBdpnSer();
	}
	
	@Command
	public void loadBdpnSer() {
		long start = System.currentTimeMillis();
		out.println("Load Dump of bdpn...", org.crsh.text.Color.red);
		out.flush();

		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
		if (r == null) {
			out.println("routes are null", org.crsh.text.Color.red);
			return;
		}

		r.loadBdpnSer();
		long duration = System.currentTimeMillis() - start;
		out.println("Dump restored "  + duration + " milli seconds", org.crsh.text.Color.red);
		out.flush();
	}
	
	@Command
	public void stat() {
		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
		
		out.println("Routes: " + r.getAll().size() + " BDPNs: " + r.getBdpnMap().size(), org.crsh.text.Color.red);
		out.flush();
	}
}