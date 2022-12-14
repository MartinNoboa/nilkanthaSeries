/*----------------------------------------------------------------
*
* Programación avanzada: Proyecto final
* Fecha: 30-Nov-2022
* Autor: A01704052 Martin Noboa
* Descripción: Implementacion secuencial de la serie de Nilkantha en Java Fork Join
*
*--------------------------------------------------------------*/
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
public class NilkanthaForkJoin extends RecursiveTask<Double> {
	private static final int SIZE = 100_000;
	private static final int MIN = 10_000;
	private double result,sign;
	private int start, end;
	public NilkanthaForkJoin(int start, int end) {
		this.start = start;
		this.end = end;
		this.sign = 1;
		this.result = 0;
	}
	public Double computeDirectly() {
		if(this.start % 2 == 0){
			this.sign = this.sign * -1;
		}
		for (int i = this.start; i < SIZE; i++){
			this.result = this.result + (this.sign * (4.0 / ((this.start) * (this.start + 1) * (this.start + 2))));
			this.sign = this.sign * (-1);
			this.start += 2;
		}
		return result;
	}
	@Override
	protected Double compute() {
		if ((end - start) <= MIN) {
			return computeDirectly();
		} else {
			int mid = start + ((end - start) / 2);
			NilkanthaForkJoin lowerMid = new NilkanthaForkJoin(start, mid);
			lowerMid.fork();
			NilkanthaForkJoin upperMid = new NilkanthaForkJoin(mid, end);
			return (upperMid.compute() + lowerMid.join());
		}
	}
	public double getResult() {
		return result;
	}
	public static void main(String args[]) {
		long startTime, stopTime;
		double acum = 0;
		double res = 0;
		ForkJoinPool pool;
		acum = 0;
		System.out.printf("Starting with %d threads\n", Utils.MAXTHREADS);
		for (int i = 0; i < Utils.N; i++) {
			res = 0;
			startTime = System.currentTimeMillis();
			pool = new ForkJoinPool(Utils.MAXTHREADS);
			res = pool.invoke(new NilkanthaForkJoin(1, SIZE));
			stopTime = System.currentTimeMillis();

			acum += (stopTime - startTime);
		}
		res +=3;
		System.out.printf("pi = %.8f\n", res);
		System.out.printf("avg time = %.5f ms\n", (acum / Utils.N));
	}
}