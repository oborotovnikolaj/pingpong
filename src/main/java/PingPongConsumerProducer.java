public class PingPongConsumerProducer {
    public static void main(String[] args) {
        Object o = new Object();
        Changer changer1 = new Changer(o);
        Changer changer2 = new Changer(o);
        new PingThread(changer1, changer2, "ping");
        new PongThread(changer2, changer1, "pong");
    }
}

class PingThread extends Thread{
    private Changer consumer;
    private Changer producer;
    private final String sound = "ping";

    public PingThread(Changer consumer, Changer producer, String sound) {
        this.consumer = consumer;
        this.producer = producer;
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(consumer.take());
                producer.put(sound);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class PongThread extends Thread{
    private Changer consumer;
    private Changer producer;
    private final String sound = "pong";

    public PongThread(Changer consumer, Changer producer, String sound) {
        this.consumer = consumer;
        this.producer = producer;
        start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                producer.put(sound);
                System.out.println(consumer.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Changer{
    private String sound;
    private boolean isEmpty = true;
    private final Object monitor;

    public Changer(Object monitor) {
        this.monitor = monitor;
    }

    public void put(String sound) throws InterruptedException {
        synchronized (monitor) {
            while (!isEmpty) {
                monitor.wait();
            }
            isEmpty = false;
            this.sound = sound;
            monitor.notifyAll();
        }
    }

    public String take() throws InterruptedException {
        synchronized (monitor) {
            while (isEmpty) {
                monitor.wait();
            }
            isEmpty = true;
            monitor.notifyAll();
            return sound;
        }
    }
}
