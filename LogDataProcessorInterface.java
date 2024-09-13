package lwLogDataProcessor;

interface LogDataProcessorInterface {
    boolean isHandler();
    void process(String data);
}