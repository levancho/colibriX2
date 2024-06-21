package com.despani.x2.core.utils;

import org.springframework.stereotype.Component;

@Component
public class PagingUtil {

    public int calcNumberOfPages(int rowsInDb,int limit){
        //results in quatient Ex:10/3 = 3
        int quatientPages = rowsInDb/limit;
        //results in reminder Ex: 10%3=1
        int showNumberOfPages = (rowsInDb%limit>0)?quatientPages+1:quatientPages;

        return showNumberOfPages;

    }



    public boolean isNumber(String  pageNumber){
        try{
           int num = Integer.parseInt(pageNumber);
        }catch(NumberFormatException|NullPointerException ex){
            return false;
        }

        return true;
    }


    /*--------------------------------*/
    final int MAX_ROWS_PER_PAGE = 3;
    final   int MINIMUM_ROWS_PER_PAGE = 1;
    final  int FIRST_PAGE = 1;
    final int DEFAULT_ROWS_PER_PAGE = 3;
    private int parsedRequestedPageNumber;
    private int parsedRequestedLimitPerPage;
    private int pageNumberCount;
    int offsetMultiplier=0;

    public int getOffsetMultiplier(){
        return  this.offsetMultiplier;
    }
    public int getParsedRequestedPageNumber(){
        return this.parsedRequestedPageNumber;
    }

    public int getParsedRequestedLimitPerPage(){
        return this.parsedRequestedLimitPerPage;
    }

    public int getPageNumberCount(){
        return this.pageNumberCount;
    }



    public boolean isInputValid(String pageNumber, String limitp, int numberOfUsers){
        boolean isInputValid=false;
        if(this.isNumber(pageNumber) && this.isNumber(limitp)){
            parsedRequestedPageNumber = Integer.parseInt(pageNumber);
            parsedRequestedLimitPerPage = Integer.parseInt(limitp);
//            highlight= parsedRequestedPageNumber;

//            int numberOfPages = pageNumberCount;

//            String reqRequestUri = request.getRequestURI();

            pageNumberCount = this.calcNumberOfPages(numberOfUsers,parsedRequestedLimitPerPage);



            if(parsedRequestedLimitPerPage>MAX_ROWS_PER_PAGE || parsedRequestedLimitPerPage<MINIMUM_ROWS_PER_PAGE || parsedRequestedPageNumber>pageNumberCount || parsedRequestedPageNumber<=0){

                //return "redirect:"+reqRequestUri+"?page="+FIRST_PAGE+"&limit="+DEFAULT_ROWS_PER_PAGE;
                isInputValid = false;
            }
            isInputValid = true;
            this.setOffsetMultiplierBasedOnPageNumber(parsedRequestedPageNumber);
        }else{

            //return "redirect:/listOfUsers?page="+FIRST_PAGE+"&limit="+DEFAULT_ROWS_PER_PAGE;
            isInputValid = false;
        }

        return isInputValid;
    }

    // Validate inputs
    //validate requested page number


//        int offsetMultiplier = parsedRequestedPageNumber;

    //int numberOfUsers = userService.getNumOfAllUsers();


    public void  setOffsetMultiplierBasedOnPageNumber(int  parsedRequestedPageNumber){
        if(parsedRequestedPageNumber == 1){
           offsetMultiplier=1;

        }else{
           offsetMultiplier = parsedRequestedLimitPerPage*parsedRequestedPageNumber-parsedRequestedLimitPerPage;
        }
    }


    //List<er> users =  userService.getAllUsers(parsedRequestedLimitPerPage,offsetMultiplier);




    public boolean isLastPage(){
        if(this.getParsedRequestedPageNumber() == this.getPageNumberCount()){
            return true;
        }else{
            return false;
        }

    }
    //for disabling next button
//        if(parsedRequestedPageNumber == pageNumberCount){
//        model.addAttribute("lastPage",true);
//    }else{
//        model.addAttribute("lastPage",false);
//    }
}
