package lwLogDataProcessor;

import java.util.TreeMap;

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
    public void process(int data, String rest) {
        System.out.println("LogDataProcessorHexu16.process prefix=" + prefix + " data=" + data + " rest=" + rest);
    }
}

class LogDataProcessorHexu8Array extends LogDataProcessorHexu8ArrayBase {

    LogDataProcessorHexu8Array(String prefix) {
        super(prefix);
    }

    @Override
    public void process(byte[] data, String rest) {
        String dataStr = "";
        for (int i = 0; i < data.length; i++)
            dataStr = dataStr + ' ' + (((int)data[i]) & 0xFF);
        System.out.println("LogDataProcessorHexu16.process prefix=" + prefix + " dataStr=" + dataStr + " rest=" + rest);
    }
}

public class LogDataProcessor {
    public LogDataProcessor() {
        
    }

    public void addHandler(LogDataProcessorHandlerBase handler) {
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

    public void process(String data, LogDataProcessorInterface defaultHandler) {
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
