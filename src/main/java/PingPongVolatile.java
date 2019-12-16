public class PingPongVolatile {
    volatile static boolean pinged = false;

    public static void main(String[] args) {
        Object o = new Object();
        new VolPingThread(o);
        new VolPongThread(o);
    }
}

class VolPingThread extends Thread {
    private final Object monitor;
    private final String sound = "ping";

    public VolPingThread(Object monitor) {
        this.monitor = monitor;
        start();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (monitor) {
                if (!PingPongVolatile.pinged) {
                    System.out.println(sound);
                    PingPongVolatile.pinged = true;
                }
            }
        }
    }
}

class VolPongThread extends Thread {
    private final Object monitor;
    private final String sound = "pong";

    public VolPongThread(Object monitor) {
        this.monitor = monitor;
        start();
    }

    @Override
    public void run() {
        while (true) {
            synchronized (monitor) {
                if (PingPongVolatile.pinged) {
                    System.out.println(sound);
                    PingPongVolatile.pinged = false;
                }
            }
        }
    }
}
