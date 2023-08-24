package com.vel.common.connector.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;




public interface IBUSAPIConnectorService {
	
	public ResponseEntity<String> CallGetRequest(HttpHeaders l_headers, String l_input,String l_URL) throws Exception;
	public ResponseEntity<String> CallPutRequest(HttpHeaders l_headers,String l_input,String l_URL)  throws Exception;
	public ResponseEntity<String> CallPostRequest(HttpHeaders l_headers,String l_input,String l_URL)  throws Exception;
	public ResponseEntity<String> CallDeleteRequest(HttpHeaders l_headers, String l_URL)  throws Exception;
}
