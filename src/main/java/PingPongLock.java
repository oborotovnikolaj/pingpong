import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PingPongLock {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition pinged = lock.newCondition();
        Condition ponged = lock.newCondition();
        new PingLock(pinged, ponged, lock);
        new PongLock(pinged, ponged, lock);
    }


}

class PingLock extends Thread {
    private Condition pinged;
    private Condition ponged;
    private Lock lock;

    public PingLock(Condition pinged, Condition ponged, Lock lock) {
        this.pinged = pinged;
        this.ponged = ponged;
        this.lock = lock;
        start();
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            System.out.println("ping");
            pinged.signalAll();
            try {
                ponged.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}


class PongLock extends Thread {
    private Condition pinged;
    private Condition ponged;
    private Lock lock;

    public PongLock(Condition pinged, Condition ponged, Lock lock) {
        this.pinged = pinged;
        this.ponged = ponged;
        this.lock = lock;
        start();
    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
            System.out.println("pong");
            ponged.signalAll();
            try {
                pinged.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
