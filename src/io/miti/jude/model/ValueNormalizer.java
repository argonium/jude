/**
 * @(#)ValueNormalizer.java
 * 
 * Created on Dec 27, 2007
 */

package io.miti.jude.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

/**
 * Class that provides a public static method for generating
 * a toString() for different classes, based on the value
 * passed to the method.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class ValueNormalizer
{
  /**
   * Default constructor.
   */
  private ValueNormalizer()
  {
    super();
  }
  
  
  /**
   * Return a reasonable toString() value for the object.
   * 
   * @param obj the object to describe
   * @return a description of the object
   */
  public static String makeString(final Object obj)
  {
    // Check for null
    if (obj == null)
    {
      return "[null]";
    }
    
    // Check for different classes
    String value;
    if (obj instanceof Integer)
    {
      value = Integer.toString(((Integer) obj));
    }
    else if (obj instanceof Color)
    {
      Color c = (Color) obj;
      value = "Color: R=" + c.getRed() + " G=" + c.getGreen() + " B=" + c.getBlue();
    }
    else if (obj instanceof Insets)
    {
      Insets c = (Insets) obj;
      value = "Insets: Top=" + c.top + " Left=" + c.left +
              " Bottom=" + c.bottom + " Right=" + c.right;
    }
    else if (obj instanceof Dimension)
    {
      Dimension c = (Dimension) obj;
      value = "Dimension: Width=" + c.width + " Height=" + c.height;
    }
//    else if (obj instanceof Border)
//    {
//      Border c = (Border) obj;
//      value = "Border: " + c.toString();
//    }
    else if (obj instanceof Font)
    {
      Font c = (Font) obj;
      value = "Font: Family=" + c.getFamily() + "  Name=" + c.getName() +
              "  Style=";
      
      // Check the font style
      if ((c.isBold()) && (c.isItalic()))
      {
        value += "Bold-Italic";
      }
      else if (c.isBold())
      {
        value += "Bold";
      }
      else if (c.isItalic())
      {
        value += "Italic";
      }
      else if (c.isPlain())
      {
        value += "Plain";
      }
      else
      {
        value += "Unknown";
      }
      
      value += "  Size=" + c.getSize();
    }
    else
    {
      value = obj.toString();
    }
    
    return value;
  }
}
