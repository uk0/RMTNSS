package start;

import fuck.gfw.client.Client;
import fuck.gfw.server.FUCKServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadowsocks.socks6Vertx;
import shadowsocks.crypto.CryptoFactory;
import shadowsocks.util.GlobalConfig;

import java.net.BindException;

/**
 * Created by zhangjianxin on 2017/8/21.
 */
public class Main {
    public static Logger log = LogManager.getLogger(start.Main.class.getName());
    public static final String VERSION = "1.1.1";
    public static void main(String[] args) {
        if(!"server".equals(args[0])){
        try {
            log.info("Socks5obfs Version " + VERSION);
            new Client();
            GlobalConfig.getConfigFromFile(args[0]);
        }catch(ClassCastException e) {
            log.fatal("Get config from json file error.", e);
            return;
        }
        //make sure this method could work.
        try{
            CryptoFactory.create(GlobalConfig.get().getMethod(), GlobalConfig.get().getPassword());
        }catch(Exception e){
            log.fatal("Error crypto method", e);
            return;
        }
        GlobalConfig.get().printConfig();
        new socks6Vertx(GlobalConfig.get().isServerMode()).start();
        }else{

            try {
                new FUCKServer();
                GlobalConfig.getConfigFromFile(args[0]);
                try{
                    CryptoFactory.create(GlobalConfig.get().getMethod(), GlobalConfig.get().getPassword());
                }catch(Exception e){
                    log.fatal("Error crypto method", e);
                    return;
                }
                GlobalConfig.get().printConfig();
                new socks6Vertx(GlobalConfig.get().isServerMode()).start();
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof BindException){
                    log.info("Udp port already in use.");
                }
                log.info("Start failed.");
                System.exit(0);
            }
        }
    }

}
