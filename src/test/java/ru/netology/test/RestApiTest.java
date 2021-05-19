package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.RequestApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataHelper.getAuthInfo;
import static ru.netology.data.DataHelper.getVerificationInfoFor;
import static ru.netology.data.RequestApi.getVerificationCode;

public class RestApiTest {
    int balance1;
    int balance2;
    int sum = 5000;

    @Test
    void shouldMakeTransfer() {
        RequestApi.getRequest(getAuthInfo());
        String token = RequestApi.getToken(getVerificationInfoFor(getAuthInfo(), getVerificationCode()));
        balance1 = RequestApi.getFirstBalanceCard(token);
        balance2 = RequestApi.getSecondBalanceCard(token);
        RequestApi.makeTransferFromSecondToFirst(token, sum);
        int endBalance1 = RequestApi.getFirstBalanceCard(token);
        int endBalance2 = RequestApi.getSecondBalanceCard(token);
        assertEquals(balance1 - sum, endBalance1);
        assertEquals(balance2 + sum, endBalance2);
    }
}