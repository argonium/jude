/**
 * @(#)SysProps.java
 * 
 * Created on Dec 30, 2007
 */

package io.miti.jude.model;

/**
 * Class to encapsulate the data displayed in the system properties table.
 * 
 * @author mwallace
 * @version 1.0
 */
public final class SysProps implements Comparable<SysProps>
{
  /**
   * The key.
   */
  private String key;
  
  /**
   * The value for this key.
   */
  private String value;
  
  
  /**
   * Default constructor.
   */
  public SysProps()
  {
    super();
  }
  
  
  /**
   * Constructor taking all parameters.
   * 
   * @param sKey the key
   * @param sValue the value
   */
  public SysProps(final String sKey, final String sValue)
  {
    key = sKey;
    value = sValue;
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
   * Return the value (String) for this key.
   * 
   * @return the String value
   */
  public String getValue()
  {
    return value;
  }
  
  
  /**
   * Compare two instances of this class.
   * 
   * @param o the object to compare this to
   * @return the result of the comparison
   */
  public int compareTo(final SysProps o)
  {
    return key.compareTo(o.key);
  }
}
