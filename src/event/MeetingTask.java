package event;

public class MeetingTask extends Task {

    public MeetingTask(String id, int day, int month, int year, int hour, int minute,
                       String title, int duration, String description, boolean canceled,
                       String location, int priority, String partner, String email, double revenue) {


        super(id, day, month, year, hour, minute, title, duration, description,
              canceled, location, priority, partner, email, "Meeting", revenue);
    }

    @Override
    public void displayAll() {
        System.out.println("cong viec hop");
        super.displayAll();
        System.out.println("doi tac: " + getPartner() + " | Email: " + getEmail());
    }
}