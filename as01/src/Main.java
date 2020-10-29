import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    final static int coreMultiplier = 3;

    public static void main(String[] args) {

        String[] urls = {
                "http://www.ubicomp.org/ubicomp2003/adjunct_proceedings/proceedings.pdf",
                "https://www.hq.nasa.gov/alsj/a17/A17_FlightPlan.pdf",
                "https://ars.els-cdn.com/content/image/1-s2.0-S0140673617321293-mmc1.pdf",
                "http://www.visitgreece.gr/deployedFiles/StaticFiles/maps/Peloponnese_map.pdf"
        };

        Instant start = Instant.now();

        if(args.length > 0 && "1".equals(args[0])){
            MultiThread(urls);
        }
        else{
            SingleThread(urls);
        }


        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).getSeconds();


        System.out.println("Time:" + timeElapsed + " seconds");


    }
    public static void LinkDownload(String url){
        try
        {
            String fileName = url.substring(url.lastIndexOf('/')+1,url.length());

            InputStream input = new URL(url).openStream();
            Files.copy(input, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);

            print(fileName);

        }catch(Exception e){
            System.out.println("Error: " + e);
        }

    }


    public static synchronized void print(String fileName){

        System.out.println("   File --> " + fileName + " is done; ");
        System.out.flush();
    }

    public static void SingleThread(String[] urls){
        System.out.println("Mode:Single Threaded");
        System.out.println("Files: ");
        for(int i=0;i<urls.length;i++){
            LinkDownload(urls[i]);
        }
    }

    public static void MultiThread(String[] urls){
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Cores:" + cores );


        final ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<String>();
        for(int i=0;i<urls.length;i++){
            clq.add(urls[i]);
        }

        System.out.println("Mode:Multi Threaded");
        System.out.println("Files: ");

        Thread[] threads = new Thread[cores*coreMultiplier];
        for(int i=0;i<cores*coreMultiplier;i++){

            threads[i] =  new Thread()
            {
                public void run() {
                    while(true){
                        String u = clq.poll();
                        if(u == null)
                            break;
                        LinkDownload(u);
                    }
                }
            };
            threads[i].start();
        }

        for(int i=0;i<threads.length;i++){
            try{
                threads[i].join();
            }catch(Exception e){
                System.out.println("Error: " + e);
            }
        }
    }
}