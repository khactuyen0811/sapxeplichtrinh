package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    static final int MAX = 200;
    static final String DATA_FILE = "tasks.csv";
    static final String LOG_FILE = "app.log";

    static String[] id = new String[MAX];

    // thoi gian bat dau
    static int[] day = new int[MAX];
    static int[] month = new int[MAX];
    static int[] year = new int[MAX];
    static int[] hour = new int[MAX];
    static int[] minute = new int[MAX];

    static int[] priority = new int[MAX];
    static String[] category = new String[MAX];
    static String[] description = new String[MAX];
    static String[] status = new String[MAX];
    static int[] duration = new int[MAX];  
    static String[] email = new String[MAX];
    static String[] partner = new String[MAX];
    static String[] location = new String[MAX];
    static String[] meetingType = new String[MAX];
    static double[] revenue = new double[MAX]; 

    static int[] queue = new int[MAX];
    static int front = 0, rear = -1, queueSize = 0;
    static int count = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadFromFile();
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
            System.out.println("8. Dem nguoc time den cong viec can lam ");
            System.out.println("9. Xu ly cong viec tiep theo (FIFO)");
            System.out.println("10. Hien thi hang doi");
            System.out.println("11. Thong ke doanh thu");
            System.out.println("0. Luu va thoat");
            System.out.print("Chon: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1: addEvent(); break;
                case 2: displayAll(); break;
                case 3: filterMenu(); break;
                case 4: binarySearchById(); break;
                case 5: sortByStartTime(); displayAll(); break;
                case 6: updateEvent(); break;
                case 7: deleteEvent(); break;
                case 8: countdownToTask(); break;
                case 9: processNextTask(); break;
                case 10: displayQueue(); break;
                case 11: thongKeDoanhThu(); break;
                case 0: saveToFile(); System.out.println("Tam biet!"); break;
                default: System.out.println("Nhap sai, thu lai.");
            }
        } while (choice != 0);
    }


    // hàm chuẩn hóa
    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Nhap sai kieu du lieu,nhap so nguyen.");
            }
        }
    }

    static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Nhap sai kieu du lieu so thuc (vi du: 10.5).");
            }
        }
    }

    static String readString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    static int daysInMonth(int month, int year) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12: return 31;
            case 4: case 6: case 9: case 11: return 30;
            case 2: return isLeapYear(year) ? 29 : 28;
            default: return 0;
        }
    }

    static void log(String msg) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
            PrintWriter pw = new PrintWriter(fw)) {
            pw.println(new java.util.Date() + " - " + msg);
        } catch (IOException e) {
            System.err.println("Lỗi ghi log: " + e.getMessage());
        }
    }

    static long toMinutes(int d, int m, int y, int h, int min) {
        long total = 0;
        for (int i = 1; i < y; i++) {
            total += isLeapYear(i) ? 366 : 365;
        }
        for (int i = 1; i < m; i++) {
            total += daysInMonth(i, y);
        }
        total += (d - 1);
        total = total * 24 + h;
        total = total * 60 + min;
        return total;
    }

    static void countdownToTask() {
        if (count == 0) {
            System.out.println("Chua co cong viec nao.");
            return;
        }
        displayAll();
        System.out.print("Nhap ID cong viec: ");
        String cdId = sc.nextLine();
        int idx = -1;
        for (int i = 0; i < count; i++) {
            if (id[i].equals(cdId)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            System.out.println("Khong thay.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        int hNow = now.getHour();
        int minNow = now.getMinute();
        int dNow = now.getDayOfMonth();
        int mNow = now.getMonthValue();
        int yNow = now.getYear();

        long nowMins = toMinutes(dNow, mNow, yNow, hNow, minNow);
        long eventMins = toMinutes(day[idx], month[idx], year[idx], hour[idx], minute[idx]);

        long diff = eventMins - nowMins;

        if (diff > 0) {
            long days = diff / (24 * 60);
            long hours = (diff % (24 * 60)) / 60;
            long minutes = diff % 60;
            System.out.printf("Con %d ngay %d gio %d phut nua den cong viec.\n", days, hours, minutes);
        } else if (diff < 0) {
            long diffPast = -diff;
            long days = diffPast / (24 * 60);
            long hours = (diffPast % (24 * 60)) / 60;
            long minutes = diffPast % 60;
            System.out.printf("Cong viec da bat dau %d ngay %d gio %d phut truoc.\n", days, hours, minutes);
        } else {
            System.out.println("Cong viec dang dien ra ngay bay gio!");
        }
    }

    //save file
    
    static final int[] W = {8, 4, 4, 6, 4, 4, 4, 12, 20, 10, 6, 20, 15, 15, 15, 12};// do rong cua tung cot

    static void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            // Header
            pw.printf("%-8s %-4s %-4s %-6s %-4s %-4s %-4s %-12s %-20s %-10s %-6s %-20s %-15s %-15s %-15s %-12s%n",
                    "ID", "Ngay", "Thang", "Nam", "Gio", "Phut", "UT", "PhanLoai", "MoTa", "TrangThai", "TGian", "Email", "DoiTac", "DiaDiem", "LoaiHop", "DoanhThu");
            for (int i = 0; i < count; i++) {
                pw.printf("%-8s %-4d %-4d %-6d %-4d %-4d %-4d %-12s %-20s %-10s %-6d %-20s %-15s %-15s %-15s %-12.2f%n",
                        id[i], day[i], month[i], year[i], hour[i], minute[i],
                        priority[i], category[i], description[i], status[i], duration[i],
                        email[i], partner[i], location[i], meetingType[i], revenue[i]);
            }
            log("Da luu " + count + " cong viec vao file (dang bang).");
        } catch (IOException e) {
            System.err.println("Loi ghi file: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            count = 0;
            front = 0; rear = -1; queueSize = 0;
            br.readLine();
            while ((line = br.readLine()) != null && count < MAX) {
                String[] p = new String[16];
                int pos = 0;
                for (int i = 0; i < W.length; i++) {
                    if (pos >= line.length()) break;
                    int end = Math.min(pos + W[i], line.length());
                    p[i] = line.substring(pos, end).trim();
                    pos = end;
                    // bỏ qua khoảng trắng phân cách (1 space)
                    if (pos < line.length() && line.charAt(pos) == ' ') pos++;
                }
                if (p[0] == null) continue;
                try {
                    id[count] = p[0];
                    day[count] = Integer.parseInt(p[1]);
                    month[count] = Integer.parseInt(p[2]);
                    year[count] = Integer.parseInt(p[3]);
                    hour[count] = Integer.parseInt(p[4]);
                    minute[count] = Integer.parseInt(p[5]);
                    priority[count] = Integer.parseInt(p[6]);
                    category[count] = p[7];
                    description[count] = p[8];
                    status[count] = p[9];
                    duration[count] = Integer.parseInt(p[10]);
                    email[count] = p[11];
                    partner[count] = p[12];
                    location[count] = p[13];
                    meetingType[count] = p[14];
                    revenue[count] = Double.parseDouble(p[15]);
                } catch (Exception e) {
                    continue;
                }
                if (status[count].equals("pending")) {
                    rear = (rear + 1) % MAX;
                    queue[rear] = count;
                    queueSize++;
                }
                count++;
            }
            log("Da doc " + count + " cong viec tu file (dang bang).");
        } catch (Exception e) {
            System.err.println("Loi doc file: " + e.getMessage());
        }
    }

    static void addEvent() {
        if (count >= MAX) {
            System.out.println("Day, khong them duoc.");
            return;
        }
        String newId;
        while (true) {
            System.out.print("Nhap ID: ");
            newId = sc.nextLine();
            boolean exist = false;
            for (int i = 0; i < count; i++) {
                if (id[i].equals(newId)) { exist = true; break; }
            }
            if (exist) System.out.println("ID da ton tai, nhap lai.");
            else break;
        }
        id[count] = newId;

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
        day[count] = d;
        month[count] = m;
        year[count] = y;
        hour[count] = h;
        minute[count] = min;

        int prio = readInt("Do uu tien (1-5): ");
        while (prio < 1 || prio > 5) {
            System.out.println("Chi nhap 1 den 5.");
            prio = readInt("Do uu tien: ");
        }
        priority[count] = prio;

        System.out.print("Phan loai: ");
        category[count] = sc.nextLine();
        System.out.print("Mo ta: ");
        description[count] = sc.nextLine();

        String st;
        System.out.println("Chon trang thai:");
        System.out.println("1. pending");
        System.out.println("2. done");
        System.out.println("3. canceled");
        int choiceStatus = readInt("Nhap so (1-3): ");
        switch (choiceStatus) {
            case 1: st = "pending"; break;
            case 2: st = "done"; break;
            case 3: st = "canceled"; break;
            default: st = "pending"; break;
        }

        int dur = readInt("Thoi gian thuc hien (phut): ");
        while (dur <= 0) {
            System.out.println("Phai lon hon 0.");
            dur = readInt("Thoi gian thuc hien: ");
        }
        duration[count] = dur;
        System.out.print("Email: ");
        email[count] = sc.nextLine();
        System.out.print("Doi tac: ");
        partner[count] = sc.nextLine();
        System.out.print("Dia diem: ");
        location[count] = sc.nextLine();
        System.out.print("Loai hinh hop (work/study/meeting/conference): ");
        meetingType[count] = sc.nextLine();
        revenue[count] = readDouble("Doanh thu (VND): ");

        // Kiem tra conflict
        long startNew = toMinutes(d, m, y, h, min);
        long endNew = startNew + dur;
        boolean conflict = false;
        for (int i = 0; i < count; i++) {
            long startOld = toMinutes(day[i], month[i], year[i], hour[i], minute[i]);
            long endOld = startOld + duration[i];
            if (startNew < endOld && endNew > startOld) {
                conflict = true;
                break;
            }
        }
        if (conflict) {
            System.out.println("CANH BAO: Cong viec bi trung lich voi cong viec khac!");
            System.out.print("Van muon them? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                System.out.println("Huy them.");
                return;
            }
        }

        // Them vao queue neu trang thai pending
        if (st.equals("pending")) {
            rear = (rear + 1) % MAX;
            queue[rear] = count;
            queueSize++;
        }

        count++;
        System.out.println("Them thanh cong.");
        log("Them cong viec ID " + newId);
    }

    static String getThu(int d, int m, int y) {
        int mTmp = m, yTmp = y;
        if (mTmp == 1 || mTmp == 2) {
            mTmp += 12;
            yTmp--;
        }
        int K = yTmp % 100;
        int J = yTmp / 100;
        int h = (d + (13 * (mTmp + 1)) / 5 + K + K/4 + J/4 + 5 * J) % 7;
        String[] thuArr = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        return thuArr[h];
    }

    static void printEvent(int idx) {
        System.out.println("==================================");
        System.out.println("ID: " + id[idx]);
        System.out.printf("Thoi gian bat dau: %02d:%02d %02d/%02d/%04d\n", hour[idx], minute[idx], day[idx], month[idx], year[idx]);
        System.out.println("Do uu tien: " + priority[idx]);
        System.out.println("Phan loai: " + category[idx]);
        System.out.println("Mo ta: " + description[idx]);
        System.out.println("Trang thai: " + status[idx]);
        System.out.println("Thoi luong: " + duration[idx] + " phut");
        System.out.println("Email: " + email[idx]);
        System.out.println("Doi tac: " + partner[idx]);
        System.out.println("Dia diem: " + location[idx]);
        System.out.println("Loai hinh: " + meetingType[idx]);
        System.out.printf("Doanh thu: %.2f VND\n", revenue[idx]);
        System.out.println("Thu: " + getThu(day[idx], month[idx], year[idx]));
        
    }

    static void displayAll() {
        if (count == 0) {
            System.out.println("Danh sach trong.");
            return;
        }
        for (int i = 0; i < count; i++) {
            printEvent(i);
        }
    }

    static void deleteEvent() {
        displayAll();
        System.out.print("Nhap ID can xoa: ");
        String delId = sc.nextLine();
        int idx = -1;
        for (int i = 0; i < count; i++) {
            if (id[i].equals(delId)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            System.out.println("Khong thay ID " + delId);
            return;
        }

        // Xoa khoi queue

        int newRear = rear;
        for (int i = front; i <= rear; i++) {
            if (queue[i % MAX] == idx) {
                // dich trai queue
                for (int j = i; j < rear; j++) {
                    queue[j % MAX] = queue[(j+1) % MAX];
                }
                newRear--;
                queueSize--;
                break;
            }
        }
        rear = newRear;
        if (queueSize == 0) {
            front = 0;
            rear = -1;
        }

        // Xoa khoi mang chinh
        for (int i = idx; i < count - 1; i++) {
            id[i] = id[i+1];
            day[i] = day[i+1]; month[i] = month[i+1]; year[i] = year[i+1];
            hour[i] = hour[i+1]; minute[i] = minute[i+1];
            priority[i] = priority[i+1];
            category[i] = category[i+1];
            description[i] = description[i+1];
            status[i] = status[i+1];
            duration[i] = duration[i+1];
            email[i] = email[i+1];
            partner[i] = partner[i+1];
            location[i] = location[i+1];
            meetingType[i] = meetingType[i+1];
            revenue[i] = revenue[i+1];
        }
        count--;
        System.out.println("Xoa thanh cong.");
        log("Xoa cong viec ID " + delId);
    }

    static void sortByStartTime() {
        if (count < 2) return;
        for (int i = 0; i < count-1; i++) {
            for (int j = 0; j < count-1-i; j++) {
                long t1 = toMinutes(day[j], month[j], year[j], hour[j], minute[j]);
                long t2 = toMinutes(day[j+1], month[j+1], year[j+1], hour[j+1], minute[j+1]);
                if (t1 > t2) {
                    swap(j, j+1);
                }
            }
        }
        System.out.println("Da sap xep theo thoi gian bat dau.");
        log("Sap xep cong viec theo thoi gian.");
    }

    static void swap(int i, int j) {
        String tmp;
        int t;
        tmp = id[i]; id[i] = id[j]; id[j] = tmp;
        t = day[i]; day[i] = day[j]; day[j] = t;
        t = month[i]; month[i] = month[j]; month[j] = t;
        t = year[i]; year[i] = year[j]; year[j] = t;
        t = hour[i]; hour[i] = hour[j]; hour[j] = t;
        t = minute[i]; minute[i] = minute[j]; minute[j] = t;
        t = priority[i]; priority[i] = priority[j]; priority[j] = t;
        tmp = category[i]; category[i] = category[j]; category[j] = tmp;
        tmp = description[i]; description[i] = description[j]; description[j] = tmp;
        tmp = status[i]; status[i] = status[j]; status[j] = tmp;
        t = duration[i]; duration[i] = duration[j]; duration[j] = t;
        tmp = email[i]; email[i] = email[j]; email[j] = tmp;
        tmp = partner[i]; partner[i] = partner[j]; partner[j] = tmp;
        tmp = location[i]; location[i] = location[j]; location[j] = tmp;
        tmp = meetingType[i]; meetingType[i] = meetingType[j]; meetingType[j] = tmp;
        double tmpD = revenue[i]; revenue[i] = revenue[j]; revenue[j] = tmpD;
    }

    static void binarySearchById() {
        // Tao mang index sap xep theo ID
        int[] idxArr = new int[count];
        for (int i = 0; i < count; i++) idxArr[i] = i;
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (id[idxArr[j]].compareTo(id[idxArr[j + 1]]) > 0) {
                    int t = idxArr[j];
                    idxArr[j] = idxArr[j + 1];
                    idxArr[j + 1] = t;
                }
            }
        }
        System.out.print("Nhap ID can tim: ");
        String key = sc.nextLine();
        int left = 0, right = count - 1;
        boolean found = false;
        while (left <= right) {
            int mid = (left + right) / 2;
            int cmp = id[idxArr[mid]].compareTo(key);
            if (cmp == 0) {
                printEvent(idxArr[mid]);
                found = true;
                break;
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        if (!found) System.out.println("Khong tim thay ID " + key);
    }

    static void updateEvent() {
        displayAll();
        System.out.print("Nhap ID can cap nhat: ");
        String updId = sc.nextLine();
        int idx = -1;
        for (int i = 0; i < count; i++) {
            if (id[i].equals(updId)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            System.out.println("Khong thay.");
            return;
        }
        System.out.println("De trong neu khong doi.");
        System.out.print("Mo ta moi: ");
        String newDesc = sc.nextLine();
        String newEmail = readString("Email moi: ");
        if (!newEmail.isEmpty()) email[idx] = newEmail;
        String newPartner = readString("Doi tac moi: ");
        if (!newPartner.isEmpty()) partner[idx] = newPartner;
        String newLocation = readString("Dia diem moi: ");
        if (!newLocation.isEmpty()) location[idx] = newLocation;
        String newMeetingType = readString("Loai hinh hop moi: ");
        if (!newMeetingType.isEmpty()) meetingType[idx] = newMeetingType;
        String revStr = readString("Doanh thu moi: ");
        if (!revStr.isEmpty()) {
            double rev = Double.parseDouble(revStr);
            if (rev >= 0) revenue[idx] = rev;
        }
        if (!newDesc.isEmpty()) description[idx] = newDesc;

        String pStr = readString("Do uu tien moi (1-5): ");
        if (!pStr.isEmpty()) {
            int p = Integer.parseInt(pStr);
            if (p >= 1 && p <= 5) priority[idx] = p;
        }

        String cat = readString("Phan loai moi: ");
        if (!cat.isEmpty()) category[idx] = cat;

        System.out.println("Chon trang thai moi:");
        System.out.println("1. pending");
        System.out.println("2. done");
        System.out.println("3. canceled");
        System.out.println("0. bo qua (giu nguyen)");
        int chonSt = readInt("Nhap so: ");
        String st = "";
        switch (chonSt) {
            case 1: st = "pending"; break;
            case 2: st = "done"; break;
            case 3: st = "canceled"; break;
            default: st = "";
        }
        if (!st.isEmpty()) {
            // Cap nhat queue neu trang thai thay doi
            if (status[idx].equals("pending") && !st.equals("pending")) {
                // xoa khoi queue
                for (int i = front; i <= rear; i++) {
                    if (queue[i % MAX] == idx) {
                        for (int j = i; j < rear; j++) {
                            queue[j % MAX] = queue[(j + 1) % MAX];
                        }
                        rear--;
                        queueSize--;
                        break;
                    }
                }
                if (queueSize == 0) {
                    front = 0;
                    rear = -1;
                }
            } else if (!status[idx].equals("pending") && st.equals("pending")) {
                // them vao queue
                rear = (rear + 1) % MAX;
                queue[rear] = idx;
                queueSize++;
            }
            status[idx] = st;
        }

        String durStr = readString("Thoi gian thuc hien moi (phut): ");
        if (!durStr.isEmpty()) {
            int dur = Integer.parseInt(durStr);
            if (dur > 0) duration[idx] = dur;
        }

        // Cap nhat thoi gian bat dau
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
                    // Kiem tra conflict
                    long startNew = toMinutes(d, m, y, h, min);
                    long endNew = startNew + duration[idx];
                    boolean conflict = false;
                    for (int i = 0; i < count; i++) {
                        if (i == idx) continue;
                        long startOld = toMinutes(day[i], month[i], year[i], hour[i], minute[i]);
                        long endOld = startOld + duration[i];
                        if (startNew < endOld && endNew > startOld) {
                            conflict = true;
                            break;
                        }
                    }
                    if (conflict) {
                        System.out.print("CANH BAO trung lich! Van cap nhat? (y/n): ");
                        if (!sc.nextLine().equalsIgnoreCase("y")) return;
                    }
                    day[idx] = d;
                    month[idx] = m;
                    year[idx] = y;
                    hour[idx] = h;
                    minute[idx] = min;
                } else {
                    System.out.println("Ngay gio khong hop le, giu nguyen.");
                }
            } else {
                System.out.println("Sai dinh dang, giu nguyen.");
            }
        }
        System.out.println("Cap nhat thanh cong.");
        log("Cap nhat cong viec ID " + updId);
    }

    
    // static void thongKeDoanhThu() {
    //     if (count == 0) {
    //         System.out.println("Khong co su kien nao.");
    //         return;
    //     }
    //     double tong = 0;
    //     System.out.println("\n=== THONG KE DOANH THU ===");
    //     for (int i = 0; i < count; i++) {
    //         System.out.printf("ID: %s | Tieu de: %s | Doanh thu: %.2f \n", id[i], title[i], revenue[i]);
    //         tong += revenue[i];
    //     }
    //     System.out.printf("TONG DOANH THU: %.2f \n", tong);
    // }

        static void filterMenu() {
        System.out.println("\n--- LOC CONG VIEC ---");
        System.out.println("1. Theo do uu tien");
        System.out.println("2. Theo khoang thoi gian");
        System.out.println("3. Theo trang thai");
        System.out.print("Chon: ");
        int chon = Integer.parseInt(sc.nextLine());
        switch (chon) {
            case 1:
                int p = readInt("Do uu tien (1-5): ");
                for (int i = 0; i < count; i++) {
                    if (priority[i] == p) printEvent(i);
                }
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
                long startM = toMinutes(d1, m1, y1, h1, min1);
                long endM = toMinutes(d2, m2, y2, h2, min2);
                for (int i = 0; i < count; i++) {
                    long t = toMinutes(day[i], month[i], year[i], hour[i], minute[i]);
                    if (t >= startM && t <= endM) printEvent(i);
                }
                break;
            case 3:
                System.out.print("Trang thai (pending/done/canceled): ");
                String st = sc.nextLine();
                for (int i = 0; i < count; i++) {
                    if (status[i].equals(st)) printEvent(i);
                }
                break;
            default:
                System.out.println("Sai chon.");
        }
    }

    static void processNextTask() {
        if (queueSize == 0) {
            System.out.println("Hang doi rong.");
            return;
        }
        int idx = queue[front];
        front = (front + 1) % MAX;
        queueSize--;
        System.out.println("Dang xu ly cong viec:");
        printEvent(idx);
        System.out.print("Danh dau la done? (y/n): ");
        if (sc.nextLine().equalsIgnoreCase("y")) {
            status[idx] = "done";
            System.out.println("Da cap nhat trang thai done.");
            log("Da xu ly xong cong viec ID " + id[idx]);
        } else {
            // dua lai vao cuoi queue
            rear = (rear + 1) % MAX;
            queue[rear] = idx;
            queueSize++;
            System.out.println("Chua xu ly, dua ve cuoi hang doi.");
        }
    }

    static void displayQueue() {
        if (queueSize == 0) {
            System.out.println("Hang doi trong.");
            return;
        }
        System.out.println("Danh sach cong viec cho xu ly (FIFO):");
        int i = front;
        for (int k = 0; k < queueSize; k++) {
            int idx = queue[i % MAX];
            String shortDesc = description[idx].length() > 20 ? description[idx].substring(0, 20) + "..." : description[idx];
            System.out.printf("- %s : %s\n", id[idx], shortDesc);
            i++;
        }
    }

    static void thongKeDoanhThu() {
        if (count == 0) {
            System.out.println("Khong co du lieu.");
            return;
        }
        double tong = 0;
        System.out.println("\n=== THONG KE DOANH THU ===");
        for (int i = 0; i < count; i++) {
            System.out.printf("ID: %s | Mo ta: %s | Doanh thu: %.2f VND\n", id[i], description[i], revenue[i]);
            tong += revenue[i];
        }
        System.out.printf("TONG DOANH THU: %.2f VND\n", tong);
    }
}