package him.sniffer.proxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.SERVER)
public class ServerProxy extends CommonProxy {
    @Override
    public void loadConfig(File cfgDir) {

    }

    @Override
    public void registCommand() {

    }

    @Override
    public void registKeyBinding() {

    }

    @Override
    public void registEventHandler() {

    }

    @Override
    public void addChatMessage(String message) {

    }
}
