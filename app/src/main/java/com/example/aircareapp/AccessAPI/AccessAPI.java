package com.example.aircareapp.AccessAPI;

public class AccessAPI {
    public static String urlMap, urlUserCurrent, urlGoogleWeather, urlAirPollution, urlUserUser;
    public static String token;

    public AccessAPI(String url, String token, String urlUserCurrent, String urlGoogleWeather, String urlAirPollution) {
        this.urlMap = url;
        this.token = token;
        this.urlUserCurrent = urlUserCurrent;
        this.urlGoogleWeather = urlGoogleWeather;
        this.urlAirPollution = urlAirPollution;
    }

    public static String getURLMap() {
        urlMap = "https://uiot.ixxc.dev/api/master/map";
        return urlMap;
    }
    public static String getUrlUserCurrent () {
        urlUserCurrent = "https://uiot.ixxc.dev/api/master/asset/user/current";
//        urlUserID = "https://uiot.ixxc.dev/api/master/asset/7TSscVCYKmONsznsgKRmkH";
        return urlUserCurrent;
    }

    public static String getUrlGoogleWeather () {
//        urlGoogleWeather = "https://api.openweathermap.org/data/2.5/weather?q=Saigon&units=metric&appid=ac212f5768bf3e2f84201adbd2bc7961";
        urlGoogleWeather = "https://api.openweathermap.org/data/2.5/weather?q=Saigon&units=metric&appid=2cd54a866e6c50acd7951f230be4b369";
        return urlGoogleWeather;
    }

    public static String getUrlAirPollution () {
        urlAirPollution = "http://api.openweathermap.org/data/2.5/air_pollution?lat=10.8703886667679&lon=106.80297065706905&appid=ac212f5768bf3e2f84201adbd2bc7961";
        return urlAirPollution;
    }

    public static String getToken() {
        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDE3MTI3NzYsImlhdCI6MTcwMTYzMjM5MSwiYXV0aF90aW1lIjoxNzAxNjI2Mzc2LCJqdGkiOiIzZTlhNjI5Yi0xM2E4LTRjZWItYTNlNC1hMDM3NWQzZDYwNGYiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiYjc1NGI2MzgtYTQzYS00OTNkLTg1NjAtNTAxNmVhNjgyODRmIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIGVtYWlsIiwic2lkIjoiYjc1NGI2MzgtYTQzYS00OTNkLTg1NjAtNTAxNmVhNjgyODRmIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJuYW1lIjoiRmlyc3QgTmFtZSBMYXN0IG5hbWUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IkZpcnN0IE5hbWUiLCJmYW1pbHlfbmFtZSI6Ikxhc3QgbmFtZSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.KL3vd1Ypt9kQkObuI29UqlHSRB0O6Qn4bKfLfOafuKVaZ2gyjNE8m0k0U9-GWVpBfg35KzJmQn8C_9nZy6yCIKFNhnjSpMKuePaE9GsPzr6vZ0JcPir04DHunsmfAGehlLmlXHG2kIA4pmnsUKSZ5pWGgNRdPKUEXV3sQRoQsTwCqQCYt1kz7b7dDovSFS6iRyOa2TaYlMEE0TozrwrEP294z_zjCxjsjdthFCoAGFQ-n7V6WGfE10Zaardq7f5KbwVuUvk9BWAC6uNr3DxkY96C8yZ6ynH9xggIkYuh6A7D3sWtO1nRTwPE39GJaMwD0mkwtZDIARD3eZtHOPo4Ww";
        return token;
    }
}
