import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.Semaphore;


public class Monitor {
    private Semaphore semaphore;
    private List<Semaphore> colas;
    private RDP rdp;
    private int Lugares_Vacios_1;
    private int Lugares_Vacios_2;
    private int Lugares_Ocupados_1;
    private int Lugares_Ocupados_2;
    private int este;
    private boolean k;

    public Monitor(){
        semaphore=new Semaphore(1,true);
        colas=new ArrayList<>();
        for(int i=0; i<8; i++){
            colas.add(new Semaphore(0, true)); //hay un semaforo para cada transicion, y una cola asociada a cad uno de los semafros
        }
        rdp=new RDP();
        Lugares_Vacios_1=10;
        Lugares_Vacios_2=15;
        Lugares_Ocupados_1=0;
        Lugares_Ocupados_2=0;
    }

    public int disparar(int T){
        try {
            semaphore.acquire(); //lo primero que hago es intentar adquirir el semaforo de entrada al monitor
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        k=true;
        while(k==true){

            k=rdp.dispararRed(T); //si pude disparar la red, k sigue en true 

            if(k==true){

                switch(T){
                    case 0: Lugares_Ocupados_1++;   break; //actualizo los lugares vacios y ocupados
                    case 1: Lugares_Vacios_1--;     break;
                    case 2: Lugares_Vacios_2--;     break;
                    case 3: Lugares_Ocupados_1--;   break;
                    case 4: Lugares_Ocupados_2--;   break;
                    case 5: Lugares_Ocupados_2++;   break;
                    case 6: Lugares_Vacios_2++;     break;
                    case 7: Lugares_Vacios_1++;     break;
                    default:                        break;
                }

                if(hayHilos()){ //me fijo si hay hilos esperando 

                    colas.get(este).release(); //libero al primero que encontre que tenia hilos en la cola y estaba la t sensibilizada
                    return 0;
                }
                k=false;//si no hay hilos para despertar, coloco k en false y salgo de este bloque 

            }
            else{
                semaphore.release(); // si no pude disparar, hago un release al semaforo del monitor
                try {
                    colas.get(T).acquire();  //y me voy a dormir a la cola asociada a la transicion que quise disparar
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        semaphore.release(); //libero el monitor
        return 0;
    }

    private boolean hayHilos(){
        if(largoColas()==0){ //no hay hilos encolados en ninguna cola
            return false;
        }
        BitSet sensibilizadas=rdp.getSensibilizadas(); //me traigo las sensibilizadas 
        for(int i=0; i<8; i++){ //recorro las colas desde la asociada a la t0 hasta la 7
            este=i; 
            if(sensibilizadas.get(i)&& colas.get(i).hasQueuedThreads()){ //hago un and para saber si la cola tiene hilo y si esta sensibilizada

                return true;
            }
        }
        return false;
    }

    private int largoColas(){
        int ret=0;
        for(int i=0; i<8; i++){
            ret=ret+colas.get(i).getQueueLength(); //ret tiene la cantidad total de hilos encolados en todas las colas
        }
        return ret;
    }


    public String estadisticas (){
        String stats;
        stats="\nLugares ocupados buffer1: " + Lugares_Ocupados_1 + "\nLugares ocupados buffer2: " + Lugares_Ocupados_2
                + "\nHilos en las colas de espera: " + largoColas()
                + "\nHilos en espera del mutex: " + semaphore.getQueueLength();
        return stats;

    }

}
