public class Main {
    public static void main(String[] args) {

        Monitor monitor=new Monitor();
        Control log=new Control(monitor,"Ejecucion");

        Thread thread[]=new Thread[13];
        for (int i=0; i<5; i++){
            thread[i]=new Productor(10000, monitor);
        }

        for (int i=5; i<13; i++){
            thread[i]=new Consumidor(10000, monitor);
        }

        log.start();

        for (int i=0; i<13; i++){
            thread[i].start();
        }

        for (int i=0; i<5; i++){
            try {
                thread[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            log.join(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.setEstado("Fin");
    }
}
