package lwLogDataProcessor;

import java.util.Vector;

public abstract class LogDataProcessorHexu8ArrayBase extends LogDataProcessorHandlerBase {

    public abstract void process(byte[] data, String rest);

    protected LogDataProcessorHexu8ArrayBase(String prefix) {
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