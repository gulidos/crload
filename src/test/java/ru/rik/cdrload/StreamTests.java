package ru.rik.cdrload;

import static java.util.stream.Collectors.groupingBy;

import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ru.rik.cdrload.domain.Pair;
import ru.rik.cdrload.stream.PairCollector;

public class StreamTests {
	private String [][] arr ;
	@Before
	public void beforeExec() {
		arr = new String[][] {
				new String[]{"a","b"},
				new String[]{"a","j"},
				new String[]{"a","f"},
				new String[]{"a","z"},
				new String[]{"a","b"},
				new String[]{"f","a"},
				new String[]{"f","j"},
				new String[]{"t","b"},
				new String[]{"t","b"},
				new String[]{"j","a"}
		};
	}
	
	@Test
	public void test() {
		Map<String, int[]> m = Arrays.stream(arr) 
				.map(a -> {
					return new Pair[]{
						new Pair(a[0], a[1], Direction.FROM_ME), 
						new Pair(a[1], a[0], Direction.TO_ME)
					};
				}) 
				.flatMap(pair -> Arrays.stream(pair))
				.collect(
					groupingBy(pair -> pair.getN1(),
							new PairCollector())
				);
				
		m.entrySet().stream().forEach(e -> {
			System.out.print(e.getKey() + " -> ");
			for (int i: e.getValue()) 
				System.out.print(i + " ");
			System.out.println(" ");
		});

	}
	

	public enum Direction {FROM_ME, TO_ME};

}
