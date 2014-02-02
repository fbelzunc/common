package com.cisco.vss.foundation.environment.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * utility yp update environemnt variables in the system via code.
 * useful for affecting CCP_ENABLED variables and others.
 * @author yogen
 *
 */
public final class EnvUtils {
	
	public static void updateEnv(String key, String value)
	{
		try
		{
			String os = System.getProperty("os.name");
			Class<?> procEnvClass = EnvUtils.class.forName("java.lang.ProcessEnvironment");

			if (os.toLowerCase().contains("win"))
			{
				Field field = procEnvClass.getDeclaredField("theCaseInsensitiveEnvironment");
				field.setAccessible(true);
				Map map = (Map) field.get(procEnvClass);
				map.put(key, value);
			} else
			{
//				System.out.println("---- set ENV parameters ----");
				Field mapField = procEnvClass.getDeclaredField("theUnmodifiableEnvironment");
				mapField.setAccessible(true);
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(mapField, mapField.getModifiers() & ~Modifier.FINAL);

				Map<String, String> env = (Map<String, String>) mapField.get(procEnvClass);
				Map<String, String> copyEnv = new HashMap<String, String>();
				for (String k : env.keySet())
				{
					String v = env.get(k);
					copyEnv.put(k, v);
				}
				copyEnv.put(key, value);
				mapField.set(null, (Map<String, String>) copyEnv);
			}
		} catch (Exception e)
		{
			e.printStackTrace();

		}
	}

}
