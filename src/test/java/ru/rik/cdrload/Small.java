package ru.rik.cdrload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

import ru.rik.cdrload.db.Results;

public class Small {

	@Test
	public void test() {
		Results r = new Results();
		Assert.assertTrue(r.isMobile("9257665544"));
		Assert.assertTrue(r.isMobile("9000000000"));
		
		Assert.assertFalse(r.isMobile("92000000000"));
		Assert.assertFalse(r.isMobile("2000000000"));
		Assert.assertFalse(r.isMobile("90f0000000"));
	}
	
	@Test
	public void testNorma() {
		Results r = new Results();
		Assert.assertEquals(r.normalize("89031113322"),"9031113322" );
		Assert.assertEquals(r.normalize("9031113322"),"9031113322" );
		Assert.assertEquals(r.normalize("84951113322"),"84951113322" );
	}
	
	@Test
	public void fileWalk() throws IOException {
		Files.walk(Paths.get("/Users/gsv/Documents/doc"))
		.parallel()
		.filter(p -> p.getFileName().toString().endsWith("csv"))
		.forEach(p -> System.out.println( p.getFileName()));
	}

}
