package it.polito.tdp.yelp.model;

public class Migliori {

	Review r;
	int archi;
	@Override
	public String toString() {
		return "Migliori [r=" + r.toString() + ", archi=" + archi + "]";
	}
	public Migliori(Review r, int archi) {
		super();
		this.r = r;
		this.archi = archi;
	}
	public Review getR() {
		return r;
	}
	public void setR(Review r) {
		this.r = r;
	}
	public int getArchi() {
		return archi;
	}
	public void setArchi(int archi) {
		this.archi = archi;
	}
}
