package edu.kit.informatik.ragnarok.util;

public class Tuple<T, U> {
	private final T t;
	private final U u;

	public static final <V, W> Tuple<V, W> create(V v, W w) {
		return new Tuple<>(v, w);
	}

	public Tuple(T t, U u) {
		this.t = t;
		this.u = u;
	}

	public T getT() {
		return this.t;
	}

	public U getU() {
		return this.u;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.t == null) ? 0 : this.t.hashCode());
		result = prime * result + ((this.u == null) ? 0 : this.u.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		Tuple other = (Tuple) obj;
		if (this.t == null && other.t != null) {
			return false;
		} else if (!this.t.equals(other.t)) {
			return false;
		}
		if (this.u == null && other.u != null) {
			return false;
		} else if (!this.u.equals(other.u)) {
			return false;
		}
		return true;
	}

}
