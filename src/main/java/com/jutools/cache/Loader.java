package com.jutools.cache;

@FunctionalInterface
public interface Loader<T> {
	
	public T get(String key);

}
