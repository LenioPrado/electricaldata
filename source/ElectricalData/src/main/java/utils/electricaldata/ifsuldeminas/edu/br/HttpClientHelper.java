package utils.electricaldata.ifsuldeminas.edu.br;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

public class HttpClientHelper {
	
	public static void main(String[] args) {
		String filePath = "D:\\Develop\\FileRepository\\60\\Como_Aprender_Ingles_O+guia+definitivo_1_6_2.pdf";
		String url = "http://localhost:8080/ElectricalData/rest/file/upload";
		
		HttpClientHelper uploader = new HttpClientHelper();		
		uploader.uploadFile(url, filePath);		
	}
	
	public void uploadFile(String url, String filePath) {
		// the file we want to upload
		File inFile = new File(filePath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(inFile);
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			
			// server back-end URL
			HttpPost httppost = new HttpPost(url);
			// set the file input stream and file name as arguments
			
			builder.addPart("uploadfile", new InputStreamBody(fis, inFile.getName()));

			HttpEntity entity = builder.build();
			httppost.setEntity(entity);
			// execute the request
			HttpResponse response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity responseEntity = response.getEntity();
			String responseString = EntityUtils.toString(responseEntity, "UTF-8");
			
			System.out.println("[" + statusCode + "] " + responseString);
			
		} catch (ClientProtocolException e) {
			System.err.println("Unable to make connection");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Unable to read file");
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) fis.close();
			} catch (IOException e) {}
		}
	}
	
	public void doPostRequest(String url, JSONObject object) {
		try {
			HttpPost request = new HttpPost(url);
			request.addHeader("user-agent", "Android");
			request.addHeader("content-type", "application/json");
			StringEntity params = new StringEntity(object.toString());
			request.setEntity(params);
			
			HttpClient httpClient = new DefaultHttpClient();		    
		    HttpResponse response = httpClient.execute(request);
		    HttpEntity entity = response.getEntity();

		    if (entity != null) {
		        InputStream instream = entity.getContent();
		        instream.close();
		    }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}