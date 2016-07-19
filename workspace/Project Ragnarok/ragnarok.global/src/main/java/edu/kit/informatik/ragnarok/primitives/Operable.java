package edu.kit.informatik.ragnarok.primitives;

public interface Operable<T> {
	
	public T scalar(float scalar);
	
	public T multiply(T other);
	
	public T add(T other);
	
	public T sub(T other);
	
}
