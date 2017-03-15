package ru.rik.cdrload.db;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.crsh.text.RenderPrintWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component 
public class FileParser {
	@Value("${file.delimiter:;}")	private String delimiter;
	@Value("${file.parallel:true}")	private boolean parallel;
	
	@Autowired Results res;
	public FileParser() {	}
	 
	public void parse(Path file, RenderPrintWriter out) throws IOException {
		Stream<String> stream = Files.readAllLines(file).stream();
		if (parallel) stream = stream.parallel();

		stream
	     .map(line -> line.split(delimiter))
	     .filter(a -> a.length > 2)
	     .forEach(a -> res.add(a));
	}
	
	
	public void getFileList(String mask, RenderPrintWriter out) throws IOException {
		Path maskPath = FileSystems.getDefault().getPath(mask);
		Path dir = maskPath.getParent();
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir, maskPath.getFileName().toString());
		for (Path path : stream) {
		    out.println(path.getFileName() );
		}
	}
	
	
	public void processFileList(String mask, RenderPrintWriter out) throws IOException {
		Path maskPath = FileSystems.getDefault().getPath(mask);
		Path dir = maskPath.getParent();
		DirectoryStream<Path> stream = Files.newDirectoryStream(dir, maskPath.getFileName().toString());
		res.init();
		for (Path path : stream) {
			out.println("Processing file " + path.getFileName());
			out.flush();
			parse(path, out);
		}
		out.println("in bdpn: " + res.inBdpn.get() + " wrong route: " + res.wrongRoute);
		out.flush();
	}
}
