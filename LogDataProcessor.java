package LwLogDataProcessor;

import java.util.TreeMap;
import java.util.Vector;

interface LogDataProcessorInterface {
    boolean isHandler();
    void process(String data);
}

class LogDataProcessorBase implements LogDataProcessorInterface {
    @Override
    public boolean isHandler() {
        return false;
    }

    @Override
    public void process(String data) {
        throw new Error("Invalid software state!");
    }
}

abstract class LogDataProcessorHandlerBase implements LogDataProcessorInterface {
    LogDataProcessorHandlerBase(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean isHandler() {
        return true;
    }

    public String getPrefix() {
        return prefix;
    }

    String prefix;
}

class LogDataProcessorDefaultHandler implements LogDataProcessorInterface {

    @Override
    public boolean isHandler() {
        return true;
    }

    @Override
    public void process(String data) {
        System.out.println("LogDataProcessorDefaultHandler.process data=" + data);
    }
}

abstract class LogDataProcessorHexu16Base extends LogDataProcessorHandlerBase {

    abstract void process(int data, String rest);

    LogDataProcessorHexu16Base(String prefix) {
        super(prefix);
    }

    @Override
    public final void process(String data) {
        String dataOrig = data;
        data = data.substring(getPrefix().length());
        String rest = null;
        int val = 0;
        boolean valValid = false;
        for(int i = 0; i < data.length(); i++) {
            char c = data.charAt(i);
            if ((c >= '0') && (c <= '9'))
                val = (val * 16) + (c - '0');
            else
            if ((c >= 'A') && (c <= 'F'))
                val = (val * 16) + (c - 'A' + 10);
            else
            if ((c >= 'a') && (c <= 'f'))
                val = (val * 16) + (c - 'a' + 10);
            else {
                rest = data.substring(i);
                break;
            }
            valValid = true;
        }
        if (valValid)
            process(val, rest);
        else
            throw new Error("Invalid data to be processed (data=" + dataOrig + ")!");
    }
}

abstract class LogDataProcessorHexu8ArrayBase extends LogDataProcessorHandlerBase {

    abstract void process(byte[] data, String rest);

    LogDataProcessorHexu8ArrayBase(String prefix) {
        super(prefix);
    }

    private int hex2dec(char c) throws Exception {
        if ((c >= '0') && (c <= '9'))
            return (c - '0');
        else
        if ((c >= 'A') && (c <= 'F'))
            return (c - 'A' + 10);
        else
        if ((c >= 'a') && (c <= 'f'))
            return (c - 'a' + 10);
        else
            throw new Exception("It's not a hex digit!");
    }

    @Override
    public final void process(String data) {
        String dataOrig = data;
        data = data.substring(getPrefix().length());
        String rest = null;
        Vector<Integer> val = new Vector<>();
        for(int i = 0; i < data.length(); i+=2) {
            int d0, d1;
            try {
                d0 = hex2dec(data.charAt(i));
            }catch (Exception e) {
                rest = data.substring(i);
                break;
            }
            try {
                d1 = hex2dec(data.charAt(i + 1));
            }catch (Exception e) {
                rest = data.substring(i + 1);
                break;
            }
            val.add(d0 * 16 + d1);
        }
        if (val.size() > 0) {
            byte [] result = new byte[val.size()];
            for (int i = 0; i < result.length; i++)
                result[i] = (byte)(val.get(i) & 0xFF);
            process(result, rest);
        } else
            throw new Error("Invalid data to be processed (data=" + dataOrig + ")!");
    }
}

class LogDataProcessorHandler extends LogDataProcessorHandlerBase {

    LogDataProcessorHandler(String prefix) {
        super(prefix);
    }

    @Override
    public void process(String data) {
        System.out.println("LogDataProcessorHandler.process prefix=" + prefix + " data=" + data);
    }
}

class LogDataProcessorHexu16 extends LogDataProcessorHexu16Base {

    LogDataProcessorHexu16(String prefix) {
        super(prefix);
        // TODO Auto-generated constructor stub
    }

    @Override
    void process(int data, String rest) {
        System.out.println("LogDataProcessorHexu16.process prefix=" + prefix + " data=" + data + " rest=" + rest);
    }
}

class LogDataProcessorHexu8Array extends LogDataProcessorHexu8ArrayBase {

    LogDataProcessorHexu8Array(String prefix) {
        super(prefix);
    }

    @Override
    void process(byte[] data, String rest) {
        String dataStr = "";
        for (int i = 0; i < data.length; i++)
            dataStr = dataStr + ' ' + (((int)data[i]) & 0xFF);
        System.out.println("LogDataProcessorHexu16.process prefix=" + prefix + " dataStr=" + dataStr + " rest=" + rest);
    }
}

public class LogDataProcessor {
    LogDataProcessor() {
        
    }

    void addHandler(LogDataProcessorHandlerBase handler) {
        String prefix = handler.getPrefix();
        for (int i = 1; i < prefix.length(); i++) {
            String subStr = prefix.substring(0, i);
            if (receivers.get(subStr) == null) {
                receivers.put(subStr, baseObject);
            }
        }
        LogDataProcessorInterface base = receivers.get(prefix);
        if (base != null) {
            if (base.isHandler()) {
                String msg = "Error";
                throw new Error(msg);
            }
        }
        receivers.put(prefix, handler);
    }

    void process(String data, LogDataProcessorInterface defaultHandler) {
        LogDataProcessorInterface lastValidHandler = defaultHandler;
        for (int i = 1; i <= data.length(); i++) {
            String subStr = data.substring(0, i);
            LogDataProcessorInterface handler = receivers.get(subStr);
            if (handler == null) {
                break;
            } else {
                if (handler.isHandler())
                    lastValidHandler = handler;
            }
        }
        if (lastValidHandler != null) {
            lastValidHandler.process(data);
        }
    }

    TreeMap<String, LogDataProcessorInterface> receivers = new TreeMap<>();
    static LogDataProcessorInterface baseObject = new LogDataProcessorBase();
}
