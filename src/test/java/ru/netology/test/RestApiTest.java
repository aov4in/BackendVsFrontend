package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.RequestApi;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RestApiTest {
    int Balance1;
    int Balance2;
    int sum = 5000;

    @Test
    void shouldMakeTransfer() throws SQLException {
        RequestApi.getRequest();
        String token = RequestApi.getToken();
        Balance1 = RequestApi.getFirstBalanceCard(token);
        Balance2 = RequestApi.getSecondBalanceCard(token);
        RequestApi.makeTransferFromSecondToFirst(token, sum);
        int endBalance1 = RequestApi.getFirstBalanceCard(token);
        int endBalance2 = RequestApi.getSecondBalanceCard(token);
        assertEquals(Balance1 - sum, endBalance1);
        assertEquals(Balance2 + sum, endBalance2);
    }


}
