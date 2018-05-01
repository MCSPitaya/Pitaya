package ch.pitaya.pitaya.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Utils {

	public static <T, U> List<U> map(List<T> list, Function<T, U> f) {
		if (list == null)
			return null;
		List<U> output = new ArrayList<>();
		for (T element : list)
			output.add(f.apply(element));
		return output;
	}

}
