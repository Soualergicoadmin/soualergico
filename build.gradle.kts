// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.google.devtools.ksp) apply false
  alias(libs.plugins.roborazzi) apply false
  alias(libs.plugins.secrets) apply false
  alias(libs.plugins.google.services) apply false
}
dependencies {
    // Biblioteca para geração do QR Code (ZXing)
    implementation("com.google.zxing:core:3.5.3")
}
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

fun generateQrCodeBitmap(content: String, sizePx: Int = 512): Bitmap? {
    return try {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import java.io.OutputStream

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String = "emergency_qrcode"): Boolean {
    val filename = "${title}_${System.currentTimeMillis()}.png"
    var outputStream: OutputStream? = null
    var imageUri: Uri? = null

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/EmergencyApp")
        }
    }

    val resolver = context.contentResolver

    return try {
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        imageUri = resolver.insert(contentUri, contentValues)

        if (imageUri != null) {
            outputStream = resolver.openOutputStream(imageUri)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)
            outputStream?.close()
            Toast.makeText(context, "QR Code salvo na galeria!", Toast.LENGTH_LONG).show()
            true
        } else {
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Erro ao salvar imagem", Toast.LENGTH_SHORT).show()
        false
    }
}
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var currentQrBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageViewQrCode = findViewById<ImageView>(R.id.imageViewQrCode)
        val btnSaveGallery = findViewById<Button>(R.id.btnSaveGallery)

        // 1. A URL que o backend retornou (ex: com UUID)
        val myEmergencyUrl = "https://safe-my-alerts.onrender.com/v/9b1deb4d-3b7d-4bad-9bdd"

        // 2. Gera o QR Code visual
        currentQrBitmap = generateQrCodeBitmap(myEmergencyUrl)

        // 3. Exibe na tela
        currentQrBitmap?.let {
            imageViewQrCode.setImageBitmap(it)
        }

        // 4. Salva na galeria ao clicar no botão
        btnSaveGallery.setOnClickListener {
            currentQrBitmap?.let { bitmap ->
                saveBitmapToGallery(this, bitmap)
            }
        }
    }
}
