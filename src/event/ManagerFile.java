package event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class ManagerFile {
    private static final String fileName = "tasks.csv";
    private static final String logFile = "app.log";
    private static final int[] W = {8, 4, 4, 6, 4, 4, 20, 30, 6, 6, 6, 20, 15, 15, 15, 12};

    public static void log(String mess) {
        try (FileWriter fw = new FileWriter(logFile, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(new Date() + " - " + mess);
        } catch (IOException e) {
            System.err.println("Loi ghi log: " + e.getMessage());
        }
    }

    public static void saveTasks(Task[] tasks, int count) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName))) {
            // Header
            pw.printf("%-8s %-4s %-4s %-6s %-4s %-4s %-20s %-30s %-6s %-6s %-6s %-20s %-15s %-15s %-15s %-12s%n",
                    "ID", "Ngay", "Thang", "Nam", "Gio", "Phut", "TieuDe", "MoTa", "Huy", "TGian", "UuTien",
                    "Email", "DoiTac", "DiaDiem", "LoaiHop", "DoanhThu");
            for (int i = 0; i < count; i++) {
                Task t = tasks[i];
                pw.printf("%-8s %-4d %-4d %-6d %-4d %-4d %-20s %-30s %-6s %-6d %-6d %-20s %-15s %-15s %-15s %-12.2f%n",
                        t.getId(), t.getDay_(), t.getMonth(), t.getYear(),
                        t.getHour(), t.getMinute(), t.getTille(), t.getDescription(),
                        (t.isCanceled() ? "1" : "0"), t.getDuration(), t.getPriority(),
                        t.getEmail(), t.getPartner(), t.getLocation(),
                        t.getMeetingType(), t.getRevenue());
            }
            log("Da luu " + count + " cong viec vao file.");
        } catch (IOException e) {
            System.err.println("Loi ghi file: " + e.getMessage());
        }
    }

    public static void loadTasks(Task[] tasks, int[] countRef) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Khong tim thay file tasks.csv, se tao moi khi luu.");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int count = 0;
            br.readLine();
            while ((line = br.readLine()) != null && count < tasks.length) {
                String[] p = new String[W.length];
                int pos = 0;
                for (int i = 0; i < W.length; i++) {
                    if (pos >= line.length()) {
                        p[i] = "";
                        continue;
                    }
                    int end = Math.min(pos + W[i], line.length());
                    p[i] = line.substring(pos, end).trim();
                    pos = end;
                    while (pos < line.length() && line.charAt(pos) == ' ') pos++;
                }
                if (p[0].isEmpty()) continue;
                try {

                    String meetingType = p[14];
                    Task task = null;

                    if ("Meeting".equals(meetingType)) {
                        task = new MeetingTask(
                            p[0],                               // id
                            Integer.parseInt(p[1]),             // day
                            Integer.parseInt(p[2]),             // month
                            Integer.parseInt(p[3]),             // year
                            Integer.parseInt(p[4]),             // hour
                            Integer.parseInt(p[5]),             // minute
                            p[6],                               // title
                            Integer.parseInt(p[9]),             // duration
                            p[7],                               // description
                            (Integer.parseInt(p[8]) == 1),      // canceled
                            p[13],                              // location
                            Integer.parseInt(p[10]),            // priority
                            p[12],                              // partner
                            p[11],                              // email
                            Double.parseDouble(p[15])           // revenue
                        );
                    } else if ("Study".equals(meetingType)) {
                        task = new StudyTask(
                            p[0],                               // id
                            Integer.parseInt(p[1]),             // day
                            Integer.parseInt(p[2]),             // month
                            Integer.parseInt(p[3]),             // year
                            Integer.parseInt(p[4]),             // hour
                            Integer.parseInt(p[5]),             // minute
                            p[6],                               // title
                            Integer.parseInt(p[9]),             // duration
                            p[7],                               // description
                            (Integer.parseInt(p[8]) == 1),      // canceled
                            p[13],                              // location
                            Integer.parseInt(p[10]),            // priority
                            Double.parseDouble(p[15])           // revenue
                        );
                    } else {
                        task = new Task(
                            p[0],                               // id
                            Integer.parseInt(p[1]),             // day
                            Integer.parseInt(p[2]),             // month
                            Integer.parseInt(p[3]),             // year
                            Integer.parseInt(p[4]),             // hour
                            Integer.parseInt(p[5]),             // minute
                            p[6],                               // title
                            Integer.parseInt(p[9]),             // duration
                            p[7],                               // description
                            (Integer.parseInt(p[8]) == 1),      // canceled
                            p[13],                              // location
                            Integer.parseInt(p[10]),            // priority
                            p[12],                              // partner
                            p[11],                              // email
                            p[14],                              // meetingType
                            Double.parseDouble(p[15])           // revenue
                        );
                    }

                    tasks[count] = task;
                    count++;
                } catch (Exception e) {
                    System.err.println("Loi phan tich dong: " + line);
                }
            }
            countRef[0] = count;
            log("Da doc " + count + " cong viec tu file.");
            System.out.println("Da doc " + count + " cong viec.");
        } catch (IOException e) {
            System.err.println("Loi doc file: " + e.getMessage());
        }
    }
}

