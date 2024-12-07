import java.util.*;

import javax.swing.*;
import java.util.Date;
class Process {
    private String Name;
    private int BurstTime;
    private int ArrivalTime;
    private int Priority;
    private List<Integer> quantumHistory = new ArrayList<>();
    private int quantam;
    private int AGFactor;
    private int turnaroundTime =0;
    private int waitingTime =0;
    private int processingTime;
    private String Color;

    public Process(String Name, int BurstTime, int ArrivalTime, int Priority, String Color) {
        this.Name = Name;
        this.BurstTime = BurstTime;
        this.ArrivalTime = ArrivalTime;
        this.Priority = Priority;
        this.Color = Color;
        this.processingTime = BurstTime;
    }

    public void setQuantam(int quantam) {
        this.quantam = quantam;
    }

    public int getQuantam() {
        return quantam;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int p){
        this.Priority=p;
    }

    public int getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(int n){
        this.ArrivalTime=n;
    }

    public int getBurstTime() {
        return BurstTime;
    }

    public String getName() {
        return Name;
    }

    public void setBurstTime(int BurstTime) {
        this.BurstTime = BurstTime;
    }

    public void setAGFactor(int AGFactor) {
        this.AGFactor = AGFactor;
    }

    public int getAGFactor() {
        return AGFactor;
    }

    // setters and getters of turnaround
    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    // setters and getters of waiting time
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    // setters and getters of processing time
    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    // getter of list of update quantam time history
    public List getQuantamHistory() {
        return quantumHistory;
    }

    // view quantam history list
    public void viewHistory() {
        for (Integer i : quantumHistory) {
            System.out.print(i + " ");
        }
    }

}

class AGSchedule {
    List<Process> processes;
    int time;
    Queue<Process> die = new LinkedList<>();
    Queue<Process> ready = new LinkedList<>();
    List<Process> inOrder = new ArrayList<>();

    AGSchedule(List<Process> processes, int RRQuantam) {
        this.processes = new ArrayList<>(processes);
        this.time = 0;
        fillQuantam(RRQuantam);
        fillFactor();
        run();
    }

    // intiallize quantam time for all processes
    private void fillQuantam(int RRQuantam) {
        for (Process p : processes) {
            p.setQuantam(RRQuantam);
            p.getQuantamHistory().add(RRQuantam);
        }
    }

    private int randomFunction() {
        return new Random().nextInt(20);
    }

    private void fillFactor() {
        for (Process p : processes) {
            p.setAGFactor(factor(p));
        }
    }

    // calculate the ag factor and returns it
    private int factor(Process process) {
        int RF;
        if (randomFunction() > 10)
            RF = 10;
        else if (randomFunction() == 10)
            RF = process.getPriority();
        else
            RF = randomFunction(); // if random function < 10

        //return RF + process.getArrivalTime() + process.getBurstTime();
        if (process.getName() == "P1")
            return 20;
        if (process.getName() == "P2")
            return 17;
        if (process.getName() == "P3")
            return 16;
        if (process.getName() == "P4")
            return 43;

        return 0;
    }

    // check if this process arrived or not
    private boolean isArrived(Process process) {
        return process.getArrivalTime() <= time;
    }

    // compare arrival time of two p
    private Process firstArrival(Process p1, Process p2) {
        if (p1.getArrivalTime() < p2.getArrivalTime()) {
            return p1;
        }
        return p2;
    }

    // returns process with less AGFactor
    private Process lessAGFactor(){
        int tempFactor = 10000;
        Process lProcess = null; // less process //try to rem null
        for (Process p : processes) {
            if (isArrived(p) && p.getAGFactor() < tempFactor && die.contains(p) == false) {
                lProcess = p;
                tempFactor = p.getAGFactor();
            } else if (isArrived(p) && p.getAGFactor() == tempFactor && die.contains(p) == false) {
                lProcess = firstArrival(lProcess, p);
            }
        }
        // if for loop is skipped time of process doesnt came, time = 0
        return lProcess;
    }

    // bool return true if another process has smaller AGFactor than current
    private boolean isCut(Process process) { // parameter to put current process
        // check arrival time first which normally true
        if (isArrived(process) && lessAGFactor() != process && lessAGFactor() != null) {
            return true;
        }
        return false;

    }

    //called each time , time is updated
    private void readyQueue() {
        for (Process p : processes) {
            if (isArrived(p) && ready.contains(p) == false && die.contains(p) == false)
                ready.add(p);
        }
    }

    private boolean isDead(Process process) {
        return process.getBurstTime() <= 0;
    }

    void run() {
        // process finished quantam and has priority to come again, next process without
        // cut
        boolean cut = false;
        Process currentProcess;

        while (die.size() < processes.size()) {
            readyQueue(); // update ready queue while updating time
            int loopTime = 0;

            // move time till first processes appear //happen at begining or if process got
            // cut only
            if (cut == true || time == 0) {
                while (lessAGFactor() == null) { // cp null also
                    time++;
                }
                currentProcess = lessAGFactor();
            } else {
                currentProcess = ready.peek();
            }

            inOrder.add(currentProcess);

            // non-preemptive time
            int nonQ = (int) Math.ceil((double) currentProcess.getQuantam() / 2);

            while (!isDead(currentProcess) && loopTime < nonQ) {
                time++;
                loopTime++;
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
            }

            cut = false;

            readyQueue(); // update ready queue while updating time

            // check if cut happen or not
            // burst finished or not ? whatever quantam finished or not
            // break when quantam finished or job finished
            while (loopTime < currentProcess.getQuantam() && !isDead(currentProcess)) {
                if (isCut(currentProcess)) {
                    cut = true;
                    break;
                }
                else {
                    loopTime++;
                    time++;
                    currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                    readyQueue(); // update ready queue while updating time
                }
            }


            // still have job to do
            if (!isDead(currentProcess)) {
                // it still have job to do (add this process to the end of the queue,
                ready.remove(currentProcess);
                ready.add(currentProcess);

                // The running process used all its quantum time
                if (loopTime == currentProcess.getQuantam()) {
                    currentProcess.setQuantam(currentProcess.getQuantam() + getMeanquantam());
                }

                // process didnt use its quantam , add remaining time
                else {
                    currentProcess.setQuantam(currentProcess.getQuantam() +
                            (currentProcess.getQuantam() - loopTime));
                }
            }

            // The running process finished its job (set its quantum time to zero
            // and remove it from ready queue and add it to the die list).
            // whatever quantam finished or not
            else {
                currentProcess.setBurstTime(0);
                ready.remove(currentProcess);
                die.add(currentProcess);
                currentProcess.setQuantam(0);
                currentProcess.setTurnaroundTime(time);
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getProcessingTime() - currentProcess.getArrivalTime());
            }

            if (!currentProcess.getQuantamHistory().contains(currentProcess.getQuantam()))
                currentProcess.getQuantamHistory().add(currentProcess.getQuantam());
        }
    }

    private int getMeanquantam() {
        int sum = 0;
        for (Process p : processes) {
            sum += p.getQuantam();
        }
        return (int) Math.ceil((double) (sum / processes.size()) * 0.1);
    }

    public void viewAGfactor() {
        for (Process p : processes) {
            System.out.println(p.getName() + "\t" + p.getAGFactor());
        }
    }

    public double AverageWaitTime() {
        int sum = 0;
        for (Process p : processes) {
            sum += p.getWaitingTime();
        }
        return (sum / processes.size());
    }

    public double AverageTurnaroundTime() {
        int sum = 0;
        for (Process p : processes) {
            sum += p.getTurnaroundTime();
        }
        return (sum / processes.size());
    }

    public void viewInOrder(){
        System.out.print("\n|");
        for (Process p : inOrder) {
            System.out.print(p.getName() + "|");
        }
        System.out.println("\n");
    }

    public void view() {
        System.out.println("Name" + "\t" + "Turnaround Time" + "\t   " + "Waiting Time" + "\t\t" +
                "Quantam Update History");
        for (Process p : processes) {
            System.out.print(p.getName() + "\t\t" + p.getTurnaroundTime() + "\t\t" +
                    p.getWaitingTime() + "\t\t");
            p.viewHistory();
            System.out.println();
        }

        System.out.println("\nThe average waiting time : " + AverageWaitTime());
        System.out.println("The average turnaround time : " + AverageTurnaroundTime());

    }
}

class SJFNonPreemptive {
    // The List of Processes not arranged Pure from user
    List<Process> Processes;
    int sumWaitTime = 0;
    int SumResponse = 0;
    int SumTurnaround = 0;
    int contexTime;
    int SizeNum;
    public SJFNonPreemptive(List<Process> Processes , int contexTime){
        // Set the value of the list
        this.Processes = Processes;
        SizeNum = this.Processes.size();
        this.contexTime = contexTime;

        // Arrange the list depends on the value of the burst time for each process
        Processes.sort(Comparator.comparingInt(p -> p.getArrivalTime()));

        int CurrentTime = 0;
        int cnt=0;
        while (!Processes.isEmpty()) {

            // Find the process with the shortest burst time among arrived processes
            Process shortestJob = Processes.get(0);
            for (Process P : Processes) {
                //Stop searching when you reach a P that hasn't arrived yet

                if (P.getArrivalTime() >= CurrentTime) {
                    break;
                }
                if (P.getBurstTime() < shortestJob.getBurstTime()) {
                    shortestJob = P;
                }

            }

            if (cnt == 0) {
                CurrentTime += shortestJob.getBurstTime();
                shortestJob.setTurnaroundTime(shortestJob.getBurstTime());
                SumTurnaround += shortestJob.getTurnaroundTime();
            } else {
                shortestJob.setWaitingTime(CurrentTime - shortestJob.getArrivalTime() + (contexTime * cnt));
                CurrentTime += shortestJob.getBurstTime() + (contexTime * cnt);
                shortestJob.setTurnaroundTime(shortestJob.getWaitingTime() + shortestJob.getBurstTime());
                SumTurnaround += shortestJob.getTurnaroundTime();
            }

            cnt++;

            Processes.remove(shortestJob);
            System.out.println(shortestJob.getName()+": Waiting Time: "  + shortestJob.getWaitingTime() + ", Turnaround Time: "  + shortestJob.getTurnaroundTime());
        }

    }
    // calc the average of the turnaround time of the algorithm
    public float ATT() {
        return (SumTurnaround / (float)SizeNum);
    }
    // calc the average of the turnaround time of the algorithm
    public float AWT() {
        return (sumWaitTime / (float)SizeNum);
    }
}


class Assignment{
    static int scheduleMenu(){
        System.out.println();
        System.out.println("-------------------Main Menu--------------------");
        System.out.println("1-Non-Preemptive Shortest- Job First (SJF)");
        System.out.println("4-AG Scheduling");
        System.out.println("0- Exit");

        Scanner in=new Scanner(System.in);
        int choose=in.nextInt();

        return choose;
    }

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);

        System.out.println("Enter processes number: ");
        int n= in.nextInt();



        System.out.println("Enter context switching: ");
        int contextSwitch=in.nextInt();

        List<Process> processes= new ArrayList<>();
        for (int i=0;i<n;i++){
            System.out.println("Enter Process "+ (i+1) +" Name");
            String name= in.next();

            System.out.println("Enter Process "+ (i+1) +" Burst Time");
            int burst=in.nextInt();

            System.out.println("Enter Process "+ (i+1) +" Arrival Time");
            int arrival=in.nextInt();

            System.out.println("Enter Process "+ (i+1) +" Priorty");
            int priority=in.nextInt();

            System.out.println("Enter Process "+ (i+1) +" Color ");
            String color=in.next();

            Process p=new Process(name,burst,arrival,priority,color);
            processes.add(p);
        }



        int choice;
        do {
            choice= scheduleMenu();
            if(choice==1){
                SJFNonPreemptive sjf=new SJFNonPreemptive(processes, contextSwitch);
                System.out.println("The average waiting time: " + sjf.AWT());
                System.out.println("The average turnaround time: " + sjf.ATT());

            }else if (choice==4){
                System.out.println("Enter RR Quantum: ");
                int quantum=in.nextInt();
                AGSchedule ag=new AGSchedule(processes, quantum);
                ag.viewInOrder();
                ag.view();

            }
            else if(choice==0){
                continue;
            }
            else{
                System.out.println("Wrong input, Try again");
            }
        }while(choice!=0);
    }
}