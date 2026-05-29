package main;
import event.ManagerFile;
import event.ManagerTask;
import event.Task;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    private static ManagerTask manager = new ManagerTask();
    private static Scanner sc = new Scanner(System.in);

    private static int daysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12: return 31;
            case 4: case 6: case 9: case 11: return 30;
            case 2: return isLeapYear(year) ? 29 : 28;
            default: return 0;
        }
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static long nowMinutes() {
        LocalDateTime now = LocalDateTime.now();
        return Task.toMinutes(now.getDayOfMonth(), now.getMonthValue(), now.getYear(),
                              now.getHour(), now.getMinute());
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(sc.nextLine()); }
            catch (Exception e) { System.out.println("Nhap sai, nhap so nguyen."); }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Double.parseDouble(sc.nextLine()); }
            catch (Exception e) { System.out.println("Nhap sai so thuc."); }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static Task binarySearchById(String key) {
        Task[] tasks = manager.getTask();
        int count = manager.getCount();
        Integer[] idxArr = new Integer[count];

        for (int i = 0; i < count; i++) idxArr[i] = i;
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (tasks[idxArr[j]].getId().compareTo(tasks[idxArr[j+1]].getId()) > 0) {
                    int tmp = idxArr[j];
                    idxArr[j] = idxArr[j+1];
                    idxArr[j+1] = tmp;
                }
            }
        }
        int left = 0, right = count - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = tasks[idxArr[mid]].getId().compareTo(key);
            if (cmp == 0) return tasks[idxArr[mid]];
            else if (cmp < 0) left = mid + 1;
            else right = mid - 1;
        }
        return null;
    }

    public static void main(String[] args) {
        Task[] tasksArray = manager.getTask();
        int[] countRef = {0};
        ManagerFile.loadTasks(tasksArray, countRef);
        manager.setCount(countRef[0]);
        manager.rebuildQueue(nowMinutes());

        int choice;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Them cong viec");
            System.out.println("2. Hien thi tat ca");
            System.out.println("3. Loc cong viec (uu tien / khoang thoi gian / trang thai)");
            System.out.println("4. Tim kiem theo ID");
            System.out.println("5. Sap xep theo thoi gian bat dau");
            System.out.println("6. Cap nhat cong viec");
            System.out.println("7. Xoa cong viec");
            System.out.println("8. Dem nguoc time den cong viec can lam");
            System.out.println("9. Xu ly cong viec tiep theo (FIFO)");
            System.out.println("10. Thong ke doanh thu");
            System.out.println("0. Luu va thoat");
            System.out.print("Chon: ");
            try { choice = Integer.parseInt(sc.nextLine()); }
            catch (Exception e) { choice = -1; }
            long currentTime = nowMinutes();
            switch (choice) {
                case 1: addEvent(currentTime); break;
                case 2: manager.displayAllTask(currentTime); break;
                case 3: filterMenu(currentTime); break;
                case 4: {
                    System.out.print("Nhap ID can tim: ");
                    String key = sc.nextLine();
                    Task result = binarySearchById(key);
                    if (result != null) {
                        result.displayAll();
                        System.out.println("Trang thai: " + result.getStatus(nowMinutes()));
                    } else {
                        System.out.println("Khong tim thay ID " + key);
                    }
                    break;
                }
                case 5: {
                    manager.sortByTime(currentTime);
                    manager.rebuildQueue(currentTime);
                    System.out.println("Da sap xep theo thoi gian bat dau.");
                    ManagerFile.log("Sap xep cong viec theo thoi gian.");
                    break;
                }
                case 6: updateEvent(currentTime); break;
                case 7: deleteEvent(currentTime); break;
                case 8: countdownToTask(); break;
                case 9: manager.processNextTask(currentTime); break;
                case 10: thongKeDoanhThu(); break;
                case 0:
                    ManagerFile.saveTasks(manager.getTask(), manager.getCount());
                    System.out.println("Tam biet!");
                    break;
                default: System.out.println("Nhap sai, thu lai.");
            }
        } while (choice != 0);
    }

    private static void addEvent(long currentTime) {
        if (manager.getCount() >= 200) { // Max_event = 200
            System.out.println("Day, khong them duoc.");
            return;
        }
        String newId;
        while (true) {
            System.out.print("Nhap ID: ");
            newId = sc.nextLine();
            if (manager.findById(newId) != -1)
                System.out.println("ID da ton tai, nhap lai.");
            else break;
        }
        int d, m, y, h, min;
        while (true) {
            d = readInt("Ngay: ");
            m = readInt("Thang: ");
            y = readInt("Nam: ");
            h = readInt("Gio: ");
            min = readInt("Phut: ");
            if (m >= 1 && m <= 12 && d >= 1 && d <= daysInMonth(m, y) && h >= 0 && h <= 23 && min >= 0 && min <= 59)
                break;
            System.out.println("Ngay gio khong hop le, nhap lai.");
        }
        System.out.print("Tieu de: ");
        String tille = sc.nextLine();
        System.out.print("Mo ta: ");
        String description = sc.nextLine();
        int prio = readInt("Do uu tien (1-5): ");
        while (prio < 1 || prio > 5) {
            System.out.println("Chi nhap 1 den 5.");
            prio = readInt("Do uu tien: ");
        }
        int dur = readInt("Thoi gian thuc hien (phut): ");
        while (dur <= 0) {
            System.out.println("Phai lon hon 0.");
            dur = readInt("Thoi gian thuc hien: ");
        }
        System.out.print("Dia diem: ");
        String location = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Doi tac: ");
        String partner = sc.nextLine();
        System.out.println("Loai hinh hop:");
        System.out.println("1. work");
        System.out.println("2. study");
        System.out.println("3. meeting");
        System.out.println("4. conference");
        int choiceType = readInt("Nhap so (1-4): ");
        String meetingType;
        switch (choiceType) {
            case 1: meetingType = "work"; break;
            case 2: meetingType = "study"; break;
            case 3: meetingType = "meeting"; break;
            case 4: meetingType = "conference"; break;
            default: meetingType = "work";
        }
        double revenue = readDouble("Doanh thu (VND): ");
        System.out.print("Huy cong viec ngay? (y/n): ");
        boolean isCanceled = sc.nextLine().equalsIgnoreCase("y");

        Task newTask = new Task(newId, d, m, y, h, min, tille, dur, description,
                isCanceled, location, prio, partner, email, meetingType, revenue);
        if (manager.addTask(newTask, currentTime)) {
            System.out.println("Them thanh cong.");
            ManagerFile.log("Them cong viec ID " + newId);
        } else {
            System.out.println("Them that bai.");
        }
    }

    private static void filterMenu(long currentTime) {
        System.out.println("\n--- LOC CONG VIEC ---");
        System.out.println("1. Theo do uu tien");
        System.out.println("2. Theo khoang thoi gian");
        System.out.println("3. Theo trang thai");
        System.out.print("Chon: ");
        int chon = Integer.parseInt(sc.nextLine());
        try{
            switch (chon) {
                case 1:
                    int p = readInt("Do uu tien (1-5): ");
                    manager.filterPriority(p, currentTime);
                    break;
                case 2:
                    System.out.println("Nhap moc bat dau:");
                    int d1 = readInt("Ngay: ");
                    int m1 = readInt("Thang: ");
                    int y1 = readInt("Nam: ");
                    int h1 = readInt("Gio: ");
                    int min1 = readInt("Phut: ");
                    System.out.println("Nhap moc ket thuc:");
                    int d2 = readInt("Ngay: ");
                    int m2 = readInt("Thang: ");

                    int y2 = readInt("Nam: ");
                    int h2 = readInt("Gio: ");
                    int min2 = readInt("Phut: ");

                    manager.filterByRange(d1, m1, y1, h1, min1, d2, m2, y2, h2, min2);
                    
                    break;
                case 3:
                    System.out.print("Trang thai (pending/done/canceled): ");
                    String stFilter = sc.nextLine();
                    manager.filterByStatus(stFilter, currentTime);
                    break;
                default: System.out.println("Sai chon.");
            }
        }catch(Exception e){
            System.out.println("loi: " + e.getMessage());
        }
    }

    private static void updateEvent(long currentTime) {
        System.out.print("Nhap ID can cap nhat: ");
        String updId = sc.nextLine();
        int idx = manager.findById(updId);
        if (idx == -1) {
            System.out.println("Khong thay.");
            return;
        }
        Task task = manager.getTask()[idx];
        System.out.println("De trong neu khong doi.");
        System.out.print("Tieu de moi (" + task.getTille() + "): ");
        String newTille = sc.nextLine();

        if (!newTille.isEmpty()) task.setTille(newTille);
        System.out.print("Mo ta moi (" + task.getDescription() + "): ");
        String newDesc = sc.nextLine();

        if (!newDesc.isEmpty()) task.setDescription(newDesc);
        System.out.print("Do uu tien moi (" + task.getPriority() + "): ");
        String pStr = sc.nextLine();

        if (!pStr.isEmpty()) task.setPriority(Integer.parseInt(pStr));
        System.out.print("Thoi gian thuc hien moi (phut) (" + task.getDuration() + "): ");
        String durStr = sc.nextLine();

        if (!durStr.isEmpty()) task.setDuration(Integer.parseInt(durStr));
        System.out.print("Dia diem moi (" + task.getLocation() + "): ");
        String newLocation = sc.nextLine();

        if (!newLocation.isEmpty()) task.setLocation(newLocation);
        System.out.print("Email moi (" + task.getEmail() + "): ");
        String newEmail = sc.nextLine();
        if (!newEmail.isEmpty()) task.setEmail(newEmail);

        System.out.print("Doi tac moi (" + task.getPartner() + "): ");
        String newPartner = sc.nextLine();

        if (!newPartner.isEmpty()) task.setPartner(newPartner);
        System.out.println("Loai hinh moi:");
        System.out.println("1. work");
        System.out.println("2. study");
        System.out.println("3. meeting");
        System.out.println("4. conference");
        int choiceType = readInt("Nhap so (1-4): ");
        String newType;
        switch (choiceType) {
            case 1: newType = "work"; break;
            case 2: newType = "study"; break;
            case 3: newType = "meeting"; break;
            case 4: newType = "conference"; break;
            default: newType = task.getMeetingType();
        }
        task.setMeetingType(newType);
        String revStr = readString("Doanh thu moi (" + task.getRevenue() + "): ");

        if (!revStr.isEmpty()) task.setRevenue(Double.parseDouble(revStr));
        System.out.print("Huy cong viec? (y/n, hien tai: " + (task.isCanceled() ? "da huy" : "chua huy") + "): ");
        String cancelChoice = sc.nextLine();
        if (!cancelChoice.isEmpty()) task.setCanceled(cancelChoice.equalsIgnoreCase("y"));

        System.out.print("Thoi gian bat dau moi (ngay thang nam gio phut, cach nhau bang space): ");
        String timeLine = sc.nextLine();
        if (!timeLine.isEmpty()) {
            String[] parts = timeLine.split(" ");
            if (parts.length == 5) {
                int d = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int h = Integer.parseInt(parts[3]);
                int min = Integer.parseInt(parts[4]);
                if (m >= 1 && m <= 12 && d >= 1 && d <= daysInMonth(m, y) && h >= 0 && h <= 23 && min >= 0 && min <= 59) {
                    long startNew = Task.toMinutes(d, m, y, h, min);
                    long endNew = startNew + task.getDuration();
                    boolean conflict = false;
                    for (int i = 0; i < manager.getCount(); i++) {
                        Task other = manager.getTask()[i];
                        if (other == task) continue;
                        long startOld = other.GetTimeStart();
                        long endOld = startOld + other.getDuration();
                        if (startNew < endOld && endNew > startOld) { 
                            conflict = true; break; 
                        }
                    }
                    if (conflict) {
                        System.out.print("CANH BAO trung lich! Van cap nhat? (y/n): ");
                        if (!sc.nextLine().equalsIgnoreCase("y")) return;
                    }
                    task.setDay(d); task.setMonth(m); task.setYear(y);
                    task.setHour(h); task.setMinute(min);
                } else System.out.println("Ngay gio khong hop le, giu nguyen.");
            } else System.out.println("Sai dinh dang, giu nguyen.");
        }
        manager.rebuildQueue(currentTime);
        System.out.println("Cap nhat thanh cong.");
        ManagerFile.log("Cap nhat cong vịec ID " + updId);
    }

    private static void deleteEvent(long currentTime) {
        System.out.print("Nhap ID can xoa: ");
        String delId = sc.nextLine();
        if (manager.deleteTask(delId, currentTime)) {
            System.out.println("Xoa thanh cong.");
            ManagerFile.log("Xoa cong viec ID " + delId);
        } else {
            System.out.println("Khong thay ID " + delId);
        }
    }

    private static void countdownToTask() {
        System.out.print("Nhap ID cong viec: ");
        String cdId = sc.nextLine();
        int idx = manager.findById(cdId);
        if (idx == -1) {
            System.out.println("Khong thay.");
            return;
        }
        Task task = manager.getTask()[idx];
        long nowMins = nowMinutes();
        long eventMins = task.GetTimeStart();
        long diff = eventMins - nowMins;
        if (diff > 0) {
            long days = diff / (24*60);
            long hours = (diff % (24*60)) / 60;
            long minutes = diff % 60;
            System.out.printf("Con %d ngay %d gio %d phut nua den cong viec.\n", days, hours, minutes);
        } else if (diff < 0) {
            long diffPast = -diff;
            long days = diffPast / (24*60);
            long hours = (diffPast % (24*60)) / 60;
            long minutes = diffPast % 60;
            System.out.printf("Cong viec da bat dau %d ngay %d gio %d phut truoc.\n", days, hours, minutes);
        } else {
            System.out.println("Cong viec dang dien ra ngay bay gio!");
        }
    }

    private static void thongKeDoanhThu() {
        double total = manager.getTotalRevenue();
        System.out.printf("TONG DOANH THU: %.2f VND\n", total);
    }
}