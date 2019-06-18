package cn.wulin.spring.boot.temp1.resolvable_type;

import org.springframework.core.ResolvableType;

public class GenericAB<A,B>{

	private A a;
	private B b;
	
	
	public GenericAB(A a, B b) {
		super();
		this.a = a;
		this.b = b;
	}
	public A getA() {
		return a;
	}
	public void setA(A a) {
		this.a = a;
	}
	public B getB() {
		return b;
	}
	public void setB(B b) {
		this.b = b;
	}
	
	public ResolvableType getAResolvableType() {
		return ResolvableType.forInstance(getA());
	}
	
	public ResolvableType getBResolvableType() {
		return ResolvableType.forInstance(getB());
	}
	
}
