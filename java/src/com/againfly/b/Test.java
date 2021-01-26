package com.againfly.b;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Test {

    public volatile static long i = 0;

    public static void main(String[] args) throws InterruptedException {
        List<Thread> list = new ArrayList<Thread>();
        for(int i = 0; i < 100; i++){
            Thread t = new Thread(new ThreadBean(i));
            list.add(t);
        }
        for(Thread t : list){
            t.start();
        }
        Thread.sleep(1000 * 10);
        System.out.println("最终 i : " + i);
    }
}

class ThreadBean implements Runnable{
    int id ;

    public ThreadBean(int id){
        this.id = id;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10000; i++){
//            Test.i ++;
            synchronized (Test.class){
                long t = Test.i;
                t += 1;
                Test.i = t;
            }
        }
    }
}
class TimeUtil{
    private static Map<String, Long> cache = new HashMap<>();

    private static void time(String key){
        cache.put(key, System.currentTimeMillis());
    }

    private static void timeEnd(String key){
        if(!cache.containsKey(key)) return;

        long time = cache.remove(key);
        long now = System.currentTimeMillis();
        System.out.println(now - time);
    }
}