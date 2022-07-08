import java.lang.Runnable;
import java.util.List;

public class Car implements Runnable
{
    private long startTime;
    private long finishTime;
    private List<Road> pickedRoute;

    public Car(List<Road> pickedRoute) {
        this.pickedRoute = pickedRoute;
    }

    // in nanoseconds
    public long getTravelTime() {
        return finishTime - startTime;
    }

    public String routeToString() {
        String route = "A";
        for (Road road : pickedRoute)
        {
            route += road.getEnd();
        }
        return route;
    }

    @Override
    public void run()
    {
        startTime = System.nanoTime();
        for (Road road : pickedRoute)
        {
            try
            {
                road.joinTheRoad(this);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        finishTime = System.nanoTime();
    }
}
