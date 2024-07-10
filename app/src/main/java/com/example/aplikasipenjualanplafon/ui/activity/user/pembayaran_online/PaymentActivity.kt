package com.example.aplikasipenjualanplafon.ui.activity.user.pembayaran_online

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aplikasipenjualanplafon.R
import com.example.aplikasipenjualanplafon.adapter.PembayaranPesananAdapter
import com.example.aplikasipenjualanplafon.data.model.AlamatModel
import com.example.aplikasipenjualanplafon.data.model.PesananModel
import com.example.aplikasipenjualanplafon.data.model.ResponseModel
import com.example.aplikasipenjualanplafon.databinding.ActivityPaymentBinding
import com.example.aplikasipenjualanplafon.ui.activity.user.alamat.PilihAlamatActivity
import com.example.aplikasipenjualanplafon.ui.activity.user.main.MainActivity
import com.example.aplikasipenjualanplafon.utils.Constant
import com.example.aplikasipenjualanplafon.utils.KataAcak
import com.example.aplikasipenjualanplafon.utils.KonversiRupiah
import com.example.aplikasipenjualanplafon.utils.LoadingAlertDialog
import com.example.aplikasipenjualanplafon.utils.SharedPreferencesLogin
import com.example.aplikasipenjualanplafon.utils.TanggalDanWaktu
import com.example.aplikasipenjualanplafon.utils.network.UIState
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private val viewModel: PaymentViewModel by viewModels()
    @Inject
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject
    lateinit var hurufAcak: KataAcak
    @Inject
    lateinit var rupiah: KonversiRupiah
    @Inject lateinit var loading: LoadingAlertDialog
    private var totalBiaya = 0.0
    private var uuid = UUID.randomUUID().toString()
    @Inject
    lateinit var kataAcak: KataAcak

    private var acak = ""

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var customerDetails: CustomerDetails
    private var itemDetails: ArrayList<ItemDetails> = arrayListOf()
    private lateinit var initTransactionDetails: SnapTransactionDetail

    private var idUser = ""
    private var namaLengkap = ""
    private var nomorHp = ""
    private var alamat = ""
    private var detailAlamat = ""
    private var jenisPembayaran = ""

    private var selectedValue = ""
    private var numberPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        acak = kataAcak.getHurufDanAngka()
        setSharedPreferencesLogin()
        setDataSebelumnya()
        setButton()
        konfigurationMidtrans()
        fetchDataPembayaran(sharedPreferencesLogin.getIdUser().toString())
        getDataPembayaran()
        getDataRegistrasiPembayaran()
        getTambahPesananDitempat()
    }

    private fun setDataSebelumnya() {
        val i = intent.extras
        if(i!=null){
//            alamat = i.getString("alamat")!!
//            jenisPembayaran = i.getString("jenisPembayaran")!!
        }
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@PaymentActivity)
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnBayar.setOnClickListener {
                if(tvAlamat.text.toString().trim() == resources.getString(R.string.alamat).trim()){
                    Toast.makeText(this@PaymentActivity, "Tolong masukkan alamat anda", Toast.LENGTH_SHORT).show()
                } else{
                    if(numberPosition == 0){
//                        fetchDataRegistrasiPembayaran(uuid, idUser)
                        fetchDataRegistrasiPembayaran(acak, idUser, namaLengkap, nomorHp, alamat, detailAlamat)
                    } else{
                        postTambahPesananDitempat(idUser, namaLengkap, nomorHp, alamat, detailAlamat, jenisPembayaran)
                    }
                }
            }

            clAlamat.setOnClickListener {
                startActivity(Intent(this@PaymentActivity, PilihAlamatActivity::class.java))
                finish()
            }
        }
    }

    private fun postTambahPesananDitempat(
        idUser: String,
        namaLengkap: String,
        nomorHp: String,
        alamat: String,
        detailAlamat: String,
        jenisPembayaran: String
    ) {
        viewModel.postPesan(idUser, namaLengkap, nomorHp, alamat, detailAlamat, jenisPembayaran)
    }
    private fun getTambahPesananDitempat(){
        viewModel.getPostPesan().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Loading -> loading.alertDialogLoading(this@PaymentActivity)
                is UIState.Failure -> setFailurePostPesan(result.message)
                is UIState.Success -> setSuccessPostPesan(result.data)
            }
        }
    }

    private fun setFailurePostPesan(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessPostPesan(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status=="0"){
                Toast.makeText(this@PaymentActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
                finish()
            } else{
                Toast.makeText(this@PaymentActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@PaymentActivity, "No respon", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDataRegistrasiPembayaran(
        idPembayaran: String, idUser: String,
        namaLengkap: String, nomorHp: String,
        alamat: String, detailAlamat: String
    ) {
        viewModel.postRegistrasiPembayaran(idPembayaran, idUser, "Pending", namaLengkap, nomorHp, alamat, detailAlamat)
    }

    private fun getDataRegistrasiPembayaran() {
        viewModel.getRegistrasiPembayaran().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@PaymentActivity)
                is UIState.Success-> setDataSuccessRegistrasiPembayaran(result.data)
                is UIState.Failure-> setDataFailureRegistrasiPembayaran(result.message)
            }
        }
    }

    private fun setDataFailureRegistrasiPembayaran(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@PaymentActivity, "Ada masalah : $message", Toast.LENGTH_SHORT).show()
    }

    private fun setDataSuccessRegistrasiPembayaran(data: ArrayList<ResponseModel>) {
        loading.alertDialogCancel()
        if(data.isNotEmpty()){
            if(data[0].status == "0"){
                UiKitApi.getDefaultInstance().startPaymentUiFlow(
                    activity = this@PaymentActivity,
                    launcher = launcher,
                    transactionDetails = initTransactionDetails,
                    customerDetails = customerDetails,
                    itemDetails = itemDetails
                )
            }else{
                Toast.makeText(this@PaymentActivity, "Gagal Registrasi", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@PaymentActivity, "Bermasalah di web", Toast.LENGTH_SHORT).show()
        }
    }

    private fun konfigurationMidtrans() {
        setLauncher()
        setCustomerDetails()
        setInitTransactionDetails()
        buildUiKit()
    }

    private fun buildUiKit() {
        setInitTransactionDetails()
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(Constant.MIDTRANS_BASE_URL)
            .withMerchantClientKey(Constant.MIDTRANS_CLIENT_KEY)
            .enableLog(true)
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }

    private fun fetchDataPembayaran(idUser: String) {
        viewModel.fetchDataPembayaran(idUser)
    }

    private fun getDataPembayaran() {
        viewModel.getDataPembayaran().observe(this@PaymentActivity){ result ->
            when(result){
                is UIState.Loading-> loading.alertDialogLoading(this@PaymentActivity)
                is UIState.Success -> setDataSuccessPembayaran(result.data)
                is UIState.Failure -> setDataFailurePembayaran(result.message)
            }
        }
    }

    private fun setDataFailurePembayaran(message: String) {
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setDataSuccessPembayaran(data: ArrayList<PesananModel>) {
        loading.alertDialogCancel()
        Log.d("PaymentActivityTAG", "setDataSuccessPembayaran: ${data.size}")
        if(data.size>0){
            idUser = data[0].idUser!!
            var harga = 0

            Log.d("MainActivityTag", "setData: $data")

            var no = 1

            for (value in data){
                val hargaSatuan = value.plafon!![0].harga!!.toDouble()
                val totalHargaSatuan = value.jumlah!!.toDouble()*value.plafon[0].harga!!.toDouble()
                val jenisPlafon = value.plafon[0].jenis_plafon!![0].jenis_plafon
//                val panjang = value.panjang
                val jumlah = value.jumlah.trim()

                harga += totalHargaSatuan.toInt()
                Log.d("PaymentActivityTAG", "set: no: $no, " +
                        "totalHargaSatuan: $totalHargaSatuan, jumlah: $jumlah, jenisPlafon: $jenisPlafon ")

                itemDetails.add(
                    ItemDetails(
//                        "$no", 12.0, 1, " m2 X m2)"
//                        "$no", totalHargaSatuan, jumlah.toInt(), "$jenisPlafon"
                        "$no", totalHargaSatuan, 1, "$jenisPlafon"
                    )
                )
                no++
            }

            val total = harga
            totalBiaya = total.toDouble()
            Log.d("PaymentActivityTAG", "setDataSuccessPembayaran: $totalBiaya")

            initTransactionDetails = SnapTransactionDetail(
                uuid,
                totalBiaya
            )

            binding.apply {
                setData(data)
                setAdapter(data)
                tvTotalTagihan.text = rupiah.rupiah(total.toLong())
            }
        } else{
            Toast.makeText(this@PaymentActivity, "Terima Kasih Telah Membayar", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
            finish()
        }

    }

    private fun setData(data: ArrayList<PesananModel>) {
        binding.apply {
            // Alamat
            var alamatModel = AlamatModel("", "", "", "", "", "", "")
            if(data[0].alamat!!.isNotEmpty()){
                for(values in data[0].alamat!!){
                    if(values.main == "1"){
                        alamatModel = values
                    }
                }
            } else{
                // Tidak ada data
                tvNama.text = sharedPreferencesLogin.getNama()
                tvNomorHp.text = sharedPreferencesLogin.getNomorHp()
                tvAlamat.text = resources.getString(R.string.alamat)
            }
            if(alamatModel.id_alamat!!.isNotEmpty()){
                tvNama.text = alamatModel.nama_lengkap
                tvNomorHp.text = alamatModel.nomor_hp
                tvAlamat.text = alamatModel.alamat
                tvAlamatDetail.text = alamatModel.detail_alamat
            } else{
                tvNama.text = sharedPreferencesLogin.getNama()
                tvNomorHp.text = sharedPreferencesLogin.getNomorHp()
                tvAlamat.text = resources.getString(R.string.alamat)
            }
//            tvNama.text = alamatModel.nama_lengkap
//            tvNomorHp.text = alamatModel.nomor_hp
//            tvAlamat.text = alamatModel.alamat
//            tvAlamatDetail.text = alamatModel.detail_alamat

            namaLengkap = alamatModel.nama_lengkap!!
            nomorHp = alamatModel.nomor_hp!!
            alamat = alamatModel.alamat!!
            detailAlamat = alamatModel.detail_alamat!!

            // Spinner Metode Pembayaran
            val arrayAdapter = ArrayAdapter.createFromResource(
                this@PaymentActivity,
                R.array.metode_pembayaran,
                android.R.layout.simple_spinner_item
            )
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spMetodePembayaran.adapter = arrayAdapter

            spMetodePembayaran.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    numberPosition = spMetodePembayaran.selectedItemPosition
                    selectedValue = spMetodePembayaran.selectedItem.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            spMetodePembayaran.adapter = arrayAdapter
        }
    }

    private fun setAdapter(data: ArrayList<PesananModel>) {
        binding.apply {
            val adapter = PembayaranPesananAdapter(data)
            rvPesanan.layoutManager = LinearLayoutManager(this@PaymentActivity, LinearLayoutManager.VERTICAL, false)
            rvPesanan.adapter = adapter
        }
    }

    private fun setInitTransactionDetails() {
//        initTransactionDetails = SnapTransactionDetail(
//            uuid,
//            totalBiaya
//        )
        initTransactionDetails = SnapTransactionDetail(
            acak,
            totalBiaya
        )
    }

    private fun setCustomerDetails() {
//        var nomorHp = sharedPreferencesLogin.getNomorHp()
//        if(nomorHp == ""){
//            nomorHp = "0"
//        }
//        customerDetails = CustomerDetails(
//            firstName = sharedPreferencesLogin.getNama(),
//            customerIdentifier = "${sharedPreferencesLogin.getIdUser()}",
//            email = "mail@mail.com",
//            phone = nomorHp
//        )
        if(nomorHp == ""){
            nomorHp = "0"
        }
        customerDetails = CustomerDetails(
            firstName = namaLengkap,
            customerIdentifier = "${sharedPreferencesLogin.getIdUser()}",
            email = "mail@mail.com",
            phone = nomorHp
        )
    }

    private fun setLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(
                        UiKitConstants.KEY_TRANSACTION_RESULT)
//                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}