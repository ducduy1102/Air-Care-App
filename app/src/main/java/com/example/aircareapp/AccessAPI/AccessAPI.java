package com.example.aircareapp.AccessAPI;

public class AccessAPI {
    public static String urlMap, urlAirPollution, urlAsset1, urlAsset2, urlAsset3, urlAssetUser, urlAssetDefault;
    public static String token;

    public AccessAPI(String url, String token, String urlAsset1, String urlAsset2, String urlAsset3, String urlAssetDefault, String urlAssetUser, String urlAirPollution) {
        this.urlMap = url;
        this.token = token;
        this.urlAsset1 = urlAsset1;
        this.urlAsset2 = urlAsset2;
        this.urlAsset3 = urlAsset3;
        this.urlAssetDefault = urlAssetDefault;
        this.urlAssetUser = urlAssetUser;
        this.urlAirPollution = urlAirPollution;
    }

    public static String getURLMap() {
        urlMap = "https://uiot.ixxc.dev/api/master/map";
        return urlMap;
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
        urlAsset3 = "https://uiot.ixxc.dev/api/master/asset/4lt7fyHy3SZMgUsECxiOgQ";
        return urlAsset3;
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
//        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDMwNDI3NTYsImlhdCI6MTcwMjk1NjM1NywiYXV0aF90aW1lIjoxNzAyOTU2MzU2LCJqdGkiOiIxZjI1ZmI2YS0yZDY0LTQ1NmQtYWQwZi1mYTQxYzcxNmJlZWIiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiOTZhZmQ0ZGMtMTA3MS00MDhkLWI5ZjQtOWJiYWYzMjZiZWNiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI5NmFmZDRkYy0xMDcxLTQwOGQtYjlmNC05YmJhZjMyNmJlY2IiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyMSBVaXQxIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlciIsImdpdmVuX25hbWUiOiJVc2VyMSIsImZhbWlseV9uYW1lIjoiVWl0MSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.HFdLVURbBluc1j3-IJErnXUeVxV4KZV21xJRmdvB89kzBHasK8_RKLPvW7bxU7CwA9miZC1NeFPe85OrURsEeMFdvIOwxhbQTnzMaCZ7RGg35Xut5TGXyiyYdqfAPSCobYIsXV8K1OOrPBitdJa8mCD-1bZQUpKbUzTFJ-x1hl_KDjkTaZKBqC-RXZrKI0MWqqKshJZQkq-W1NTtA4ySJJfSaOJlqWqtTGJ2tVx42Q3OPMOkctuc8nlxLGClY1ZE7ICiDJa2v5oc_qFntCta-2TGsaWXdIWJdpOzwMrogn28r-k0RVHfgfni3K6HEIQ90fP_U7gjqCILD0HsNfXnrg";
//        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDMxNTE4MjksImlhdCI6MTcwMzA2NTQyOSwiYXV0aF90aW1lIjoxNzAzMDY1NDI5LCJqdGkiOiJlZmFhYmRmNy04ZjE2LTRkMjAtYTU2Mi0zNTA5NzcyY2RkMGIiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiOGViM2E0MmQtMDA1My00Y2U0LTgwZWYtNTYxMjFlMjc3YjE2IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI4ZWIzYTQyZC0wMDUzLTRjZTQtODBlZi01NjEyMWUyNzdiMTYiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyMSBVaXQxIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlciIsImdpdmVuX25hbWUiOiJVc2VyMSIsImZhbWlseV9uYW1lIjoiVWl0MSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.KBgLNypFF2Ev7vU9MalUvK0dNRlOaGdt49Z4WYozLhFLhzyjUVFPN4oaAiL7fpTRcTp5D4g12eIas-yOxx0lTIWqCNKnIrW3-5Gv62JTVJe9azUlEXpd7ojuByy4_Vs_LTuAZ739VBpI8Bnfk4qc0CnIyIUa3EdCiZ_oqnWGGVYs8LTqHoq0zxd1LUQ5D8loLXAq1JORB_iEFYHu0W5Hl0Th9i9AzjExttXDKsLb-F5v483Etn5vnvDwkD3FqRgyqCQ143Yyr8tBqbtqdMhlNFuhyGY18DE2WRXdKTrT-hAwHFbz6DsM3gh5-1XnBlblNzejYtVeEIW02JuUZp3H1A";
        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDMyNTI3ODIsImlhdCI6MTcwMzE2NjM4MiwiYXV0aF90aW1lIjoxNzAzMTY2MzgyLCJqdGkiOiJhNDgzNjI1MC05MTI0LTRiY2UtYWQ0ZC02MjE2MTBiOWVhNWYiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiYWRjMDg4ZjItZWM1MC00MTIyLWJmNWYtZmJmMTZlNmQ5MzdkIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiJhZGMwODhmMi1lYzUwLTQxMjItYmY1Zi1mYmYxNmU2ZDkzN2QiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyMSBVaXQxIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlciIsImdpdmVuX25hbWUiOiJVc2VyMSIsImZhbWlseV9uYW1lIjoiVWl0MSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.Zdikn9ceId1eR18fykvKixwhitYU5Lb6OJc4fLJHkgguvTiG2tXaDw17kF1F795woksPOhuGKTTsDxyngCD_-xdXNLLtowGW-jjnozDKXXl2JO4DarplDajKVDM1qZYcp94RYSxCyCotXepjpY0s8hjX3UarbwpFj_fUVBuOY8p8spkBxN8fQ44EfrQINatXD1aOXZfkEtcbUP-Wsv44A-jiJzfA6i48Z6BUtFq4eed5eq1wS3i5ExOqDIno7mP3qIjIxdWcuoultU5vx8FrrNFXwOAxStX62ZUphAklPBYUmqDLFKFhEon-sS_J6gRFcQAdm0jO119p3CeSYWMppg";
        return token;
    }
}
