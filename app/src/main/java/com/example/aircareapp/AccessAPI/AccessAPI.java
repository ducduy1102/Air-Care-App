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
        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDI0NTQ0MzcsImlhdCI6MTcwMjM2ODAzOCwiYXV0aF90aW1lIjoxNzAyMzY4MDM3LCJqdGkiOiI4MTE2MTRhYS1mZDBiLTQ1NGUtYWM4Yi0yYTU4ZWE1MjhmY2UiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiMDA4OTgyMzgtMzg0OC00ZjQ1LWIyNTktM2JlNzhmNmY0YjI5IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIwMDg5ODIzOC0zODQ4LTRmNDUtYjI1OS0zYmU3OGY2ZjRiMjkiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyIFVpdCIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIiLCJnaXZlbl9uYW1lIjoiVXNlciIsImZhbWlseV9uYW1lIjoiVWl0IiwiZW1haWwiOiJ1c2VyQGl4eGMuZGV2In0.b3vD02jEJXLNYEc5-7LYmXyvcQVXIciB1VHts91s8emuLeKto3JtyQwheJrPXhZP0r3TKcwG0Pe7mfUMJiRrK_dobQax27MLtPAv7Y0pP9OTHZxU0Veee7fodDh65rbh8csACnjRqolK7D5GZvO3mpy0dSrlyXQA_zShOHqYukqPDbvg3hAfTycPrMkh6xHX_LFtmcpCruG6fbvhUcBxsR2MHxy2FM9mkrdV4wpouyeputCqo18POE7yBecq3eWcaTb-S4v2rS-lt2yui9xRFr3XV8yPoJYuNnDFenqlzGIWW57AU6T5orZo1TJzh07QTnFAhfM_zOtJdJZ232f6ZQ";
        return token;
    }
}
