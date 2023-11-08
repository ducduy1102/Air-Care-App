package com.example.aircareapp.AccessAPI;

public class AccessAPI {
    public static String urlMap, urlUserCurrent, urlGoogleWeather, urlAirPollution;
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
//        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJLcHRXNWJCcTlsRGliY2s5NHI3TldHQVl0SHBrUFI3N1A4V0hMWDVIX1E0In0.eyJleHAiOjE2NzI3NTI2MDcsImlhdCI6MTY3MjcyMzM3MSwiYXV0aF90aW1lIjoxNjcyNjY2MjA3LCJqdGkiOiI5ZmRjZTdmZi1hY2YwLTRmNmItOWI3MC0yYjE1NWVlNzJkN2MiLCJpc3MiOiJodHRwczovLzEwMy4xMjYuMTYxLjE5OS9hdXRoL3JlYWxtcy9tYXN0ZXIiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiMTAxZGQ1MmMtMjNiYS00ZjM4LWExMjQtYjc4MGUxYjVhODFiIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoib3BlbnJlbW90ZSIsInNlc3Npb25fc3RhdGUiOiJjYjk3OTM5ZS1kM2ZkLTRhY2YtOTFkMi0xYTlkNjM2ODcwN2IiLCJhY3IiOiIwIiwiYWxsb3dlZC1vcmlnaW5zIjpbImh0dHBzOi8vbG9jYWxob3N0IiwiaHR0cHM6Ly8xMDMuMTI2LjE2MS4xOTkiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDp1c2VycyIsInJlYWQ6bG9ncyIsInJlYWQ6bWFwIiwicmVhZDpydWxlcyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6ImVtYWlsIHByb2ZpbGUiLCJzaWQiOiJjYjk3OTM5ZS1kM2ZkLTRhY2YtOTFkMi0xYTlkNjM2ODcwN2IiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIxIn0.aVJ1h3helkX1UhdHGyJLoHQWx2cd1m4hp4NJTbjaqTSVS2eKEoFHJqSIXLXsz7yXOeZuHMvWeiBvTK8peEAGI1ral98mgtqo-Fac0T_NpzHVeR0A4J1J9pQr3TXxVlc_KmxkbPuILewO2Z6J6yPYT-GDDtb4DAp2nrJMwRL17wyYsPIOSqYrqXonMC0WoR4c5FWeTACGFwTibhDPkd4eb3YITZtxXl3sSiIU6xCylVWTqsh7AaZh5TRdw5nts1ywriXPkdLj63sELI4uDOwHojyPW-VPAYcU2ng_VprCY5PH0jRTTe6yWxT23cvyxwfVjQdf04bM1dOzftLbvtxSjw";
        token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE2OTk0MzM4MDIsImlhdCI6MTY5OTQzMzc0MiwiYXV0aF90aW1lIjoxNjk5NDMzNDc0LCJqdGkiOiI5NGE4MWVmMC1hOWJjLTQ5YTItODdjZi1jNzBiOGM3NDY3ZWIiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiNTUzMWE3OWMtNGQ0Yy00YmMyLTgxMjYtYTkyMmFiNmE2ZDAzIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI1NTMxYTc5Yy00ZDRjLTRiYzItODEyNi1hOTIyYWI2YTZkMDMiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJVc2VyIFRlc3QiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJ1c2VyIiwiZ2l2ZW5fbmFtZSI6IlVzZXIiLCJmYW1pbHlfbmFtZSI6IlRlc3QiLCJlbWFpbCI6InVzZXJAaXh4Yy5kZXYifQ.SxJyOsxg4Na77b7W3ts_tI-rIewKQbjK8BYX6BA1d-Hi6PF7X4LSv3_Du4u0Jn3sBgRsCjLyP44vSYLunIwLp90ZQh4HFXA39brbC0oCi5zfl2FEogh42tUQtLSg_wfqXutyy89XSYb6zQhxSVfD3hPZXIYwA2OmqejKGi2oytpCX10JRIH7WkX7JXyRm0KuOWx__hp29T1YTrKktQnQNokqxsApuoA_yE82nt0EyPEG-Mw9bUKtkt84RRJsCFlHVHtpGToJnwkr2CjV20ORPwYFNZZvw71EH27bzuni7CSKAkE2v7mewtz1hKNJdBF7h83S6hNrvQVCQhyLoAGgkQ";
        return token;
    }
}
