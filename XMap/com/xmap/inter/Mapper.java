package com.xmap.inter;

import java.util.List;


public interface Mapper {
	public abstract List getAll();
	public abstract Object get(int index);
	public abstract void assemble();
	public abstract void add(Object o);
	public abstract void addAll(List l);
	public abstract void close();
}
