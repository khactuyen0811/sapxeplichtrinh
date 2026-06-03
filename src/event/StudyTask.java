package event;

public class StudyTask extends Task {
    public StudyTask(String id, int day, int month, int year, int hour, int minute,
                     String title, int duration, String description, boolean canceled,
                     String location, int priority, double revenue) {
        

        super(id, day, month, year, hour, minute, title, duration, description,
              canceled, location, priority, "", "", "Study", revenue);
    }

    @Override
    public void displayAll() {
        System.out.println("hoc tap");
        super.displayAll();
        System.out.println("dia diem: " + getLocation());
    }
}