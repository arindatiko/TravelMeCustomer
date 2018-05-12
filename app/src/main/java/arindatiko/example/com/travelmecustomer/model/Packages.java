package arindatiko.example.com.travelmecustomer.model;

import java.util.ArrayList;

public class Packages {

    private ArrayList<Wisata> wisata;
    private ArrayList<Kamar> kamar;
    private ArrayList<Menu> menu;

    public Packages() {
    }

    public Packages(ArrayList<Wisata> wisata, ArrayList<Kamar> kamar, ArrayList<Menu> menu) {
        this.wisata = wisata;
        this.kamar = kamar;
        this.menu = menu;
    }

    public ArrayList<Wisata> getWisata() {
        return wisata;
    }

    public ArrayList<Kamar> getKamar() {
        return kamar;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }
}
