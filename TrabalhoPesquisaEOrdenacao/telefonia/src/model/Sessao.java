package model;

/**
 * Representa uma sessão de telefonia (uma linha do arquivo).
 * Objeto imutável: seguro para ser lido por várias threads ao mesmo tempo.
 */
public class Sessao {

    private final String sessionId;
    private final String imsi;
    private final String imei;
    private final String mcc;
    private final String mnc;
    private final int cellId;
    private final int lac;
    private final String bearer;
    private final String startTime;
    private final String endTime;
    private final int durationS;
    private final String ipAddr;
    private final String sipCallId;
    private final String codec;
    private final long bytesUp;
    private final long bytesDown;
    private final double lat;
    private final double lon;
    private final String encryption;

    public Sessao(String sessionId, String imsi, String imei, String mcc, String mnc,
                  int cellId, int lac, String bearer, String startTime, String endTime,
                  int durationS, String ipAddr, String sipCallId, String codec,
                  long bytesUp, long bytesDown, double lat, double lon, String encryption) {
        this.sessionId = sessionId;
        this.imsi = imsi;
        this.imei = imei;
        this.mcc = mcc;
        this.mnc = mnc;
        this.cellId = cellId;
        this.lac = lac;
        this.bearer = bearer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationS = durationS;
        this.ipAddr = ipAddr;
        this.sipCallId = sipCallId;
        this.codec = codec;
        this.bytesUp = bytesUp;
        this.bytesDown = bytesDown;
        this.lat = lat;
        this.lon = lon;
        this.encryption = encryption;
    }

    public String getSessionId()  { return sessionId; }
    public String getImsi()       { return imsi; }
    public String getImei()       { return imei; }
    public String getMcc()        { return mcc; }
    public String getMnc()        { return mnc; }
    public int getCellId()        { return cellId; }
    public int getLac()           { return lac; }
    public String getBearer()     { return bearer; }
    public String getStartTime()  { return startTime; }
    public String getEndTime()    { return endTime; }
    public int getDurationS()     { return durationS; }
    public String getIpAddr()     { return ipAddr; }
    public String getSipCallId()  { return sipCallId; }
    public String getCodec()      { return codec; }
    public long getBytesUp()      { return bytesUp; }
    public long getBytesDown()    { return bytesDown; }
    public double getLat()        { return lat; }
    public double getLon()        { return lon; }
    public String getEncryption() { return encryption; }

    @Override
    public String toString() {
        return String.format(
            "Sessao{id=%s, imsi=%s, imei=%s, mcc/mnc=%s/%s, cell=%d, lac=%d, %s, "
          + "%s -> %s (%ds), ip=%s, sip=%s, codec=%s, up=%d, down=%d, "
          + "lat=%.6f, lon=%.6f, cripto=%s}",
            sessionId, imsi, imei, mcc, mnc, cellId, lac, bearer,
            startTime, endTime, durationS, ipAddr, sipCallId, codec,
            bytesUp, bytesDown, lat, lon, encryption);
    }
}
