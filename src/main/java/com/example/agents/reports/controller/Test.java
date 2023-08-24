package com.example.agents.reports.controller;

public class Test implements Runnable{

    public int PRINT_NUMBERS_UPTO=10;
    static int  number=1;
    int remainder;
    static Object lock=new Object();
    Test(int remainder)
    {
        this.remainder=remainder;
    }
    @Override
    public void run() {
        while (number < PRINT_NUMBERS_UPTO) {
            synchronized (lock) {
                while (number % 2 != remainder) { // wait for numbers other than remainder
                    try {
                        lock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + " " + number);
                number++;
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        String s= "Hello";
        String s2= "Hello";
        String s1 = new String ("Hello");
        if(s.equals(s1)) {
            System.out.println(true);
        }

        if(s.equals(s2)) {
            System.out.println("=="+true);
        }


        Test odd = new Test(1);
        Test even = new Test(0);

        Thread t1=new Thread(odd,"Odd");
        Thread t2=new Thread(even,"Even");

        t1.start();
        t2.start();

    }
}

