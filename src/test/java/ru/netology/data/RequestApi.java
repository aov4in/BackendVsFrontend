package ru.netology.data;

import com.google.gson.Gson;
import com.mysql.cj.xdevapi.Collection;
import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static ru.netology.data.DataHelper.*;

public class RequestApi {
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void getRequest(AuthInfo info) {
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new Gson().toJson(new AuthInfo(info.getLogin(), info.getPassword())))
                .when() // "когда"
                .post("/api/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String getVerificationCode() {
        Object code = null;
        val codeSQL = "SELECT code FROM auth_codes WHERE created = (SELECT max(created) FROM auth_codes);";
        val runner = new QueryRunner();

        try (
                val conn = DriverManager.getConnection(
                        "jdbc:mysql://0.0.0.0:3306/app", "vasya", "qwerty123"
                );
        ) {
            code = runner.query(conn, codeSQL, new ScalarHandler<>());
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        return (String) code;
    }

    public static String getToken() {
        String token = given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(getVerificationInfoFor(getAuthInfo(),getVerificationCode())) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/auth/verification") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200) // код 200 OK
                .extract()
                .path("token");
        return token;
    }


    public static int getFirstBalanceCard(String token) {
        int firstBalance;
        Card[] cards =
                given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .header("Authorization", "Bearer " + token)
                        .when() // "когда"
                        .get("/api/cards") // на какой путь, относительно BaseUri отправляем запрос
                        .then() // "тогда ожидаем"
                        .statusCode(200) // код 200 OK
                        .extract()
                        .as(Card[].class);
        return firstBalance = Integer.parseInt(cards[0].getBalance());
    }

    public static int getSecondBalanceCard(String token) {
        int secondBalance;
        Card[] cards =
                given() // "дано"
                        .spec(requestSpec) // указываем, какую спецификацию используем
                        .header("Authorization", "Bearer " + token)
                        .when() // "когда"
                        .get("/api/cards") // на какой путь, относительно BaseUri отправляем запрос
                        .then() // "тогда ожидаем"
                        .statusCode(200) // код 200 OK
                        .extract()
                        .as(Card[].class);
        return secondBalance = Integer.parseInt(cards[1].getBalance());
    }

    public static void makeTransferFromSecondToFirst(String token, int sum) {
        given()
                .spec(requestSpec)
                .header("Authorization", "Bearer " + token)
                .body(getTransaction("5559 0000 0000 0002", "5559 0000 0000 0001", sum))
                .when() // "когда"
                .post("/api/transfer")
                .then() // "тогда ожидаем"
                .statusCode(200);
    }
}