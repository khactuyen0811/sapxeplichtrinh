package main;

import java.util.Calendar;
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
                System.out.println("7. Dem nguoc den su kien");
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
            System.out.print("Nhap (day month year hour minute): ");
            day[count] = sc.nextInt();
            month[count] = sc.nextInt();
            year[count] = sc.nextInt();
            hour[count] = sc.nextInt();
            minute[count] = sc.nextInt();
            sc.nextLine();
            System.out.print("Nhap thoi gian ket thuc (day month year hour minute): ");
            int d2 = sc.nextInt();
            int m2 = sc.nextInt();
            int y2 = sc.nextInt();
            int h2 = sc.nextInt();
            int min2 = sc.nextInt();
            sc.nextLine();
            Calendar startCal = Calendar.getInstance();
            startCal.set(year[count], month[count]-1, day[count], hour[count], minute[count]);
            Calendar endCal = Calendar.getInstance();
            endCal.set(y2, m2-1, d2, h2, min2);
            if (endCal.before(startCal) || endCal.equals(startCal)) {
                System.out.println("Ket thuc phai sau bat dau. Them that bai.");
                return;
            }
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
                    int y1 = year[j], y2 = year[j+1];
                    int m1 = month[j], m2 = month[j+1];
                    int d1 = day[j], d2 = day[j+1];
                    int h1 = hour[j], h2 = hour[j+1];
                    int mi1 = minute[j], mi2 = minute[j+1];
                    if (y1 > y2 ||
                        (y1 == y2 && m1 > m2) ||
                        (y1 == y2 && m1 == m2 && d1 > d2) ||
                        (y1 == y2 && m1 == m2 && d1 == d2 && h1 > h2) ||
                        (y1 == y2 && m1 == m2 && d1 == d2 && h1 == h2 && mi1 > mi2)) {
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
                    day[idx] = Integer.parseInt(parts[0]);
                    month[idx] = Integer.parseInt(parts[1]);
                    year[idx] = Integer.parseInt(parts[2]);
                    hour[idx] = Integer.parseInt(parts[3]);
                    minute[idx] = Integer.parseInt(parts[4]);
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
            Calendar now = Calendar.getInstance();
            Calendar eventStart = Calendar.getInstance();
            eventStart.set(year[idx], month[idx]-1, day[idx], hour[idx], minute[idx]);
            long diffMillis = eventStart.getTimeInMillis() - now.getTimeInMillis();
            if (diffMillis <= 0) {
                System.out.println("Su kien da dien ra.");
            } else {
                long diffMinutes = diffMillis / (1000 * 60);
                long days = diffMinutes / (24 * 60);
                long hours = (diffMinutes % (24 * 60)) / 60;
                long minutes = diffMinutes % 60;
                System.out.printf("Con %d ngay %d gio %d phut.\n", days, hours, minutes);
            }
        }
}