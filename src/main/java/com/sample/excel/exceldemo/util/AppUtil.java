package com.sample.excel.exceldemo.util;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

@Component
public class AppUtil {

	
	public static <T> Optional<T> resolve(Supplier<T> resolver) {
	    try {
	        T result = resolver.get();
	        return Optional.ofNullable(result);
	    }
	    catch (NullPointerException e) {
	        return Optional.empty();
	    }
	}
}
