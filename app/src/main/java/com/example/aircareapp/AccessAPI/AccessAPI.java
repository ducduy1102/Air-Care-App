package com.example.aircareapp.AccessAPI;

public class AccessAPI {
    public static String urlMap, urlGoogleWeather, urlAirPollution, urlAsset1, urlAsset2, urlAsset3, urlAssetUser, urlAssetDefault;
    public static String token;

    public AccessAPI(String url, String token, String urlAsset1, String urlAsset2, String urlAsset3, String urlAssetDefault, String urlAssetUser, String urlGoogleWeather, String urlAirPollution) {
        this.urlMap = url;
        this.token = token;
        this.urlAsset1 = urlAsset1;
        this.urlAsset2 = urlAsset2;
        this.urlAsset3 = urlAsset3;
        this.urlAssetDefault = urlAssetDefault;
        this.urlAssetUser = urlAssetUser;
        this.urlGoogleWeather = urlGoogleWeather;
        this.urlAirPollution = urlAirPollution;
    }

    public static String getURLMap() {
        urlMap = "https://uiot.ixxc.dev/api/master/map";
        return urlMap;
    }

    public static String getUrlGoogleWeather () {
        urlGoogleWeather = "https://api.openweathermap.org/data/2.5/weather?q=Saigon&units=metric&appid=2cd54a866e6c50acd7951f230be4b369";
        return urlGoogleWeather;
    }

    public static String getUrlAirPollution () {
        urlAirPollution = "http://api.openweathermap.org/data/2.5/air_pollution?lat=10.8703886667679&lon=106.80297065706905&appid=2cd54a866e6c50acd7951f230be4b369";
        return urlAirPollution;
    }

    public static String getUrlAsset1(){
        urlAsset1 = "https://uiot.ixxc.dev/api/master/asset/4EqQeQ0L4YNWNNTzvTOqjy";
        return urlAsset1;
    }

    public static String getUrlAsset2(){
        urlAsset2 = "https://uiot.ixxc.dev/api/master/asset/6iWtSbgqMQsVq8RPkJJ9vo";
        return urlAsset2;
    }

    public static String getUrlAsset3(){
        urlAsset2 = "https://uiot.ixxc.dev/api/master/asset/4lt7fyHy3SZMgUsECxiOgQ";
        return urlAsset2;
    }

    public static String getUrlAssetDefault(){
        urlAssetDefault = "https://uiot.ixxc.dev/api/master/asset/5zI6XqkQVSfdgOrZ1MyWEf";
        return urlAssetDefault;
    }

    public static String getUrlAssetUser(){
        urlAssetUser = "https://uiot.ixxc.dev/api/master/asset/user/link";
        return urlAssetUser;
    }


    public static String getToken() {
        token ="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDIzNTY0OTMsImlhdCI6MTcwMjI3MDA5MywiYXV0aF90aW1lIjoxNzAyMjcwMDkzLCJqdGkiOiJiMmUxYmY1My0yNmU2LTRjNjktOGI0NS05Y2M4NzY0MzBmYTAiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiZjhiNzRiZTEtNmFmMC00NzVkLWE3ODktMjFkYjI1Mzk1Mjk1IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJmOGI3NGJlMS02YWYwLTQ3NWQtYTc4OS0yMWRiMjUzOTUyOTUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJEbyBLaWVuIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlciIsImdpdmVuX25hbWUiOiJEbyIsImZhbWlseV9uYW1lIjoiS2llbiIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.BbJ3WaQegT7ioG7l7fKa8Ztx8bqy8OS1Z0Hz-sS_x5If9JhO8WbCIOA9_pKE6RDX551r4ZmMoTF_AKZkxsTvBGqWzQ1hg_qbbv2rNt_JONJ35aOwLo-VgAQvxGyLSB-vT2UPES9bqve6WyfszmOgVN0AQ4yw_HuGFeX312oT8K7DEWPc4EemOQl6c3uBbNqkh_EVSNxZRB1m_9CW6bK7enjrIuPMwdXn1hQq7ya1Zppz75QAm20kek35TuWos5ZYcHTBj3tx1PhU2Gypkctbrb_SO2xBMBdPVJdJEtVWUqJbN3kCAPt5l1gHhSC68vD_YkfN1IYMAQ3uAoe7IxN3DA";
        return token;
    }
}
