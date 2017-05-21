package him.sniffer.constant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class IMod {
    public static final String NAME = "Sniffer";
    public static final String MODID = "sniffer";
    public static final String VERSION = "1.11.2-1.1.0";
    public static final String ACMCVERSION = "[1.11.2]";
    public static final String CLIENT_PROXY_CLASS = "him.sniffer.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "him.sniffer.proxy.ServerProxy";
    public static final Logger logger = LogManager.getLogger(NAME);

    private IMod() {
    }
}
