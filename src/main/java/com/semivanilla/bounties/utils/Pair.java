package com.semivanilla.bounties.utils;

public class Pair<A, B> {
	private A value0;
	private B value1;

	public Pair(final A v0, B v1) {
		this.value0 = v0;
		this.value1 = v1;
	}

	/**
	 * gets the first value
	 *
	 * @return first value
	 */
	public A getValue0() {
		return value0;
	}

	/**
	 * gets the second value
	 *
	 * @return second value
	 */
	public B getValue1() {
		return value1;
	}
}
