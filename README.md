# Web Scraping App

Una aplicación Android que realiza scraping de una página web para buscar una palabra clave específica. La aplicación permite configurar una URL y una palabra clave, y luego buscarla periódicamente en segundo plano utilizando **WorkManager**. Si la palabra se encuentra en el contenido de la página, la aplicación envía una notificación al usuario.

## 🛠 Características

- **Búsqueda en segundo plano**: Realiza la búsqueda en segundo plano sin necesidad de mantener la aplicación abierta.
- **Notificaciones**: Recibe una notificación cuando la palabra clave es encontrada.
- **Configuración flexible**: Establece una URL y una palabra clave, y elige la frecuencia de búsqueda (10 minutos, 30 minutos o 1 hora).
- **Contador de apariciones**: Lleva un registro de cuántas veces la palabra clave ha sido encontrada en la página.

- ## 🚀 Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/juliogtrenard/WebScraping.git

2. **Importa el proyecto en Android Studio**:
   - Abre Android Studio.
   - Selecciona **Open an existing project**.
   - Navega al directorio donde clonaste el repositorio y abre la carpeta del proyecto.

3. **Configura tu entorno**:
   - Asegúrate de tener configurado tu **emulador** o **dispositivo físico** para probar la aplicación.

4. **Construye y ejecuta la aplicación**:
   - Haz clic en el botón de **Run** (`Shift + F10`) en Android Studio.
   - La aplicación debería instalarse en tu dispositivo/emulador.

## ⚙️ Configuración

1. **Permisos necesarios**  
   La aplicación necesita el permiso de notificaciones, que se solicita automáticamente en tiempo de ejecución si es necesario.

2. **Frecuencia de búsqueda**  
   Puedes configurar la frecuencia de búsqueda (cada 10 minutos, 30 minutos o 1 hora) desde la interfaz de usuario.

3. **Datos guardados**  
   Los datos de la URL, la palabra clave y el estado de la búsqueda se guardan en **SharedPreferences**.

## 📱 Capturas de pantalla

<img src="https://github.com/juliogtrenard/WebScraping/blob/master/app/src/main/res/drawable/imgdemo.png" alt="Aplicación" align="center"/>

## 🔧 Tecnologías usadas

- **Kotlin**: Lenguaje de programación para el desarrollo Android.
- **Android SDK**: Herramientas para crear aplicaciones Android.
- **WorkManager**: Librería de Android para tareas en segundo plano.
- **Jsoup**: Librería para realizar scraping de páginas web.
- **SharedPreferences**: Almacenamiento local para guardar los datos de configuración.
