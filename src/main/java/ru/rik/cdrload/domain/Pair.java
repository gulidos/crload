package ru.rik.cdrload.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rik.cdrload.StreamTests.Direction;

@Data 
@AllArgsConstructor
public class Pair {
	String n1; 
	String n2;
	Direction d;
}
