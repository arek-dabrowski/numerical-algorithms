package types;

public class MyFloat extends Number implements DefinedOperations<MyFloat>{
	private Float val;
	
	public MyFloat(Float val){
		this.val = val;
	}

	public MyFloat() {

	}

	private Float getVal() {
		return val;
	}
	
	public void setVal(Float val) {
		this.val = val;
	}
	
	@Override
	public MyFloat add(MyFloat a) {
		return new MyFloat(this.getVal() + a.getVal());
	}

	@Override
	public MyFloat sub(MyFloat a) {
		return new MyFloat(this.getVal() - a.getVal());
	}

	@Override
	public MyFloat mul(MyFloat a) {
		return new MyFloat(this.getVal() * a.getVal());
	}

	@Override
	public MyFloat div(MyFloat a) {
		return new MyFloat(this.getVal() / a.getVal());
	}

	@Override
	public boolean lessThan(MyFloat a) {
		if(this.getVal() < a.getVal()) return true;
		else return false;
	}

	@Override
	public void changeSign() {
		this.setVal(this.getVal() * (float)-1.0);
	}

	public MyFloat newInstance(){
		return new MyFloat();
	}

	@Override
	public void zero() {
		setVal((float)0.0);
	}

	public void abs() {
		if(this.val<0)
			this.setVal(this.getVal() * (float)-1.0);
	}
	
	@Override
	public String toString() {
		return val.toString();
	}
	
	@Override
	public double doubleValue() {
		return val.doubleValue();
	}

	@Override
	public float floatValue() {
		return val.floatValue();
	}

	@Override
	public int intValue() {
		return val.intValue();
	}

	@Override
	public long longValue() {
		return val.longValue();
	}

}
