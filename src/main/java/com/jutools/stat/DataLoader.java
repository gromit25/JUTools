package com.jutools.stat;

@FunctionalInterface
public interface DataLoader {
	
	public void load(Statistic stat);
}
