package ru.rik.cdrload.cmd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.crsh.cli.Argument;
import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Required;
import org.crsh.cli.Usage;
import org.crsh.command.BaseCommand;

import ru.rik.cdrload.db.RouteRepo;
import ru.rik.cdrload.db.RouteRepoImpl;
import ru.rik.cdrload.domain.Route;

public class file  extends BaseCommand{

	@Usage("show the current time")
	@Command
	public Object main(@Usage("the time format") @Required @Argument(name = "arg1") String arg,
			@Option(names = { "f", "format" }) String format) {
		if (format == null)
			format = "EEE MMM d HH:mm:ss z yyyy";
		out.println("format: " + format, org.crsh.text.Color.red);
		out.println("arg: " + arg, org.crsh.text.Color.red);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		out.println("hello 2", org.crsh.text.Color.red);
		return formatter.format(date);
	}

	@Command
	public void sub1() {
		out.println("sub1", org.crsh.text.Color.red);
		RouteRepo r = (RouteRepoImpl) RouteRepoImpl.context.getBean("routeRepoImpl");
		if (r == null)
			out.println("routes are null", org.crsh.text.Color.red);
		Map<Long, Route> map = r.getAll();
		for (Long k : map.keySet()) {
			out.println(k + " " + map.get(k), org.crsh.text.Color.yellow);
		}
	}
	
	@Command
	public void parse(@Required @Argument(name = "arg1") String filename) {
		out.println("Opening file " + filename, org.crsh.text.Color.red);
		RouteRepoImpl.context.getBean("fileParser");
		
	
	}

}
