package main;

import java.util.Scanner;

public class Main {
    static final int MAX = 100;
    static String[] id = new String[MAX];
    static String[] title = new String[MAX];
    static String[] desc = new String[MAX];

    // thời gian bắt đầu
    static int[] day = new int[MAX];
    static int[] month = new int[MAX];
    static int[] year = new int[MAX];
    static int[] hour = new int[MAX];
    static int[] minute = new int[MAX];
    
    // thời gian kết thúc
    static int [] endDay = new int[MAX];
    static int [] endMonth = new int[MAX];
    static int [] endYear = new int[MAX];
    static int [] endHour = new int[MAX];
    static int [] endMinute = new int[MAX];
    
    static String[] st = new String[MAX];
    static String[] email = new String[MAX];
    static String[] partner = new String[MAX];
    static String[] location = new String[MAX];
    static double[] revenue = new double[MAX];
    static int count = 0;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Them su kien");
            System.out.println("2. Hien thi tat ca");
            System.out.println("3. Xoa su kien theo ID");
            System.out.println("4. Tim kiem theo tieu de");
            System.out.println("5. Sap xep theo thoi gian bat dau");
            System.out.println("6. Cap nhat su kien");
            System.out.println("7. Dem nguoc den su kien (nhap thoi gian hien tai)");
            System.out.println("8. Thong ke doanh thu");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1: addEvent(); break;
                case 2: displayAll(); break;
                case 3: deleteEvent(); break;
                case 4: searchMenu(); break;
                case 5: sortByStartTime(); displayAll(); break;
                case 6: updateEvent(); break;
                case 7: showCountdown(); break;
                case 8: thongKeDoanhThu(); break;
                case 0: System.out.println("Tam biet!"); break;
                default: System.out.println("Nhap sai roi, thu lai.");
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

    static boolean isBefore(int d1, int m1, int y1, int h1, int min1,
                            int d2, int m2, int y2, int h2, int min2) {
        if (y1 != y2) return y1 < y2;
        if (m1 != m2) return m1 < m2;
        if (d1 != d2) return d1 < d2;
        if (h1 != h2) return h1 < h2;
        return min1 < min2;
    }

    static long totalMinutes(int d, int m, int y, int h, int min) {
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

    static void addEvent() {
        // hàm dùng để thêm sự kiện 
        if (count < 0) {
            System.out.println("loi khong the them duoc su kien");
            return;
        }

        if (count >= MAX) {
            System.out.println("Day, ko them dc.");
            return;
        }
        String newId = "";
        while (true) {
            System.out.print("Nhap ID: ");
            newId = sc.nextLine();

            boolean exist = false;
            for (int i = 0; i < count; i++) {
                if (id[i].equals(newId)) {
                    exist = true;
                    break;
                }
            }

            if (exist) {
                System.out.println("ID da ton tai! Vui long nhap lai ID khac.");
            } else {
                break;
            }
        }
        id[count] = newId;
        System.out.print("Tieu de: ");
        title[count] = sc.nextLine();
        System.out.print("Mo ta: ");
        desc[count] = sc.nextLine();
        System.out.print("Email: ");
        email[count] = sc.nextLine();
        System.out.print("Partner: ");
        partner[count] = sc.nextLine();
        System.out.print("Dia diem: ");
        location[count] = sc.nextLine();
        revenue[count] = readDouble("Doanh thu (VND): ");
        int h1, min1, d1, m1, y1;
        int h2, min2, d2, m2, y2;

        while (true) {
            System.out.println("\n--- NHAP THOI GIAN SU KIEN ---");
            System.out.println("Nhap thoi gian bat dau:");
            h1 = readInt("Gio: ");
            min1 = readInt("Phut: ");
            d1 = readInt("Ngay: ");
            m1 = readInt("Thang: ");
            y1 = readInt("Nam: ");
            
            System.out.println("Nhap thoi gian ket thuc:");
            h2 = readInt("Gio: ");
            min2 = readInt("Phut: ");
            d2 = readInt("Ngay: ");
            m2 = readInt("Thang: ");
            y2 = readInt("Nam: ");

            if (m1 < 1 || m1 > 12 || m2 < 1 || m2 > 12 ||
                d1 < 1 || d1 > daysInMonth(m1, y1) ||
                d2 < 1 || d2 > daysInMonth(m2, y2) ||
                h1 < 0 || h1 > 23 || h2 < 0 || h2 > 23 ||
                min1 < 0 || min1 > 59 || min2 < 0 || min2 > 59) {
                
                System.out.println("Loi: Ngay thang gio phut khong ton tai! Vui long nhap lai.");
                continue; 
            }

            if (!isBefore(d1, m1, y1, h1, min1, d2, m2, y2, h2, min2)) {
                System.out.println("Loi: Thoi gian ket thuc phai SAU thoi gian bat dau! Vui long nhap lai.");
                continue; 
            }
            break; 
        }

        day[count] = d1;
        month[count] = m1;
        year[count] = y1;
        hour[count] = h1;
        minute[count] = min1;
        st[count] = "Available";
        endDay[count] = d2;
        endMonth[count] = m2;   
        endYear[count] = y2;
        endHour[count] = h2;
        endMinute[count] = min2;
        count++;
        System.out.println("Them thanh cong");
    }

    static void displayAll() {
        if (count < 0) {
            System.out.println("Danh sach trong.");
            return;
        }
        if (count >= 0 || count <= MAX) {
        for (int i = 0; i < count; i++) {
            printEvent(i);
        }
        if (count > MAX){
            System.out.println("Danh sach da day, khong the hien thi het.");
            
        }
    }

    }

    static void printEvent(int idx) {
        System.out.println("==================================");
        System.out.println("ID: " + id[idx]);
        System.out.println("Tieu de: " + title[idx]);
        System.out.println("Mo ta: " + desc[idx]);
        System.out.printf("Thoi gian bat dau: %02d:%02d %02d/%02d/%04d\n", hour[idx], minute[idx], day[idx], month[idx], year[idx]);
        System.out.println("Trang thai: " + st[idx]);
        System.out.printf("Thoi gian ket thuc: %02d:%02d %02d/%02d/%04d\n", endHour[idx], endMinute[idx], endDay[idx], endMonth[idx], endYear[idx]);
        System.out.println("Email: " + email[idx]);
        System.out.println("Doi tac: " + partner[idx]);
        System.out.println("Dia diem: " + location[idx]);
        System.out.printf("Doanh thu: %.2f VND\n", revenue[idx]);
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
        for (int i = idx; i < count - 1; i++) {
            id[i] = id[i+1];
            title[i] = title[i+1];
            desc[i] = desc[i+1];
            day[i] = day[i+1];
            month[i] = month[i+1];
            year[i] = year[i+1];
            hour[i] = hour[i+1];
            minute[i] = minute[i+1];
            st[i] = st[i+1];
            email[i] = email[i+1];
            partner[i] = partner[i+1];
            location[i] = location[i+1];
            revenue[i] = revenue[i+1];

            // end
            endDay[i] = endDay[i+1];
            endMonth[i] = endMonth[i+1];
            endYear[i] = endYear[i+1];
            endHour[i] = endHour[i+1];
            endMinute[i] = endMinute[i+1];
        }
        count--;
        System.out.println("Xoa thanh cong.");
    }

    // menu tim kiem
    static void searchMenu() {
        if (count == 0) {
            System.out.println("Danh sach trong, khong co gi de tim.");
            return;
        }
        int subChoice;
        do {
            System.out.println("\n--- TIM KIEM NANG CAO ---");
            System.out.println("1. Tim theo tieu de (chuoi con)");
            System.out.println("2. Tim theo ID chinh xac");
            System.out.println("3. Tim theo dia diem (chuoi con)");
            System.out.println("4. Tim theo doi tac (chuoi con)");
            System.out.println("0. Quay lai menu chinh");
            System.out.print("Chon: ");
            try {
                subChoice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                subChoice = -1;
            }
            switch (subChoice) {
                case 1: searchByTitle(); break;
                case 2: searchByExactID(); break;
                case 3: searchByLocation(); break;
                case 4: searchByPartner(); break;
                case 0: System.out.println("Thoat tim kiem."); break;
                default: System.out.println("Nhap sai, thu lai.");
            }
        } while (subChoice != 0);
    }

    static void searchByExactID() {
        System.out.println("\nDanh sach ID hien co:");
        for (int i = 0; i < count; i++) {
            System.out.println(" - " + id[i]);
        }
        System.out.print("Nhap ID can tim: ");
        String keyword = sc.nextLine();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (id[i].equals(keyword)) {
                printEvent(i);
                found = true;
                break;
            }
        }
        if (!found) System.out.println("Khong tim thay ID \"" + keyword + "\".");
    }

    static void searchByLocation() {
        // Hiển thị danh sách địa điểm đã có (không trùng)
        System.out.println("\nDanh sach dia diem hien co:");
        String[] locList = new String[MAX];
        int locCount = 0;
        for (int i = 0; i < count; i++) {
            boolean existed = false;
            for (int j = 0; j < locCount; j++) {
                if (locList[j].equals(location[i])) {
                    existed = true;
                    break;
                }
            }
            if (!existed && !location[i].isEmpty()) {
                locList[locCount++] = location[i];
            }
        }
        if (locCount == 0) {
            System.out.println("Chua co dia diem nao.");
        } else {
            for (int j = 0; j < locCount; j++) {
                System.out.println(" - " + locList[j]);
            }
        }
        System.out.print("Nhap tu khoa dia diem (chuoi con): ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (location[i].toLowerCase().contains(keyword)) {
                printEvent(i);
                found = true;
            }
        }
        if (!found) System.out.println("Khong tim thay dia diem chua \"" + keyword + "\".");
    }

    static void searchByPartner() {
        System.out.println("\nDanh sach doi tac hien co:");
        String[] partList = new String[MAX];
        int partCount = 0;
        for (int i = 0; i < count; i++) {
            boolean existed = false;
            for (int j = 0; j < partCount; j++) {
                if (partList[j].equals(partner[i])) {
                    existed = true;
                    break;
                }
            }
            if (!existed && !partner[i].isEmpty()) {
                partList[partCount++] = partner[i];
            }
        }
        if (partCount == 0) {
            System.out.println("Chua co doi tac nao.");
        } else {
            for (int j = 0; j < partCount; j++) {
                System.out.println(" - " + partList[j]);
            }
        }
        System.out.print("Nhap tu khoa doi tac (chuoi con): ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (partner[i].toLowerCase().contains(keyword)) {
                printEvent(i);
                found = true;
            }
        }
        if (!found) System.out.println("Khong tim thay doi tac chua \"" + keyword + "\".");
    }

    static void searchByTitle() {
        System.out.print("Nhap tu khoa: ");
        String keyword = sc.nextLine().toLowerCase();
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (title[i].toLowerCase().contains(keyword)) {
                printEvent(i);
                found = true;
            }
        }
        if (!found) System.out.println("Khong tim thay");
    }

    static void sortByStartTime() {
        if (count < 2) return;
        for (int i = 0; i < count-1; i++) {
            for (int j = 0; j < count-1-i; j++) {
                if (!isBefore(day[j], month[j], year[j], hour[j], minute[j],
                              day[j+1], month[j+1], year[j+1], hour[j+1], minute[j+1])) {
                    swap(j, j+1);
                }
            }
        }
        System.out.println("Da sap xep xong.");
    }

    static void swap(int i, int j) {
        String tmp = id[i]; id[i] = id[j]; id[j] = tmp;
        tmp = title[i]; title[i] = title[j]; title[j] = tmp;
        tmp = desc[i]; desc[i] = desc[j]; desc[j] = tmp;
        tmp = st[i]; st[i] = st[j]; st[j] = tmp;
        int t = day[i]; day[i] = day[j]; day[j] = t;
        t = month[i]; month[i] = month[j]; month[j] = t;
        t = year[i]; year[i] = year[j]; year[j] = t;
        t = hour[i]; hour[i] = hour[j]; hour[j] = t;
        t = minute[i]; minute[i] = minute[j]; minute[j] = t;
        String tmpStr = email[i]; email[i] = email[j]; email[j] = tmpStr;
        tmpStr = partner[i]; partner[i] = partner[j]; partner[j] = tmpStr;
        tmpStr = location[i]; location[i] = location[j]; location[j] = tmpStr;
        double tmpD = revenue[i]; revenue[i] = revenue[j]; revenue[j] = tmpD;

        // end
        t = endDay[i]; endDay[i] = endDay[j]; endDay[j] = t;
        t = endMonth[i]; endMonth[i] = endMonth[j]; endMonth[j] = t;
        t = endYear[i]; endYear[i] = endYear[j]; endYear[j] = t;
        t = endHour[i]; endHour[i] = endHour[j]; endHour[j] = t;
        t = endMinute[i]; endMinute[i] = endMinute[j]; endMinute[j] = t;
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
        System.out.print("Tieu de moi: ");
        String newTitle = sc.nextLine();
        if (!newTitle.isEmpty()) title[idx] = newTitle;
        System.out.print("Mo ta moi: ");
        String newDesc = sc.nextLine();
        if (!newDesc.isEmpty()) desc[idx] = newDesc;
        System.out.print("Thoi gian bat dau moi (day month year hour minute): ");
        String line = sc.nextLine();
        if (!line.isEmpty()) {
            String[] parts = line.split(" ");
            if (parts.length == 5) {
                int d = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int h = Integer.parseInt(parts[3]);
                int min = Integer.parseInt(parts[4]);
                if (m >= 1 && m <= 12 && d >= 1 && d <= daysInMonth(m, y) &&
                    h >= 0 && h <= 23 && min >= 0 && min <= 59) {
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
        System.out.print("Thoi gian ket thuc moi (day month year hour minute), de trong neu khong doi: ");
        String lineEnd = sc.nextLine();
        if (!lineEnd.isEmpty()) {
            String[] parts = lineEnd.split(" ");
            if (parts.length == 5) {
                try {
                    int h = Integer.parseInt(parts[0]);
                    int min = Integer.parseInt(parts[1]);
                    int d = Integer.parseInt(parts[2]);
                    int m = Integer.parseInt(parts[3]);
                    int y = Integer.parseInt(parts[4]);
                    if (m >= 1 && m <= 12 && d >= 1 && d <= daysInMonth(m, y) &&
                        h >= 0 && h <= 23 && min >= 0 && min <= 59) {
                        if (isBefore(day[idx], month[idx], year[idx], hour[idx], minute[idx], d, m, y, h, min)) {
                            endDay[idx] = d;
                            endMonth[idx] = m;
                            endYear[idx] = y;
                            endHour[idx] = h;
                            endMinute[idx] = min;
                        } else {
                            System.out.println("Thoi gian ket thuc moi phai sau thoi gian bat dau, giu nguyen.");
                        }
                    } else {
                        System.out.println("Ngay gio khong hop le, giu nguyen.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Sai dinh dang so, giu nguyen.");
                }
            } else {
                System.out.println("Sai dinh dang, giu nguyen.");
            }
        } 
        System.out.println("Cap nhat ok.");
    }

    static void showCountdown() {
        displayAll();
        System.out.print("Nhap ID su kien: ");
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

        System.out.print("Nhap thoi gian hien tai (hour minute day month year): ");
        int hNow = readInt("Gio: ");
        int minNow = readInt("Phut: ");
        int dNow = readInt("Ngay: ");
        int mNow = readInt("Thang: ");
        int yNow = readInt("Nam: ");

        // Kiểm tra tính hợp lệ
        if (mNow < 1 || mNow > 12 || dNow < 1 || dNow > daysInMonth(mNow, yNow) ||
            hNow < 0 || hNow > 23 || minNow < 0 || minNow > 59) {
            System.out.println("Thoi gian hien tai khong hop le!");
            return;
        }

        // Lấy thời gian sự kiện
        int dEvent = day[idx];
        int mEvent = month[idx];
        int yEvent = year[idx];
        int hEvent = hour[idx];
        int minEvent = minute[idx];

        
        int mTmp = mEvent, yTmp = yEvent;
        if (mTmp == 1 || mTmp == 2) {
            mTmp += 12;
            yTmp--;
        }
        int K = yTmp % 100;
        int J = yTmp / 100;
        int h = (dEvent + (13 * (mTmp + 1)) / 5 + K + K/4 + J/4 + 5 * J) % 7;
        String[] thuArr = {"Thu Bay", "Chu Nhat", "Thu Hai", "Thu Ba", "Thu Tu", "Thu Nam", "Thu Sau"};
        String thu = thuArr[h];

        // Hiển thị thông tin sự kiện
        System.out.println("\n=== SU KIEN ===");
        System.out.println("ID: " + id[idx]);
        System.out.println("Tieu de: " + title[idx]);
        System.out.printf("Thoi gian bat dau: %02d:%02d %02d/%02d/%04d\n", hEvent, minEvent, dEvent, mEvent, yEvent);
        System.out.printf("Thoi gian ket thuc: %02d:%02d %02d/%02d/%04d\n", endHour[idx], endMinute[idx], endDay[idx], endMonth[idx], endYear[idx]);
        System.out.println("Thu: " + thu);

        // Đếm ngược
        long nowMins = totalMinutes(dNow, mNow, yNow, hNow, minNow);
        long eventMins = totalMinutes(endDay[idx], endMonth[idx], endYear[idx], endHour[idx], endMinute[idx]);
        long diff = eventMins - nowMins;

        if (diff <= 0) {
            System.out.println("Su kien da dien ra.");
        } else {
            long days = diff / (24 * 60);
            long hours = (diff % (24 * 60)) / 60;
            long minutes = diff % 60;
            System.out.printf("Con %d ngay %d gio %d phut nua.\n", days, hours, minutes);
        }
    }
    static void thongKeDoanhThu() {
        if (count == 0) {
            System.out.println("Khong co su kien nao.");
            return;
        }
        double tong = 0;
        System.out.println("\n=== THONG KE DOANH THU ===");
        for (int i = 0; i < count; i++) {
            System.out.printf("ID: %s | Tieu de: %s | Doanh thu: %.2f \n", id[i], title[i], revenue[i]);
            tong += revenue[i];
        }
        System.out.printf("TONG DOANH THU: %.2f \n", tong);
    }
}