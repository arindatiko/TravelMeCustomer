package arindatiko.example.com.travelmecustomer;

import java.util.ArrayList;

import arindatiko.example.com.travelmecustomer.model.Kamar;
import arindatiko.example.com.travelmecustomer.model.Kuliner;
import arindatiko.example.com.travelmecustomer.model.Menu;
import arindatiko.example.com.travelmecustomer.model.Penginapan;
import arindatiko.example.com.travelmecustomer.model.Pesanan;
import arindatiko.example.com.travelmecustomer.model.Rekomendasi;
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
        @POST("user")
        Call<User> get_user(
                @Field("id_user") String id_user
        );

        @FormUrlEncoded
        @POST("update_profil")
        Call<User> edit_profil(
            @Field("id_user") String id_user,
            @Field("nama_lengkap") String nama_lengkap,
            @Field("username") String username,
            @Field("password") String password,
            @Field("user_type") String user_type,
            @Field("no_telp") String no_telp,
            @Field("flag") int flag
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
        Call<ArrayList<Kuliner>> package_recomendation_menu(
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
        Call<ArrayList<Kuliner>> get_r_menu(
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
        @POST("get_rekomendasi")
        Call<ArrayList<Rekomendasi>> get_rekomendasi_wisata(
                @Field("id_pesanan") String id_pesanan,
                @Field("jenis_layanan") String jenis_layanan,
                @Field("id_tujuan") String id_tujuan
        );

        @FormUrlEncoded
        @POST("get_rekomendasi")
        Call<ArrayList<Rekomendasi>> get_rekomendasi_kamar(
                @Field("id_pesanan") String id_pesanan,
                @Field("jenis_layanan") String jenis_layanan,
                @Field("id_tujuan") String id_tujuan
        );

        @FormUrlEncoded
        @POST("get_rekomendasi")
        Call<ArrayList<Rekomendasi>> get_rekomendasi_kuliner(
                @Field("id_pesanan") String id_pesanan,
                @Field("jenis_layanan") String jenis_layanan,
                @Field("id_tujuan") String id_tujuan
        );

        @FormUrlEncoded
        @POST("get_rekomendasi")
        Call<Rekomendasi> get_rekomendasi_2(
                @Field("id_pesanan") String id_pesanan,
                @Field("jenis_layanan") String jenis_layanan,
                @Field("id_tujuan") int id_tujuan
        );

        @FormUrlEncoded
        @POST("rekomendasi")
        Call<ArrayList<Wisata>> add_objek_wisata(
                @Field("id_user") int id_user,
                @Field("id_tujuan") String id_tujuan,
                @Field("jenis_layanan") String jenis_layanan
        );

        @FormUrlEncoded
        @POST("rekomendasi")
        Call<ArrayList<Kamar>> add_objek_kamar(
                @Field("id_user") int id_user,
                @Field("id_tujuan") String id_tujuan,
                @Field("jenis_layanan") String jenis_layanan
        );

        @FormUrlEncoded
        @POST("rekomendasi")
        Call<ArrayList<Menu>> add_objek_menu(
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

        @FormUrlEncoded
        @POST("pesanan")
        Call<Pesanan> get_pesanan(
                @Field("id_customer") int id_customer,
                @Field("id_driver") String id_driver,
                @Field("posisi_lat") Double posisi_lat,
                @Field("posisi_lng") Double posisi_lng,
                @Field("total_budget") Double total_budget
        );

        @FormUrlEncoded
        @POST("kamar_update")
        Call<Kamar> update_kamar(
                @Field("id_kamar") int id_kamar,
                @Field("id_penginapan") int id_penginapan,
                @Field("nama") String nama,
                @Field("banyak_kamar") int banyak_kamar,
                @Field("kapasitas") int kapasitas,
                @Field("fasilitas") String fasilitas,
                @Field("harga") Double harga,
                @Field("foto") String foto
        );

        @FormUrlEncoded
        @POST("history_pesanan_cust")
        Call<ArrayList<Pesanan>> history_pesanan(
                @Field("id_customer") String id_customer
        );

        @FormUrlEncoded
        @POST("update")
        Call<ArrayList<Rekomendasi>> update_pesanan(
                @Field("id_pesanan") String id_pesanan,
                @Field("id_user") String id_user
        );
    }

    public interface GetService {

    }

    public interface DeleteService {

    }
}
