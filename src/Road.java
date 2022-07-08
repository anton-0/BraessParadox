import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Road
{
    public long drivingDuration; // in milliseconds
    private String start;
    private String end;
    private List<Car> cars;
    private Queue<Car> queue;
    private int capacity;

    public Road(long time, String start, String end, int capacity) {
        this.drivingDuration = time;
        this.start = start;
        this.end = end;
        this.cars = new LinkedList<>();
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public double getDrivingDuration() {
        return drivingDuration;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    // time to spend on waiting to join the road and going through it
    public long timeToPass() {
        return queue.size() + cars.size();
    }

    public boolean freeEntry() {
        return cars.size() < capacity;
    }

    public void joinTheRoad(Car car) throws InterruptedException {
        synchronized (this) {
            if (!freeEntry()) {
                queue.add(car);
                wait();
                queue.remove(car);
            }
            cars.add(car);
        }

        Thread.sleep(drivingDuration);

        synchronized (this) {
            cars.remove(car);
            notify();
        }
    }
}
