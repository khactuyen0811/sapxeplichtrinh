package event;


public class Task {
    private String id;
    private int day, month, year, hour, minute;
    private String tille;
    private int duration;
    private String description;
    private boolean canceled;
    private String location;
    private int priority;
    protected String partner;
    protected String email;
    protected String meetingType;
    private Double revenue;

    public Task(String id, int day, int month, int year, int hour, int minute, String tille, int duration,
            String description, boolean canceled, String location, int priority, String partner, String email,
            String meetingType, Double revenue) {
        this.id = id;
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.tille = tille;
        this.duration = duration;
        this.description = description;
        this.canceled = canceled;
        this.location = location;
        this.priority = priority;
        this.partner = partner;
        this.email = email;
        this.meetingType = meetingType;
        this.revenue = revenue;
    }

    private static boolean isLeapYear(int year){
        if(year % 4 == 0){
            if(year % 100 == 0){
                if(year % 400 == 0){
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private static int dayIsMonth(int month,int year){
        switch(month){
            case 1: case 3: case 5 : case 7: case 8 : case 10: case 12:
                return 31;
                case 4: case 6: case 9 : case 11:
                return 30;
                case 2:
                return isLeapYear(year) ? 29 : 28;
                default: return 0;
        }
    }

    public static long toMinutes(int d ,int m , int y , int h ,int min){
        long total = 0;
        for(int i = 0 ; i < y ; i++){
            total += isLeapYear(i) ? 366 : 365;
        }
        for(int i = 1; i < m; i++){
            total += dayIsMonth(i,y);
        }
        total += (d -1);
        total = total * 24 + h;
        total = total * 60 + min;
        return total;
    }

    public  long GetTimeStart(){
        return toMinutes(day, month, year, hour, minute);
    }

    public String getStatus(long currentTime) {
        if (canceled) return "canceled";
        long start = GetTimeStart();
        long end = start + duration;
        if (currentTime < start) return "pending";
        if (currentTime >= end) return "done";
        return "ongoing";
    }

        public boolean ConfictTask(Task other){
            long start1 = this.GetTimeStart();
            long end1 = start1 + this.duration;
            long start2 = other.GetTimeStart();
            long end2 = start2 + other.duration;
            return(start1 < end2 && start2 < end1);
        }

    public void displayAll(){
        System.out.println("======================================");
        System.out.println("ID: " + id);
        System.out.println("Date: " + day + "/" + month + "/" + year);
        System.out.println("Time: " + hour + ":" + minute);
        System.out.println("Title: " + tille);
        System.out.println("Duration: " + duration + " minutes");
        System.out.println("Description: " + description);
        System.out.println("Canceled: " + (canceled ? "Yes" : "No"));
        System.out.println("Location: " + location);
        System.out.println("Priority: " + priority);
        System.out.println("Partner: " + partner);
        System.out.println("Email: " + email);
        System.out.println("Meeting Type: " + meetingType);
        System.out.println("Revenue: $" + revenue);
        System.out.println("thu: " + getDay());
        
    }

    private String getDay(){
         int m = month, y = year;
        if (m == 1 || m == 2) {
            m += 12;
            y--;
        }
        int K = y % 100;
        int J = y / 100;
        int h = (day + (13 * (m + 1)) / 5 + K + K/4 + J/4 + 5 * J) % 7;
        String[] thuArr = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        return thuArr[h];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDay_() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    public boolean isCanceled() {
        return canceled;
    }
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public int getPriority(){
        return priority;
    }
    public void setPriority(int priority){
        this.priority = priority;
    }
    
    public boolean getStatus(){
        return canceled;
    }
    public void setStatus(boolean canceled){
        this.canceled = canceled;
    }

    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDuration() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public String getTille() {
        return tille;
    }
    public void setTille(String tille) {
        this.tille = tille;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }


    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }
    
}