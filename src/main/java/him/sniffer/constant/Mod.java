package him.sniffer.constant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class Mod {
    public static final String NAME = "Sniffer";
    public static final String MODID = "sniffer";
    public static final String VERSION = "1.7.10-1.0.0";
    public static final String ACMCVERSION = "[1.7.10]";
    public static final String CLIENT_PROXY_CLASS = "him.sniffer.proxy.ClientProxy";
    public static final String SERVER_PROXY_CLASS = "him.sniffer.proxy.ServerProxy";
    public static final Logger logger = LogManager.getLogger(NAME);
    public static final boolean DEBUG = true;

    private Mod() {
    }
}
