package ada.edu;

import java.util.*;
import java.io.*;
import java.util.ArrayList;


public class Main {

    public static void main (String[] args) {

        Publisher p = new Publisher();
        Producer pr = new Producer(p);

        Subscriber s1 = new Subscriber();
        p.subscribe(s1);


        Subscriber s2 = new Subscriber();
        p.subscribe(s2);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {

            try {
                System.out.println("Please enter the folder: ");
                String folderName = bufferedReader.readLine();
                pr.publish(folderName);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static class Publisher {

        ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();

        public void publish (String message) {
            for (Subscriber s : subscribers) {
                s.onMessage(message);
            }
        }

        public void subscribe (Subscriber s) {
            subscribers.add(s);

        }
    }

    public static class Subscriber {

        public void onMessage (String message) {
            System.out.println(message);

        }
    }

    public static class Producer {

        Publisher publisher;

        public Producer (Publisher publisher) {
            this.publisher = publisher;
        }

        public void publish (String folder) {

            File[] files = new File(folder).listFiles();


            for (int i = 0; files != null && i < files.length; i++) {
                if(files[i].isDirectory()){
                    publish(folder+"/"+files[i].getName());
                }
                publisher.publish("The folder " + folder + " has file " + files[i]);
            }
        }


    }
}