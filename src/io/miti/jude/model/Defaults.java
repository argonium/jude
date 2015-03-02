/**
 * @(#)Defaults.java
 * 
 * Created on Dec 27, 2007
 */

package io.miti.jude.model;

/**
 * Class to encapsulate the data displayed in the table.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class Defaults implements Comparable<Defaults>
{
  /**
   * The key.
   */
  private String key;
  
  /**
   * The normalized class name for the value.
   */
  private String valueClass;
  
  /**
   * The value, as an Object.
   */
  private Object valueObject;
  
  /**
   * The value for this key.
   */
  private String value;
  
  
  /**
   * Default constructor.
   */
  public Defaults()
  {
    super();
  }
  
  
  /**
   * Constructor taking all parameters.
   * 
   * @param sKey the key
   * @param sValue the value
   * @param sClass the class name for the key
   */
  public Defaults(final String sKey, final String sValue,
                  final String sClass)
  {
    key = sKey;
    value = sValue;
    valueClass = sClass;
  }
  
  
  /**
   * Constructor taking the original objects.
   * 
   * @param pKey the key
   * @param pValue the value
   */
  public Defaults(final Object pKey, final Object pValue)
  {
    // Save the data
    key = pKey.toString();
    valueObject = pValue;
    if (pValue == null)
    {
      valueClass = "[null]";
      value = "[null]";
    }
    else
    {
      valueClass = pValue.getClass().getName();
      value = ValueNormalizer.makeString(pValue);
    }
  }
  
  
  /**
   * Return the key.
   * 
   * @return the key
   */
  public String getKey()
  {
    return key;
  }
  
  
  /**
   * Return the value's class name.
   * 
   * @return the value's class name
   */
  public String getValueClass()
  {
    return valueClass;
  }
  
  
  /**
   * Return the value (String) for this key.
   * 
   * @return the String value
   */
  public String getValue()
  {
    return value;
  }
  
  
  /**
   * Return the value (Object) for this key.
   * 
   * @return the Object value
   */
  public Object getValueAsObject()
  {
    return valueObject;
  }
  
  
  /**
   * Compare two instances of this class.
   * 
   * @param o the object to compare this to
   * @return the result of the comparison
   */
  public int compareTo(final Defaults o)
  {
    return key.compareTo(o.key);
  }
}
