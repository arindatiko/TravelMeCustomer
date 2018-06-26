package arindatiko.example.com.travelmecustomer;

import java.util.ArrayList;

import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.Penginapan;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.model.Tujuan;
import arindatiko.example.com.travelmecustomer.model.User;
import arindatiko.example.com.travelmecustomer.model.Wisata;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class API {
    //public static String BASE_URL = "http://muhibush.xyz/api_trme/index.php/Api/";
    //public static String BASE_URL = "http://192.168.100.3/travel_me/index.php/Api/";
    //public static String BASE_URL = "http://192.168.43.47/travel_me/index.php/Api/";
    //public static String BASE_URL = "http://ichoarinda.000webhostapp.com/travel_me/index.php/Api/";
    public static String BASE_URL = "http://arinda.jagopesan.com/travel_me/index.php/Api/";


    public static PostService service_post = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(API.PostService.class);

    public static GetService service_get = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(API.GetService.class);

    public static DeleteService service_delete = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build().create(API.DeleteService.class);

    public interface PostService {

        @FormUrlEncoded
        @POST("login")
        Call<User> login(
                @Field("no_telp") String no_telp,
                @Field("password") String pass/*,
                @Field("user_type") String user_type*/
        );

        @FormUrlEncoded
        @POST("register")
        Call<User> register(
                @Field("username") String username,
                @Field("password") String pass,
                @Field("no_telp") String no_telp,
                @Field("user_type") String user_type
        );

        @FormUrlEncoded
        @POST("package_recomendation")
        Call<ArrayList<Wisata>> package_recomendation_wisata(
                @Field("list_id_wisata") String list_id_wisata,
                @Field("jenis_layanan") String jenis_layanan
        );

        @FormUrlEncoded
        @POST("package_recomendation")
        Call<ArrayList<Kamar>> package_recomendation_kamar(
                @Field("list_id_kamar") String list_id_kamar,
                @Field("jenis_layanan") String jenis_layanan
        );
        @FormUrlEncoded
        @POST("package_recomendation")
        Call<ArrayList<Menu>> package_recomendation_menu(
                @Field("list_id_menu") String list_id_menu,
                @Field("jenis_layanan") String jenis_layanan
        );

        @FormUrlEncoded
        @POST("recomendation")
        Call<ArrayList<Wisata>> get_r_wisata(
                @Field("jenis_layanan") String jenis_layanan,
                @Field("wisata_jenis") String wisata_jenis,
                @Field("wisata_jumlah_anak") int wisata_jumlah_anak,
                @Field("wisata_jumlah_dewasa") int wisata_jumlah_dewasa,
                @Field("jumlah_motor") int jumlah_motor,
                @Field("jumlah_mobil") int jumlah_mobil,
                @Field("jumlah_bus") int jumlah_bus,
                @Field("budget_wisata") Double budget_wisata
        );

        @FormUrlEncoded
        @POST("recomendation")
        Call<Double> get_r_jarak(
                @Field("jenis_layanan") String jenis_layanan,
                @Field("jasa") Double jasa,
                @Field("tambahan") Double tambahan,
                @Field("total_km") Double total_km,
                @Field("jumlah_motor") int jumlah_motor,
                @Field("jumlah_mobil") int jumlah_mobil,
                @Field("jumlah_bus") int jumlah_bus
        );

        @FormUrlEncoded
        @POST("recomendation")
        Call<ArrayList<Kamar>> get_r_kamar(
                @Field("jenis_layanan") String jenis_layanan,
                @Field("kamar_jumlah") int kamar_jumlah,
                @Field("kamar_jumlah_hari") int kamar_jumlah_hari,
                @Field("budget_kamar") Double budget_kamar
        );

        @FormUrlEncoded
        @POST("recomendation")
        Call<ArrayList<Menu>> get_r_menu(
                @Field("jenis_layanan") String jenis_layanan,
                @Field("menu_porsi") int menu_porsi,
                @Field("budget_menu") Double budget_menu
        );

        @FormUrlEncoded
        @POST("all_kuliner")
        Call<ArrayList<Kuliner>> get_all_kuliner(@Field("id") int id);

        @FormUrlEncoded
        @POST("all_wisata")
        Call<ArrayList<Wisata>> get_all_wisata(@Field("id") int id);

        @FormUrlEncoded
        @POST("all_penginapan")
        Call<ArrayList<Penginapan>> get_all_penginapan(@Field("id") int id);

        @FormUrlEncoded
        @POST("detail_wisata")
        Call<Wisata> get_detail_wisata(@Field("id_wisata") int id_wisata);

        @FormUrlEncoded
        @POST("detail_kuliner")
        Call<Kuliner> get_detail_kuliner(@Field("id_kuliner") int id_kuliner);

        @FormUrlEncoded
        @POST("detail_penginapan")
        Call<Penginapan> get_detail_penginapan(@Field("id_penginapan") int id_penginapan);

        @FormUrlEncoded
        @POST("detail_kamar")
        Call<Kamar> get_detail_kamar(@Field("id_kamar") int id_kamar);

        @FormUrlEncoded
        @POST("cek_lokasi")
        Call<User> cek_lokasi(@Field("id_user") int id_user,
                              @Field("posisi_lat") String posisi_lat,
                              @Field("posisi_lng") String posisi_lng,
                              @Field("user_type") String user_type);

        @FormUrlEncoded
        @POST("rekomendasi")
        Call<ArrayList<Tujuan>> add_objek(
                @Field("id_user") int id_user,
                @Field("id_tujuan") String id_tujuan,
                @Field("jenis_layanan") String jenis_layanan
        );

        @FormUrlEncoded
        @POST("pesanan")
        Call<Pesanan> send_pesanan(
                @Field("id_customer") int id_customer,/*
                @Field("id_driver") String id_driver,*/
                @Field("posisi_lat") Double posisi_lat,
                @Field("posisi_lng") Double posisi_lng,
                @Field("total_budget") Double total_budget
        );
    }

    public interface GetService {

    }

    public interface DeleteService {

    }
}
