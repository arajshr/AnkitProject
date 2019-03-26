package configuration;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Random;

public class ActiveScreen implements Runnable
{
	Robot hal;
    Random random;
	
	public ActiveScreen() 
	{
		try 
		{
			hal = new Robot();
	        random = new Random();
		} 
		catch (AWTException e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() 
	{
		while(true)
        {
            hal.delay(1000 * 60);
            
           /* int x = random.nextInt() % 640;
            int y = random.nextInt() % 480;
            hal.mouseMove(x,y);*/
            
            hal.keyPress(KeyEvent.VK_CONTROL);
            hal.keyRelease(KeyEvent.VK_CONTROL);
            
            //System.out.println("In thread");
        }
		
	}
}
