package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.RequestApi;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestApiTest {
    int balance1;
    int balance2;
    int sum = 5000;

    @Test
    void shouldMakeTransfer() {
        RequestApi.getRequest();
        String token = RequestApi.getToken();
        balance1 = RequestApi.getFirstBalanceCard(token);
        balance2 = RequestApi.getSecondBalanceCard(token);
        RequestApi.makeTransferFromSecondToFirst(token, sum);
        int endBalance1 = RequestApi.getFirstBalanceCard(token);
        int endBalance2 = RequestApi.getSecondBalanceCard(token);
        assertEquals(balance1 - sum, endBalance1);
        assertEquals(balance2 + sum, endBalance2);
    }


}
