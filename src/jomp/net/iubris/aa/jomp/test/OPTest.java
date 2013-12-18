package net.iubris.aa.jomp.test;

public class OPTest {
	
	public static void main(String[] args) {

		
		long sum = 0;
		long[] arr = new long[100000000];
		for (int i=0;i<1e7;i++){
			arr[i]=i;
		}
		for (int i=0;i<arr.length;i++) {
			sum+=arr[i]*+arr[i];
		}
		System.out.println(sum);
	}

}
