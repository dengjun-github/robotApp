package com.dj.test;

import api.card.AllCard;
import api.future.AllCardFutureListener;
import api.future.Future;
import serialize.pojo.SecurityCard;

import java.util.List;

public class Card {


    public static void main(String[] args) {
        Future future = AllCard.get("zhangsan", 1, (byte) 1, 30);
        future.addListener(new AllCardFutureListener() {
            @Override
            public void onSuccess(List<SecurityCard> list) {
                System.out.println(list);
            }

            @Override
            public void onFailure(int i) {

            }
        });
    }

}
