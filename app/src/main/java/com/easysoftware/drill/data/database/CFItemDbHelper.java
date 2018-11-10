package com.easysoftware.drill.data.database;

import com.easysoftware.drill.data.model.CFItem;

import java.util.List;

import io.reactivex.Observable;

public interface CFItemDbHelper {
    CFItem getCFItem(String content);

    Observable<CFItem> getCFItemObservable(String content);

    List<CFItem> getCFItemsStartwith(String start);

    Observable<List<CFItem>> getCFItemsStartwithObservable(String start);

    List<CFItem> getCFItemsEndwith(String end);

    Observable<List<CFItem>> getCFItemsEndwithObservable(String end);

    List<CFItem> getCFItemsContainKeyword(String keyword);

    Observable<List<CFItem>> getCFItemsContainKeywordObservable(String keyword);

    List<CFItem> getCFItemsContainKeywords(List<String> keywords);

    Observable<List<CFItem>> getCFItemsContainKeywordsObservable(List<String> keywords);
}
