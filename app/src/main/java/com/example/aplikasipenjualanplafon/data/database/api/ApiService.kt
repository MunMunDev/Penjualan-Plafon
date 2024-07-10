package com.example.aplikasipenjualanplafon.data.database.api

import com.example.aplikasipenjualanplafon.data.model.AdminPesananDetailModel
import com.example.aplikasipenjualanplafon.data.model.AlamatModel
import com.example.aplikasipenjualanplafon.data.model.JenisPlafonModel
import com.example.aplikasipenjualanplafon.data.model.ListKeranjangBelanjaModel
import com.example.aplikasipenjualanplafon.data.model.ListPesananModel
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.PlafonModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananHalModel
import com.example.aplikasipenjualanplafon.data.model.RiwayatPesananValModel
import com.example.aplikasipenjualanplafon.data.model.UsersModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @GET("penjualan-plafon/api/get.php")
    suspend fun getAllUser(@Query("all_user") allUser: String
    ): ArrayList<UsersModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getUser(@Query("get_user") getUser: String,
                        @Query("username") username: String,
                        @Query("password") password: String
    ): ArrayList<UsersModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getKeranjangBelanjaUser(
        @Query("get_keranjang_belanja_user") get_keranjang_belanja_user: String,
        @Query("id_user") idUser: String
    ): ArrayList<PesananModel>


    @GET("penjualan-plafon/api/get.php")
    suspend fun getAlamatUser(
        @Query("get_pilih_alamat") get_pilih_alamat: String,
        @Query("id_user") idUser: String
    ): ArrayList<AlamatModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getPesanan(
        @Query("get_pesanan") get_pesanan: String,
        @Query("id_user") idUser: String
    ): ArrayList<RiwayatPesananHalModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getPesananDetail(
        @Query("get_pesanan_detail") get_pesanan_detail: String,
        @Query("id_pemesanan") idPemesanan: String
    ): ArrayList<RiwayatPesananValModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getRiwayatPesanan(
        @Query("get_riwayat_pesanan") get_riwayat_pesanan: String,
        @Query("id_user") idUser: String
    ): ArrayList<RiwayatPesananHalModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getDetailRiwayatPesanan(
        @Query("get_detail_riwayat_pesanan") get_detail_riwayat_pesanan: String,
        @Query("id_pemesanan") id_pemesanan: String
    ): ArrayList<RiwayatPesananValModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getJenisPlafon(@Query("get_jenis_plafon") getJenisPlafon: String
    ): ArrayList<JenisPlafonModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getAllPlafon(@Query("get_all_plafon") getAllPlafon: String
    ): ArrayList<PlafonModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getPembayaranUser(@Query("user_pembayaran") user_pembayaran: String,
                                  @Query("id_user") id_user: String
    ): ArrayList<PesananModel>

//    @GET("penjualan-plafon/api/get.php")
//    suspend fun getAdminPesanan(@Query("get_admin_pesanan") get_admin_pesanan: String
//    ): ArrayList<PesananModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getAdminKeranjangBelanja(
        @Query("get_admin_keranjang_belanja") get_admin_keranjang_belanja: String
    ): ArrayList<ListKeranjangBelanjaModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getAdminKeranjangBelanjaDetail(
        @Query("get_admin_keranjang_belanja_detail") get_admin_keranjang_belanja_detail: String,
        @Query("id_user") id_user: String
    ): ArrayList<RiwayatPesananValModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getAdminPesanan(
        @Query("get_admin_pesanan") get_admin_pesanan: String
    ): ArrayList<ListPesananModel>


    @GET("penjualan-plafon/api/get.php")
    suspend fun getAdminPesananDetail(
        @Query("get_admin_pesanan_detail") get_admin_pesanan_detail: String,
        @Query("id_pemesanan") id_pemesanan: String
    ): ArrayList<AdminPesananDetailModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getAdminRiwayatPesanan(
        @Query("get_admin_riwayat_pesanan") get_admin_riwayat_pesanan: String
    ): ArrayList<ListPesananModel>

    @GET("penjualan-plafon/api/get.php")
    suspend fun getAdminRiwayatPesananDetail(
        @Query("get_admin_riwayat_pesanan_detail") get_admin_riwayat_pesanan_detail: String,
        @Query("id_user") id_user: String
    ): ArrayList<AdminPesananDetailModel>


    @GET("penjualan-plafon/api/get.php")
    suspend fun getAdminPrintLaporan(
        @Query("get_admin_print_laporan") get_admin_print_laporan: String,
        @Query("metode_pembayaran") metode_pembayaran: String,
    ): ArrayList<RiwayatPesananValModel>


    // POST
    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun addUser(
        @Field("add_user") addUser:String,
        @Field("nama") nama:String,
        @Field("alamat") alamat:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("sebagai") sebagai:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdateUser(
        @Field("update_akun") updateAkun:String,
        @Field("id_user") idUser: String,
        @Field("nama") nama:String,
        @Field("alamat") alamat:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("username_lama") usernameLama: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postHapusUser(
        @Field("hapus_akun") hapusAkun:String,
        @Field("id_user") idUser: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postTambahPesanan(
        @Field("tambah_pesanan") tambahPesanan: String,
        @Field("id_plafon") id_plafon: String,
        @Field("id_user") idUser: String,
        @Field("jumlah") jumlah: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdatePesanan(
        @Field("update_pesanan") updatePesanan:String,
        @Field("id_pesanan") idPesanan: String,
        @Field("jumlah") jumlah: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postHapusPesanan(
        @Field("hapus_pesanan") hapusPesanan:String,
        @Field("id_pesanan") idPesanan: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdateMainAlamat(
        @Field("update_main_alamat") update_main_alamat: String,
        @Field("id_alamat") id_alamat: String,
        @Field("id_user") id_user: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postTambahAlamat(
        @Field("tambah_pilih_alamat") tambah_pilih_alamat: String,
        @Field("id_user") id_user: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("alamat") alamat: String,
        @Field("detail_alamat") detail_alamat: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdateAlamat(
        @Field("update_pilih_alamat") update_pilih_alamat: String,
        @Field("id_alamat") id_alamat: String,
        @Field("id_user") id_user: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("alamat") alamat: String,
        @Field("detail_alamat") detail_alamat: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postPesan(
        @Field("post_pesan") postPesan:String,
        @Field("id_user") id_user: String,
        @Field("alamat") alamat: String,
        @Field("metode_pembayaran") metode_pembayaran: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postPesan(
        @Field("post_pesan") postPesan:String,
        @Field("id_user") id_user: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("alamat") alamat: String,
        @Field("detail_alamat") detail_alamat: String,
        @Field("metode_pembayaran") metode_pembayaran: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postRegistrasiPembayaran(
        @Field("registrasi_pembayaran") registrasiPembayaran:String,
        @Field("id_pembayaran") id_pembayaran:String,
        @Field("id_user") id_user:String,
        @Field("keterangan") keterangan:String,
        @Field("nama_lengkap") nama_lengkap:String,
        @Field("nomor_hp") nomor_hp:String,
        @Field("alamat") alamat:String,
        @Field("detail_alamat") detail_alamat:String,
    ): ArrayList<ResponseModel>

    // Post Jenis Plafon
    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postTambahJenisPlafon(
        @Field("tambah_jenis_plafon") tambahJenisPlafon: String,
        @Field("jenis_plafon") jenis_plafon: String,
        @Field("keunggulan") keunggulan: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdateJenisPlafon(
        @Field("update_jenis_plafon") updateJenisPlafon: String,
        @Field("id_jenis_plafon") id_jenis_plafon: String,
        @Field("jenis_plafon") jenis_plafon: String,
        @Field("keunggulan") keunggulan: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postDeleteJenisPlafon(
        @Field("delete_jenis_plafon") delete_jenis_plafon:String,
        @Field("id_jenis_plafon") id_jenis_plafon: String
    ): ArrayList<ResponseModel>

    // Post Plafon
    @Multipart
    @POST("penjualan-plafon/api/post.php")
    suspend fun postTambahPlafon(
        @Part("tambah_plafon") tambah_plafon: RequestBody,
        @Part("id_jenis_plafon") id_jenis_plafon: RequestBody,
        @Part gambar: MultipartBody.Part,
        @Part("harga_permeter") harga_permeter: RequestBody,
    ): ArrayList<ResponseModel>

    @Multipart
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdatePlafon(
        @Part("update_plafon") updatePlafon: RequestBody,
        @Part("id_plafon") id_plafon: RequestBody,
        @Part("id_jenis_plafon") id_jenis_plafon: RequestBody,
        @Part gambar: MultipartBody.Part,
        @Part("harga_permeter") harga_permeter: RequestBody,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdatePlafonNoImage(
        @Field("update_plafon_no_image") update_plafon_no_image:String,
        @Field("id_plafon") id_plafon:String,
        @Field("id_jenis_plafon") id_jenis_plafon:String,
        @Field("harga_permeter") harga_permeter:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postDeletePlafon(
        @Field("delete_plafon") deletePlafon:String,
        @Field("id_plafon") id_plafon:String
    ): ArrayList<ResponseModel>



//    @FormUrlEncoded
//    @POST("penjualan-plafon/api/post.php")
//    suspend fun postTambahPesanan(
//        @Field("tambah_pesanan") tambah_pesanan: String,
//        @Field("id_pemesanan") id_pemesanan: String,
//        @Field("id_user") id_user: String,
//        @Field("nama") nama: String,
//        @Field("alamat") alamat: String,
//        @Field("nomor_hp") nomor_hp: String,
//        @Field("jenis_plafon") jenis_plafon: String,
//        @Field("harga") harga: String,
//        @Field("panjang") panjang: String,
//        @Field("lebar") lebar: String,
//        @Field("total_harga") total_harga: String,
//        @Field("waktu") waktu: String,
//        @Field("waktu_konfirmasi") waktu_konfirmasi: String,
//        @Field("metode_pembayaran") metode_pembayaran: String,
//        @Field("waktu_bayar") waktu_bayar: String
//    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postTambahPesananAdmin(
        @Field("tambah_pesanan") tambah_pesanan: String,
        @Field("id_pemesanan") id_pemesanan: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("jenis_plafon") jenis_plafon: String,
        @Field("harga") harga: String,
        @Field("panjang") panjang: String,
        @Field("lebar") lebar: String,
        @Field("total_harga") total_harga: String,
        @Field("waktu") waktu: String,
        @Field("waktu_konfirmasi") waktu_konfirmasi: String
    ): ArrayList<ResponseModel>

//    @FormUrlEncoded
//    @POST("penjualan-plafon/api/post.php")
//    suspend fun postUpdatePesanan(
//        @Field("update_pesanan") update_pesanan: String,
//        @Field("id_pesanan") id_pesanan: String,
//        @Field("id_user") id_user: String,
//        @Field("nama") nama: String,
//        @Field("alamat") alamat: String,
//        @Field("nomor_hp") nomor_hp: String,
//        @Field("jenis_plafon") jenis_plafon: String,
//        @Field("harga") harga: String,
//        @Field("panjang") panjang: String,
//        @Field("lebar") lebar: String,
//        @Field("total_harga") total_harga: String,
//        @Field("gambar") gambar: String,
//        @Field("waktu") waktu: String,
//        @Field("waktu_konfirmasi") waktu_konfirmasi: String,
//        @Field("metode_pembayaran") metode_pembayaran: String,
//        @Field("waktu_bayar") waktu_bayar: String
//    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdatePesanan(
        @Field("update_pesanan") update_pesanan: String,
        @Field("id_pesanan") id_pesanan: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("jenis_plafon") jenis_plafon: String,
        @Field("harga") harga: String,
        @Field("panjang") panjang: String,
        @Field("lebar") lebar: String,
        @Field("total_harga") total_harga: String,
        @Field("gambar") gambar: String,
        @Field("waktu") waktu: String,
        @Field("waktu_konfirmasi") waktu_konfirmasi: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postDeletePesanan(
        @Field("delete_pesanan") delete_pesanan:String,
        @Field("id_pesanan") id_pesanan: String
    ): ArrayList<ResponseModel>


    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postAdminTambahPesananDetail(
        @Field("admin_tambah_pesanan_detail") admin_tambah_pesanan_detail: String,
        @Field("id_user") id_user: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("id_jenis_plafon") id_jenis_plafon: String,
        @Field("harga") harga: String,
        @Field("panjang") panjang: String,
        @Field("lebar") lebar: String,
        @Field("total_harga") total_harga: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postAdminUpdatePesananDetail(
        @Field("admin_update_pesanan_detail") admin_update_pesanan_detail: String,
        @Field("id_pesanan") id_pesanan: String,
        @Field("id_pemesanan") id_pemesanan: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("jenis_plafon") jenis_plafon: String,
        @Field("harga") harga: String,
        @Field("panjang") panjang: String,
        @Field("lebar") lebar: String,
        @Field("total_harga") total_harga: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postAdminDeletePesananDetail(
        @Field("admin_delete_pesanan_detail") admin_delete_pesanan_detail:String,
        @Field("id_pesanan") id_pesanan: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postAdminUpdateKonrimasiPembayaran(
        @Field("admin_update_konfirmasi_pembayaran") admin_update_konfirmasi_pembayaran: String,
        @Field("id_pemesanan") id_pemesanan: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postAdminUpdateKonrimasiSelesai(
        @Field("admin_update_konfirmasi_selesai") admin_update_konfirmasi_selesai: String,
        @Field("id_pemesanan") id_pemesanan: String
    ): ArrayList<ResponseModel>


    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postTambahRiwayatPesanan(
        @Field("tambah_pesanan") tambah_pesanan: String,
        @Field("id_pemesanan") id_pemesanan: String,
        @Field("id_user") id_user: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("jenis_plafon") jenis_plafon: String,
        @Field("harga") harga: String,
        @Field("panjang") panjang: String,
        @Field("lebar") lebar: String,
        @Field("total_harga") total_harga: String,
        @Field("waktu") waktu: String,
        @Field("waktu_konfirmasi") waktu_konfirmasi: String,
        @Field("metode_pembayaran") metode_pembayaran: String,
        @Field("waktu_bayar") waktu_bayar: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdateRiwayatPesanan(
        @Field("update_pesanan") update_pesanan: String,
        @Field("id_pesanan") id_pesanan: String,
        @Field("id_user") id_user: String,
        @Field("nama") nama: String,
        @Field("alamat") alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("jenis_plafon") jenis_plafon: String,
        @Field("harga") harga: String,
        @Field("panjang") panjang: String,
        @Field("lebar") lebar: String,
        @Field("total_harga") total_harga: String,
        @Field("gambar") gambar: String,
        @Field("waktu") waktu: String,
        @Field("waktu_konfirmasi") waktu_konfirmasi: String,
        @Field("metode_pembayaran") metode_pembayaran: String,
        @Field("waktu_bayar") waktu_bayar: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postDeleteRiwayatPesanan(
        @Field("delete_pesanan") delete_pesanan:String,
        @Field("id_riwayat_pesanan") id_riwayat_pesanan: String
    ): ArrayList<ResponseModel>


    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postTambahRiwayatPesananDetail(
        @Field("admin_tambah_riwayat_pesanan_detail") admin_tambah_riwayat_pesanan_detail: String,
        @Field("id_user") id_user: String,
        @Field("id_pemesanan") id_pemesanan: String,
        @Field("id_plafon") id_plafon: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("alamat") alamat: String,
        @Field("detail_alamat") detail_alamat: String,
        @Field("jumlah") jumlah: String,
//        @Field("harga") harga: String,
//        @Field("total_harga") total_harga: String,
        @Field("metode_pembayaran") metode_pembayaran: String,
//        @Field("waktu") waktu: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postUpdateRiwayatPesananDetail(
        @Field("admin_update_riwayat_pesanan_detail") admin_update_riwayat_pesanan_detail: String,
        @Field("id_riwayat_pesanan") id_riwayat_pesanan: String,
        @Field("id_user") id_user: String,
        @Field("id_pemesanan") id_pemesanan: String,
        @Field("id_plafon") id_plafon: String,
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("alamat") alamat: String,
        @Field("detail_alamat") detail_alamat: String,
        @Field("jumlah") jumlah: String,
        @Field("metode_pembayaran") metode_pembayaran: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("penjualan-plafon/api/post.php")
    suspend fun postDeleteRiwayatPesananDetail(
        @Field("admin_delete_riwayat_pesanan") admin_delete_riwayat_pesanan:String,
        @Field("id_riwayat_pesanan") id_riwayat_pesanan: String
    ): ArrayList<ResponseModel>



}