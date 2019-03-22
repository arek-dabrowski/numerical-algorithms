package types;

public interface DefinedOperations<T> {
	
	T add(T a);
	
	T sub(T a);
	
	T mul(T a);
	
	T div(T a);

	void changeSign();
	
	void zero();

	void abs();
	
	boolean lessThan(T a);

	T newInstance();

}
