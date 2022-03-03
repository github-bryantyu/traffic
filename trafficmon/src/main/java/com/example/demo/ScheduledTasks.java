package com.example.demo;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Component
public class ScheduledTasks {

	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	private static final String uri = "https://resource.data.one.gov.hk/td/speedmap.xml";
	
	@Autowired
	private TSMService tsmService;
	
	@Scheduled(fixedRate = 5 * 60 * 1000)
	public void getTSMRecords() throws Exception {
		log.info("The time is now {}", dateFormat.format(new Date()));
		// Disable 
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		SSLContext sslcontext = SSLContexts.custom() .loadTrustMaterial(null, (chain, authType) -> true) .build(); 
	    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1.2", "TLSv1.3"}, null, new NoopHostnameVerifier()); 
	    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
	    requestFactory.setHttpClient(httpclient);
	    RestTemplate restTemplate = new RestTemplate(requestFactory);
	    
		
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
		Document doc = convertStringToXMLDocument( response.getBody() );
	    XPath xPath = XPathFactory.newInstance().newXPath();

	    try {
		    NodeList nl2 = (NodeList) xPath.compile("/jtis_speedlist/jtis_speedmap").evaluate(doc, XPathConstants.NODESET);
		    ArrayList<TSMRow> tsmRecords = new ArrayList<TSMRow>();
		    
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		    
		    for (int i = 0; i < nl2.getLength(); i++) {
		    	TSMRow tsmRecord = new TSMRow();
		    	Node n = nl2.item(i);
		    	NodeList children = n.getChildNodes();
		    	for (int a = 0; a < children.getLength(); a++) {
		    		Node child = children.item(a);
		    		if (child.getNodeType() == Node.ELEMENT_NODE) {
		    			String nodeName = child.getNodeName();
		    			String nodeValue = child.getFirstChild().getNodeValue();
		    			if (nodeName == "LINK_ID")
		    				tsmRecord.setLinkId(nodeValue);
		    			if (nodeName == "REGION")
		    				tsmRecord.setRegion(nodeValue);
		    			if (nodeName == "ROAD_TYPE")
		    				tsmRecord.setRoadType(nodeValue);
		    			if (nodeName == "ROAD_SATURATION_LEVEL")
		    				tsmRecord.setRoadSaturationLevel(nodeValue);
		    			if (nodeName == "TRAFFIC_SPEED")
		    				tsmRecord.setTrafficSpeed(Integer.parseInt(nodeValue));
		    			if (nodeName == "CAPTURE_DATE") {
		    				Date d = sdf.parse(nodeValue);
		    				tsmRecord.setCaptureDate(new java.sql.Timestamp(d.getTime()));
		    			}
		    		}		
		    	}
		    	tsmRecords.add(tsmRecord);
		    }
		    log.info("Number of TSM record pulled : " + tsmRecords.size());
		    tsmService.saveAll(tsmRecords); 
	    } catch (XPathExpressionException xe) {
	    	xe.printStackTrace();
	    }
	}
	
	private static Document convertStringToXMLDocument(String xmlString)  {
	    //Parser that produces DOM object trees from XML content
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	     
	    //API to obtain DOM Document instance
	    DocumentBuilder builder = null;
	    try {
	      //Create DocumentBuilder with default configuration
	      builder = factory.newDocumentBuilder();
	       
	      //Parse the content to Document object
	      Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
	      return doc;
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return null;
	}

	
}
