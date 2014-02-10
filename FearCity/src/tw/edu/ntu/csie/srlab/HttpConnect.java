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
	/*�ŧi���}�r��*/
    private String uriAPI = "";
    private String strResult,requestStr;
    public HttpConnect(String uriAPI) {
	     
	      
        
	       this.uriAPI = uriAPI;
	        /*�إ�HTTP Post�s�u*/
	        HttpPost httpRequest = new HttpPost(uriAPI); 
	        /*
	         * Post�B�@�ǰe�ܼƥ�����NameValuePair[]�}�C�x�s
	        */
	        List <NameValuePair> params = new ArrayList <NameValuePair>(); 
	        params.add(new BasicNameValuePair("str", requestStr)); 
	        try 
	        { 
	          /*�o�XHTTP request*/
	          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
	          /*���oHTTP response*/
	          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
	          /*�Y���A�X��200 ok*/
	          if(httpResponse.getStatusLine().getStatusCode() == 200)  
	          { 
	            /*���X�^���r��*/
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
	        /*�إ�HTTP Post�s�u*/
	        HttpPost httpRequest = new HttpPost(uriAPI); 
	        /*
	         * Post�B�@�ǰe�ܼƥ�����NameValuePair[]�}�C�x�s
	        */
	        List <NameValuePair> params = new ArrayList <NameValuePair>(); 

	        	params.add(new BasicNameValuePair("str1", requestStr)); 
	       
	        
	        
	        try 
	        { 
	          /*�o�XHTTP request*/
	          httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8)); 
	          /*���oHTTP response*/
	          HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest); 
	          /*�Y���A�X��200 ok*/
	          if(httpResponse.getStatusLine().getStatusCode() == 200)  
	          { 
	            /*���X�^���r��*/
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
