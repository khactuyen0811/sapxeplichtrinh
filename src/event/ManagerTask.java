package event;

import java.util.Scanner;

public class ManagerTask {
    private Task[] tasks;
    private int count;
    private static final int Max_event = 200;

    private int[] queue;

    private int front, rear, size;
    
    // khởi tạo queue
    public ManagerTask(){
        tasks = new Task[Max_event];
        queue = new int[Max_event];
        front = 0;
        rear = -1;
        size = 0;
        count = 0;
    }

    public void rebuildQueue(long currentTime) {
        front = 0;
        rear = -1;
        size = 0;
        for (int i = 0; i < count; i++) {
            Task t = tasks[i];
            if (!t.isCanceled() && t.getStatus(currentTime).equals("pending")) {
                rear = (rear + 1) % Max_event;
                queue[rear] = i;
                size++;
            }
        }
    }

    // add task
    public boolean addTask(Task newTask, long currentTime){
        if(count >= Max_event){
            System.out.println("da day task khong the them");
            return false;
        }

        for (int i = 0; i < count; i++){
            if(tasks[i].ConfictTask(newTask))
            {
                System.out.println("task bi trung lich ban van muon them (y/n) ?");
                Scanner sc = new Scanner(System.in);
                if(!sc.nextLine().equalsIgnoreCase("y")){
                    return false;
                }

                sc.close();
                break;
            }
        }

        for(int i = 0;i< count; i++){
            if(tasks[i].getId().equals(newTask.getId())){
                System.out.println("id nay da ton tai ");
                return false;
            }
        }

        tasks[count] = newTask;
        count++;
        rebuildQueue(currentTime);
        return true;
    }

    //delete task
    public boolean deleteTask(String id, long currentTime){
        int index = -1;
        for(int i = 0; i < count; i++){
            if(tasks[i].getId().equals(id)){
                index = i;
                break;
            }
        }
        if(index  == -1){
            System.out.println("error khong tim thay Id");
            return false;
        }
        for(int i = index; i< count - 1; i++){
            tasks[i] = tasks[i + 1];
        }
        count--;
        rebuildQueue(currentTime);
        return true;
    }

    public int findById(String id){
        if(id == null){
            return -1;
        }
        for (int i = 0;i< count; i++){
            if(tasks[i].getId().equals(id)){
                return i;
            }
        }
        return -1;
    }

    public void sortByTime(long currentTime){
        for(int i = 0; i< count; i++){
            for(int j = 0; j < count - 1; j++){
                long t1 = tasks[j].GetTimeStart();
                long t2 = tasks[j + 1].GetTimeStart();
                if(t1> t2){
                    Task temp = tasks[j];
                    tasks[j] = tasks[j+1];
                    tasks[j+1] = temp;
                }
            }
        }
        rebuildQueue(currentTime);
    }

    public void processNextTask(long currentTime){
        if(size == 0){
            System.out.println("ham doi rong");
            return;
        }
        int idx = queue[front];
        Task task = tasks[idx];
        if (task.isCanceled() || !task.getStatus(currentTime).equals("pending")) {
            
            front = (front + 1) % Max_event;
            size--;
            System.out.println("cong viec " + task.getId() + " khong con pending nua , next.");
            processNextTask(currentTime);
            return;
        }

        front = (front + 1) % Max_event;
        size--;
        System.out.println("dang xu ly cong viec " + task.getId());
        task.displayAll();
        System.out.println("da xu ly xong cong viec " + task.getId());
        System.out.print("da hoan thanh  (y/n): ");
        Scanner sc = new Scanner(System.in);
        if (sc.nextLine().equalsIgnoreCase("y")) {
            System.out.println("has been done");
        } else {
            if (!task.isCanceled() && task.getStatus(currentTime).equals("pending")) {
                rear = (rear + 1) % Max_event;
                queue[rear] = idx;
                size++;
                System.out.println("chua xu li xong,chuyen ve cuoi hang doi ");
            } else {
                System.out.println("cong viec khong con pending, khong dua lai.");
            }
        }
    }

    public void displayAllTask(long currentTime){
        if(count == 0){
            System.out.println("khong co cong viec nao");
            return;
        }

        for(int i = 0; i < count; i++){
            Task t = tasks[i];

            t.displayAll();
            System.out.println("trang thai: " + t.getStatus(currentTime));
        }
    }

    public void filterPriority(int p,long currentTime){
        for(int i = 0; i < count; i++){
            Task t = tasks[i];
            if(t.getPriority() == p){
                System.out.println("======================================");
                System.out.println("do uu tien: " + p +"la");
                t.displayAll();
                System.out.println("trang thai: " + t.getStatus(currentTime));
            }
        }
    }

    public void filterByStatus(String status, long currentTime) {
    boolean found = false;
    System.out.println("--- Danh sach cong viec co trang thai: " + status + " ---");

    for (int i = 0; i < count; i++) {
        Task t = tasks[i];
        
        String currentStatus = t.getStatus(currentTime);
        if (currentStatus.equalsIgnoreCase(status)) {
            t.displayAll();
            System.out.println("Trang thai thuc te: " + currentStatus);
            System.out.println("-------------------------");
            found = true;
        }
    }

    if (!found) {
        System.out.println("Khong co cong viec nao o trang thai: " + status);
    }
}

    public void filterByRange(int d1, int m1, int y1, int h1, int min1, int d2, int m2, int y2, int h2, int min2) {
        
        long startLimit = Task.toMinutes(d1, m1, y1, h1, min1);
        long endLimit = Task.toMinutes(d2, m2, y2, h2, min2);
        
        boolean found = false;
        System.out.println("--- Ket qua loc tu " + d1 + "/" + m1 + " den " + d2 + "/" + m2 + " ---");
        
        if(startLimit > endLimit){
            System.out.println("start time khong duoc lon hon end time");
            return;
        }

        if(startLimit == endLimit){
            System.out.println("start time khong duoc trung voi end time");
            return;
        }
        for (int i = 0; i < count; i++) {
            Task t = tasks[i];
            long taskTime = t.GetTimeStart();

            if (taskTime >= startLimit && taskTime <= endLimit) {
                t.displayAll();
                System.out.println("Trang thai: " + t.getStatus(System.currentTimeMillis()));
                System.out.println("-------------------------");
                found = true;
            }
        }
        if (!found) {
            System.out.println("Khong tim thay cong viec nao trong khoang thoi gian nay.");
        }
    }

    public double getTotalRevenue(){
        double sum = 0;
        for(int i = 0; i<count; i++){
            sum += tasks[i].getRevenue();
        }
        return sum;
    }

    public Task[] getTask(){
        return tasks;
    }

    public int getCount(){
        return count;
    }

    public void setCount(int count){
        this.count = count;
    }
}
