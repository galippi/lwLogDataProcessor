package lwLogDataProcessor;

abstract public class LogDataProcessorHandlerBase implements LogDataProcessorInterface {
    public LogDataProcessorHandlerBase(String prefix) {
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