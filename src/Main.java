import java.util.*;

public class Main
{
    public static int NUM_OF_THREADS = 10000;

    public static void main(String[] args) {

        Highway highway_AX = new Highway("A", "X");
        Highway highway_YB = new Highway("Y", "B");
        Highway highway_YX = new Highway("Y", "X");
        LocalRoad localRoad_AY = new LocalRoad("A", "Y");
        LocalRoad localRoad_XB = new LocalRoad("X", "B");

        List<List<Road>> possibleRoutes = new ArrayList<>();
        possibleRoutes.add(Arrays.asList(highway_AX, localRoad_XB));
        possibleRoutes.add(Arrays.asList(localRoad_AY, highway_YB));

        System.out.println("\nScenario without Y->X connection:");
        try
        {
            runSimulation(possibleRoutes);
            System.out.println("\nScenario with Y->X connection: ");
            possibleRoutes.add(Arrays.asList(localRoad_AY, highway_YX, localRoad_XB));
            runSimulation(possibleRoutes);

        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public static void runSimulation(List<List<Road>> possibleRoutes) throws InterruptedException
    {
        LinkedList<Thread> threads = new LinkedList<>();
        LinkedList<Car> cars = new LinkedList<>();

        long start = System.currentTimeMillis();
        for (int i=0; i<NUM_OF_THREADS; i++) {
            Car car = new Car(getOptimalRoute(possibleRoutes));
            cars.add(car);
            Thread thread = new Thread(car);
            threads.add(thread);
            thread.start();

            Thread.sleep(0, 250000);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long finish = System.currentTimeMillis();

        double sum = 0;
        int axb = 0;
        int ayb = 0;
        int ayxb = 0;
        for (Car car : cars) {
            sum += car.getTravelTime();
            switch (car.routeToString()) {
                case "AXB" -> axb += 1;
                case "AYB" -> ayb += 1;
                case "AYXB" -> ayxb += 1;
            }
        }
        System.out.println("Average travel time: " + sum / NUM_OF_THREADS / 1000000000);

        // investigate route choices
       /* System.out.println("route choices:");
        System.out.println("AXB -> " + axb);
        System.out.println("AYB -> " + ayb);
        System.out.println("AYXB -> " + ayxb);

        System.out.println("\nTime elapsed: " + (finish - start)/1000.0 + "s");*/
    }

    public static List<Road> getOptimalRoute(List<List<Road>> possibleRoutes)
    {
        double timeToGoal = Double.POSITIVE_INFINITY;
        List<Road> pickedRoute = null;

        for (List<Road> route : possibleRoutes)
        {
            double tmpTime = 0.0;
            for (Road road : route)
            {
                tmpTime += road.timeToPass();
            }
            if (timeToGoal > tmpTime) {
                timeToGoal = tmpTime;
                pickedRoute = route;
            }
        }
        return pickedRoute;
    }
}
