
public class Productor extends Thread{
    private int Repeticiones;
    private Monitor monitor;
    private int T;

    public Productor(int veces, Monitor monitor){
        this.monitor=monitor;
        this.Repeticiones = veces;

    }

    @Override
    public void run(){
        for (int i=0; i<Repeticiones; i++){
            if(i%2==0){ //si el numero de repeticion es par
                T=1;
                monitor.disparar(T); //primero disparo t1 y luego del sleep disparo t0
                T=0;
            }
            else { //si es impar primero disparo t2 y luego del sleep disparo t5
                T=2;
                monitor.disparar(T);
                T=5;
            }
            try {
                sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            monitor.disparar(T);
        }
    }
}