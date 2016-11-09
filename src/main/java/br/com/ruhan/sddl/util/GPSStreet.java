package br.com.ruhan.sddl.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author ruhan
 *
 */
public class GPSStreet {

	private static List<String> ruas = Arrays.asList("Rua 1", "Rua 2", "Rua 3",
			"Rua 4", "Rua 5", "Rua 6", "Rua 7", "Rua 8", "Rua 9");

	/**
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static String getStreetName(final Double latitude,
			final Double longitude) {

		try {

			final String url = String.format(
					"https://maps.googleapis.com/maps/api/geocode"
							+ "/json?latlng=%s,%s&key=AIzaSyBtjs7jB8E0gk10HOO4OjptqHpVf5mUuls",
					latitude, longitude);

			final String content = URLUtil.readFromUrl(url);

			final JSONObject jsonObject = new JSONObject(content);

			final JSONArray results = jsonObject.getJSONArray("results");
			final JSONArray components = results.getJSONObject(0)
					.getJSONArray("address_components");

			final String nomeRua = components.getJSONObject(1)
					.getString("long_name");
			final String bairro = components.getJSONObject(2)
					.getString("long_name");

			return nomeRua + " - " + bairro;

		} catch (final Throwable e) {
			final Random rand = new Random();
			final int index = rand.nextInt(ruas.size());

			return ruas.get(index);
		}
	}

}
