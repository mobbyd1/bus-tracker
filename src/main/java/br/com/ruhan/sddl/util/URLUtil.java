package br.com.ruhan.sddl.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author ruhan
 *
 */
public class URLUtil {

	/**
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String readFromUrl(final String url) throws IOException {

		final URL content = new URL(url);
		final BufferedReader in = new BufferedReader(
				new InputStreamReader(content.openStream()));

		final StringBuilder sb = new StringBuilder();

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
		}

		in.close();

		return sb.toString();
	}

}
