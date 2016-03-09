package com.viglucci.app.handler;

import org.cef.callback.CefCallback;
import org.cef.handler.CefResourceHandlerAdapter;
import org.cef.misc.IntRef;
import org.cef.misc.StringRef;
import org.cef.network.CefResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Kevin
 * @since 12/22/2015
 */
public class SchemaHandler extends CefResourceHandlerAdapter {

	private byte[] data_;
	private String mime_type_;
	private int offset_ = 0;

	public SchemaHandler() {
		super();
	}

	public boolean getNotFoundResponse(String loadPath) {
		System.out.println("Failed to load template: " + loadPath);
		String html = "<html><head><title>Error 404</title></head>";
		html+= "<body><h1>Error 404</h1>";
		html+= "File  " + loadPath + " ";
		html+= "does not exist</body></html>";
		data_ = html.getBytes();
		return true;
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

	protected boolean loadContent(String loadPath) {
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

	public byte[] getData_() {
		return data_;
	}

	public void setData_(byte[] data_) {
		this.data_ = data_;
	}

	public String getMime_type_() {
		return mime_type_;
	}

	public void setMime_type_(String mime_type_) {
		this.mime_type_ = mime_type_;
	}

	public int getOffset_() {
		return offset_;
	}

	public void setOffset_(int offset_) {
		this.offset_ = offset_;
	}
}
