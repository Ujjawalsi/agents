package com.example.agents.vel.common.connector.service.impl;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
//import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.agents.vel.common.connector.service.IBUSAPIConnectorService;
/**
 * 
 * @author Ranvijay
 *
 */

@Service(value="busService")
public class BUSAPIConnectorImpl implements IBUSAPIConnectorService{
	private final Logger LOG = LoggerFactory.getLogger(BUSAPIConnectorImpl.class);
	private static TrustManager[] trustAllCerts = null;

	static {
		// Create a trust manager that does not validate certificate chains
		trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
				// TODO Auto-generated method stub

			}
		} };
	}

	private void trustCerts() throws NoSuchAlgorithmException,KeyManagementException {
		// Install the all-trusting trust manager
		final SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			
			@Override
			public boolean verify(String hostname, SSLSession session) {
				// TODO Auto-generated method stub
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
	
	public BUSAPIConnectorImpl() {
		try {
			trustCerts();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	@Override
	public ResponseEntity<String> CallGetRequest(HttpHeaders l_headers, String l_input,String l_URL)  throws Exception{
		final String uri = l_URL+l_input;
		ResponseEntity<String> response = null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			HttpEntity<String> entity = new HttpEntity<String>(l_headers);
			response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
		} catch (Exception e) {
			JSONObject _j = new JSONObject();
			_j.put("code", "99");
			_j.put("description", e.getMessage());
			return new ResponseEntity<String>(_j.toString(),HttpStatus.SERVICE_UNAVAILABLE);
		}
		return response;
	}




	@Override
	public ResponseEntity<String> CallPutRequest(HttpHeaders l_headers, String l_input, String l_URL)  throws Exception {
		final String uri = l_URL;
		//"http://devapi.gstsystem.co.in/taxpayerapi/v0.3/returns/gstr1";
		ResponseEntity<String> response = null;
		try{
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			HttpEntity<String> entity = new HttpEntity<String>(l_input, l_headers);
			response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
		}catch(Exception ex){
			JSONObject _j = new JSONObject();
			_j.put("code", "99");
			_j.put("description", ex.getMessage());
			return new ResponseEntity<String>(_j.toString(),HttpStatus.SERVICE_UNAVAILABLE);
		}
		return response;
	}

	@Override
	public ResponseEntity<String> CallPostRequest(HttpHeaders l_headers, String l_input, String l_URL)  throws Exception{
		final String uri = l_URL;
		ResponseEntity<String> response =null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			HttpEntity<String> entity = new HttpEntity<String>(l_input, l_headers);
			response=restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		} catch (Exception ex) {
			JSONObject _j = new JSONObject();
			_j.put("code", "99");
			_j.put("description", ex.getMessage());
			return new ResponseEntity<String>(_j.toString(),HttpStatus.SERVICE_UNAVAILABLE);
		}

		return response;
	}

	@Override
	public ResponseEntity<String> CallDeleteRequest(HttpHeaders l_headers, String l_URL) throws Exception {
		ResponseEntity<String> response =null;
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			HttpEntity<String> entity = new HttpEntity<String>(l_headers);
			response=restTemplate.exchange(l_URL, HttpMethod.DELETE, entity, String.class);
		} catch (Exception ex) {
			JSONObject _j = new JSONObject();
			_j.put("code", "99");
			_j.put("description", ex.getMessage());
			return new ResponseEntity<String>(_j.toString(),HttpStatus.SERVICE_UNAVAILABLE);
		}

		return response;
	}

	// Use this for SSL Service
	/*@Override
	public ResponseEntity<String> CallPostRequest(HttpHeaders l_headers, String l_input, String l_gstnURL)  throws Exception{
		final String uri = tokenService.getStringValFromProperty("ENTRY_POINT_URL")+""+l_gstnURL;
		//"http://devapi.gstsystem.co.in/taxpayerapi/v0.2/authenticate";
		ResponseEntity<String> _response =null;
		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
			        .loadTrustMaterial(null, acceptingTrustStrategy)
			        .build();

			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

			CloseableHttpClient httpClient = HttpClients.custom()
			        .setSSLSocketFactory(csf)
			        .build();

			HttpComponentsClientHttpRequestFactory requestFactory =
			        new HttpComponentsClientHttpRequestFactory();

			requestFactory.setHttpClient(httpClient);

			RestTemplate restTemplate = new RestTemplate(requestFactory);
//		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<String> entity = new HttpEntity<String>(l_input, l_headers);
		_response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		System.out.println(_response);
		}
		catch (Exception ex) {
			//throw new HttpServerErrorException(HttpStatus.SERVICE_UNAVAILABLE);
			JSONObject _j = new JSONObject();
			_j.put("error_cd", "99");
			_j.put("description", ex.getMessage());
			return new ResponseEntity<String>(_j.toString(),HttpStatus.SERVICE_UNAVAILABLE);
		}
		return _response;
}
	 */

}
