package com.despani.core.beans;

import com.despani.core.interfaces.ILinkable;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Pagination {

    public Pagination(ILinkable page, int contentCount) {
        this.page = page;
        this.contentCount = contentCount;
//         this.MAX_ROWS_PER_PAGE =contentCount;
    }

    List<Integer> pageRanges = Arrays.asList(new Integer[] { 10, 25, 30, 50 });

    ILinkable page;

    final int MINIMUM_ROWS_PER_PAGE = 1;
    final int FIRST_PAGE = 1;
    final int DEFAULT_ROWS_PER_PAGE = 10;
    int MAX_ROWS_PER_PAGE =50;


    private  int requestedPageNumber;
    private  int limite;
    private  int pageNumber;
    private  int offsetMultiplier;
    private boolean lastPage;
    private  int contentCount;


    public void calculate (int _requestedPageNumber, int _limite) {
        assert _limite>0;
        requestedPageNumber = _requestedPageNumber;
        limite=  _limite;

        int pageNumberCount;
        int numberofRows = this.contentCount;
        int quatientPages = numberofRows/limite;
        pageNumberCount = (numberofRows%limite>0)?quatientPages+1:quatientPages;
        setPageNumber(pageNumberCount);
        if(limite>MAX_ROWS_PER_PAGE || limite>contentCount || limite<MINIMUM_ROWS_PER_PAGE || requestedPageNumber>pageNumberCount || requestedPageNumber<=0){
            limite= (contentCount>MAX_ROWS_PER_PAGE?MAX_ROWS_PER_PAGE:contentCount);
            return ;//model.getOffsetMultiplier();
        }
        if(requestedPageNumber == 1){
             setOffsetMultiplier(0);
        }else{
            setOffsetMultiplier((limite*requestedPageNumber-limite));
        }
         setLastPage((requestedPageNumber == pageNumberCount));
        //  return model.getOffsetMultiplier();
    }

}
