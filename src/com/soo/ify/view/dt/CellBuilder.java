/*
 * Copyright (c) 2013-2014 Soo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.soo.ify.view.dt;

import java.util.Date;
import java.util.List;

import android.content.Context;

/**
 * @author Soo
 */
public class CellBuilder {
    
    private Context context;
    private Date date;
    private List<Cell> cells; 
    
    public CellBuilder(Context context) {
        this.context = context;
    }
    
    public void setCurrentDate(Date date) {
        this.date = date;
    }
    
    public Date getCurrentDate() {
        return date;
    }

    public List<Cell> getCells() {
        return null;
    }
    
//    private void initArray(){
//        if (hasTitle){
//            for (int i = 0;i < 7;i++){
//                content.add(new TitleData(new DateData(0,0,i+1)));
//            }
//        }
//        DayData addDate;
//        for(int i = 0;i < totalDay+7;i++){
//            if(i < startDay) {
//                addDate = new DayData(new DateData(date.getYear(),
//                                                    lastMonth,
//                                                    lastMonthTotalDay - (startDay- i)+1));
//                addDate.setTextColor(Color.GRAY);
//                addDate.setTextSize(0);
//                content.add(addDate);
//                continue;
//            }
//            if((i >= totalDay) && (i % 7 !=0)){
//                // Maybe move them to DateData class.
//                boolean happyNewYear = false;
//                int nextYear, nextMonth;
//                happyNewYear = date.getMonth() == 12;
//                nextMonth = happyNewYear ? 1 : date.getMonth() + 1;
//                nextYear = happyNewYear ? date.getYear() + 1 : date.getYear();
//
//                addDate = new DayData(new DateData(nextYear,
//                                                    nextMonth,
//                                                    i - totalDay + 1));
//                addDate.setTextColor(Color.GRAY);
//                addDate.setTextSize(0);
//                content.add(addDate);
//                continue;
//            }else if((i >= totalDay) && (i % 7 ==0)){
//                return;
//            }
//            addDate = new DayData(new DateData(date.getYear(),
//                                    date.getMonth(),
//                                    i + 1 - startDay));
//            addDate.setTextColor(Color.BLACK);
//            addDate.setTextSize(1);
//            content.add(addDate);
//        }
//    }
}
