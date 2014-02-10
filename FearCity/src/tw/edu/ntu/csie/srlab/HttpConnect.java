package tw.edu.ntu.csie.srlab;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.view.View;


public class HttpConnect {
	/*宣告網址字串*/
    private String uriAPI = "";
    private String strResult,requestStr;
    public HttpConnect(String uriAPI) {
	     
	      
        
	       this.uriAPI = uriAPI;
	        /*建立HTTP Post連線*/
	        HttpPost httpRequest = new HttpPost(uriAPI); 
	        /*
	         * Post運作傳送變數必須用NameValuePair[]陣列儲存
	        */
	        List <NameValuePair> params = new ArrayList <NameValuePair>(); 
	        params.add(new BasicNameValuePair("str", requestStr)); 
	        try 
	        { 
	          /*發出HTTP request*/
	          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
	          /*取得HTTP response*/
	          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
	          /*若狀態碼為200 ok*/
	          if(httpResponse.getStatusLine().getStatusCode() == 200)  
	          { 
	            /*取出回應字串*/
	             /*strResult = EntityUtils.toString(httpResponse.getEntity()); */
	             InputStream is =httpResponse.getEntity().getContent(); 
	             
	             byte[] data = new byte[1024];
	             int n;
	             ByteArrayBuffer buf = new ByteArrayBuffer(1024);
	             while ((n = is.read(data)) != -1)
	               buf.append(data, 0, n);
	               
	             strResult = new String(buf.toByteArray(), HTTP.UTF_8);
	          } 
	        } 
	        catch (ClientProtocolException e) 
	        {  
	         
	          e.printStackTrace(); 
	        } 
	        catch (IOException e) 
	        {  
	         
	          e.printStackTrace(); 
	        } 
	        catch (Exception e) 
	        {  
	          
	          e.printStackTrace();  
	        }  
	         
	      
	}
	public HttpConnect(String uriAPI,String requestStr) {
	     
	      
		   this.requestStr=requestStr;
	       this.uriAPI = uriAPI;
	        /*建立HTTP Post連線*/
	        HttpPost httpRequest = new HttpPost(uriAPI); 
	        /*
	         * Post運作傳送變數必須用NameValuePair[]陣列儲存
	        */
	        List <NameValuePair> params = new ArrayList <NameValuePair>(); 

	        	params.add(new BasicNameValuePair("str1", requestStr)); 
	       
	        
	        
	        try 
	        { 
	          /*發出HTTP request*/
	          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
	          /*取得HTTP response*/
	          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
	          /*若狀態碼為200 ok*/
	          if(httpResponse.getStatusLine().getStatusCode() == 200)  
	          { 
	            /*取出回應字串*/
	             //strResult = EntityUtils.toString(httpResponse.getEntity()); 
	             InputStream is =httpResponse.getEntity().getContent(); 
	             
	             byte[] data = new byte[1024];
	             int n;
	             ByteArrayBuffer buf = new ByteArrayBuffer(1024);
	             while ((n = is.read(data)) != -1)
	               buf.append(data, 0, n);
	               
	             strResult = new String(buf.toByteArray(), HTTP.UTF_8);
	          } 
	        } 
	        catch (ClientProtocolException e) 
	        {  
	         
	          e.printStackTrace(); 
	        } 
	        catch (IOException e) 
	        {  
	         
	          e.printStackTrace(); 
	        } 
	        catch (Exception e) 
	        {  
	          
	          e.printStackTrace();  
	        }  
	         
	      
	}
	
	public String getResponse(){
		

		return strResult;
	}

}
