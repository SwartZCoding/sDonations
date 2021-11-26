package fr.swartz.sdonations.utils.menu.menus;

import lombok.Getter;

public enum Size {
  UNE_LIGNE(9), 
  DEUX_LIGNE(18), 
  TROIS_LIGNE(27), 
  QUATRE_LIGNE(36), 
  CINQ_LIGNE(45), 
  SIX_LIGNE(54);
  
  private @Getter int size;
  
  private Size(int size) {
    this.size = size;
  }
  
  public static Size fit(int slots) {
    if (slots < 10)
      return UNE_LIGNE;
    if (slots < 19)
      return DEUX_LIGNE;
    if (slots < 28)
      return TROIS_LIGNE;
    if (slots < 37)
      return QUATRE_LIGNE;
    if (slots < 46) {
      return CINQ_LIGNE;
    }
    return SIX_LIGNE;
  }
}
