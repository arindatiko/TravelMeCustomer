package arindatiko.example.com.travelmecustomer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afdol on 4/18/2018.
 */

public class MyChoice implements Parcelable {
    private Double budget, totalBiaya, budget_awal;
    private List<String> categoryWisata = new ArrayList<>();
    private int ticketMotor, ticketCar, ticketBus, ticketAdult, ticketChild, jumPorsi, jumKamar, jumDay;

    public MyChoice() {
    }

    public MyChoice(Double budget, List<String> categoryWisata, int ticketMotor, int ticketCar, int ticketBus, int ticketAdult, int ticketChild, int jumPorsi, int jumKamar, int jumDay) {
        this.budget = budget;
        this.categoryWisata = categoryWisata;
        this.ticketMotor = ticketMotor;
        this.ticketCar = ticketCar;
        this.ticketBus = ticketBus;
        this.ticketAdult = ticketAdult;
        this.ticketChild = ticketChild;
        this.jumPorsi = jumPorsi;
        this.jumKamar = jumKamar;
        this.jumDay = jumDay;
    }

    /*public MyChoice(Double budget, List<String> categoryWisata, int ticketMotor, int ticketCar, int ticketBus, int ticketAdult, int ticketChild, int jumKamar, int jumDay) {
        this.budget = budget;
        this.categoryWisata = categoryWisata;
        this.ticketMotor = ticketMotor;
        this.ticketCar = ticketCar;
        this.ticketBus = ticketBus;
        this.ticketAdult = ticketAdult;
        this.ticketChild = ticketChild;
        this.jumKamar = jumKamar;
        this.jumDay = jumDay;
    }*/

    public Double getBudget_awal() {
        return budget_awal;
    }

    public void setBudget_awal(Double budget_awal) {
        this.budget_awal = budget_awal;
    }

    public Double getTotalBiaya() {
        return totalBiaya;
    }

    public void setTotalBiaya(Double totalBiaya) {
        this.totalBiaya = totalBiaya;
    }

    public Double getBudget() {
        return budget;
    }

    public List<String> getCategoryWisata() {
        return categoryWisata;
    }

    public int getTicketMotor() {
        return ticketMotor;
    }

    public int getTicketCar() {
        return ticketCar;
    }

    public int getTicketBus() {
        return ticketBus;
    }

    public int getTicketAdult() {
        return ticketAdult;
    }

    public int getTicketChild() {
        return ticketChild;
    }

    public void setJumPorsi(int jumPorsi) {
        this.jumPorsi = jumPorsi;
    }

    public int getJumPorsi() {
        return jumPorsi;
    }

    public int getJumKamar() {
        return jumKamar;
    }

    public int getJumDay() {
        return jumDay;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.budget);
        dest.writeValue(this.totalBiaya);
        dest.writeStringList(this.categoryWisata);
        dest.writeInt(this.ticketMotor);
        dest.writeInt(this.ticketCar);
        dest.writeInt(this.ticketBus);
        dest.writeInt(this.ticketAdult);
        dest.writeInt(this.ticketChild);
        dest.writeInt(this.jumPorsi);
        dest.writeInt(this.jumKamar);
        dest.writeInt(this.jumDay);
    }

    protected MyChoice(Parcel in) {
        this.budget = (Double) in.readValue(Double.class.getClassLoader());
        this.totalBiaya = (Double) in.readValue(Double.class.getClassLoader());
        this.categoryWisata = in.createStringArrayList();
        this.ticketMotor = in.readInt();
        this.ticketCar = in.readInt();
        this.ticketBus = in.readInt();
        this.ticketAdult = in.readInt();
        this.ticketChild = in.readInt();
        this.jumPorsi = in.readInt();
        this.jumKamar = in.readInt();
        this.jumDay = in.readInt();
    }

    public static final Parcelable.Creator<MyChoice> CREATOR = new Parcelable.Creator<MyChoice>() {
        @Override
        public MyChoice createFromParcel(Parcel source) {
            return new MyChoice(source);
        }

        @Override
        public MyChoice[] newArray(int size) {
            return new MyChoice[size];
        }
    };
}
