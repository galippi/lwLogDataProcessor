package lwLogDataProcessor;

public abstract class LogDataProcessorHexu16Base extends LogDataProcessorHandlerBase {

    abstract public void process(int data, String rest);

    public LogDataProcessorHexu16Base(String prefix) {
        super(prefix);
    }

    @Override
    public final void process(String data) throws Exception {
        String dataOrig = data;
        data = data.substring(getPrefix().length());
        String rest = null;
        int val = 0;
        boolean valValid = true;
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
                valValid = false;
                break;
            }
        }
        if (valValid)
            process(val, rest);
        else
            throw new Exception("Invalid data to be processed (data=" + dataOrig + ")!");
    }
}
