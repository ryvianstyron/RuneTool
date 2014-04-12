package utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginSessionTracker
{
	public static boolean resyncRequired = false;
	int hourNow = 0;
	int hourThen = 0;
	public static int getLastLoginSession(String loginSession)
	{
		if(loginSession.charAt(0) == '0')
		{
			loginSession = loginSession.substring(1,loginSession.length());
			return Integer.parseInt(loginSession);
		}
		else return Integer.parseInt(loginSession);
	}
	public static int getCurrentTime()
	{
		SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String now = fmt.format(date).substring(0,3);
		if(now.charAt(0) == '0')
		{
			now = now.substring(1,now.length());
		}
		return(Integer.parseInt(now));
	}
	public static boolean reset(int now, int then)
	{
		if(now >= then + 4)
		{
			return true;
		}
		else return false;
	}

}
