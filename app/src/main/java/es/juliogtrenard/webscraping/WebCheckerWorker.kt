package es.juliogtrenard.webscraping

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.jsoup.Jsoup
import java.lang.Exception
import java.util.Locale

/**
 * Worker que realiza la tarea de comprobar una página web en segundo plano
 * para verificar si contiene una palabra específica y, si es así, envía
 * una notificación.
 *
 * <p>Esta clase utiliza 'Jsoup' para realizar la conexión HTTP y extraer
 * el contenido de texto de una página web. La URL y la palabra a buscar
 * se almacenan en las preferencias compartidas de la aplicación
 * ('SharedPreferences'). Si la palabra se encuentra en el contenido de la
 * página, se envía una notificación al usuario.</p>
 *
 * @param appContext El contexto de la aplicación.
 * @param workerParams Parámetros adicionales proporcionados por WorkManager.
 * @author Julio González
 */
class WebCheckerWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    /**
     * Función principal que realiza la tarea en segundo plano.
     *
     * <p>Obtiene la URL y la palabra a buscar desde 'SharedPreferences',
     * se conecta a la página web usando 'Jsoup', y verifica si la palabra
     * está presente en el contenido. Si encuentra la palabra, llama a
     * 'sendNotification' para enviar una notificación.</p>
     *
     * @return Un objeto [Result] que indica si el trabajo fue exitoso o no.
     */
    override fun doWork(): Result {
        try {
            val sharedPreferences = applicationContext.getSharedPreferences("WebCheckerPrefs", Context.MODE_PRIVATE)

            val semaforo = sharedPreferences.getString("semaforo", null) ?: return Result.failure()

            if (isStopped || semaforo.equals("R")) {
                println("stopped")
                return Result.failure()

            }

            // Obtener la URL y la palabra desde las preferencias compartidas
            val url = sharedPreferences.getString("url", null) ?: return Result.failure()
            val word = sharedPreferences.getString("word", null) ?: return Result.failure()
            if (isStopped || semaforo.equals("R")) {
                return Result.failure()
            }

            // Conectar a la página web
            val doc = Jsoup.connect(url).get()
            if (isStopped || semaforo.equals("R")) {
                return Result.failure()
            }

            // Verificar si la palabra está presente
            if (doc.text().contains(word, ignoreCase = true)) {
                // Actualizar el contador y fecha
                val currentCount = sharedPreferences.getInt("word_count", 0)
                val newCount = currentCount + 1
                val lastFoundDate = System.currentTimeMillis()

                // Guardar el nuevo contador y la fecha
                sharedPreferences.edit().apply {
                    putInt("word_count", newCount)
                    putLong("last_found_date", lastFoundDate)
                    apply()
                }

                sendNotification(newCount, lastFoundDate)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }

        return Result.success()
    }

    /**
     * Envía una notificación al usuario si se encuentra la palabra en la página web.
     *
     * <p>La notificación se muestra con el título "¡Se encontró la palabra!"
     * y un mensaje que indica que la palabra fue encontrada.</p>
     */
    private fun sendNotification(count: Int, lastFoundDate: Long) {
        val context = applicationContext

        // Crear el canal de notificación (solo necesario para API 26+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "guestlist_channel",
                "Guestlist Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de palabras encontradas"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val ultimaFechaFormateada = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(lastFoundDate)

        // Crear y enviar la notificación
        val notification: Notification = NotificationCompat.Builder(context, "guestlist_channel")
            .setContentTitle("¡Se encontró la palabra!")
            .setContentText("La palabra fue encontrada $count veces. Última vez: $ultimaFechaFormateada")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}