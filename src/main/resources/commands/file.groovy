import java.text.SimpleDateFormat;

import org.crsh.cli.Argument;
import org.crsh.cli.Command;
import org.crsh.cli.Option;
import org.crsh.cli.Required;
import org.crsh.cli.Usage;

import ru.rik.cdrload.db.FileParser;
import ru.rik.cdrload.db.Results
import ru.rik.cdrload.db.RouteRepo;
import ru.rik.cdrload.db.RouteRepoImpl;
import ru.rik.cdrload.domain.Route;

class file {

//	@Usage("show the current time ")
//	@Command
//	public Object main(@Usage("the time format") @Required @Argument(name = "arg1") String arg,
//			@Option(names = [ "f", "format" ]) String format) {
//		if (format == null)
//			format = "EEE MMM d HH:mm:ss z yyyy";
//		out.println("format: " + format, org.crsh.text.Color.red);
//		out.println("arg: " + arg, org.crsh.text.Color.red);
//		Date date = new Date();
//		SimpleDateFormat formatter = new SimpleDateFormat(format);
//		out.println("hello 2", org.crsh.text.Color.red);
//		return formatter.format(date);
//	}


//	
//	@Command
//	public void parse(@Required @Argument(name = "arg1") String filename) {
//		long start = System.currentTimeMillis();
//		out.println("Opening file " + filename, org.crsh.text.Color.red);
//		FileParser p = (FileParser) RouteRepoImpl.context.getBean("fileParser");
//		try {
//			p.parse(out, filename);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		long duration = System.currentTimeMillis() - start;
//		out.println("Loaded " + " in " + duration + " milli seconds", org.crsh.text.Color.red);
//		out.flush();
//	}
	
	@Command
	public void count() {
		Results r = (Results) RouteRepoImpl.context.getBean("results");
		try {
			out.println("srcMap: " + r.getSrcMap().size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Command
	public void saveToFile(@Required @Argument(name = "arg1") String filename) {
		long start = System.currentTimeMillis();
		out.println("Opening file " + filename, org.crsh.text.Color.red);
		out.flush();
		Results r = (Results) RouteRepoImpl.context.getBean("results");
		try {
			r.loadToFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		long duration = System.currentTimeMillis() - start;
		out.println("Loaded " + " in " + duration + " milli seconds", org.crsh.text.Color.red);
		out.flush();
	}

	@Command
	public void fileLst(@Required @Argument(name = "arg1") String mask) {
		FileParser p = (FileParser) RouteRepoImpl.context.getBean("fileParser");
		p.getFileList(mask, out);
	}
	
	@Command
	public void processFiles(@Required @Argument(name = "arg1") String mask) {
		FileParser p = (FileParser) RouteRepoImpl.context.getBean("fileParser");
		p.processFileList(mask, out);
	}
	
}
