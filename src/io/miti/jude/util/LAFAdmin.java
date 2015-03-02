/**
 * @(#)LAFAdmin.java
 * 
 * Created on Dec 31, 2007
 */

package io.miti.jude.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Class to manage look and feels.
 * 
 * @author Mike
 * @version 1.0
 */
public final class LAFAdmin
{
  /**
   * The installed look-and-feels.
   */
  private static List<LookAndFeelInfo> lafs = null;
  
  
  /**
   * Default constructor.
   */
  private LAFAdmin()
  {
    super();
  }
  
  
  /**
   * Load the installed and referenced L&Fs.
   */
  private static void loadData()
  {
    if (lafs != null)
    {
      return;
    }
    
    lafs = new ArrayList<LookAndFeelInfo>(20);
    for (LookAndFeelInfo lafi : UIManager.getInstalledLookAndFeels())
    {
      lafs.add(lafi);
    }
    
    Set<Entry<String, String>> themes = ThemeLoader.getInstance().getThemes();
    for (Entry<String, String> entry : themes)
    {
      lafs.add(new LookAndFeelInfo(entry.getValue(), entry.getKey()));
    }
  }
  
  
  /**
   * Return the number of look and feels.
   * 
   * @return the number of look and feels
   */
  public static int getSize()
  {
    loadData();
    return lafs.size();
  }
  
  
  /**
   * Set the look and feel by array index.
   * 
   * @param index the index of the LAF to use
   */
  public static void setByIndex(final int index)
  {
    loadData();
    Utility.initLookAndFeel(lafs.get(index).getClassName());
  }
  
  
  /**
   * Return an array of the names of the LAFs.
   * 
   * @return an array of the names of the LAFs
   */
  public static String[] getNames()
  {
    loadData();
    
    String[] names = new String[lafs.size()];
    for (int i = 0; i < lafs.size(); ++i)
    {
      names[i] = lafs.get(i).getName();
    }
    return names;
  }
  
  
  /**
   * Return the index in lafs of the default (current) LAF.
   * 
   * @return the index in lafs of the default (current) LAF
   */
  public static int getDefaultIndex()
  {
    loadData();
    
    // Get the name of the current LAF
    String name = UIManager.getLookAndFeel().getName();
    
    // Iterate over the list of LAFs and find the match
    int match = -1;
    for (int i = 0; (match < 0) && (i < lafs.size()); ++i)
    {
      if (name.equals(lafs.get(i).getName()))
      {
        // We have a match, so save the index
        match = i;
      }
    }
    
    // Return the matching index
    return match;
  }
}
