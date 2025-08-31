package DomainLayer.Employee;
import java.time.LocalDate;

//import java.time.Time;
import java.time.Month;
import java.util.Date;



public class Availability {
        private int id;
        private int employeeID;
        private int dayOfWeek; // 1 = Monday, 2 = Tuesday, ..., 7 = Sunday
       // private Time startTime;
       // private Time endTime;

        private boolean morningShift;
        private boolean eveningShift;
    //private Date date;

        public Availability( int employeeID, int dayOfWeek , boolean morningShift, boolean eveningShift) {
            this.id = id;
            this.employeeID = employeeID;
            this.dayOfWeek = dayOfWeek; // 1 = Monday, 2 = Tuesday, ..., 7 = Sunday
            //this.startTime = startTime;
            //this.endTime = endTime;
            this.morningShift = morningShift;
            this.eveningShift = eveningShift;
            //this.date = date;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getEmployeeID() {
            return employeeID;
        }
        public void setEmployeeID(int employeeID) {
            this.employeeID = employeeID;
        }
        public Integer getDayOfWeek() {
            return dayOfWeek;
        }
        public void setDayOfWeek(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }
//        public Time getStartTime() {
//            return startTime;
//        }
//        public void setStartTime(Time startTime) {
//            this.startTime = startTime;
//        }
//        public Time getEndTime() {
//            return endTime;
//        }
//        public void setEndTime(Time endTime) {
//            this.endTime = endTime;
//        }
        public boolean isMorningShift() {
            return morningShift;
        }
        public void setMorningShift(boolean morningShift) {
            this.morningShift = morningShift;
        }
        public boolean isEveningShift() {
            return eveningShift;
        }
        public void setEveningShift(boolean eveningShift) {
            this.eveningShift = eveningShift;
        }


}
