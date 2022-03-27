
public class Consumidor extends Thread{
    private int T;
    private int Repeticiones;
    private Monitor monitor;

    public Consumidor(int veces, Monitor monitor) {
        this.monitor=monitor;
        this.Repeticiones = veces;

    }

    @Override
    public void run(){
        for (int i=0; i<Repeticiones; i++){
            if(i%2==0){
                T=3;
                monitor.disparar(T);
                T=7;
            }
            else {
                T=4;
                monitor.disparar(T);
                T=6;
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