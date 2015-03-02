/**
 * @(#)ThemeLoader.java
 * 
 * Created on Jan 5, 2008
 */

package io.miti.jude.util;

import java.io.BufferedReader;
import java.io.FilenameFilter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/*
.themes\
  <Multiple jar files>
  themes.txt

themes.txt
----------
# List of classes to load
a.b.c.d
e.f.g.h
i.j.k.l


Steps
-----
1. At startup, if .themes and themes.txt exist,
read the class names in the file. If a line in
themes.txt starts with a #, skip it.
3. If the list is not empty, get the list of
jars in .themes and add each one to the 
classpath via ResourceLoader.
4. Instantiate each L&F class and add the
class name and L&F name to the list in LAFAdmin.
The L&F name comes from getName().  Skip an entry
if it's already in the list (match on class name).

Create via: LookAndFeelInfo x = new LookAndFeelInfo("name", "class");
 */


/**
 * Class to handle loading external themes (look and feels).
 * 
 * @author Mike
 * @version 1.0
 */
public final class ThemeLoader
{
  /**
   * The single instance of this class.
   */
  private static final ThemeLoader loader = new ThemeLoader();
  
  /**
   * The list of class names and theme names.
   */
  private HashMap<String, String> themes = new LinkedHashMap<String, String>(20);
  
  /**
   * Whether the themes have been loaded.
   */
  private boolean themesLoaded = false;
  
  
  /**
   * Default constructor.
   */
  private ThemeLoader()
  {
    super();
  }
  
  
  /**
   * Return the single instance of this class.
   * 
   * @return the single instance of this class
   */
  public static ThemeLoader getInstance()
  {
    return loader;
  }
  
  
  /**
   * Return the set of themes.
   * 
   * @return the set of themes
   */
  public Set<Entry<String, String>> getThemes()
  {
    loadThemes();
    return themes.entrySet();
  }
  
  
  /**
   * Load the themes in the subdirectory.
   */
  public void loadThemes()
  {
    // Check if the themes have already been loaded
    if (themesLoaded)
    {
      return;
    }
    
    // Load the themes now
    themesLoaded = true;
    final File dir = new File(".", ".themes");
    if ((dir.exists()) && (dir.isDirectory()))
    {
      // Look for the .ini file
      File file = new File(dir, "themes.txt");
      if ((file.exists()) && (file.isFile()))
      {
        // Get the contents (class names) of the file
        Iterator<String> lines = getFileContents(file);
        
        // If there are class names to load, load any jars
        if (lines.hasNext())
        {
          // Get all the jar files in the directory
          String[] list = dir.list(new JarFilter());
          if ((list != null) && (list.length > 0))
          {
            for (String jar : list)
            {
              // Add this file to the class path
              ResourceLoader.addFile(new File(dir, jar));
            }
          }
          
          // Get a set of the installed class names
          Set<String> installedLAFs = getInstalledLAFClasses();
          
          // Now instantiate each L&F class and save it
          while (lines.hasNext())
          {
            String clazz = lines.next();
            
            // Check that we haven't already added this class name
            // and it's not in the list of installed class names
            if ((!themes.containsKey(clazz)) && (!installedLAFs.contains(clazz)))
            {
              // Get the name of the L&F, by instantiating the class
              String name = getLAFName(clazz);
              if (name != null)
              {
                // The name isn't null, so add the class and name
                // Logger.info("Adding class " + clazz + ", named " + name);
                themes.put(clazz, name);
              }
            }
          }
        }
      }
    }
  }
  
  
  /**
   * Return a set of the installed L&F classes.
   * 
   * @return a set of the installed L&F classes
   */
  private static Set<String> getInstalledLAFClasses()
  {
    Set<String> set = new HashSet<String>(20);
    LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
    if ((infos != null) && (infos.length > 0))
    {
      for (LookAndFeelInfo lafi : infos)
      {
        set.add(lafi.getClassName());
      }
    }
    
    return set;
  }
  
  
  /**
   * Return the name of the L&F referenced by this class.
   * 
   * @param className the L&F class name
   * @return the name of the L&F
   */
  private static String getLAFName(final String className)
  {
    String lafName = null;
    try
    {
      Class<?> cl = Class.forName(className);
      if (cl != null)
      {
        LookAndFeel laf = (LookAndFeel) cl.newInstance();
        if (laf != null)
        {
          lafName = laf.getName();
        }
      }
    }
    catch (Exception e)
    {
      Logger.error("Exception instantiating " + className +
                   ": " + e.getMessage());
    }
    
    return lafName;
  }
  
  
  /**
   * Return the contents of the specified file.
   * 
   * @param file the file to read
   * @return an iterator to each line in the file
   */
  private static Iterator<String> getFileContents(final File file)
  {
    BufferedReader in = null;
    List<String> lines = new ArrayList<String>(20);
    try
    {
      in = new BufferedReader(new FileReader(file));
      String str;
      while ((str = in.readLine()) != null)
      {
        // Trim the string
        str = str.trim();
        
        // If it doesn't start with a #, add it to the list
        if ((!str.startsWith("#")) && (str.length() > 0))
        {
          lines.add(str);
        }
      }
      
      // Close the reader
      in.close();
      in = null;
    }
    catch (IOException e)
    {
      Logger.error("IOException reading themes.txt: " + e.getMessage());
    }
    finally
    {
      if (in != null)
      {
        try
        {
          in.close();
        }
        catch (IOException ioe)
        {
          Logger.error(ioe);
        }
        in = null;
      }
    }
    
    return lines.iterator();
  }
}


/**
 * A file filter to only accept JARs.
 */
class JarFilter implements FilenameFilter
{
  /**
   * Method to only accept files ending with .jar.
   * 
   * @param dir the directory
   * @param name the file name
   * @return whether the file was accepted
   */
  @Override
  public boolean accept(final File dir, final String name)
  {
    // Only accept files ending with .jar
    if (name.toLowerCase().endsWith(".jar"))
    {
      if ((new File(dir, name)).isFile())
      {
        return true;
      }
    }
    
    return false;
  }
}
