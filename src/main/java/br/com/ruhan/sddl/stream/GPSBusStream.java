package br.com.ruhan.sddl.stream;

import br.com.ruhan.sddl.model.GPSBus;
import br.com.ruhan.sddl.util.GPSStreet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ruhan
 *
 */
public class GPSBusStream {

	private static int DATA_HORA_INDEX = 0;
	private static int ORDEM_INDEX = 1;
	private static int LINHA_INDEX = 2;
	private static int LATITUDE_INDEX = 3;
	private static int LONGITUDE_INDEX = 4;
	private static int VELOCIDADE_INDEX = 5;
	private static String DATE_FORMAT = "MM-dd-yyyy hh:mm:ss";

	/**
	 * @return
	 * @throws IOException
	 */
	public static String readGPS() throws IOException {

		final InputStream resource = GPSBusStream.class
				.getResourceAsStream("/gps.json");

		final StringBuilder sb = new StringBuilder();
		final BufferedReader br = new BufferedReader(
				new InputStreamReader(resource));

		String sCurrentLine;

		while ((sCurrentLine = br.readLine()) != null) {
			sb.append(sCurrentLine);
		}

		br.close();
		return sb.toString();
	}


	/**
	 * 
	 */
	public static List<GPSBus> build() {

		final List<GPSBus> list = new ArrayList<GPSBus>();

		try {
			// final String content = URLUtil.readFromUrl(
			// "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/onibus2");

			final String content = readGPS();

			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					DATE_FORMAT);

			final JSONObject obj = new JSONObject(content);
			final JSONArray jsonArray = obj.getJSONArray("DATA");

			for (int i = jsonArray.length() - 1; i >= 0; i--) {
				final JSONArray gpsData = jsonArray.getJSONArray(i);

				final Object dataHoraObj = gpsData.get(DATA_HORA_INDEX);
				final Object ordemObj = gpsData.get(ORDEM_INDEX);
				final Object linhaObj = gpsData.get(LINHA_INDEX);
				final Object latitudeObj = gpsData.get(LATITUDE_INDEX);
				final Object longitudeObj = gpsData.get(LONGITUDE_INDEX);
				final Object velocidadeObj = gpsData.get(VELOCIDADE_INDEX);

				final Date dataHora = simpleDateFormat
						.parse(dataHoraObj.toString());
				final String ordem = ordemObj.toString();
				final String linha = linhaObj.toString().replace(".0", "");
				final Double latitude = Double.valueOf(latitudeObj.toString());
				final Double longitude = Double
						.valueOf(longitudeObj.toString());
				final Double velocidade = Double
						.valueOf(velocidadeObj.toString());

				final GPSBus gpsBus = new GPSBus();
				gpsBus.setHora(dataHora);
				gpsBus.setOrdem(ordem);
				gpsBus.setLinha(linha);
				gpsBus.setLatitude(latitude);
				gpsBus.setLongitude(longitude);
				gpsBus.setVelocidade(velocidade);
				//gpsBus.setNomeRua(GPSStreet.getStreetName(latitude, longitude));

				list.add( gpsBus );

			}

		} catch (final Exception e) {
			e.printStackTrace();
		}

		return list;

	}

}
