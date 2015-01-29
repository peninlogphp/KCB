package lipuka.android.model;

import kcb.android.LipukaApplication;
import kcb.android.Main;
import android.util.Log;

public class Waiter extends Thread
{
    private long lastUsed;
    private long period;
    private boolean stop;
    LipukaApplication lipukaApplication;
    public Waiter(LipukaApplication lipukaApplication, long period)
    {
        this.lipukaApplication=lipukaApplication;
        this.period=period;
        stop=false;
    }

    public void run()
    {
        long idle=0;
        this.touch();
        do
        {
            idle=System.currentTimeMillis()-lastUsed;
           // Log.d(Main.TAG, "Application is idle for "+idle +" ms");
            try
            {
                Thread.sleep(5000); //check every 5 seconds
            }
            catch (InterruptedException e)
            {
                Log.d(Main.TAG, "Waiter interrupted!");
            }
            if(idle > period)
            {
                idle=0;
	    		lipukaApplication.setProfileID(0);
                //do something here - e.g. call popup or so
            }
        }
        while(!stop);
        Log.d(Main.TAG, "Finishing Waiter thread");
    }

    public synchronized void touch()
    {
        lastUsed=System.currentTimeMillis();
    }

    public synchronized void forceInterrupt()
    {
        this.interrupt();
    }

    //soft stopping of thread
    public synchronized void stopWaiter()
    {
        stop=true;
    }

    public synchronized void setPeriod(long period)
    {
        this.period=period;
    }

}