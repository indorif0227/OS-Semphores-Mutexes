
/* 
* Student Name:    Furqaan Indori
* Student Id:      010852950
* Professor Name:  Dr. Dale Thompson
* Course Name:     Operating Systems
*/
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ChangingRoom {

    private static Semaphore changingRooms;
    private static Semaphore waitingRoomChairs;

    static class Customer implements Runnable {
        private String prefix;

        Customer(int id, int padding) {
            this.prefix = "Customer " + String.format("%0" + padding + "d", id) + ": ";
        }

        public void run() {
            // If the store is already full, the customer leaves
            if (waitingRoomChairs.availablePermits() == 0 && changingRooms.availablePermits() == 0) {
                System.out.println(prefix + "Left in frustration because the store was full >:(");
                return;
            }

            try {
                // Getting a chair in the waiting room
                System.out.println(prefix + "Waiting to enter the waiting room..." + "                   "
                        + "[" + waitingRoomChairs.availablePermits() + " chairs available]");

                waitingRoomChairs.acquire();

                System.out.println(prefix + "Got a chair in the waiting room!" + "                       "
                        + "[" + waitingRoomChairs.availablePermits() + " chairs available]");

                // Getting a changing room
                System.out.println(prefix + "Waiting to enter a changing room..." + "                    "
                        + "[" + changingRooms.availablePermits() + " rooms available]");

                changingRooms.acquire();
                waitingRoomChairs.release();

                System.out.println(prefix + "Got a changing room to themselves!" + "                     "
                        + "[" + changingRooms.availablePermits() + " rooms available]");

                // Spends a random amount of time in the changing room
                int changingTime = new Random().nextInt(1000);
                Thread.sleep(changingTime);
                changingRooms.release();

                // Customer leaves the store satisfied
                System.out.println(prefix + "Left the store after finishing with the changing room! ["
                        + changingRooms.availablePermits() + " rooms available]");

            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws InterruptedException {
        // Create the provided number of changing rooms and waiting room chairs
        int totalChangingRooms = Integer.parseInt(args[1]);
        int totalWaitingRoomChairs = totalChangingRooms * 2;

        changingRooms = new Semaphore(totalChangingRooms);
        waitingRoomChairs = new Semaphore(totalWaitingRoomChairs);

        // Create and populate customers array and create threads array
        int totalCustomers = totalChangingRooms + (totalWaitingRoomChairs);
        Customer[] customers = new Customer[totalCustomers];
        Thread[] customerThreads = new Thread[totalCustomers];

        int padding = (Integer.toString(totalCustomers)).length();
        int closingTime = Integer.parseInt(args[0]) * 1000;

        for (int id = 0; id < totalCustomers; id++) {
            customers[id] = new Customer(id + 1, padding);
        }

        // Simulate random arrival of customers until closing time
        int elapsedTime = 0;
        for (int id = 0; id < customers.length; id++) {

            int arrivalInterval = new Random().nextInt(1000);
            try {
                Thread.sleep(arrivalInterval);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            // Customers that come after closing time cannot enter
            elapsedTime += arrivalInterval;
            if (elapsedTime >= closingTime) {
                System.out.println("[!] " + closingTime + " milliseconds have passed. It's closing time!");
                break;
            }

            // Create a new thread and drop the customer into the system
            customerThreads[id] = new Thread(customers[id], "Customer " + (id + 1));
            customerThreads[id].start();
        }

        // Wait for all threads to die
        for (int id = 0; id < customerThreads.length; id++) {
            if (customerThreads[id] != null && customerThreads[id].isAlive()) {
                customerThreads[id].join();
            }
        }

        // Output if all customers finish shopping before closing time
        if (elapsedTime < closingTime) {
            System.out.println(
                    "[+] Customers have finished shopping. Total runtime: " + elapsedTime + " milliseconds");
        }

    }
}