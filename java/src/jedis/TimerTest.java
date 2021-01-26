package jedis;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimerTest {
    public static void main(String[] args) throws Exception {
//        Timer timer = new Timer();
//
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println(System.currentTimeMillis());
//            }
//        },0, 1000);


        HashMap ma = null;
        LinkedList li = null;


        Map m1 = new HashMap();
        Map m2 = new ConcurrentHashMap();

        List l1 = new LinkedList();
        List l2 = new ArrayList();
        List l3 = new CopyOnWriteArrayList();


        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(System.currentTimeMillis());
            }
        }).start();
    }


}
