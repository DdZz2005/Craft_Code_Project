import android.content.Context
import android.net.wifi.WifiManager
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

fun getIPAddress(useIPv4: Boolean): String {
    try {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
        for (intf in interfaces) {
            val addrs = Collections.list(intf.inetAddresses)
            for (addr in addrs) {
                if (!addr.isLoopbackAddress) {
                    val sAddr = addr.hostAddress
                    // Проверяем, IPv4 или IPv6
                    val isIPv4 = sAddr.indexOf(':') < 0
                    if (useIPv4) {
                        if (isIPv4) return sAddr
                    } else {
                        if (!isIPv4) {
                            val delim = sAddr.indexOf('%') // Удаляем часть после '%'
                            return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(0, delim).uppercase(Locale.getDefault())
                        }
                    }
                }
            }
        }
    } catch (ex: Exception) {
        // Логгирование ошибок
    }
    return ""
}
