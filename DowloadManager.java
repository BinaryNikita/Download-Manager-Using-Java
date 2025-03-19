import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DowloadManager {
    private static final int MAX_THREADS = 3;
    private final ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

    public void startDownload(String urls[]) {
        for (String url : urls) {
            FileDownloader fileDownloader = new FileDownloader(url);
            executorService.execute(fileDownloader);
        }
    }
public static void main(String[] args) {



}
}
