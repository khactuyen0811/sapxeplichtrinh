package main;

import java.util.Scanner;

public class Main {
    static final int MAX = 100;
    static String[] id = new String[MAX];
    static String[] title = new String[MAX];
    static String[] desc = new String[MAX];
    static int[] day = new int[MAX];
    static int[] month = new int[MAX];
    static int[] year = new int[MAX];
    static int[] hour = new int[MAX];
    static int[] minute = new int[MAX];
    static String[] st = new String[MAX];
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
                case 4: searchByTitle(); break;
                case 5: sortByStartTime(); displayAll(); break;
                case 6: updateEvent(); break;
                case 7: showCountdown(); break;
                case 0: System.out.println("Tam biet!"); break;
                default: System.out.println("Nhap sai roi, thu lai.");
            }
        } while (choice != 0);
    }

    static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
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
        if (count >= MAX) {
            System.out.println("Day, ko them dc.");
            return;
        }
        System.out.print("Nhap ID: ");
        String newId = sc.nextLine();
        boolean exist = false;
        for (int i = 0; i < count; i++) {
            if (id[i].equals(newId)) {
                exist = true;
                break;
            }
        }
        if (exist) {
            System.out.println("ID da ton tai");
            return;
        }
        id[count] = newId;
        System.out.print("Tieu de: ");
        title[count] = sc.nextLine();
        System.out.print("Mo ta: ");
        desc[count] = sc.nextLine();
        System.out.print("Nhap thoi gian bat dau (day month year hour minute): ");
        int d1 = sc.nextInt();
        int m1 = sc.nextInt();
        int y1 = sc.nextInt();
        int h1 = sc.nextInt();
        int min1 = sc.nextInt();
        sc.nextLine();
        System.out.print("Nhap thoi gian ket thuc (day month year hour minute): ");
        int d2 = sc.nextInt();
        int m2 = sc.nextInt();
        int y2 = sc.nextInt();
        int h2 = sc.nextInt();
        int min2 = sc.nextInt();
        sc.nextLine();

        if (m1 < 1 || m1 > 12 || m2 < 1 || m2 > 12 ||
            d1 < 1 || d1 > daysInMonth(m1, y1) ||
            d2 < 1 || d2 > daysInMonth(m2, y2) ||
            h1 < 0 || h1 > 23 || h2 < 0 || h2 > 23 ||
            min1 < 0 || min1 > 59 || min2 < 0 || min2 > 59) {
            System.out.println("Ngay thang gio phut khong hop le!");
            return;
        }

        if (!isBefore(d1, m1, y1, h1, min1, d2, m2, y2, h2, min2)) {
            System.out.println("Ket thuc phai sau bat dau. Them that bai.");
            return;
        }

        day[count] = d1;
        month[count] = m1;
        year[count] = y1;
        hour[count] = h1;
        minute[count] = min1;
        st[count] = "Available";
        count++;
        System.out.println("Them thanh cong");
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

    static void printEvent(int idx) {
        System.out.println("==================================");
        System.out.println("ID: " + id[idx]);
        System.out.println("Tieu de: " + title[idx]);
        System.out.println("Mo ta: " + desc[idx]);
        System.out.printf("Thoi gian bat dau: %02d/%02d/%04d %02d:%02d\n", day[idx], month[idx], year[idx], hour[idx], minute[idx]);
        System.out.println("Trang thai: " + st[idx]);
    }

    static void deleteEvent() {
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
        }
        count--;
        System.out.println("Xoa thanh cong.");
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
    }

    static void updateEvent() {
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
        System.out.println("Cap nhat ok.");
    }

    static void showCountdown() {
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
        System.out.print("Nhap thoi gian hien tai (day month year hour minute): ");
        int dNow = sc.nextInt();
        int mNow = sc.nextInt();
        int yNow = sc.nextInt();
        int hNow = sc.nextInt();
        int minNow = sc.nextInt();
        sc.nextLine();

        if (mNow < 1 || mNow > 12 || dNow < 1 || dNow > daysInMonth(mNow, yNow) ||
            hNow < 0 || hNow > 23 || minNow < 0 || minNow > 59) {
            System.out.println("Thoi gian hien tai khong hop le!");
            return;
        }

        long nowMins = totalMinutes(dNow, mNow, yNow, hNow, minNow);
        long eventMins = totalMinutes(day[idx], month[idx], year[idx], hour[idx], minute[idx]);

        long diff = eventMins - nowMins;
        if (diff <= 0) {
            System.out.println("Su kien da dien ra.");
        } else {
            long days = diff / (24 * 60);
            long hours = (diff % (24 * 60)) / 60;
            long minutes = diff % 60;
            System.out.printf("Con %d ngay %d gio %d phut.\n", days, hours, minutes);
        }
    }
}
