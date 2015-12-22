package com.viglucci.app.handler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefRequest;
import org.cef.network.CefResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Kevin
 * @since 12/20/2015.
 */
public class LocalSchemaHandler extends CefResourceHandlerAdapter  {

	public static final String scheme = "local";
	public static final String domain = "app";

	private byte[] data_;
	private String mime_type_;
	private int offset_ = 0;

	public LocalSchemaHandler() {
		super();
	}

	@Override
	public synchronized boolean processRequest(CefRequest request, CefCallback callback) {
		boolean handled = false;
		String url = request.getURL();
		String loadPath = url.substring(url.indexOf('/') + 1);
		if (url.endsWith(".html")) {
			loadPath = "templates" + loadPath;
			handled = loadContent(loadPath);
			mime_type_ = "text/html";
			if (!handled) {
				handled = getNotFoundResponse(loadPath);
			}
		} else if (url.endsWith(".css")) {
			loadPath = "css" + loadPath;
			handled = loadContent(loadPath);
			mime_type_ = "text/css";
		} else if (url.endsWith(".png")) {
			loadPath = "images" + loadPath;
			handled = loadContent(loadPath);
			mime_type_ = "image/png";
		}

		if (handled) {
			// Indicate the headers are available.
			callback.Continue();
			return true;
		}

		System.out.println("Failed to respond to local resource: " + loadPath);

		return false;
	}

	@Override
	public void getResponseHeaders(CefResponse response, IntRef response_length, StringRef redirectUrl) {
		response.setMimeType(mime_type_);
		response.setStatus(200);

		// Set the resulting response length
		response_length.set(data_.length);
	}

	@Override
	public synchronized boolean readResponse(byte[] data_out, int bytes_to_read, IntRef bytes_read, CefCallback callback) {
		boolean has_data = false;

		if (offset_ < data_.length) {
			// Copy the next block of data into the buffer.
			int transfer_size = Math.min(bytes_to_read, (data_.length - offset_));
			System.arraycopy(data_, offset_, data_out, 0, transfer_size);
			offset_ += transfer_size;

			bytes_read.set(transfer_size);
			has_data = true;
		} else {
			offset_ = 0;
			bytes_read.set(0);
		}

		return has_data;
	}

	private boolean loadContent(String loadPath) {
		Resource resource = new ClassPathResource(loadPath);
		try {
			InputStream inStream = resource.getInputStream();
			ByteArrayOutputStream outFile = new ByteArrayOutputStream();
			int readByte = -1;
			while ((readByte = inStream.read()) >= 0)
				outFile.write(readByte);
			data_ = outFile.toByteArray();
			System.out.println("Loaded local resource: " + loadPath);
			return true;
		} catch (IOException e) { }
		return false;
	}

	private boolean getNotFoundResponse(String loadPath) {
		System.out.println("Failed to load template: " + loadPath);
		String html = "<html><head><title>Error 404</title></head>";
		html+= "<body><h1>Error 404</h1>";
		html+= "File  " + loadPath + " ";
		html+= "does not exist</body></html>";
		data_ = html.getBytes();
		return true;
	}
}
