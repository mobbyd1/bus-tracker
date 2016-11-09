package br.com.ruhan.sddl.model;

import java.util.Date;

/**
 * Created by ruhan on 03/11/16.
 */
public class GPSBus {

    private Date hora;
    private String ordem;
    private String linha;
    private double latitude;
    private double longitude;
    private double velocidade;
    private String nomeRua;

    /**
     *
     */
    public GPSBus() {

    }

    /**
     * @return
     */
    public Date getHora() {
        return this.hora;
    }

    /**
     * @return
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * @return
     */
    public String getLinha() {
        return this.linha;
    }

    /**
     * @return
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * @return
     */
    public String getNomeRua() {
        return this.nomeRua;
    }

    /**
     * @return
     */
    public String getOrdem() {
        return this.ordem;
    }

    /**
     * @return
     */
    public double getVelocidade() {
        return this.velocidade;
    }

    /**
     * @param hora
     */
    public void setHora(final Date hora) {
        this.hora = hora;
    }

    /**
     * @param latitude
     */
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    /**
     * @param linha
     */
    public void setLinha(final String linha) {
        this.linha = linha;
    }

    /**
     * @param longitude
     */
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    /**
     * @param nomeRua
     */
    public void setNomeRua(final String nomeRua) {
        this.nomeRua = nomeRua;
    }

    /**
     * @param ordem
     */
    public void setOrdem(final String ordem) {
        this.ordem = ordem;
    }

    /**
     * @param velocidade
     */
    public void setVelocidade(final double velocidade) {
        this.velocidade = velocidade;
    }

    @Override
    public String toString() {
        return "GPSBus{" +
                "hora=" + hora +
                ", ordem='" + ordem + '\'' +
                ", linha='" + linha + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", velocidade=" + velocidade +
                ", nomeRua='" + nomeRua + '\'' +
                '}';
    }
}
