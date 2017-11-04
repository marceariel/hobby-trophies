package com.marbit.hobbytrophies.dao;

import com.marbit.hobbytrophies.model.market.Filter;
import com.marbit.hobbytrophies.model.market.Item;

public interface ItemDAOInterface {
    void loadItems();
    void loadItemById(String itemId, ItemDAO.SingleItemDAOListener singleItemDAOListener);
    void loadUserItems(String UserId);
    void loadItemsByFilter(Filter filter);
    void markSold(Item item);
    void unmarkAsSold(Item item);

    void addWishList(Item item);
    void removeWishList();
}
