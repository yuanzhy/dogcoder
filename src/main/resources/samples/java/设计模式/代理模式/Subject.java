public interface Subject {
	void request();
}

class RealSubjectA implements Subject {
	public void request()
	{
		System.out.println("真实主题类A");
	}
}

class RealSubjectB implements Subject {
	public void request()
	{
		System.out.println("真实主题类B");
	}
}