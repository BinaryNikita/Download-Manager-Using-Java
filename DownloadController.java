import java.util.Scanner;

public class DownloadController {
    public static void main(String[] args) throws InterruptedException {
        FileDownloader downloader = new FileDownloader("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf");
        Thread downloadThread = new Thread(downloader);
        downloadThread.start();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nType 'pause' to pause or 'resume' to continue:");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("pause")) {
                downloader.pause();
                System.out.println("Download paused...");
            } else if (command.equalsIgnoreCase("resume")) {
                downloader.resume();
                System.out.println("Download resumed...");
            }
        }
    }
}
