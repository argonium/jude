/**
 * @(#)ResourceLoader.java
 * 
 * Created on Jan 5, 2008
 */

package io.miti.jude.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Class to add a file to the classpath.
 * 
 * @author Tres
 * @version 1.0
 */
public final class ResourceLoader
{
  /**
   * Parameters.
   */
  private static final Class<?>[] parameters = new Class[]{URL.class};
  
  /**
   * Default constructor.
   */
  private ResourceLoader()
  {
    super();
  }
  
  
  /**
   * Add a file by name to the classpath.
   * 
   * @param s the file name
   */
  public static void addFile(final String s)
  {
    addFile(new File(s));
  }
  
  
  /**
   * Add a file by reference to the classpath.
   * 
   * @param f the file reference
   */
  public static void addFile(final File f)
  {
    if (f.exists())
    {
      try
      {
        addURL(f.toURI().toURL());
      }
      catch (Exception e)
      {
        Logger.error(f.getName() + " has malformed URL/URI: " + e.getMessage());
        Logger.error(e);
      }
    }
  }
  
  
  /**
   * Add a file by URL to the classpath.
   * 
   * @param u the URL
   */
  public static void addURL(final URL u)
  {  
    URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
    try
    {
      Method method = URLClassLoader.class.getDeclaredMethod("addURL", parameters);
      method.setAccessible(true);
      method.invoke(sysloader, new Object[]{u});
      // Logger.info("Success adding " + u.toString());
    } 
    catch (Exception e)
    {
      Logger.error("Could not add " + u.toString() +
                   " to system classLoader: " + e.getMessage());
      Logger.error(e);
    }
  }
}
