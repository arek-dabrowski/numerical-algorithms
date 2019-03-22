package types;

@SuppressWarnings("serial")
public class MyDouble extends Number implements DefinedOperations<MyDouble>{

    private Double val;

    public MyDouble(int i){

    }
    public MyDouble(Double val) {
        this.val = val;
    }

    public MyDouble() {

    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    @Override
    public MyDouble add(MyDouble a) {
        return new MyDouble(this.getVal() + a.getVal());
    }

    @Override
    public MyDouble sub(MyDouble a) {
        return new MyDouble(this.getVal() - a.getVal());
    }

    @Override
    public MyDouble mul(MyDouble a) {
        return new MyDouble(this.getVal() * a.getVal());
    }

    @Override
    public MyDouble div(MyDouble a) {
        return new MyDouble(this.getVal() / a.getVal());
    }

    @Override
    public boolean lessThan(MyDouble a) {
        if(this.getVal() < a.getVal()) return true;
        else return false;
    }
    
    @Override
    public boolean isZero() {
    	if(this.val == 0.0) return true;
    	else return false;
    }

    @Override
    public MyDouble newInstance() {
        return new MyDouble();
    }

    @Override
    public void changeSign() {
        this.setVal(this.getVal() * -1.0);
    }

    @Override
    public void zero() {
        setVal(0.0);
    }

    @Override
    public MyDouble abs() {
        if(this.val<0) return new MyDouble(this.getVal() * -1.0);
        else return this;
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

    public void setVal(MyDouble val) {
        this.val = val.getVal();
    }
}