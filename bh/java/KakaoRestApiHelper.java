package com.orangeline.return_confirmation;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


///////////////////////////////////////////////////////////////////
//Kakao REST API 통신에 필요한 기능들을 구현한 프래그먼트
///////////////////////////////////////////////////////////////////
public class KakaoRestApiHelper {

    public enum HttpMethodType { POST, GET, DELETE }	//REST API와 통신방식 변수들 선언 : POST=0, GET=1, DELETE=2 (현재 코드에서는 GET만 사용)

    private static final String API_SERVER_HOST = "https://dapi.kakao.com";	//Kakao 서버 주소

    private static final String LOCAL_SEARCH_ADDRESS_PATH = "/v2/local/search/address.json?";	//Kakao 로컬 REST API 서버 주소 + json 포맷으로 설정

    private static final ObjectMapper JACKSON_OBJECT_MAPPER = new ObjectMapper();

    private static final String PROPERTIES_PARAM_NAME = "properties";

    private static final List<String> adminApiPaths = new ArrayList<String>();

    private String accessToken;
    private String adminKey;

    private double longitude, latitude;	//주소지의 경도와 위도 (MainActivity.java에서는 같은 변수명으로 사용자 경도위도입니다 제성합니다.....나중에...수정할게요

    public void setAccessToken(final String accessToken) {	//호출 시 인자로 accessToken을 받아옴
        this.accessToken = accessToken;
    }

    public void setAdminKey(final String adminKey) {	//호출 시 인자로 adminKey를 받아옴
        this.adminKey = adminKey;
    }


	///////////////////////////////////////////////////////////////////
    //REST API 통신 구현
	///////////////////////////////////////////////////////////////////
    public String[] searchAddress(final Map<String, String> params) {	//서버에 요청을 보내고 응답을 받는 기능을 구현. 인자로 query string 파라미터를 받음
        final String[] address_req = new String[1];	//서버 응답을 할당할 변수
        String curAddress;	//address_req 값을 임시로 할당하여 JsonParser함수에 인자로 넣을 변수

        new Thread() {
            public void run() {	//이 내부에 쓴 코드들은 외부의 코드들과 동시에 돌아가기 때문에 아래에서 while문으로 request의 응답이 돌아올 때까지 루프를 돌게 해줌
                address_req[0] = request(HttpMethodType.GET, LOCAL_SEARCH_ADDRESS_PATH, mapToParams(params));	//line 73 : request함수에 인자로 통신방식, 요청할 URL, 인코딩한 query를 전달	//mapToParams()=line 153
            }
        }.start();

        while (address_req[0] == null) { }	//루프를 돌며 기다리지 않으면 request 함수가 값을 반환하기 전에, 즉 address_req[0]가 null인 채 코드가 진행되어 버림

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>while statements Done");	//루프를 빠져나왔는지 테스트
        try {
            curAddress = address_req[0];
            address_req[0] = null;	//혹시 재검색을 하면 잘 실행되게 해보려고 다시 null로 만들었는데 이거랑은 별개로 두 번부터는 동작하지 않음. 딴짓한거라서 그냥 address_req[0]을 JsonParser에 넘겨도 상관없음.
            return JsonParser(curAddress);  //line 163 : JsonParser는 [0]=도로명주소, [1]=주소지경도, [2]=주소지위도인 String 타입 배열을 반환
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;	//에러가 발생하면 null을 반환
    }

    public String request(HttpMethodType httpMethod, final String apiPath, final String params) {
        String requestURL = API_SERVER_HOST + apiPath;  //URL = "https://dapi ~ ?query="
        if (httpMethod == null) {
            httpMethod = HttpMethodType.GET;
        }
        if (params != null && params.length() > 0
                && (httpMethod == HttpMethodType.GET || httpMethod == HttpMethodType.DELETE)) {
            requestURL += params;   //서버에 요청할 url에 인코딩한 검색어값을 추가한다.
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>params는 " + params);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>최종 URL 주소는" + requestURL);
        }

        ///////////////////////////HTTP통신(88-141)
        HttpsURLConnection conn;
        OutputStreamWriter writer = null;
        BufferedReader reader = null;
        InputStreamReader isr = null;

        try {
            final URL url = new URL(requestURL);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod(httpMethod.toString());
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setRequestProperty("User-Agent", "Java-Client");   // https 호출시 user-agent 필요

            //////////////////////////예시코드 그대로 썼는데 if문이 제대로 동작 안해서 지워버림
            //if (adminApiPaths.contains(apiPath)) {
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>adminKey가 헤더에 할당됩니다.");
                conn.setRequestProperty("Authorization", "KakaoAK " + this.adminKey);
            //} else {
            //    conn.setRequestProperty("Authorization", "Bearer " + this.accessToken);
            //}

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");

            if (params != null && params.length() > 0 && httpMethod == HttpMethodType.POST) {
                conn.setDoOutput(true);
                writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(params);
                writer.flush();
            }

            final int responseCode = conn.getResponseCode();
            System.out.println(String.format("\nSending '%s' request to URL : %s", httpMethod, requestURL));
            System.out.println("Response Code : " + responseCode);
            if (responseCode == 200)    //정상적인 응답 수신
                isr = new InputStreamReader(conn.getInputStream());
            else
                isr = new InputStreamReader(conn.getErrorStream());

            reader = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            System.out.println(buffer.toString());

            return buffer.toString();   //정상적인 통신이 이루어졌다면 서버의 응답을 return -> searchAddress의 address_req[0]에 할당됨 (line 62)
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try { writer.close(); } catch (Exception ignore) {}
            if (reader != null) try { reader.close(); } catch (Exception ignore) {}
            if (isr != null) try { isr.close();} catch (Exception ignore) {}
        }

        return null;    //오류나 예외가 발생했다면 null값을 return
    }

    public String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public String mapToParams(Map<String, String> map) {
        StringBuilder paramBuilder = new StringBuilder();
        for (String key : map.keySet()) {
            paramBuilder.append(paramBuilder.length() > 0 ? "&" : "");
            paramBuilder.append(String.format("%s=%s", urlEncodeUTF8(key),
                    urlEncodeUTF8(map.get(key).toString())));
        }
        return paramBuilder.toString();
    }

    public String[] JsonParser(String resultString) throws IOException {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>json파싱할 문자열은 " + resultString);    //input은 잘 넘어옴(0921)

        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = resultString;
        String[] addressInfo = new String[3];

        ///////////////////////////시도1) 이렇게 하면 오류는 안나는데 addressJson을 documents와 meta 둘로밖에 못 나눈다
        //Map<String, Object> jsonMap = objectMapper.readValue(addressJson, new TypeReference<Map<String, Object>>(){});
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>파싱한 object는 " + jsonMap.get("documents"));

        ///////////////////////////시도2) 직접 만든 클래스들로 파싱하면 오류남(클래스 코드는 txt에 백업해놓음)
        //Address address = objectMapper.readValue(addressJson, Address.class);
        //List<Document> documents = address.getDocuments();
        //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>파싱한 object는 " + address.getDocuments());

        ///////////////////////////시도3) JsonNode 이용한 파싱(0925)
        JsonNode jsonNode = objectMapper.readValue(addressJson, JsonNode.class);

        JsonNode array = jsonNode.get("documents");
        JsonNode docNode = array.get(0);
        JsonNode nameNode = docNode.get("address_name");
        addressInfo[0] = nameNode.asText();    //address_name: 사용자의 입력과 일치하는 주소지의 공식적인 도로명주소
        JsonNode xNode = docNode.get("x");
        addressInfo[1] = xNode.asText();                  //x: 사용자의 입력과 일치하는 주소지의 x좌표, 경도(longitude)
        JsonNode yNode = docNode.get("y");
        addressInfo[2] = yNode.asText();                  //y: 사용자의 입력과 일치하는 주소지의 y좌표, 위도(latitude)

        System.out.println("당신의 귀가지는 " + addressInfo[0] + "입니다.");
        System.out.println("귀가지의 위치정보는 다음과 같습니다.\nx(longitude): " + addressInfo[1] + ",\ny(latitude): " + addressInfo[2]);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>파싱이 끝났습니다. jsonParser 함수를 종료합니다.");

        return addressInfo;
    }
}
