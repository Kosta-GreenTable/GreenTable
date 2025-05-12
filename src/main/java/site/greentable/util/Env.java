package site.greentable.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Env {
	public static Properties pr;

	static {
		InputStream is = Env.class.getClassLoader().getResourceAsStream("env.properties");
		try {
			pr.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
