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

    public static String getUrlAirPollution () {
        urlAirPollution = "http://api.openweathermap.org/data/2.5/air_pollution?lat=10.8703886667679&lon=106.80297065706905&appid=2cd54a866e6c50acd7951f230be4b369";
        return urlAirPollution;
    }

    public static String getToken() {
//        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDQyMDAzOTQsImlhdCI6MTcwNDExMzk5NCwiYXV0aF90aW1lIjoxNzA0MTEzOTk0LCJqdGkiOiJmMjllZDZkZC05NWNhLTRiOTgtYjExYy1kZmRjYjQ5YzBjMDQiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiMjc0NDgyOTYtZmZmNS00ZDk0LTljNWUtODQ2NDcyNjZiZGRlIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiIyNzQ0ODI5Ni1mZmY1LTRkOTQtOWM1ZS04NDY0NzI2NmJkZGUiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyMSBVaXQxIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlciIsImdpdmVuX25hbWUiOiJVc2VyMSIsImZhbWlseV9uYW1lIjoiVWl0MSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.GTnNjfSLhrozO1X7AXolB_aABhN976ghiz1gAsnXqEN0Ro0ZSxwwReaZy0FAZ_XT4azOPxwbb-mEDw_u4b0EM-2yZ2xB2sVG_mNfqr8K-KqetnJkXgrwQljJZrVYD6bPq-1MFyr7-7wbTVrZ3TKgnRWPefTokuprSz2pdyqIr1SSCnSuKNvRnh15mPVuPp7qJCvhk4R6YcFXU-NwDenV_Q4WSXTdCLdEMpi9phbf9P31q1x1TqZzVJ_TEd6Fj0iSGfH5MJBYYxNk82FRRuDXnguUyxxUXvXQG3kVKu4bklgEkdGyJfSzuUoKHo4Gi9_uW9c9CDBvw0Sx_NqkMAzG3A";
        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDQzNDE1ODgsImlhdCI6MTcwNDI1NTE4OSwiYXV0aF90aW1lIjoxNzA0MjU1MTg4LCJqdGkiOiI1ZDVkZDQ1ZC0xNTA2LTRhMzMtYWJiYy03YTMzYjUzMjdmNDEiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiNmQ3YWQxOGQtMDMwYS00Nzk0LWIzYzEtNzVkMjhhOTBiY2Y4IiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI2ZDdhZDE4ZC0wMzBhLTQ3OTQtYjNjMS03NWQyOGE5MGJjZjgiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyMSBVaXQxIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidXNlciIsImdpdmVuX25hbWUiOiJVc2VyMSIsImZhbWlseV9uYW1lIjoiVWl0MSIsImVtYWlsIjoidXNlckBpeHhjLmRldiJ9.mbwfAuLSyqidfmC2BSsngN-F7yRMJFIVBJrkM--awjQMgqhLhzIgZlG60TcBfAHtVKYvPeMgfCK2V7lbWF5RnUHpk89Fs3qXUEd_6a72cXFXi2nds2hnGd7bR-YiGQYxLLTHaRhBPkn7hVpXvEFG-tQO65XsYtWbBiJqfs-hmQbXxrcUHHKkN-yUGxG5AkOdRO1RKMonWAOP9JgEKq2yZTzXWCZbZlqRabVjWYxePT_xjilAlBxqGmN6rohcKCd4c1doH90Yj-PkAoPUtfCCbqHS344FUs1rWOGmOqN0kPcUQuM_RWqoHRK_PvvO5ocCaPt0T1kzTN5LpRnxZhXfsw";
        return token;
    }
}
