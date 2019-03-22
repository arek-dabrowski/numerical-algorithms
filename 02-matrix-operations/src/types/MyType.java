package types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class MyType extends Number implements DefinedOperations<MyType>{
	private BigInteger num;
	private BigInteger denom;
	private String stringValue;
	private MathContext mc = new MathContext(40, RoundingMode.HALF_DOWN);

	public MyType (double value){
		stringValue = String.valueOf(value);
		convertToFraction(stringValue);
	}
	
	public MyType (BigInteger num, BigInteger denom) {
		this.num = num;
		if(denom != BigInteger.ZERO )this.denom = denom;
		else this.denom = BigInteger.ONE;
		simplifyFraction();
	}

	public MyType(){

	}
	
	private void convertToFraction(String value) {
		BigDecimal valueBD = new BigDecimal(value);
		int numberofDecimalDigits = value.length() - 1 - value.indexOf('.');
		denom = new BigInteger(String.valueOf(1));
		for(int i = 0; i < numberofDecimalDigits; i++) {
			valueBD = valueBD.multiply(BigDecimal.TEN);
			denom = denom.multiply(BigInteger.TEN);
		}
        num = new BigInteger(trimDecimal(String.valueOf(valueBD)));
        
        simplifyFraction();
	}
	
	private String trimDecimal(String stringToTrim) {
		return stringToTrim.substring(0,stringToTrim.indexOf('.'));
	}
	
/*	Simplifying methods */
	
    private void simplifyFraction() {
    	checkSigns();
        BigInteger greatestCommonFactor = greatestCommonFactor(num.abs(),denom.abs());
        this.num = num.divide(greatestCommonFactor);
        this.denom = denom.divide(greatestCommonFactor);
    }

    private BigInteger greatestCommonFactor(BigInteger numerator, BigInteger denominator){
        if(denominator.equals(BigInteger.valueOf(0))){
            return numerator;
        }
        return greatestCommonFactor(denominator,numerator.mod(denominator));
    }
    
    private void checkSigns() {
    	if(this.denom.compareTo(BigInteger.ZERO) == -1) {
    		this.num = this.num.negate();
    		this.denom = this.denom.negate();
    	}
    }
	
//  Accessory methods
//    public MyType flipFraction(MyType a) {
//    	BigInteger temp;// = new BigInteger(String.valueOf(1));
//    	temp = a.num;
//    	a.num = a.denom;
//    	a.denom = temp;
//    	return new MyType(a.num, a.denom);
//    }
    
/*  Printing methods */
    
    public String printAsDecimal(){
        BigDecimal result = new BigDecimal(this.num.toString());
        result = result.divide(new BigDecimal(String.valueOf(denom)),mc);
        return result.toString();
    }
	
	@Override
	public String toString() {
		return num.toString() + "/" + denom.toString();
	}

/*	Operations */
	
	@Override
	public MyType add(MyType a) {
		BigInteger resultDenom;
		BigInteger resultNum;
		if(this.denom.equals(a.denom)) {
			resultDenom = this.denom;
			resultNum = this.num.add(a.num);
		}
		else {
			resultDenom = this.denom.multiply(a.denom);
			resultNum = this.num.multiply(a.denom).add(a.num.multiply(this.denom));
		}
		MyType result = new MyType(resultNum, resultDenom);
		result.simplifyFraction();
		return result;
	}

	@Override
	public MyType sub(MyType a) {
		BigInteger resultDenom;
		BigInteger resultNum;
		if(this.denom.equals(a.denom)) {
			resultDenom = this.denom;
			resultNum = this.num.subtract(a.num);
		}
		else {
			resultDenom = this.denom.multiply(a.denom);
			resultNum = this.num.multiply(a.denom).subtract(a.num.multiply(this.denom));
		}
		MyType result = new MyType(resultNum, resultDenom);
		result.simplifyFraction();
		return result;
	}

	@Override
	public MyType mul(MyType a) {
		MyType result = new MyType(this.num.multiply(a.num), this.denom.multiply(a.denom));
		result.simplifyFraction();
		return result;
	}

	@Override
	public MyType div(MyType a) {
		MyType result = new MyType(this.num.multiply(a.denom), this.denom.multiply(a.num));
		result.simplifyFraction();
		return result;
	}

	@Override
	public boolean lessThan(MyType a) {
		if(this.denom.equals(a.denom)) {
			if(this.num.compareTo(a.num) == -1) return true;
			else return false;
		}
		else {
			if(this.num.multiply(a.denom).compareTo(a.num.multiply(this.denom)) == -1) return true;
			else return false;

		}
	}

	@Override
	public MyType newInstance() {
		return new MyType();
	}

	@Override
	public void changeSign() {
		this.num = this.num.negate();
	}
	
	@Override
	public void zero() {
		this.num = BigInteger.ZERO;
		this.denom = BigInteger.ONE;
	}

	public void abs() {
		if(this.num.compareTo(BigInteger.ZERO)<0) {
			this.num = this.num.negate();
		}
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
