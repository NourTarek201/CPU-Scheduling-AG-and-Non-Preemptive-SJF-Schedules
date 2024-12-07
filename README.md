# CPU-Scheduling - AG and Non-Preemptive (SJF) Schedules

## About

### AG Scheduling
* The Round Robin (RR) CPU scheduling algorithm is a fair scheduling algorithm that gives equal time quantum to all processes So All processes are provided a static time to execute called quantum.
  
* A new factor is suggested to attach with each submitted process in our AG scheduling algorithm. This factor sums the effects of all three basic factors ((random_function(0,20) or 10 or priority), arrival time and burst time)The equation summarizes this relation is: AG-Factor = (Priority or 10 or (random_function (0,20)) + Arrival Time + Burst Time
  
* A new Random function (RF) is suggested between (0,20) and attached with each submitted process in our AG scheduling algorithm.
  This RF can update the AG-Factor based on the random number.
   * If(RF()<10 )-> AG-Factor = RF() + Arrival Time + Burst Time
   * If(RF()>10 )-> AG-Factor = 10 + Arrival Time + Burst Time
   * If(RF()=10 )-> AG-Factor = Priority + Arrival Time + Burst Time
* Once a process is executed for given time period, it’s called Non-preemptive AG till the finishing of (ceil (50%)) of its Quantum time, after that it’s converted to preemptive AG
  * preemptive AG : processes will always run until they complete or a new process is added that requires a smaller AG-Factor
  
* We have 3 scenarios of the running process
  * i. The running process used all its quantum time and it still have job to do
  (add this process to the end of the queue, then increases its Quantum time by (ceil(10% of the (mean of Quantum))) ).
  * ii. The running process didn’t use all its quantum time based on another process converted from ready to running (add this process to the end of the queue, and then increase its Quantum time     by the remaining unused Quantum time of this process).
  * iii. The running process finished its job (set its quantum time to zero and remove it from ready queue and add it to the die list).


### Non-Preemptive Shortest- Job First (SJF)
  * A scheduling algorithm that selects the process with the shortest burst time (execution time) from the ready queue to execute first. It ensures minimal average waiting and turnaround time.

  * Static Burst Time Selection: The algorithm relies on knowing the burst time of all processes beforehand. The selection is fixed, and no process is interrupted once it begins execution.

  * Process Execution: Once a process starts execution, it runs to completion without being preempted by other processes, even if a shorter process arrives during execution.

  * Priority Based on Burst Time: The process with the smallest burst time gets the highest priority. If two processes have the same burst time, the one that arrived first (FCFS tie-breaking) is     selected.

  * Key Characteristics:
    * Non-preemptive: No interruption once a process starts execution.
    * Optimal in terms of average waiting time when all burst times are known in advance.
    * Suitable for batch systems where process arrival times are predictable.
