package com.everis.reactivex;

import io.reactivex.rxjava3.core.Flowable;

/**
 * @author Giancarlo Yarleque!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Flowable<String> flow = Flowable.just("giancarlo", "elvis","yarleque","juarez");
        flow.subscribe(System.out::println);
    }
}
