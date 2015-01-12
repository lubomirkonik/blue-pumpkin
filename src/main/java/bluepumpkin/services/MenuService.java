package bluepumpkin.services;

import java.util.List;

import bluepumpkin.domain.MenuItem;

public interface MenuService {

  List<MenuItem> requestAllMenuItems();
  MenuItem requestMenuItem(String id);
  MenuItem createMenuItem(MenuItem menuItem);
  void updateMenuItem(MenuItem menuItem);
  void deleteMenuItem(String id);

}
