# Description
This program aims to simulate the flow of customers at a clothing store with multithreading. 

Each customer in the simulation is represented by a runnable object. A customer object being assigned to a thread and run represents
a customer arriving at the store. Each customer arrives at a random interval between 0 and 1 seconds. If the customer arrives at a store
where the waiting room and changing rooms are completely full, meaning both semaphores do not have any available permits, the customer
leaves in frustration and the thread dies.

If the aforementioned condition is not the case, the customer must wait for empty chairs in the waiting room, which is represented by a semaphore 
with a number of permits equivalent to the number of chairs in the physical representation. The customer's thread attempts to acquire a permit from 
the waiting room semaphore and waits if a permit is not immediately available.

Once the waiting room permit has been attained, the customer must try to acquire a changing room permit, which is the same process as before with a different
semaphore(changing room semaphore). The customer must also release their waiting room permit once they have acquired their new permit to allow other customers to enter. 
The customer then spends a random interval between 0 and 1 seconds in the changing room, and then leaves the store, releasing their changing room permit in the process.

This process takes place for all customers in the system until the store closes or all customers are finished shopping and have left the store. The customers that
are in the store after closing time are allowed to finish shopping, but no more customers are allowed entry.

# Running the simulation
Compile:
    ```bash
    javac ChangingRoom.java
    ```

Run:
    ```bash
    java ChangingRoom [Closing time (seconds)] [# of Changing Rooms]
    ```
   
> The program requires two arguments to run properly: the closing time of the store in seconds, and the number of changing rooms in the store. Both
arguments must be provided as integers. The other parameters are calculated from the ones the user provides as shown below.

## Parameter Calculation
Closing Time = User Provided
Number of Changing Rooms = User Provided
Number of Waiting Room Chairs = (Number of Changing Rooms) * 2
Number of Customers = (Number of Changing Rooms) + (Number of Waiting Room Chairs)
