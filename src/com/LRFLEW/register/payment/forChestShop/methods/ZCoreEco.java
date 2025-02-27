package com.LRFLEW.register.payment.forChestShop.methods;

import com.LRFLEW.register.payment.forChestShop.Method;
import me.zavdav.zcore.ZCore;
import me.zavdav.zcore.api.Economy;
import me.zavdav.zcore.util.PlayerUtils;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class ZCoreEco implements Method {

    private ZCore zcore;

    public Object getPlugin() {
        return this.zcore;
    }

    public String getName() {
        return "ZCore";
    }

    public String getVersion() {
        return zcore.getDescription().getVersion();
    }

    public int fractionalDigits() {
        return 2;
    }

    public String format(double amount) {
        return Economy.formatBalance(amount);
    }

    public boolean hasBanks() {
        return false;
    }

    public boolean hasBank(String bank, World world) {
        return false;
    }

    public boolean hasAccount(String name, World world) {
        try {
            UUID uuid = PlayerUtils.getUUIDFromUsername(name);
            return Economy.userExists(uuid);
        } catch (Throwable e) {
            return false;
        }
    }

    public boolean hasBankAccount(String bank, String name, World world) {
        return false;
    }

    public MethodAccount getAccount(String name, World world) {
        if (!hasAccount(name, world)) return null;
        return new ZEcoAccount(PlayerUtils.getUUIDFromUsername(name));
    }

    public MethodBankAccount getBankAccount(String bank, String name, World world) {
        return null;
    }

    public boolean isCompatible(Plugin plugin) {
        return plugin.getDescription().getName().equalsIgnoreCase("ZCore") &&
               plugin instanceof ZCore;
    }

    public void setPlugin(Plugin plugin) {
        zcore = (ZCore) plugin;
    }

    public static class ZEcoAccount implements MethodAccount {

        private final UUID uuid;

        public ZEcoAccount(UUID uuid) {
            this.uuid = uuid;
        }

        public double balance(World world) {
            return Economy.getBalance(uuid);
        }

        public boolean set(double amount, World world) {
            try {
                Economy.setBalance(uuid, amount);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean add(double amount, World world) {
            try {
                Economy.addBalance(uuid, amount);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean subtract(double amount, World world) {
            try {
                Economy.subtractBalance(uuid, amount);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean multiply(double amount, World world) {
            try {
                Economy.setBalance(uuid, Economy.getBalance(uuid) * amount);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean divide(double amount, World world) {
            try {
                Economy.setBalance(uuid, Economy.getBalance(uuid) / amount);
                return true;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean hasEnough(double amount, World world) {
            try {
                return Economy.hasEnough(uuid, amount);
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean hasOver(double amount, World world) {
            try {
                return Economy.getBalance(uuid) > amount;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean hasUnder(double amount, World world) {
            try {
                return Economy.getBalance(uuid) < amount;
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }

        public boolean isNegative(World world) {
            return false;
        }

        public boolean remove() {
            return false;
        }
    }
}
