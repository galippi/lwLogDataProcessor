package lwLogDataProcessor;

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