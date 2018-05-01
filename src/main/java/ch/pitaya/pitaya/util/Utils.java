package ch.pitaya.pitaya.util;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

	public static <T, U> List<U> map(List<T> list, Function<T, U> f) {
		if (list == null)
			return null;
		List<U> output = new ArrayList<>();
		for (T element : list)
			output.add(f.apply(element));
		return output;
	}

	public static <T> List<T> paginate(List<T> list, int start, int size) {
		return paginate(list, start, size, t -> true);
	}

	public static <T> List<T> paginate(List<T> list, int start, int size, Predicate<T> filter) {
		Stream<T> stream = list.stream().filter(filter).skip(start);
		if (size > 0)
			stream = stream.limit(size);
		return stream.collect(Collectors.toList());
	}

	public static <T, F> F ifNotNull(T t, Function<T, F> f) {
		if (t == null)
			return null;
		return f.apply(t);
	}

}
