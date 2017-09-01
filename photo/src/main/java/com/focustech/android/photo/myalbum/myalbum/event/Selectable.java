package com.focustech.android.photo.myalbum.myalbum.event;

import com.focustech.android.photo.myalbum.myalbum.bean.mediastore.MediaEntity;

public interface Selectable {


  /**
   * Indicates if the item at position position is selected
   *
   * @param mediaEntity Photo of the item to check
   * @return true if the item is selected, false otherwise
   */
  boolean isSelected(MediaEntity mediaEntity);

  /**
   * Toggle the selection status of the item at a given position
   *
   * @param mediaEntity Photo of the item to toggle the selection status for
   */
  void toggleSelection(MediaEntity mediaEntity);

  /**
   * Clear the selection status for all items
   */
  void clearSelection();

  /**
   * Count the selected items
   *
   * @return Selected items count
   */
  int getSelectedItemCount();

}