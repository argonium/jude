/**
 * @(#)Exporter.java
 * 
 * Created on Apr 30, 2007
 */

package io.miti.jude.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

/**
 * Handle exporting some particular classes to a String.
 * Other classes are dispatched to JSONExporter.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Exporter
{
  /**
   * Default constructor.
   */
  private Exporter()
  {
    super();
  }
  
  
  /**
   * Add a line to the output.
   * 
   * @param name parameter name
   * @param value parameter value
   * @param appendComma whether to append a comma
   * @return the line as a string
   */
  private static String addLine(final String name,
                                final int value,
                                final boolean appendComma)
  {
    return addLine(name, Integer.toString(value), false, appendComma, true);
  }
  
  
  /**
   * Add a line to the output.
   * 
   * @param name parameter name
   * @param value parameter value
   * @param appendComma whether to append a comma
   * @return the line as a string
   */
  private static String addLine(final String name,
                                final boolean value,
                                final boolean appendComma)
  {
    return addLine(name, Boolean.toString(value), false, appendComma, true);
  }
  
  
  /**
   * Add a line to the output.
   * 
   * @param name parameter name
   * @param value parameter value
   * @param quoteValue whether to quote the value
   * @param appendComma whether to append a comma
   * @return the line as a string
   */
  private static String addLine(final String name,
                                final String value,
                                final boolean quoteValue,
                                final boolean appendComma)
  {
    return addLine(name, value, quoteValue, appendComma, false);
  }
  
  
  /**
   * Add a line to the output.
   * 
   * @param name parameter name
   * @param value parameter value
   * @param quoteValue whether to quote the value
   * @param appendComma whether to append a comma
   * @param indented whether to indent the row
   * @return the line as a string
   */
  private static String addLine(final String name,
                                final String value,
                                final boolean quoteValue,
                                final boolean appendComma,
                                final boolean indented)
  {
    StringBuilder sb = new StringBuilder(100);
    if (indented)
    {
      sb.append("  ");
    }
    sb.append("  \"").append(name).append("\" : ");
    if (quoteValue)
    {
      sb.append("\"");
    }
    sb.append(value);
    if (quoteValue)
    {
      sb.append("\"");
    }
    if (appendComma)
    {
      sb.append(",");
    }
    sb.append(Utility.getLineSeparator());
    
    return sb.toString();
  }
  
  
  /**
   * Export the object.
   * 
   * @param obj the object to export
   * @param name the key name
   * @return the object's description
   */
  public static String export(final Object obj, final String name)
  {
    if (obj instanceof Number)
    {
      Number d = (Number) obj;
      
      StringBuilder sb = new StringBuilder(200);
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, d.toString(), false, false))
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof String)
    {
      StringBuilder sb = new StringBuilder(200);
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, obj.toString(), true, false))
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof Boolean)
    {
      StringBuilder sb = new StringBuilder(200);
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, obj.toString(), true, false))
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof Dimension)
    {
      Dimension d = (Dimension) obj;
      
      StringBuilder sb = new StringBuilder(200);
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, "{", false, false))
        .append(addLine("width", d.width, true))
        .append(addLine("height", d.height, false))
        .append("  }").append(Utility.getLineSeparator())
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof Color)
    {
      Color color = (Color) obj;
      
      StringBuilder sb = new StringBuilder(200);
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, "{", false, false))
        .append(addLine("red", color.getRed(), true))
        .append(addLine("green", color.getGreen(), true))
        .append(addLine("blue", color.getBlue(), true))
        .append(addLine("rgb", color.getRGB(), true))
        .append(addLine("alpha", color.getAlpha(), false))
        .append("  }").append(Utility.getLineSeparator())
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof java.awt.Font)
    {
      java.awt.Font f = (java.awt.Font) obj;
      StringBuilder sb = new StringBuilder(200);
      
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, "{", false, false))
        .append(addLine("family", f.getFamily(), true, true, true))
        .append(addLine("font", f.getFontName(), true, true, true))
        .append(addLine("size", f.getSize(), true))
        .append(addLine("style", f.getStyle(), true))
        .append(addLine("plain", f.isPlain(), true))
        .append(addLine("bold", f.isBold(), true))
        .append(addLine("italic", f.isItalic(), true))
        .append(addLine("bold+italic", f.isBold() && f.isItalic(), false))
        .append("  }").append(Utility.getLineSeparator())
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof Insets)
    {
      Insets f = (Insets) obj;
      StringBuilder sb = new StringBuilder(200);
      
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, "{", false, false))
        .append(addLine("top", f.top, true))
        .append(addLine("left", f.left, true))
        .append(addLine("bottom", f.bottom, true))
        .append(addLine("right", f.right, true))
        .append("  }").append(Utility.getLineSeparator())
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof ActionMap)
    {
      // Handle action maps
      StringBuilder sb = new StringBuilder(200);
      
      ActionMap am = (ActionMap) obj;
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, "{", false, false));
      
      // Iterate over the keys in the action map
      Object[] objects = am.keys();
      final int size = objects.length;
      for (int index = 0; index < size; ++index)
      {
        // Generate an output string for Actions
        Action action = am.get(objects[index]);
        final boolean lastLine = (index == (size - 1));
        sb.append(addLine(objects[index].toString(),
                          action.toString(),
                          true, !lastLine, true));
      }
      
      sb.append("  }").append(Utility.getLineSeparator())
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof InputMap)
    {
      // Handle input maps
      StringBuilder sb = new StringBuilder(200);
      
      InputMap im = (InputMap) obj;
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, "{", false, false));
      
      // Iterate over the keys in the action map
      KeyStroke[] strokes = im.keys();
      final int size = strokes.length;
      for (int index = 0; index < size; ++index)
      {
        Object object = im.get(strokes[index]);
        final boolean lastLine = (index == (size - 1));
        sb.append(addLine(strokes[index].toString(),
                          object.toString(),
                          true, !lastLine, true));
      }
      
      sb.append("  }").append(Utility.getLineSeparator())
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else if (obj instanceof Map)
    {
      // Handle maps
      StringBuilder sb = new StringBuilder(200);
      
      Map<?, ?> map = (Map<?, ?>) obj;
      sb.append("{").append(Utility.getLineSeparator())
        .append(addLine(name, "{", false, false));
      
      // Iterate over the keys in the map
      Set<?> set = map.keySet();
      final int size = set.size();
      int index = 0;
      Iterator<?> iter = set.iterator();
      while (iter.hasNext())
      {
        Object key = iter.next();
        Object object = map.get(key);
        final boolean lastLine = (index == (size - 1));
        sb.append(addLine(key.toString(),
                          object.toString(),
                          true, !lastLine, true));
        ++index;
      }
      
      sb.append("  }").append(Utility.getLineSeparator())
        .append("}").append(Utility.getLineSeparator());
      
      return sb.toString();
    }
    else
    {
      return JSONExporter.getInstance().export(obj, true);
    }
  }
}
