/**
 * @(#)DefaultsModel.java
 * 
 * Created on Dec 27, 2007
 */

package io.miti.jude.gui;

import io.miti.jude.model.Defaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;

/**
 * Table model.
 * 
 * @author mwallace
 * @version 1.0 */
public final class DefaultsModel extends AbstractTableModel
{
  /**
   * The default serial version ID.
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * The names of the columns.
   */
  private static final String[] columnNames = {"Key", "Class", "Value"};
  
  /**
   * The current row count.
   */
  private int rowCount = 0;
  
  /**
   * The list of UI defaults.
   */
  private List<Defaults> list = new ArrayList<Defaults>(800);
  
  /**
   * The filtered list of indices.
   */
  private List<Integer> filteredList = new ArrayList<Integer>(800);
  
  /**
   * Whether to use the filtered list.
   */
  private boolean useFilter = false;
  
  
  /**
   * Default constructor.
   */
  public DefaultsModel()
  {
    super();
  }
  
  
  /**
   * Returns the number of rows.
   * 
   * @return the number of rows
   */
  @Override
  public int getRowCount()
  {
    if (useFilter)
    {
      return filteredList.size();
    }
    
    return rowCount;
  }
  
  
  /**
   * Returns the number of columns.
   * 
   * @return the number of columns
   */
  @Override
  public int getColumnCount()
  {
    return 3;
  }
  
  
  /**
   * Returns the name of the column.
   * 
   * @param col the column to get the name for
   * @return the name of the specified column
   */
  @Override
  public String getColumnName(final int col)
  {
    return columnNames[col];
  }
  
  
  /**
   * Return the object at the specified row.
   * 
   * @param row the row of interest
   * @return the Defaults object at the specified row index
   */
  public Defaults getDefaultsAt(final int row)
  {
    // Get the row index to use, based on whether we're filtering
    // or not
    final int index = (useFilter) ? filteredList.get(row) : row;
    
    // Get the appropriate object, based on the index
    return list.get(index);
  }
  
  
  /**
   * Retrieves a value from a row/column.
   * 
   * @param rowIndex the row index
   * @param columnIndex the column index
   * @return the value at the specified row/column
   */
  public Object getValueAt(final int rowIndex,
                           final int columnIndex)
  {
    // Get the row index to use, based on whether we're filtering
    // or not
    final int index = (useFilter) ? filteredList.get(rowIndex) : rowIndex;
    
    // Get the appropriate object, based on the index
    Defaults def = list.get(index);
    switch (columnIndex)
    {
      case 0:
        return def.getKey();
        
      case 1:
        return def.getValueClass();
      
      case 2:
        return def.getValue();
      
      default:
        return "x";
    }
  }
  
  
  /**
   * Initialize the table with all UI defaults.
   */
  public void initialize()
  {
    // Get the UI defaults
    useFilter = false;
    filteredList.clear();
    setValueClass(0);
  }
  
  
  /**
   * Filter the data based on the class of the value.
   * 
   * @param classId the ID of the class of interest
   */
  public void setValueClass(final int classId)
  {
    // Get all of the UIDefaults
    UIDefaults defaults = UIManager.getDefaults();
    
    // Enumerate all keys
    list.clear();
    Enumeration<Object> keys = defaults.keys();
    while (keys.hasMoreElements())
    {
      Object key = keys.nextElement();
      Object value = defaults.get(key);
      
      if (shouldAdd(value, classId))
      {
        list.add(new Defaults(key, value));
      }
    }
    
    // Save the number of results
    rowCount = list.size();
    
    // Sort the list
    Collections.sort(list);
  }
  
  
  /**
   * Whether to add this value to the table.
   * 
   * @param value the value to check
   * @param classId the ID of the class we're interested in
   * @return whether to add this value to the table
   */
  private static boolean shouldAdd(final Object value, final int classId)
  {
    // If 0, always add it
    if (classId == 0)
    {
      return true;
    }
    else if (value == null)
    {
      // ClassID != 0, so skip null values
      return false;
    }
    
    switch (classId)
    {
      case 1: return (value instanceof String);
      case 2: return (value instanceof java.awt.Font);
      case 3: return (value instanceof java.awt.Color);
      case 4: return (value instanceof javax.swing.border.Border);
      case 5: return (value instanceof java.awt.Insets);
      case 6: return (value instanceof javax.swing.ActionMap);
      case 7: return (value instanceof Integer);
      case 8: return (value instanceof javax.swing.Icon);
      case 9: return (value instanceof java.awt.Dimension);
      case 10: return (value instanceof Boolean);
      case 11: return (value instanceof javax.swing.InputMap);
      default: return false;
    }
  }
  
  
  /**
   * The user entered a filter.  Show only those rows whose
   * key name contains the filter.
   * 
   * @param filter the string to check in each key value
   */
  public void setFilter(final String filter)
  {
    // Clear out the previous list of filtered objects
    filteredList.clear();
    
    // If the filter is null or empty, turn off filtering
    if ((filter == null) || (filter.length() < 1))
    {
      useFilter = false;
    }
    else
    {
      // The filter has a value, so turn on filtering
      useFilter = true;
      
      // Iterate over the rows, checking for a match on the
      // key and the filter string
      final int size = list.size();
      for (int index = 0; index < size; ++index)
      {
        Defaults def = list.get(index);
        if (def.getKey().toLowerCase().contains(filter))
        {
          // A match was found, so add the index to the list
          filteredList.add(Integer.valueOf(index));
        }
      }
    }
    
    // Update the table
    fireTableDataChanged();
  }
}
