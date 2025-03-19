import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader implements Runnable {
    private final String fileURL;
    private static final int MAX_RETRIES = 3;
    private boolean paused = false;

    public FileDownloader(String fileURL) {
        this.fileURL = fileURL;
    }

    @Override
    public void run() {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                downloadFile();
                break; 
            } catch (Exception e) {
                attempt++;
                System.out.println("\nRetrying download (" + attempt + "/" + MAX_RETRIES + ") for: " + fileURL);
            }
        }
    }

    private void downloadFile() throws Exception {
        URL url = new URL(fileURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int fileSize = connection.getContentLength();
        
        if (fileSize == -1) {
            throw new IOException("Invalid file size. The URL may be incorrect.");
        }

        String fileName = getFileName(fileURL);
        try (InputStream in = connection.getInputStream();
             FileOutputStream out = new FileOutputStream(fileName)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            int totalRead = 0;
            long startTime = System.currentTimeMillis();

            while ((bytesRead = in.read(buffer)) != -1) {
                synchronized (this) {
                    while (paused) wait(); 
                }
                out.write(buffer, 0, bytesRead);
                totalRead += bytesRead;
                
                long elapsedTime = System.currentTimeMillis() - startTime;
                showProgress(totalRead, fileSize, elapsedTime);
            }
            System.out.println("\nDownload complete: " + fileName);
        }
    }

    private void showProgress(int bytesRead, int totalSize, long elapsedTime) {
        int percent = (int) (((double) bytesRead / totalSize) * 100);
        double speed = bytesRead / (elapsedTime / 1000.0 + 1); 
        System.out.printf("\rDownloading: %d%% (%.2f KB/s)", percent, speed / 1024);
    }

    private String getFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }
}
